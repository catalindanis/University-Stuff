#include "Produs.h"

Produs::Produs(string cod, string tip, string brand, string consumEnergetic) :
	cod{ cod }, tip{ tip }, brand{ brand }, consumEnergetic{ consumEnergetic } {
}

string Produs::getCod() {
	return this->cod;
}

void Produs::setCod(string cod) {
	this->cod = cod;
}

string Produs::getBrand() {
	return this->brand;
}

void Produs::setBrand(string brand) {
	this->brand = brand;
}

string Produs::getTip() {
	return this->tip;
}

void Produs::setTip(string tip) {
	this->tip = tip;
}

string Produs::getConsum() {
	return this->consumEnergetic;
}

void Produs::setConsum(string consumEnergetic) {
	this->consumEnergetic = consumEnergetic;
}