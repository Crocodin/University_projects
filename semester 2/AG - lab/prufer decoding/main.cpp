#include <iostream>
#include <fstream>
#include <vector>
#include <unordered_set>
using namespace std;

void deBug(const vector<int>& input) {
	for (const int& i : input)
		cout << i << ' ';
	cout << '\n';
}

int lowest_int_notIn(const std::vector<int>& Q) {
	const std::unordered_set<int> elements(Q.begin(), Q.end());
	int lowest = 0;
	while (elements.contains(lowest)) {
		++lowest;
	}
	return lowest;
}

int main(const int argc, const char *argv[]) {
	if (argc != 3) { std::cerr << argv[0] << " <input file> <output file>\n"; return 1; }
	std::ifstream fin(argv[1], std::ios::in);
	if (!fin.is_open()) { std::cerr << "Error opening " << argv[1] << '\n'; return 1; }

	int n; fin >> n;
	vector<int> prufer;
	for (int i = 0, tmp; i < n; i++) {
		fin >> tmp; prufer.push_back(tmp);
	}
	fin.close();

	vector<int> decoding(n + 1, -1);
	for (int i = 0; i < n; i++) {
		int lowest = lowest_int_notIn(prufer);
		decoding[lowest] = prufer[0];
		prufer.erase(prufer.begin());
		prufer.push_back(lowest);
	}

	deBug(decoding);
	std::ofstream fout(argv[2], std::ios::out);
	if (!fout.is_open()) { std::cerr << "Error opening " << argv[2] << '\n'; return 1; }
	fout << decoding.size() << '\n';
	for (const auto& it : decoding)
		fout << it << ' ';
	fout.close();
	return 0;
}