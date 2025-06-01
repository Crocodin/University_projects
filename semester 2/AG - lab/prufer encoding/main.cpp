#include <algorithm>
#include <iostream>
#include <fstream>
#include <vector>
#include <queue>
using std::vector;

#define ROOT (-1)

void deBug(const std::vector<int>& arr) {
	for (const auto& i : arr) {
		std::cout << i << " ";
	}
	std::cout << std::endl;
}

int main(const int argc, const char *argv[]) {
	if (argc != 3) { std::cerr << "Usage: " << argv[0] << " <input_file> <output_file>\n"; }

	std::ifstream fin(argv[1], std::ios::in);
	if (!fin) { std::cerr << "Error opening file " << argv[1] << "\n"; }

	int n; fin >> n;
	vector<int> degrees(n, 0);
	vector<int> parents(n);
	for (int i = 0; i < n; i++) {
		fin >> parents[i]; degrees[parents[i]]++;
		if (parents[i] != ROOT) degrees[i]++;
	}
	deBug(degrees);
	deBug(parents);
	fin.close();

	std::priority_queue<int, std::vector<int>, std::greater<>> Q;
	for (int i = 0; i < n; i++)
		if (degrees[i] == 1 and parents[i] != ROOT) Q.push(i);

	vector<int> prufer;
	while (!Q.empty()) {
		int leaf = Q.top(); Q.pop();
		int parent = parents[leaf];
		prufer.push_back(parent);
		degrees[parent]--; degrees[leaf]--;
		if (degrees[parent] == 1 and parents[parent] != ROOT) {
			Q.push(parent);
		}
	}

	std::ofstream fout(argv[2], std::ios::out);
	if (!fout) { std::cerr << "Error opening file " << argv[2] << "\n"; }
	deBug(prufer);
	fout << prufer.size() << '\n';
	for (const auto &i : prufer) fout << i << ' ';
	fout.close();
	return 0;
}