#include <iostream>
#include <fstream>
#include <vector>
#include <queue>
#include <algorithm>
using namespace std;

constexpr int INF = 1e9;

int N, M;
vector<vector<int>> graph, flow;
vector<int> height, excess;
vector<vector<int>> adjList;

void readFile(const string &fileName) {
    ifstream fin(fileName);
    fin >> N >> M;

    graph = vector(N, vector(N, 0));
    flow = vector(N, vector(N, 0));
    adjList = vector(N, vector<int>());

    for (int i = 0, u, v, w; i < M; ++i) {
        fin >> u >> v >> w;
        graph[u][v] = w;
        adjList[u].push_back(v);
        adjList[v].push_back(u);
    }
    fin.close();
}

int push(int u, int v) {
    int send = min(excess[u], graph[u][v] - flow[u][v]);
    if (send < 0) return 0;
    flow[u][v] += send;
    flow[v][u] -= send;
    excess[u] -= send;
    excess[v] += send;
    return send;
}

void relabel(int u) {
    int minHeight = INF;
    for (int v : adjList[u]) {
        if (graph[u][v] - flow[u][v] > 0) {
            minHeight = min(minHeight, height[v]);
        }
    }
    if (minHeight < INF) {
        height[u] = minHeight + 1;
    }
}

void discharge(int u, queue<int> &active) {
    for (int i = 0; excess[u] > 0 && i < adjList[u].size(); ++i) {
        int v = adjList[u][i];
        if (graph[u][v] - flow[u][v] > 0 && height[u] == height[v] + 1) {
            int sent = push(u, v);
            if (v != 0 && v != N - 1 && excess[v] == sent) {
                active.push(v);
            }
        }
    }
    if (excess[u] > 0) {
        relabel(u);
        active.push(u); /// Reinsert u to try again
    }
}

int pushRelabel(int s, int t) {
    height = vector(N, 0);
    excess = vector(N, 0);
    height[s] = N;

    for (int v : adjList[s]) {
        flow[s][v] = graph[s][v];
        flow[v][s] = -flow[s][v];
        excess[v] = graph[s][v];
        excess[s] -= graph[s][v];
    }

    queue<int> active;
    for (int i = 0; i < N; ++i) {
        if (i != s && i != t && excess[i] > 0) {
            active.push(i);
        }
    }

    while (!active.empty()) {
        int u = active.front();
        active.pop();
        discharge(u, active);
    }

    int maxFlow = 0;
    for (int v : adjList[s])
        maxFlow += flow[s][v];

    return maxFlow;
}

int main(int argc, const char *argv[]) {
    if (argc != 3) {
        cerr << argv[0] << " <in_file> <out_file>\n";
        return 1;
    }

    readFile(argv[1]);

    int s = 0, t = N - 1;

    ofstream fout(argv[2]);
    fout << pushRelabel(s, t);
    fout.close();

    return 0;
}
