#include <mutex>

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

	/// we assume that head is not nullptr
	Node* insert_after(Node* head, T data) {
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

	/// we assume that head is not nullptr
	Node* insert_before(Node* head, T data) {
		std::lock_guard<std::mutex> head_lock(head->prev_mutex);
		Node* prev_node = head->prev;

		Node* new_node = new Node<T>(data, head, prev_node);
		if (prev_node != nullptr) {
			std::lock_guard<std::mutex> prev_lock(prev_node->next_mutex);
			prev_node->next = new_node;
		}

		head->prev = new_node;
		return new_node;
	}
};
