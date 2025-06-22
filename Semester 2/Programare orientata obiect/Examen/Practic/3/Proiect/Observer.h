#include <vector>
using namespace std;

#pragma once

class Observer {
public:
	virtual void update() = 0;
};

class Observable {
private:
	vector<Observer*> obs;
public:
	void notify() { for (const auto& o : obs) o->update(); }
	void addObserver(Observer* obs) { this->obs.push_back(obs); }
};