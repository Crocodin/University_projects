### Some sneaky code to implement graph traversing
```c++
void preorder(Node* root) {	/// (root, left, right)
	std::stack<Node*> s;
	s.push(root);
	while (!s.empty()) {
		Node* temp = s.top();
		s.pop();
		std::cout << temp->data << " ";
		if (temp->right != nullptr) s.push(temp->right);
		if (temp->left != nullptr) s.push(temp->left);
	}
}
```
```c++
void inorder(Node* root) { /// (left, root, right)
	std::stack<Node*> s;
	Node* temp = root;
	while (!s.empty() || root != nullptr) {
		while (temp != nullptr) { /// we go to the left'est part of the graph
			s.push(temp);
			temp = temp->left;
		}
		temp = s.top();
		s.pop();
		std::cout << temp->data << " ";
		temp = temp->right;
	}
}
```
```c++
void postorder(Node* root) { /// (left, right, root)
	std::stack<Node*> s;
	s.push(root);
	while (!s.empty()) {
		Node* curr = s.top();
		s.pop();

		if (curr != nullptr) {
			/// First push the node itself again, with a marker after children
			s.push(curr);		/// push the node again
			s.push(nullptr);  /// this null acts as a marker

			if (curr->right) s.push(curr->right);
			if (curr->left) s.push(curr->left);
		} else {
			/// Next node is the one we revisit after both children
			Node* node = s.top();
			s.pop();
			std::cout << node->data << " ";
		}
	}
}
```
