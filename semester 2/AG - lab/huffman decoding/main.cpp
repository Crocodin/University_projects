#include <iostream>
#include <fstream>
#include <vector>
#include <queue>
#include <unordered_map>
#include <string>
using namespace std;

char text[10001];
unordered_map<char, string> huffmanCode;

struct node {
	char data, minChar;
	int fr;
	node *left, *right;
	explicit node(const int fr, const char data) : data(data), fr(fr) {
		left = right = nullptr;
		minChar = data;
	}
	explicit node(const int freq, node* l, node* r)
		: data('\0'), fr(freq), left(l), right(r) {
		minChar = std::min(l->minChar, r->minChar);
	}
};

struct CmpNode {
	bool operator()(const node *a, const node *b) const {
		if (a->fr != b->fr) return a->fr > b->fr;
		return a->minChar > b->minChar;
	}
};

node* buildTree(const vector<pair<char, int>> &freq) {
	priority_queue<node*, vector<node*>, CmpNode> pq;
	for (auto &[c, f] : freq) {
		pq.push(new node(f, c));
	}

	if (pq.empty()) return nullptr;

	while (pq.size() > 1) {
		node *a = pq.top(); pq.pop();
		node *b = pq.top(); pq.pop();
		pq.push(new node(a->fr + b->fr, a, b));
	}

	return pq.top();
}

string decodeText(node* root) {
	string decoded;
	node *current = root;

	for (const char& bit : text) {
		if (bit == '0') current = current->left;
		else if (bit == '1') current = current->right;
		else continue;

		if (current->left == nullptr && current->right == nullptr) {
			decoded += current->data;
			current = root;
		}
	}

	return decoded;
}

int main(const int argc, const char *argv[]) {
	if (argc != 3) { cerr << "Usage: decode <input_file> <output_file>\n"; return 1; }

	ifstream fin(argv[1]);
	if (!fin) { cerr << "Error opening input file\n"; return 1; }

	vector<pair<char, int>> freq;
	int n; fin >> n;

	for (int i = 0; i < n; i++) {
		char ch; int nr; fin >> ch >> nr;
		freq.emplace_back(ch, nr);
	}
	fin.getline(text, 10001); // consume leftover newline
	fin.getline(text, 10001); // actual text line

	node* root = buildTree(freq);
	string decoded = decodeText(root);

	ofstream fout(argv[2], ios::binary);
	if (!fout) { cerr << "Error writing output file\n"; return 1; }
	fout << decoded;
	cout << decoded;
	fout.close();
}