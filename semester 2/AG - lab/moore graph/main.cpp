#include <fstream>
#include <queue>
#include <iostream>
typedef unsigned int uint;

void recursiv_print(uint* P, const uint current, const uint start) {
	if (current == start)
		std::cout << start << " ";
	else {
		recursiv_print(P, P[current], start);
		std::cout << current << " ";
	}
}

int main() {
	std::ifstream file("../graph.txt");

	uint nodes;
	file >> nodes;

	uint matrix[nodes][nodes], in, out;
	while (file >> in >> out)
		matrix[in][out] = 1;

	uint L[nodes], P[nodes];
	std::queue<uint> Q;
	for (uint i = 0; i < nodes; i++)
		L[i] = -1;

	uint start, end;
	std::cout << "Start at & end at:" << std::endl;
	std::cin >> start >> end;

	bool stop = false;
	L[start] = 0;
	Q.push(start);
	while (!Q.empty() && !stop) {
		uint u = Q.back();
		for (uint i = 0; i < nodes && !stop; i++) {
			if (matrix[u][i] == 1 and L[i] == -1) {
				P[i] = u;
				L[i] = L[u] + 1;
				Q.push(i);
			}
			if (u == end) stop = true;
		}
		Q.pop();
	}

	if (L[end] == -1) std::cout << "No path found" << std::endl;
	else {
		std::cout << "Path found of length " << L[end] + 1 << std::endl;
		/// print the path
		recursiv_print(P, end, start);
	}

	file.close();
	return 0;
}