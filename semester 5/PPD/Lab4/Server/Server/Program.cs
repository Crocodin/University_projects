using System;
using System.Collections.Generic;
using System.Net;
using System.Net.Sockets;
using System.Text;

namespace SimulationFTP
{
    internal class SendFile
    {
        private int Id;
        private Socket Client;
        public SendFile(int id, Socket socket)
        {
            this.Id = id;
            this.Client = socket;
        }
        private string Path = "";
        private int ContentLength;
        public byte[] Buffer = new byte[1024]; // the same size as the client's
        private int _position = 0;
        private List<byte> HeaderBytes = new List<byte>();
        public byte[] Response = [];

        public void OnReceive(IAsyncResult result)
        {
            if (result.AsyncState is null) return;
            SendFile sendFile = (SendFile)result.AsyncState;
            try
            {
                Socket client = sendFile.Client;
                int readBytes = client.EndReceive(result);
                if (readBytes == 0)
                {
                    client.Close();
                    return; // client disconnected
                }

                sendFile.HeaderBytes.AddRange(new ArraySegment<byte>(sendFile.Buffer, 0, readBytes));

                int i = sendFile.FindHeaderEnd();
                if (i < 0)
                {
                    // we did not finish reading the header
                    client.BeginReceive(sendFile.Buffer, 0, sendFile.Buffer.Length, SocketFlags.None, sendFile.OnReceive, sendFile);
                    return;
                }

                string header = Encoding.ASCII.GetString(sendFile.HeaderBytes.ToArray(), 0, i);
                sendFile.Path = ParsePath(header);
                Console.WriteLine($"[{sendFile.Id}] Requested path: '{sendFile.Path}'");

                sendFile.HeaderBytes.Clear();
                sendFile.Send();
            }
            catch (Exception ex)
            {
                sendFile.Client.Close();
                Console.WriteLine($"[{sendFile.Id}] Exception caught: {ex.Message}");
            }
        }

        private void Send()
        {
            byte[] head;
            byte[] body;

            // "/file.txt" -> "file.txt" (relative to the server's working directory); refuse ".." tricks
            string localPath = this.Path.TrimStart('/');
            if (localPath.Length == 0 || localPath.Contains("..") || !System.IO.File.Exists(localPath))
            {

                body = [];
                this.ContentLength = 0;
                head = Encoding.ASCII.GetBytes(ResponseHeader("404 Not Found"));
            }
            else
            {
                body = System.IO.File.ReadAllBytes(localPath);
                this.ContentLength = body.Length;
                head = Encoding.ASCII.GetBytes(ResponseHeader("200 OK"));
            }

            this.Response = new byte[head.Length + body.Length];
            Array.Copy(head, 0, this.Response, 0, head.Length);
            Array.Copy(body, 0, this.Response, head.Length, body.Length);

            this.Client.BeginSend(this.Response, 0, this.Response.Length, SocketFlags.None, OnSend, this);
        }

        private void OnSend(IAsyncResult result)
        {
            if (result.AsyncState is null) return;
            SendFile sendFile = (SendFile)result.AsyncState;
            try
            {
                Socket client = sendFile.Client;
                int sentBytes = client.EndSend(result);
                if (sentBytes == 0)
                {
                    client.Close();
                    return; // client disconnected
                }

                sendFile._position += sentBytes;
                if (sendFile._position >= sendFile.Response.Length)
                {
                    client.Shutdown(SocketShutdown.Send);
                    client.Close();
                    sendFile.Report();
                    return; // finished sending the file
                }

                client.BeginSend(sendFile.Response, sendFile._position, sendFile.Response.Length - sendFile._position, SocketFlags.None, OnSend, sendFile);
            }
            catch (Exception ex)
            {
                sendFile.Client.Close();
                Console.WriteLine($"[{sendFile.Id}] Exception caught: {ex.Message}");
            }
        }

        private int FindHeaderEnd()
        {
            byte[] pattern = Encoding.ASCII.GetBytes("\r\n\r\n");
            for (int i = 0; i <= HeaderBytes.Count - pattern.Length; ++i)
            {
                int j = 0;
                while (j < pattern.Length && HeaderBytes[i + j] == pattern[j]) ++j;
                if (j == pattern.Length) return i;
            }
            return -1;
        }

        private static string ParsePath(string header)
        {
            foreach (string line in header.Split("\r\n"))
            {
                if (line.StartsWith("GET ", StringComparison.OrdinalIgnoreCase))
                {
                    string[] parts = line.Split(' ');
                    if (parts.Length >= 2) return parts[1];
                }
            }
            return "";
        }

        public void Report()
        {
            Console.WriteLine($"[{this.Id}] Finished -- {this.Path} with {this._position} sent byte(s)");
        }

        public string ResponseHeader(string status) =>
            $"HTTP/1.1 {status}\r\nContent-Length: {this.ContentLength}\r\nContent-Type: application/octet-stream\r\nConnection: close\r\n\r\n";
    }

    internal class Server
    {
        private static int _clientCounter = -1;

        // usage: Server <port>   (serves files relative to the working directory)
        public static void Main(string[] args)
        {
            try
            {
                int port = int.Parse(args[0]);
                Console.WriteLine($"Listening on port {port}");
                IPEndPoint listeningEndpoint = new IPEndPoint(IPAddress.Any, port);

                using (Socket listeningSocket = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp))
                {
                    listeningSocket.Bind(listeningEndpoint);
                    listeningSocket.Listen(10);
                    listeningSocket.BeginAccept(OnNewConnection, listeningSocket);
                    Console.ReadLine();
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Exception caught: {ex.Message}");
            }
        }

        private static void OnNewConnection(IAsyncResult result)
        {
            if (result.AsyncState is null) return;
            try
            {
                Socket server = (Socket)result.AsyncState;
                Socket client = server.EndAccept(result);
                server.BeginAccept(OnNewConnection, server); // keep accepting

                Console.WriteLine("New client connected!");
                // callbacks run on thread-pool threads, so the counter must be incremented atomically
                SendFile sendFile = new SendFile(Interlocked.Increment(ref _clientCounter), client);
                client.BeginReceive(sendFile.Buffer, 0, sendFile.Buffer.Length, SocketFlags.None, sendFile.OnReceive, sendFile);
            }
            catch (ObjectDisposedException)
            {
                // listening socket closed on shutdown
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Exception caught: {ex.Message}");
            }
        }
    }
}