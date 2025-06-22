#include <vector>
#include "Observer.h"
#pragma once

class Observable {
private:
	vector<Observer*> observers;
public:
	void addObserver(Observer* obs) {
		observers.push_back(obs);
	}
protected:
	void notify() {
		for (auto obs : observers)
			obs->update();
	}
};
