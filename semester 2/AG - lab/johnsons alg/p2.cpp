#include <fstream>
#include <iostream>
#include <queue>
#include <vector>
#include <tuple>
#include <algorithm>
#define INF 100000000
using namespace std;

int n, m;
vector<vector<pair<int, int>>> G;
vector<int> D;                                  /// Bellman-Ford distances
vector<vector<int>> allDists;                   /// Distance matrix
vector<tuple<int, int, int>> reweightedEdges;

void initialize_B_F() {
    D = vector<int>(n + 1, INF);
    G.push_back({});                       /// adding the new node
    for (int i = 0; i < n; ++i)
        G[n].emplace_back(i, 0);
    D[n] = 0;
}

int weight(const int u, const int v) {
    for (const auto& it : G[u]) {
        if (it.first == v)
            return it.second;
    }
    return INF;
}

void relax(const int u, const int v, const int w) {
    if (D[u] != INF && D[u] + w < D[v]) {
        D[v] = D[u] + w;
    }
}

bool Bellman_Ford() {
    initialize_B_F();
    for (int k = 0; k < n; ++k)
        for (int u = 0; u <= n; ++u)
            for (auto [v, w] : G[u])
                relax(u, v, w);

    for (int u = 0; u <= n; ++u)
        for (auto [v, w] : G[u])
            if (D[u] != INF && D[u] + w < D[v])
                return false;
    return true;
}

vector<int> dijkstra(int start, const vector<vector<int>>& altG) {
    priority_queue<pair<int, int>, vector<pair<int, int>>, greater<>> Q;
    vector<int> newD(n, INF);
    vector<bool> V(n, false);
    newD[start] = 0;
    Q.emplace(0, start);

    while (!Q.empty()) {
        int nod = Q.top().second;
        Q.pop();
        if (V[nod]) continue;
        V[nod] = true;
        for (auto [neighbor, cost] : G[nod]) {
            if (cost == -1) continue; // infinite
            int newDist = newD[nod] + altG[nod][neighbor];
            if (newD[neighbor] > newDist) {
                newD[neighbor] = newDist;
                Q.emplace(newDist, neighbor);
            }
        }
    }
    return newD;
}

void Johnson(ofstream& fout) {
    if (!Bellman_Ford()) return;


    vector<vector<int>> alteredGraph(n, vector<int>(n, INF));
    for (int i = 0; i < n; ++i)
        for (auto [j, w] : G[i])
            if (j < n) {
                alteredGraph[i][j] = w + D[i] - D[j];
                reweightedEdges.emplace_back(i, j, alteredGraph[i][j]);
            }

    sort(reweightedEdges.begin(), reweightedEdges.end());
    for (auto [x, y, w] : reweightedEdges)
        fout << x << ' ' << y << ' ' << w << '\n';

    allDists.resize(n);
    for (int i = 0; i < n; ++i) {
        vector<int> res = dijkstra(i, alteredGraph);
        for (int j = 0; j < n; ++j) {
            if (res[j] == INF) res[j] = INF;
            else res[j] = res[j] - D[i] + D[j]; // unweighting
        }
        allDists[i] = res;
    }

    for (int i = 0; i < n; ++i) {
        for (int j = 0; j < n; ++j) {
            if (allDists[i][j] == INF) fout << "INF";
            else fout << allDists[i][j];
            if (j < n - 1) fout << ' ';
        }
        fout << '\n';
    }
}

int main(int argc, const char * argv[]) {
    if (argc != 3) {
        cout << "Invalid number of arguments";
        return 1;
    }

    ifstream fin(argv[1]);
    ofstream fout(argv[2]);

    fin >> n >> m;


    G = vector<vector<pair<int, int>>>(n);
    for (int i = 0, x, y, c; i < m; ++i) {
        fin >> x >> y >> c;
        G[x].emplace_back(y, c);
    }

    Johnson(fout);

    fin.close();
    fout.close();
    return 0;
}
