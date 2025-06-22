#pragma once
#include <exception>
#include <string>
using std::exception;
using std::string;

class BookCreationException : public exception {
private:
	string message;
public:
	BookCreationException(const string& message) : message(message) {}

	const char* what() const noexcept override {
		return this->message.c_str();
	}
};

class BookRepositoryException : public exception {
private:
	string message;
public:
	BookRepositoryException(const string& message) : message(message) {}

	const char* what() const noexcept override {
		return this->message.c_str();
	}
};

