#pragma once

// nu se poate importa iterator.h in stiva.h pentru ca stiva.h este importat in iterator.h
// asa ca doar specificam ca exista clasa Iterator, dar nu e implementata in acest fisier.
class Iterator;

typedef int TElem;
class Stiva
{
	friend class Iterator;  // pentru ca clasa Iterator sa acceseze elements si size care sunt private

	private:
		/* aici reprezentarea */
		TElem *elements;
		int size;
		int capacity;

		void realloc(int capacity);
	public:
		Stiva();

		//adauga un element in stiva
		void adauga(TElem e);

		//acceseaza elementul cel mai devreme introdus in stiva
		//arunca exceptie daca coada e vida
		TElem element() const;

		//sterge elementul cel mai recent introdus in stivasi returneaza elementul sters (principiul LIFO)
		//arunca exceptie daca stiva e vida
		TElem sterge();

		//verifica daca stiva e vida;
		bool vida() const;

		// operatie in plus: verifica daca elem apare în stiva
		bool cautare(TElem elem) const;

		//returneaza un iterator pe colectie
		Iterator iterator() const;

		// destructorul stivei
		~Stiva();
};
