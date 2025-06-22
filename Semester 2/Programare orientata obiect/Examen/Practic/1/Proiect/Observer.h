#pragma once

class Observer {
public:
	virtual void update() = 0;
};

class Observable {
private:
	vector<Observer*> obs;
public:
	void addObserver(Observer* obs) { this->obs.push_back(obs); }
	void notify() { for (const auto& o : this->obs) o->update(); }
};