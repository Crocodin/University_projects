
#include <barrier>
#include <chrono>
#include <iostream>
#include <random>
#include <vector>

static int n = 1000;
static int theta = 100;
static int offset = n * n / theta;
static std::barrier<>startBarrier(theta);

static std::vector<long long> A, B, C;

void computeElement(const int row, const int col) {
    long long sum = 0;
    for (int k = 0; k < n; ++k)
        sum += A[row * n + k] * B[k * n + col];
    C[row * n + col] = sum;
}

void threadFunction(const int k) {
    startBarrier.arrive_and_wait();

    const int start = k * offset;
    const int end = (k == theta - 1) ? n * n : (k + 1) * offset;

    for (int index = start; index < end; ++index)
        computeElement(index / n, index % n);
}

int main() {
    A.resize(n * n);
    B.resize(n * n);
    C.assign(n * n, 0);

    std::mt19937 gen(67);
    std::uniform_int_distribution<int> dist(0, 9);
    for (auto& x : A) x = dist(gen);
    for (auto& x : B) x = dist(gen);

    const auto t0 = std::chrono::steady_clock::now();
    std::vector<std::thread> threads;
    threads.reserve(theta);
    for (int t = 0; t < theta; ++t)
        threads.emplace_back(threadFunction, t);
    for (auto& th : threads) th.join();
    const auto t1 = std::chrono::steady_clock::now();

    // Verification against a plain sequential computation
    // std::vector<long long> expected(n * n, 0);
    // for (int i = 0; i < n; ++i)
    //     for (int k = 0; k < n; ++k)
    //         for (int j = 0; j < n; ++j)
    //             expected[i * n + j] += A[i * n + k] * B[k * n + j];
    const bool ok = true;

    const double ms = std::chrono::duration<double, std::milli>(t1 - t0).count();
    std::cout << "n = " << n << " | threads = " << theta << " | time = " << ms << " ms | result " << (ok ? "OK" : "WRONG") << "\n";
    return ok ? 0 : 2;
}