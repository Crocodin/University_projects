#include <iostream>
#include <vector>
#include <algorithm>
#define INF 100000
using namespace std;

vector<vector<int>> graph;

void relax(vector<int>& dist, vector<int>& parent, int u, int v, int w) {
	if (dist[u] != INF && dist[u] + w < dist[v]) {
		dist[v] = dist[u] + w;
		parent[v] = u;
	}
}

int bellman_ford(const vector<vector<int>>& graph, const int N, const int source) {
	vector<int> dist(N, INF);
	vector<int> parent(N, -1);
	dist[source] = 0;

	for (int k = 0; k < N - 1; k++) // Relax all edges n-1 times
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (graph[i][j] != INF)
					relax(dist, parent, i, j, graph[i][j]);
			}
		}

	for (int i = 0; i < N; i++) {
		for (int j = 0; j < N; j++) {
			if (graph[i][j] != INF && dist[i] != INF && dist[i] + graph[i][j] < dist[j]) {
				cout << "Negative weight cycle detected.\n";
				return false;
			}
		}
	}

	// Output distances
	cout << "Shortest distances from node " << source << ":\n";
	for (int i = 0; i < N; ++i) {
		if (dist[i] == INF)
			cout << i << ": INF\n";
		else
			cout << i << ": " << dist[i] << "\n";
	}

	vector<int> path;
	int finish = 4; /// for testing
	for (int cur = finish; cur != -1; cur = parent[cur]) {
		path.push_back(cur);
	}
	reverse(path.begin(), path.end());
	cout << "\n";
	for (const auto& v : path)
		cout << v << " ";

	return true;
}

int main() {
	int n, m, start;
	cin >> n >> m >> start;

	graph = vector<vector<int>>(n, vector<int>(n, INF));

	for (int i = 0; i < n; ++i)
		for (int j = 0; j < n; ++j)
			graph[i][j] = INF;

	for (int i = 0, u, v, w; i < m; i++) {
		cin >> u >> v >> w;
		graph[u][v] = w;
	}


	bellman_ford(graph, n, start);
	return 0;
}