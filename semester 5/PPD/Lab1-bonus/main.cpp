#include <atomic>
#include <iostream>
#include <mutex>
#include <random>
#include <thread>

template <typename T> class Node {
private:
	std::mutex prev_mutex, next_mutex;

public:
	Node* prev, *next;
	T data;

	explicit Node(T data, Node* prev = nullptr, Node* next = nullptr)
		: prev(prev), next(next), data(data) {}

	static Node* move_next(Node* head) {
		std::lock_guard<std::mutex> lock(head->next_mutex);
		return head->next;
	}

	static Node* move_prev(Node* head) {
		std::lock_guard<std::mutex> lock(head->prev_mutex);
		return head->prev;
	}

	/// head is not nullptr
	static Node* insert_after(Node* head, T data) {
		std::lock_guard<std::mutex> head_lock(head->next_mutex);
		Node* next_node = head->next;

		Node* new_node = new Node<T>(data, head, next_node);

		if (next_node != nullptr) {
			std::lock_guard<std::mutex> next_lock(next_node->prev_mutex);
			next_node->prev = new_node;
		}
		head->next = new_node;
		return new_node;
	}

	/// head is not nullptr
	static Node* insert_before(Node* head, T data) {
		std::lock_guard<std::mutex> head_lock(head->prev_mutex);
		Node* prev_node = head->prev;

		Node* new_node = new Node<T>(data, prev_node, head);
		if (prev_node != nullptr) {
			std::lock_guard<std::mutex> prev_lock(prev_node->next_mutex);
			prev_node->next = new_node;
		}

		head->prev = new_node;
		return new_node;
	}
};

int main() {
    auto* head = new Node<int>(1, nullptr, nullptr);
    auto* current = new Node<int>(2, nullptr, nullptr);
    head->next = current;
    current->prev = head;
    for (int i = 3; i <= 10; ++i) {
        auto* aux = new Node<int>(i, nullptr, nullptr);
        current->next = aux;
        aux->prev = current;
        current = aux;
    }

    std::atomic<int> counter(11);
    constexpr int THREADS = 10;
    constexpr int ITERATIONS = 100;

    auto worker = [&](const int id) {
        std::mt19937 gen(id);
        Node<int>* node = head;

        for (int i = 0; i < ITERATIONS; ++i) {
            switch (gen() % 4) {
            	case 0: {
            		Node<int>::insert_after(node, counter.fetch_add(1));
            		break;
            	}
				case 1: {
					Node<int>* next = Node<int>::move_next(node);
					if (next != nullptr) node = next;
            		break;
				}
            	case 2: {
            		Node<int>::insert_before(node, counter.fetch_add(1));
            		break;
            	}
            	case 3: {
            		Node<int>* before = Node<int>::move_prev(node);
            		if (before != nullptr) node = before;
            		break;
            	}
				default: ;
			}
        }
    };

    std::vector<std::thread> threads;
    for (int t = 0; t < THREADS; ++t)
        threads.emplace_back(worker, t);
    for (auto& th : threads) th.join();

	Node<int>* start = head;
	while (start->prev != nullptr) start = start->prev;

    int count = 0;
    bool consistent = true;
    for (Node<int>* n = start; n != nullptr; n = n->next) {
        ++count;
        if (n->next != nullptr && n->next->prev != n) consistent = false;
    }

    std::cout << "Nodes in list: " << count << "\n"
              << "Expected:      " << counter.load() - 1 << "\n"
              << "Links valid:   " << (consistent ? "yes" : "NO") << "\n";

    for (Node<int>* n = head; n != nullptr;) {
        Node<int>* next = n->next;
        delete n;
        n = next;
    }
    return 0;
}