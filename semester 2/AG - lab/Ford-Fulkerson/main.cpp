#include <iostream>
#include <fstream>
#include <vector>
#include <queue>
using namespace std;

int N, M;
vector<vector<int>> graph;
vector<vector<int>> adjList;

void readFile(const string& fileName) {
	ifstream fin(fileName);
	fin >> N >> M;
	adjList = vector(N, vector<int>());
	graph = vector(N, vector(N, -1));
	for (int i = 0, u, v, w; i < M; ++i) {
		fin >> u >> v >> w;
		graph[u][v] = w;
		adjList[u].push_back(v);
		adjList[v].push_back(u);
	}
	fin.close();
}

int bfs(int s, int t, vector<int>& parents) {
	fill(parents.begin(), parents.end(), -1);
	parents[s] = -2;
	queue<pair<int, int>> q;
	q.emplace(s, INT_MAX);

	while (!q.empty()) {
		int u = q.front().first;
		int flow = q.front().second;
		q.pop();

		for (auto v : adjList[u]) {
			if (parents[v] == -1 and graph[u][v] > 0) {
				parents[v] = u;
				int newFlow = min(flow, graph[u][v]);
				if (v == t) return newFlow;
				q.emplace(v, newFlow);
			}
		}
	}
	return -1;
}

/// Ford-Fulkerson using BFS
int maxFlow(int s, int t) {
	int flow = 0;
	vector<int> parents(N);

	int newFlow; while ((newFlow = bfs(s, t, parents)) > 0) {
		flow += newFlow;
		int cur = t;
		while (cur != s) {
			int u = parents[cur];
			graph[u][cur] -= newFlow;
			graph[cur][u] += newFlow;
			cur = parents[cur];
		}
	}
	return flow;
}

int main(const int argc, const char *argv[]) {
	if (argc != 3) { cerr << argv[0] << " <in_file> <out_file>\n"; return 1; }

	readFile(argv[1]);

	int s = 0, t = N - 1;

	ofstream fout(argv[2]);
	fout << maxFlow(s, t);
	fout.close();

	return 0;
}