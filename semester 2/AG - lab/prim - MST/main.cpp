#include <iostream>
#include <fstream>
#include <vector>
#include <climits>
using std::vector;

int V, E;

vector<vector<int>> graph;
vector<int> P;
vector<int> key;

int extract_min(vector<int>& Q) {
	int pop_poz = 0;
	for (int i = 1; i < Q.size(); ++i) {
		if (key[Q[i]] < key[Q[pop_poz]]) {
			pop_poz = i;
		}
	}
	int u = Q[pop_poz];
	Q.erase(Q.begin() + pop_poz);
	return u;
}

void mst_prin(int r) {
	P = vector<int>(V, -1);
	key = vector<int>(V, INT_MAX);
	key[r] = 0;
	vector<int> Q;
	for (int i = 0; i < V; i++) Q.push_back(i);

	while (!Q.empty()) {
		int u = extract_min(Q);
		for (auto& v : Q) {
			if (graph[u][v] != -1 && graph[u][v] < key[v]) {
				P[v] = u;
				key[v] = graph[u][v];
			}
		}
	}
}

int main(int argc, char* argv[]) {
	if (argc != 3) throw std::runtime_error("Wrong number of arguments");
	std::ifstream inputFile(argv[1]);

	inputFile >> V >> E;
	graph = vector<vector<int>>(V, vector<int>(V, -1));

	for (int i = 0; i < E; i++) {
		int u, v, w;
		inputFile >> u >> v >> w;
		graph[u][v] = graph[v][u] = w;
	}
	inputFile.close();

	mst_prin(0);
	int total_cost = 0;
	vector<std::pair<int,int>> edges;
	for (int v = 0; v < V; ++v) {
		if (P[v] != -1) {
			edges.emplace_back(P[v], v);
			total_cost += graph[P[v]][v];
		}
	}

	std::ofstream outputFile(argv[2]);
	outputFile << total_cost << "\n";
	outputFile << edges.size() << "\n";
	for (auto [u, v] : edges) {
		outputFile << u << " " << v << "\n";
	}
	outputFile.close();
	return 0;
}