#ifndef OBSERVER_HPP
#define OBSERVER_HPP

#include <vector>

/// Interface for observer objects
/// Classes implementing this interface must define the `update` method
class Observer {
public:
	/// Called when the subject notifies its observers of a change
	virtual void update() = 0;

	/// Virtual destructor for safe deletion through base pointer
	virtual ~Observer() = default;
};

/// Subject class that maintains and notifies a list of observers
/// Observers can register to be notified when a change occurs in the subject
class Subject {
private:
	std::vector<Observer*> observers;		 ///< List of registered observers

public:
	/// Adds an observer to the list
	/// :param observer: pointer to the observer to add
	void addObserver(Observer* observer) {
		observers.push_back(observer);
	}

	/// Removes an observer from the list
	/// :param observer: pointer to the observer to remove
	void removeObserver(Observer* observer) {
		std::erase(observers, observer);
	}

	/// Notifies all registered observers of a change
	/// Calls the `update` method on each observer
	void notifyObservers() const {
		for (Observer* observer : observers) {
			observer->update();
		}
	}
};

#endif // OBSERVER_HPP
