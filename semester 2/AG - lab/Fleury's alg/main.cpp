#include <iostream>
#include <fstream>
#include <vector>
using namespace std;

vector<vector<int>> adjList;
vector<int> path;

int N, M;

void removeEdge(const int u, const int v) {
	auto rmIn = [](const int uIn, const int vIn) {
		int i = 0; while (i < adjList[uIn].size()) {
			if (adjList[uIn][i] == vIn) break;
			++i;
		}
		adjList[uIn].erase(adjList[uIn].begin() + i);
	};

	rmIn(u, v);
	rmIn(v, u);
}

void addEdge(const int u, const int v) {
	adjList[u].push_back(v);
	adjList[v].push_back(u);
}

int dfs(const int u, vector<bool>& V) {
	int cnt = 1;
	V[u] = true;
	for (auto v : adjList[u]) {
		if (!V[v]) cnt += dfs(v, V);
	}
	return cnt;
}

bool isValid(int u, int v) {
	/// (u, v) is valid if:
	/// 1) is the only endge
	if (adjList[u].size() == 1) return true;

	/// 2) (u, v) is not a bridge
	vector<bool> V1(N, false);
	int cnt1 = dfs(u, V1);

	removeEdge(u, v);

	vector<bool> V2(N, false);
	int cnt2 = dfs(u, V2);

	addEdge(u, v);

	return cnt1 == cnt2;
}

void EulerPath(int u) {
	while (!adjList[u].empty()) {
		for (int i = 0; i < adjList[u].size(); ++i) {
			int v = adjList[u][i];
			if (isValid(u, v)) {
				removeEdge(u, v);
				EulerPath(v);
				break;
			}
		}
	}
	path.push_back(u);
}

int main(const int argc, const char *argv[]) {
	if (argc != 3) { cerr << argv[0] << " <in_file> <out_file>\n"; return 1; }

	ifstream fin(argv[1]);
	fin >> N >> M;
	adjList.resize(N);
	for (int i = 0, u, v; i < M; i++) {
		fin >> u >> v;
		adjList[u].push_back(v);
		adjList[v].push_back(u);
	}
	fin.close();

	int u = 0;
	for (int i = 0; i < N; ++i) {
		if (adjList[i].size() % 2 == 1) {
			u = i; break;
		}
	}
	EulerPath(u);

	ofstream fout(argv[2]);
	for (const auto& u : path)
		fout << u << ' ';
	fout.close();
	return 0;
}