using System;
using System.IO;
using System.Net;
using System.Net.Sockets;
using System.Text;

namespace SimulationFTP
{
    internal class Download
    {
        public int Id;
        public string Host = "";
        public string Path = "";
        public Socket Socket;
        public byte[] Buffer = new byte[1024];
        public string FileName;
        public bool Finished = false;  

        public Download(int id, string host, string path, Socket socket)
        {
            this.Id = id; this.Host = host; this.Path = path; this.Socket = socket;
            string name = System.IO.Path.GetFileName(path);
            this.FileName = $"download_{this.Id}_{name}";
        }

        // ------ HEADER STUFF
        private const int MaxHeaderSize = 64 * 1024;
        private List<byte> HeaderBytes = new List<byte>();
        private bool HeaderFinished = false;
        private long ContentLength = -1;
        private bool Failed = false;
        // ------ BODY STUFF
        private byte[] Body = [];
        private long ReadBytes = 0;

        public bool FeedBytes(int readBytes)
        {
            if (readBytes == 0) return true; // server closed the connection

            if (this.HeaderFinished)
            {
                long toCopy = Math.Min(readBytes, this.ContentLength - this.ReadBytes);
                Array.Copy(this.Buffer, 0, this.Body, this.ReadBytes, toCopy);
                this.ReadBytes += toCopy;
                return this.Complete;
            }

            HeaderBytes.AddRange(new ArraySegment<byte>(this.Buffer, 0, readBytes));

            int i = FindHeaderEnd();
            if (i < 0)
            {
                if (HeaderBytes.Count > MaxHeaderSize) { this.Failed = true; return true; }
                return false; // header not complete yet
            }

            this.HeaderFinished = true;

            string header = Encoding.ASCII.GetString(HeaderBytes.ToArray(), 0, i);
            int status = ParseStatusCode(header);
            this.ContentLength = ParseContentLength(header);

            if (status != 200 || this.ContentLength < 0)
            {
                Console.WriteLine($"[{this.Id}] Bad response (status {status}, content-length {this.ContentLength})");
                this.Failed = true;
                return true;
            }

            this.Body = new byte[this.ContentLength];
            for (int k = i + 4; k < this.HeaderBytes.Count && this.ReadBytes < this.ContentLength; ++k)
                this.Body[this.ReadBytes++] = this.HeaderBytes[k];

            HeaderBytes.Clear();

            return this.Complete;
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

        private static int ParseStatusCode(string header)
        {
            string firstLine = header.Split("\r\n")[0];
            string[] parts = firstLine.Split(' ');
            if (parts.Length >= 2 && int.TryParse(parts[1], out int code)) return code;
            return -1;
        }

        private static long ParseContentLength(string header)
        {
            foreach (string line in header.Split("\r\n"))
            {
                if (line.StartsWith("Content-Length:", StringComparison.OrdinalIgnoreCase))
                {
                    string value = line.Substring("Content-Length:".Length).Trim();
                    if (long.TryParse(value, out long length)) return length;
                }
            }
            return -1;
        }

        public void SaveDownload()
        {
            try
            {
                using (FileStream fs = File.Create(this.FileName))
                {
                    fs.Write(this.Body, 0, this.Body.Length);
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine($"[{this.Id}] Failed to save: {ex.Message}");
            }
        }

        public void Report()
        {
            string state = this.Complete ? "Finished" : "FAILED";
            Console.WriteLine($"[{this.Id}] {state} -- {this.Path} with {this.ReadBytes} read byte(s)");
        }

        public byte[] BuildRequest() => Encoding.ASCII.GetBytes($"GET {this.Path} HTTP/1.1\r\nHost: {this.Host}\r\nConnection: close\r\n\r\n");

        public bool Complete => HeaderFinished && !Failed && ContentLength >= 0 && ReadBytes >= ContentLength;
    }

    internal class Client
    {
        private static CountdownEvent _pending = new CountdownEvent(0);
        public static void Main(string[] args)
        {
            if (args.Length < 4 || !int.TryParse(args[0], out int version))
            {
                Console.WriteLine("Usage: Client <version 1|2|3> <host> <port> <path1> [path2 ...]");
                return;
            }

            try
            {
                string host = args[1];
                int port = int.Parse(args[2]);
                string[] paths = args[3..];

                IPAddress serverIp = Dns.GetHostEntry(host).AddressList.First(ip => ip.AddressFamily == AddressFamily.InterNetwork);
                var endPoint = new IPEndPoint(serverIp, port);
                string hostHeader = $"{host}:{port}";

                switch (version)
                {
                    case 1: RunCallbacks(hostHeader, endPoint, paths); break;
                    case 2: RunTasks(hostHeader, endPoint, paths); break;
                    case 3: RunAsyncAwait(hostHeader, endPoint, paths); break;
                    default:
                        Console.WriteLine("Unknown version. Use 1, 2 or 3.");
                        return;
                }

                Console.WriteLine("All downloads done.");
                Console.ReadLine();
            }
            catch (Exception ex)
            {
                Console.WriteLine("Exception caught: {0}", ex.Message);
            }
        }
        private static Download CreateDownload(int id, string hostHeader, string path)
        {
            var socket = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);
            return new Download(id, hostHeader, path, socket);
        }
        private static bool CompleteDownload(Download download)
        {
            if (download.Finished) return false;
            download.Finished = true;

            try { download.Socket.Close(); } catch { }
            download.Report();
            if (download.Complete) download.SaveDownload();
            return true;
        }
        private static void RunCallbacks(string hostHeader, IPEndPoint endPoint, string[] paths)
        {
            _pending = new CountdownEvent(paths.Length);

            for (int id = 0; id < paths.Length; ++id)
            {
                Download download = CreateDownload(id, hostHeader, paths[id]);
                try
                {
                    download.Socket.BeginConnect(endPoint, OnConnect, download);
                }
                catch (Exception ex)
                {
                    Console.WriteLine($"[{id}] Connection failed: {ex.Message}");
                    Finish(download);
                }
            }

            Console.WriteLine("Main continues immediately...");
            _pending.Wait(); // the only Wait(): blocks until every download has finished
        }
        private static void Finish(Download download)
        {
            if (!CompleteDownload(download)) return;
            _pending.Signal();
        }
        private static void OnConnect(IAsyncResult result)
        {
            if (result.AsyncState is null) return;
            var download = (Download)result.AsyncState;

            try
            {
                Socket socket = download.Socket;
                socket.EndConnect(result);
                Console.WriteLine($"[{download.Id}] Connected!");

                byte[] request = download.BuildRequest();
                socket.BeginSend(request, 0, request.Length, SocketFlags.None, OnSend, download);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"[{download.Id}] Connection failed: {ex.Message}");
                Finish(download);
            }
        }

        private static void OnSend(IAsyncResult result)
        {
            if (result.AsyncState is null) return;
            Download download = (Download)result.AsyncState;

            try
            {
                Socket socket = download.Socket;
                socket.EndSend(result);
                Console.WriteLine($"[{download.Id}] Asked for file!");

                socket.BeginReceive(download.Buffer, 0, download.Buffer.Length, SocketFlags.None, OnReceive, download);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"[{download.Id}] Connection failed: {ex.Message}");
                Finish(download);
            }
        }

        private static void OnReceive(IAsyncResult result)
        {
            if (result.AsyncState is null) return;
            Download download = (Download)result.AsyncState;

            try
            {
                Socket socket = download.Socket;
                int readBytes = socket.EndReceive(result);
                bool done = download.FeedBytes(readBytes);

                if (done)
                    Finish(download);
                else // next chunk
                    socket.BeginReceive(download.Buffer, 0, download.Buffer.Length, SocketFlags.None, OnReceive, download);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"[{download.Id}] Connection failed: {ex.Message}");
                Finish(download);
            }
        }
        private static Task ConnectTask(Socket socket, EndPoint endPoint) =>
            Task.Factory.FromAsync(
                (cb, state) => socket.BeginConnect(endPoint, cb, state),
                ar => socket.EndConnect(ar),
                null
            );
        private static Task<int> SendTask(Socket socket, byte[] data) =>
            Task.Factory.FromAsync<int>(
                (cb, state) => socket.BeginSend(data, 0, data.Length, SocketFlags.None, cb, state),
                ar => socket.EndSend(ar),
                null
            );
        private static Task<int> ReceiveTask(Download download) =>
            Task.Factory.FromAsync<int>(
                (cb, state) => download.Socket.BeginReceive(download.Buffer, 0, download.Buffer.Length, SocketFlags.None, cb, state),
                ar => download.Socket.EndReceive(ar),
                null
            );
        // continue with version
        private static void RunTasks(string hostHeader, IPEndPoint endPoint, string[] paths)
        {
            var tasks = new List<Task>();

            for (int id = 0; id < paths.Length; ++id)
            {
                Download download = CreateDownload(id, hostHeader, paths[id]);
                try
                {
                    tasks.Add(DownloadWithTasks(download, endPoint));
                }
                catch (Exception ex) // the Begin* call threw synchronously
                {
                    Console.WriteLine($"[{id}] Connection failed: {ex.Message}");
                    CompleteDownload(download);
                }
            }

            Console.WriteLine("Main continues immediately...");
            Task.WaitAll(tasks.ToArray());
        }
        private static Task DownloadWithTasks(Download download, IPEndPoint endPoint)
        {
            return ConnectTask(download.Socket, endPoint)
                .ContinueWith(prev =>
                {
                    prev.GetAwaiter().GetResult();
                    Console.WriteLine($"[{download.Id}] Connected!");
                    return SendTask(download.Socket, download.BuildRequest());
                }).Unwrap()
                .ContinueWith(prev =>
                {
                    prev.GetAwaiter().GetResult();
                    Console.WriteLine($"[{download.Id}] Asked for file!");
                    return ReceiveLoop(download);
                }).Unwrap()
                .ContinueWith(prev =>
                {
                    if (prev.IsFaulted)
                        Console.WriteLine($"[{download.Id}] Connection failed: {prev.Exception!.GetBaseException().Message}");
                    CompleteDownload(download);
                });
        }
        private static Task ReceiveLoop(Download download)
        {
            return ReceiveTask(download).ContinueWith(prev =>
            {
                int readBytes = prev.GetAwaiter().GetResult();
                bool done = download.FeedBytes(readBytes);
                return done ? Task.CompletedTask : ReceiveLoop(download);
            }).Unwrap();
        }
        // await version
        private static void RunAsyncAwait(string hostHeader, IPEndPoint endPoint, string[] paths)
        {
            // ToArray() forces all to start now so they run concurrently
            Task[] tasks = paths
                .Select((path, id) => DownloadAsync(CreateDownload(id, hostHeader, path), endPoint))
                .ToArray();

            Console.WriteLine("Main continues immediately...");
            Task.WaitAll(tasks); 
        }

        private static async Task DownloadAsync(Download download, IPEndPoint endPoint)
        {
            try
            {
                await ConnectTask(download.Socket, endPoint);
                Console.WriteLine($"[{download.Id}] Connected!");

                await SendTask(download.Socket, download.BuildRequest());
                Console.WriteLine($"[{download.Id}] Asked for file!");

                while (true)
                {
                    int readBytes = await ReceiveTask(download);
                    if (download.FeedBytes(readBytes)) break;
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine($"[{download.Id}] Connection failed: {ex.Message}");
            }
            finally
            {
                CompleteDownload(download);
            }
        }
    }
}