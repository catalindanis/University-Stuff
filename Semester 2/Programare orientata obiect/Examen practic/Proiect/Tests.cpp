#include "Tests.h"
#include "Task.h"
#include "Repository.h"
#include "Service.h"
#include "assert.h"

void Test::runAllTests() {
	runDomainTests();
	runRepositoryTests();
	runServiceTests();
}

void Test::runDomainTests() {
	Task t1(1, "descriere", { "p1", "p2", "p3" }, "open");

	assert(t1.getId() == 1);
	assert(t1.getDescrere() == "descriere");
	assert(t1.getProgramatori()[0] == "p1");
	assert(t1.getProgramatori()[1] == "p2");
	assert(t1.getProgramatori()[2] == "p3");
	assert(t1.getStare() == "open");

	t1.setStare("closed");
	assert(t1.getStare() == "closed");
}

void Test::runRepositoryTests() {
	Repository repository{ "tests.txt" };

	assert(repository.getAll().size() == 2);
	Task t1 = repository.getById(1);

	assert(t1.getId() == 1);
	assert(t1.getDescrere() == "descriere1");
	assert(t1.getProgramatori()[0] == "prog1");
	assert(t1.getProgramatori()[1] == "prog2");
	assert(t1.getProgramatori()[2] == "prog3");
	assert(t1.getStare() == "open");

	try {
		t1 = repository.getById(3);
		assert(false);
	}
	catch (exception&) {
		assert(true);
	}

	try {
		repository.remove(3);
		assert(false);
	}
	catch (exception&) {
		assert(true);
	}

	try {
		repository.update(3, Task(-1, "", { "" }, ""));
		assert(false);
	}
	catch (exception&) {
		assert(true);
	}

	repository.update(2, Task(2, "", { "" }, ""));
	t1 = repository.getById(2);

	assert(t1.getId() == 2);
	assert(t1.getDescrere() == "");
	assert(t1.getProgramatori().size() == 1);
	assert(t1.getProgramatori()[0] == "");
	assert(t1.getStare() == "");

	repository.remove(2);
	repository.add(Task(2,
		"descriere2",
		{"prog1", "prog2"},
		"closed"));
}

void Test::runServiceTests() {
	Repository repository{ "tests2.txt" };
	Service service{ repository };

	assert(service.getAll().size() == 2);
	assert(service.getAll()[0].getId() == 2);
	assert(service.getAll()[1].getId() == 1);

	assert(service.getByStare("open").size() == 1);
	assert(service.getByStare("closed").size() == 1);
	assert(service.getByStare("inprogress").size() == 0);

	assert(service.search("prog1").size() == 2);
	assert(service.search("prog3").size() == 1);

	assert(service.idExists(1) == true);
	assert(service.idExists(3) == false);

	service.updateStare(2, "inprogress");
	assert(service.getByStare("inprogress").size() == 1);
	assert(service.getByStare("closed").size() == 0);
	service.updateStare(2, "closed");
	service.updateStare(-1, "");
	service.updateStare(2, "");

	int oldLength = service.getAll().size();
	service.add(1, "", { "" }, "");
	assert(service.getAll().size() == oldLength);

	service.add(3, "", { "" }, "");
	assert(service.getAll().size() == oldLength);

	service.add(3, "test", {}, "");
	assert(service.getAll().size() == oldLength);

	service.add(3, "test", { "prog1" }, "");
	assert(service.getAll().size() == oldLength);

	service.add(3, "test", { "prog1" }, "closed");
	assert(service.getAll().size() == oldLength + 1);

	repository.remove(3);
}