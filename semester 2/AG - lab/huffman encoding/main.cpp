#include <iostream>
#include <fstream>
#include <vector>
#include <queue>
#include <unordered_map>
#include <string>
using namespace std;

char text[10001];
unordered_map<char, string> huffmanCode;
int fr[256] = {};

struct node {
	char data, minChar;
	int fr;
	node *left, *right;
	explicit node(int fr, char data) : data(data), fr(fr) {
		left = right = nullptr;
		minChar = data;
	}
	explicit node(int freq, node* l, node* r)
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

void buildCodeMap(const node* root, const string& str) {
	if (root == nullptr) return;

	if (root->left == nullptr && root->right == nullptr) {
		huffmanCode[root->data] = (str.empty() ? "0" : str);
		return;
	}

	buildCodeMap(root->left,  str + '0');
	buildCodeMap(root->right, str + '1');
}

int main(const int argc, const char *argv[]) {
	if (argc != 3) { cerr << argv[0] << " <input file> <output file>" << endl; return 1; }

	/// 1) get text to encode
	ifstream fin(argv[1], ios::in);
	if (!fin.is_open()) { cerr << "Error in opening file " << argv[1] << endl; return 1; }
	fin.read(text, 10001);
	text[fin.gcount()] = '\0';
	fin.close();

	/// 2) make a node freq vector
	for (int i = 0; text[i] != '\0'; i++)
		fr[text[i]]++;
	vector<node*> freq;
	for (int i = 0; i < 256; i++)
		if (fr[i] != 0) freq.push_back( new node(fr[i], static_cast<char>(i)));

	priority_queue<node*, vector<node*>, CmpNode> pq(freq.begin(), freq.end());

	/// 3) build the huffman tree
	while (pq.size() > 1) {
		auto left  = pq.top(); pq.pop();
		auto right = pq.top(); pq.pop();
		pq.push(new node(left->fr + right->fr, left, right));
	}
	node *root = pq.top();

	/// 4) code map
	buildCodeMap(root, "");

	/// 5) encode the text itself
	string encoded;
	for (int i = 0; text[i] != '\0'; i++) {
		encoded += huffmanCode[text[i]];
	}

	/// 6) write the info
	ofstream fout(argv[2], ios::out);
	if (!fout.is_open()) { cerr << "Error in opening file " << argv[2] << endl; return 1; }

	for (int c = 0; c < 256; c++)
		if (fr[c] != 0) fout << static_cast<char>(c) << ' ' << fr[c] << '\n';
	fout << encoded;
	cout << encoded;
	fout.close();
	return 0;
}