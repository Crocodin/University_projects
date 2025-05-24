#ifndef OBSERVER_HPP
#define OBSERVER_HPP

#include <vector>

class Observer {
public:
	virtual void update() = 0;
	virtual ~Observer() = default;
};

class Subject {
private:
	std::vector<Observer*> observers;
public:
	void addObserver(Observer* observer) {
		observers.push_back(observer);
	}

	void removeObserver(Observer* observer) {
		std::erase(observers,observer);
	}

	void notifyObservers() const {
		for (Observer* observer : observers) {
			observer->update();
		}
	}
};

#endif //OBSERVER_HPP
