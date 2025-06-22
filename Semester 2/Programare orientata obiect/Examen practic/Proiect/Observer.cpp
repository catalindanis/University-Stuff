#include "Observer.h"

void Observable::notify() {
	for (const auto& o : this->obs)
		o->update();
}

void Observable::addObserver(Observer* obs) { 
	this->obs.push_back(obs); 
}
