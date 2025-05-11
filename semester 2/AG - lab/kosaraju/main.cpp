#include <iostream>
#include <stack>
#include <vector>
using namespace std;

vector<vector<int>> graph, revers_graph;
vector<bool> visited;
stack<int> finish;

void dfs1(const int index) {
	visited[index] = true;
	for (const auto& node : graph[index]) {
		if (!visited[node]) {
			dfs1(node);
		}
	}
	finish.push(index);
}

void dfs2(const int index, vector<int>& comp) {
	visited[index] = true;
	comp.push_back(index);
	for (const auto& node : graph[index]) {
		if (!visited[node]) {
			dfs2(node, comp);
		}
	}
}

int main() {
	int n, m;
	cin >> n >> m;

	graph.resize(n);
	revers_graph.resize(n);
	visited = vector<bool>(n, false);

	for (int i = 0; i < n; i++) {
		int v, u; cin >> v >> u;
		graph[v].push_back(u);
		revers_graph[u].push_back(v);
	}

	for (int i = 0; i < n; i++) {
		if (!visited[i]) dfs1(i);
	}

	visited = vector<bool>(n, false);

	while (!finish.empty()) {
		int u = finish.top();
		finish.pop();
		if (!visited[u]) {
			vector<int> comp;
			dfs2(u, comp);

			cout << "SCC: ";
			for (const auto& v : comp)
				cout << v << ' ';
			cout << "\n";
		}
	}
	return 0;
}