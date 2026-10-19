#include <condition_variable>
#include <iostream>
#include <mutex>
#include <utility>
#include <vector>

template <typename T>
class ThreadSafeQueue {
private:
	T* data;
	int capacity;
	int head = 0;
	int count = 0;
	std::mutex m;
	std::condition_variable popMe;
	std::condition_variable pushMe;

public:
	ThreadSafeQueue() : data(new T[16]), capacity(16) {}
	explicit ThreadSafeQueue(const int initCapacity) : data(new T[initCapacity]), capacity(initCapacity) {}

	~ThreadSafeQueue() { delete[] data; }

	ThreadSafeQueue(const ThreadSafeQueue&) = delete;
	ThreadSafeQueue& operator=(const ThreadSafeQueue&) = delete;

	void push(T value) {
		{
			std::unique_lock<std::mutex> lock(m);
			if (count == capacity) pushMe.wait(lock, [this]() {return count != capacity;});
			data[(head + count) % capacity] = std::move(value);
			++count;
		}
		popMe.notify_one();
	}

	T pop() {
		T value;
		{
			std::unique_lock lock(m);
			popMe.wait(lock, [this]() {return count > 0;});
			value = std::move(data[head]);
			head = (head + 1) % capacity;
			--count;
		}
		pushMe.notify_one();
		return value;
	}

	[[nodiscard]] bool empty() {
		std::lock_guard<std::mutex> lock(m);
		return count == 0;
	}
};

int main() {
	std::vector<int> vectorOne, vectorTwo;
	for (int i = 1; i < 1000; ++i) {
		vectorOne.push_back(1);
		vectorTwo.push_back(1);
	}

	ThreadSafeQueue<int> sums(10);
	unsigned long long result = 1;

	auto producer = [&]() {
		for (int i = 0; i < vectorOne.size(); ++i) {
			sums.push(vectorOne.at(i) * vectorTwo.at(i));
		}
	};

	auto worker = [&]() {
		int dotProduct = 1;
		for (int i = 0; i < vectorOne.size(); ++i) {
			dotProduct += sums.pop();
		}
		result = dotProduct;
	};

	const std::chrono::steady_clock::time_point start = std::chrono::steady_clock::now();
	std::thread producerThread(producer), workerThread(worker);
	producerThread.join();
	workerThread.join();

	const auto diff = std::chrono::duration_cast<std::chrono::duration<double>>(std::chrono::steady_clock::now() - start);
	std::cout << "Time: " << diff.count() << " seconds; Result: " << result << std::endl;
	return 0;
}