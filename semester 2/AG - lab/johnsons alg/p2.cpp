#include <fstream>
#include <iostream>
#include <queue>
#include <vector>
#define INF 100000000
using namespace std;

int n, m, start;
vector<vector<pair<int, int>>> G;
vector<int> D;

void initialize_B_F() {
	D = vector<int>(n + 1, INF);
	vector<pair<int, int>>newNode(n);
	for (int i = 0; i < n; i++) {
		newNode[i].first = i;
		newNode[i].second = 0;
	}
	G.push_back(newNode);
	D[n] = 0;
}

int weight(const int u, const int v) {
	for (const auto& it : G[u]) {
		if (it.first == v)
			return it.second;
	}
	return INF;
}

void relax(const int u, const int v) {
	if (D[u] != INF) {
		if (D[u] + weight(u, v) < D[v]) {
			D[v] = D[u] + weight(u, v);
		}
	}
}

bool Bellman_Ford(int s = n) {
	initialize_B_F();
	/// adding one more node
	for (int k = 0; k < n; ++k)
		for (int u = 0; u <= n; ++u)
			for (auto [v,w] : G[u])
				relax(u, v);
	/// for every node
	for (int u = 0; u <= n; ++u)
		for (auto [v,w] : G[u])
			if (D[u] + w < D[v])
				return false;
	return true;
	return true;
}

vector<int> newD;

void dijkstra(const vector<vector<pair<int, int>>>& G, const vector<vector<int>>& altG, int s) {
	priority_queue<pair<int, int>, vector<pair<int, int>>, greater<pair<int, int>>> Q;
	vector<bool> V(n, false);
	newD = vector<int>(n, INF);
	newD[start] = 0;

	Q.push(make_pair(0, start));

	while (!Q.empty()) {
		int nod = Q.top().second;
		Q.pop();

		if (!V[nod]) {
			V[nod] = true;
			for (const auto& x : G[nod]) {
				int neighbor = x.first;
				int cost = x.second;
				if (cost == -1) continue;  // is infinity

				int newDist = newD[nod] + altG[nod][neighbor];
				if (newD[neighbor] > newDist) {
					newD[neighbor] = newDist;
					Q.push({newDist, neighbor});
				}
			}
		}
	}
}

void Johnson() {
	if (Bellman_Ford(start) == false)
		return;
	vector<vector<int>> alteredGraph(n, vector<int>(n, 0));
	// modify the weights of the edges to remove negative weights
	for (int i = 0; i < n; ++i)
		for (int j = 0; j < n; ++j)
			alteredGraph[i][j] = weight(i, j) + D[i] - D[j];
	dijkstra(G, alteredGraph, start);
}

int main(int argc, const char * argv[]) {
	if (argc != 3) { std::cout << "Inlaid number of arguments"; return 1; }
	/// ------
	std::ifstream fin(argv[1]);
	std::ofstream fout(argv[2]);

	fin >> n >> m >> start;
	G = vector<vector<pair<int, int>>>(n);
	for (int i = 0, x, y, c; i < m; ++i) {
		fin >> x >> y >> c;
		G[x].push_back(make_pair(y, c));
	}

	Johnson();

	for (int i = 0; i < n; ++i) {
		if (newD[i] == INF) {
			fout << "INF ";
		}
		else fout << newD[i] << ' ';
	}

	fin.close();
	fout.close();
	return 0;
}