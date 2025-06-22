#include "Tests.h"
#include "Repository.h"
#include "Service.h"
#include "assert.h"

void Tests::runAll() {
	Tests::runDomain();
	Tests::runRepository();
	Tests::runService();
}

void Tests::runDomain() {
	Music music(1, "test", "test", 2);
	assert(music.getId() == 1);
	assert(music.getArtist() == "test");
	assert(music.getTitlu() == "test");
	assert(music.getRank() == 2);

	music.setTitlu("whatever");
	assert(music.getTitlu() == "whatever");

	music.setRank(5);
	assert(music.getRank() == 5);
}

void Tests::runRepository() {
	FileRepository repo("tests.txt");

	assert(repo.getAll().size() == 3);
	assert(repo.getById(1).getArtist() == "autor");
	assert(repo.getById(6).getRank() == 10);

	repo.update(6, Music(100, "t", "a", 2));
	assert(repo.getById(100).getArtist() == "a");
	assert(repo.getById(100).getTitlu() == "t");
	assert(repo.getById(100).getRank() == 2);
	assert(repo.getById(100).getId() == 100);

	repo.remove(100);
	assert(repo.getAll().size() == 2);

	try {
		repo.update(6, Music(6, "title", "author", 3));
		assert(false);
	}
	catch (exception&) {
		assert(true);
	}

	try {
		repo.remove(4);
		assert(false);
	}
	catch (exception&) {
		assert(true);
	}

	try {
		repo.getById(4);
		assert(false);
	}
	catch (exception&) {
		assert(true);
	}

	repo.add(Music(6, "titlu3", "autor3", 10));
	assert(repo.getAll().size() == 3);
}

void Tests::runService() {
	FileRepository repo{ "tests2.txt" };
	Service service{ repo };

	assert(service.getNumberOfMusicsByRank(5) == 2);
	vector<Music> rez = service.getAll();

	assert(rez[0].getId() == 1);
	assert(rez[1].getId() == 2);
	assert(rez[2].getId() == 3);

	service.update(3, "ana", 1);

	rez = service.getAll();
	assert(rez[0].getId() == 3);
	assert(rez[1].getId() == 1);
	assert(rez[2].getId() == 2);

	service.update(3, "titlu3", 5);

	service.remove(3);
	assert(service.getNumberOfMusicsByRank(5) == 1);

	try {
		service.remove(7);
		assert(false);
	}
	catch (exception&) {
		assert(true);
	}
	try {
		service.remove(1);
		assert(false);
	}
	catch (exception&) {
		assert(true);
	}



	service.add(3, "titlu3", "autor3", 5);
}