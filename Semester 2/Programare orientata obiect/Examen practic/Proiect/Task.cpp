#include "Task.h"

int Task::getId() const {
	return this->id;
}

string Task::getDescrere() const {
	return this->descriere;
}

vector<string> Task::getProgramatori() const {
	return this->programatori;
}

string Task::getStare() const {
	return this->stare;
}

void Task::setStare(string stare) {
	this->stare = stare;
}