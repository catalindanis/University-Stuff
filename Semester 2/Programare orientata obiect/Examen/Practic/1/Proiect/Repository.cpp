#include "Repository.h"

void FileRepository::load() {
	this->v.clear();

	ifstream in(this->filename);

	if (!in.good()) {
		ofstream fout(this->filename);
		fout.close();
	}

	int id, rank;
	string titlu, artist;

	while (in >> id) {
		in >> titlu;
		in >> artist;
		in >> rank;

		this->add(Music(id, titlu, artist, rank));
	}

	in.close();
}

void FileRepository::save() const {
	ofstream out(this->filename);

	for (const auto& m : this->v) {
		out << m.getId() << '\n';
		out << m.getTitlu() << '\n';
		out << m.getArtist() << '\n';
		out << m.getRank() << '\n';
	}

	out.close();
}

void FileRepository::add(Music music) {
	this->v.push_back(music);
	this->save();
}

void FileRepository::update(const int id, Music music) {
	Music& m = this->getById(id);
	m = music;
	this->save();
}

void FileRepository::remove(const int id) {
	int position = -1;

	for (int i = 0; i < this->v.size(); i++)
		if (this->v[i].getId() == id)
			position = i;

	if (position == -1)
		throw exception("Melodia nu exista!");

	this->v.erase(this->v.begin() + position);

	this->save();
}

Music& FileRepository::getById(const int id) {
	for (auto& m : this->v)
		if (m.getId() == id)
			return m;
	throw exception("Melodia nu exista!");
}

vector<Music> FileRepository::getAll() {
	return this->v;
}

