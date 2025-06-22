#include "Validator.h"
#include <string>
using namespace std;

bool Validator::validateTitle(const string& title) const{
	for (auto ch : title)
		if (!isalnum(ch))
			return false;
	return !title.empty();
}


bool Validator::validateAuthor(const string& author) const{
	for (auto ch : author)
		if (!isalpha(ch))
			return false;
	return !author.empty();
}


bool Validator::validateType(const string& type) const{
	for (auto ch : type) 
		if (!isalpha(ch))
			return false;
	return !type.empty();
}


bool Validator::validateReleaseYear(const int& releaseYear) const{
	return releaseYear > 0 && releaseYear <= 2025;
}
