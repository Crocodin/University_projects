#include <fstream>
#include <iostream>
#include <queue>

int matrix[10000][10000], rows, cols, answer[10000][10000], __answer__[10000][10000];

void read_matrix(int &start_i, int &start_j, int &end_i, int &end_j) {
	int i { 0 }, j { 0 };
	// FILE* file = fopen("../labirint_cuvinte.txt", "r");
	FILE* file = fopen("../labirint_1.txt", "r");
	// FILE* file = fopen("../labirint_2.txt", "r");
	char character;
	while ((character = fgetc(file)) != EOF) {
		switch (character) {
			case '1':
				matrix[i][j++] = 1;
				break;
			case 'S':
				start_i = i;
				start_j = j;
				matrix[i][j++] = 0;
				break;
			case 'F':
				end_i = i;
				end_j = j;
				matrix[i][j++] = 0;
				break;
			case ' ':
				j++;
				break;
			case '\n':
				cols = std::max(cols, j);
				i++; j = 0;
				break;
			default:
				break;
		}
	}
	rows = i + 1;
	fclose(file);
}

void lee(int i, int j, int &end_i, int &end_j) {
	constexpr int move[] {0, 0, 1, -1, 1, -1, 0, 0};
	answer[i][j] = 1;
	std::queue<std::pair<int, int>> Q;
	Q.emplace(i, j);
	while (!Q.empty()) {
		i = Q.front().first;
		j = Q.front().second;
		Q.pop();
		for (int k = 0; k < 4; k++) {
			int ni = i + move[k];
			int nj = j + move[k + 4];
			if (ni >= 0 && ni < rows && nj >= 0 && nj < cols)
				if (matrix[ni][nj] == 0 && answer[ni][nj] == 0) { // Unvisited
					answer[ni][nj] = answer[i][j] + 1;
					Q.emplace(ni, nj);

					if (ni == end_i && nj == end_j) return;
				}
		}
	}
}

void rec(const int i, const int j) {
	if (answer[i][j] == 0) return;
	__answer__[i][j] = answer[i][j];
	constexpr int move[] {0, 0, 1, -1, 1, -1, 0, 0};
	for (int k = 0; k < 4; k++) {
		const int ni = i + move[k];
		const int nj = j + move[k + 4];
		if (ni >= 0 && ni < rows && nj >= 0 && nj < cols) {
			if (answer[ni][nj] >= answer[i][j])
				answer[ni][nj] = 0;
			rec(ni, nj);
		}
	}
}

int main() {
	int start_i, start_j, end_i, end_j;
	read_matrix(start_i, start_j, end_i, end_j);
	lee(start_i, start_j, end_i, end_j);


	rec(end_i, end_j);
	/// print answer
	for (int i_ = 0; i_ < rows; i_++) {
		for (int j_ = 0; j_ < cols; j_++) {
			if (__answer__[i_][j_] == 0)
				if (matrix[i_][j_] == 1)
					printf("%3d", matrix[i_][j_]);
				else printf("   ");
			else printf("\033[32m%3d\033[m", __answer__[i_][j_]);
		}
		std::cout << std::endl;
	}
}