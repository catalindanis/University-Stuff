#include "Service.h"

void Service::add(int id, string titlu, string artist, int rank) {
	this->repo.add(Music(id, titlu, artist, rank));
	this->notify();
}

void Service::update(int id, string titlu, int rank) {
	Music music = this->repo.getById(id);
	this->repo.update(id, Music(id, titlu, music.getArtist(), rank));
	this->notify();
}

void Service::remove(int id) {
	Music music = this->repo.getById(id);

	if (this->getNumberOfMusicsByRank(music.getRank()) == 1)
		throw exception("Ultima melodie a artistului!");

	this->repo.remove(id);
	this->notify();
}

vector<Music> Service::getAll() {
	vector<Music> rez = this->repo.getAll();
	sort(rez.begin(), rez.end(),
		[](Music& m1, Music& m2) { return m1.getRank() < m2.getRank(); });
	return rez;
}

int Service::getNumberOfMusicsByRank(int rank) {
	int counter = 0;
	for (const auto& e : this->repo.getAll())
		if (e.getRank() == rank)
			counter++;
	return counter;
}





