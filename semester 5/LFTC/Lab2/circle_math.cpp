#include <iostream>

struct Circle {
	float radius;
};

int main() {
	float p, a, pi;
	Circle c;


	pi = 3.14;
	std::cin >> c.radius;
	p = 2 * pi * c.radius;
	a = pi * c.radius * c.radius;
	std::cout << p;
	std::cout << '\n';
	std::cout << a;
	std::cout << '\n';
	return 0;
}
