#include <fstream>
#include <iostream>
#include <queue>
#include <vector>
#include <algorithm>
using namespace std;

int n, m, start;
vector<vector<pair<int, int>>> G;
vector<int> D;
vector<bool> V;
vector<int> T;

void dijkstra() {
	V = vector<bool>(n, false);
	D = vector<int>(n, -1); // in this case -1 is infinity
	T = vector<int>(n, -1);
	priority_queue<pair<int, int>, vector<pair<int, int>>, greater<pair<int, int>>> Q;

	D[start] = 0;
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

				int newDist = D[nod] + cost;
				if (D[neighbor] > newDist) {
					D[neighbor] = newDist;
					T[neighbor] = nod;
					Q.push({newDist, neighbor});
				}
			}
		}
	}
	/// path
	int finish = 4; /// for testing
	if (D[finish] < 0) {
		cout << "No path from " << start << " to " << finish << "\n";
		return;
	}
	vector<int> path;
	for (int cur = finish; cur != -1; cur = T[cur]) {
		path.push_back(cur);
	}
	reverse(path.begin(), path.end());
	cout << '\n';
	for (const auto& x : path) {
		cout << x << ' ';
	}
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

	dijkstra();

	for (int i = 0; i < n; ++i)
		if (D[i] == -1) fout << "INFINITY" << ' ';
		else fout << D[i] << ' ';

	fin.close();
	fout.close();
	return 0;
}
