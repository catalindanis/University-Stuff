#include "Tests.h"
#include "qdebug.h"

void Tests::runAllTests() {
	runDomainTests();
	runRepositoryTests();
	runServiceTests();
}

void Tests::runDomainTests() {
	Produs p{ "1", "tip", "brand", "100" };

	assert(p.getCod() == "1");
	assert(p.getTip() == "tip");
	assert(p.getBrand() == "brand");
	assert(p.getConsum() == "100");
	
	p.setCod("2");
	p.setTip("tipp");
	p.setBrand("brandd");
	p.setConsum("101");

	assert(p.getCod() == "2");
	assert(p.getTip() == "tipp");
	assert(p.getBrand() == "brandd");
	assert(p.getConsum() == "101");
}

void Tests::runRepositoryTests() {
	Repository repository{ "test.txt" };

	assert(repository.getAll().size() == 4);

	assert(repository.getAll()[0].getCod() == "1");
	assert(repository.getAll()[0].getTip() == "frigider");
	assert(repository.getAll()[0].getBrand() == "bosch");
	assert(repository.getAll()[0].getConsum() == "100");
}

void Tests::runServiceTests() {
	Repository repository{ "test.txt" };
	Service service{ repository };

	assert(service.getAll().size() == 4);

	assert(service.filtrareBrand("bosch").size() == 1);
	assert(service.filtrareCod("1").size() == 1);

	assert(service.filtrareBrand("tefal")[0].getCod() == "3");
	assert(service.filtrareBrand("tefal")[0].getBrand() == "tefal");
	assert(service.filtrareBrand("tefal")[0].getTip() == "cuptor");
	assert(service.filtrareBrand("tefal")[0].getConsum() == "80");

	assert(service.filtrareCod("4")[0].getCod() == "4");
	assert(service.filtrareCod("4")[0].getBrand() == "test");
	assert(service.filtrareCod("4")[0].getTip() == "masina de spalat");
	assert(service.filtrareCod("4")[0].getConsum() == "150");
}