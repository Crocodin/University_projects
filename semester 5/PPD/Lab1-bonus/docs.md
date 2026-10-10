## Concurrent double linked list

We all know what a double linked list, every node has a reference to the node in front and the node behind the current node.

We have to managed four scenarios!
### Move Next/Previous
Given a pointer to the current element, get the position of the next.

What scenarios do we need to treat hear? Well... given the fact that there might be an operation happening on the element direct next to this one or even an insert after this one we can assume that there might be a mutex on the next pointer of every node.

Because of this the structure of a node will look (for now) something like this:
```c++
class Node<T> {
    Node* prev;
    std::mutex prev_mutex;
    
    Node* next;
    std::mutec next_mutex;
    
    T value;
}
```

### Insert After
Now this is the tricky part. We need an order: first lock the node that you will change and after lock urself. NO! This will not work!

The easy solution would be just to use `std::scoped_lock`.
