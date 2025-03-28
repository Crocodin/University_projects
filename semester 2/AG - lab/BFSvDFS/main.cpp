#include <fstream>
#include <iostream>
#include <queue>
#include <vector>

void BFS(const std::vector<std::vector<uint>> &graph, const uint start) {
	std::vector<uint> distance(graph.size(), -1);
	std::queue<uint> Q;
	distance[start] = 0;
	Q.push(start);
	while (!Q.empty()) {
		const uint u = Q.front();
		Q.pop();
		for (uint i = 0; i < graph[u].size(); i++) {
			if (distance[graph[u][i]] == -1) {
				distance[graph[u][i]] = distance[u] + 1;
				Q.push(graph[u][i]);
			}
		}
	}
	/// print distance
	std::cout << "The distance from " << start << " to are: \n";
	for (uint i = 0; i < graph.size(); i++)
		std::cout << distance[i] << " for " << i << "\n";
}

void DFS_VISIT(uint u, std::vector<std::vector<uint>>& adj, std::vector<bool>& visited, std::vector<uint>& parent) {
	visited[u] = true;
	std::cout << "Vârf descoperit: " << u << ", Părinte: " << (parent[u] == -1 ? "None" : std::to_string(parent[u])) << '\n';

	for (uint v : adj[u]) {
		if (!visited[v]) {
			parent[v] = u;
			DFS_VISIT(v, adj, visited, parent);
		}
	}
}

void DFS(std::vector<std::vector<uint>>& adj) {
	uint n = adj.size();
	std::vector<bool> visited(n, false);
	std::vector<uint> parent(n, -1);

	std::cout << "Pădurea descoperită de DFS:\n";
	for (uint i = 0; i < n; i++) {
		if (!visited[i]) {
			std::cout << "Componentă nouă:\n";
			DFS_VISIT(i, adj, visited, parent);
		}
	}
}

int main() {
	std::ifstream file("../graph.txt");
	uint n, m;
	file >> n;

	std::vector<std::vector<uint>> graph(n);
	uint i, j;
	while (file >> i >> j) {
		graph[i].push_back(j);
		graph[j].push_back(i);
	}

	uint start;
	std::cout << "Enter the start vertex: ";
	std::cin >> start;

	BFS(graph, start);

	DFS(graph);
}
