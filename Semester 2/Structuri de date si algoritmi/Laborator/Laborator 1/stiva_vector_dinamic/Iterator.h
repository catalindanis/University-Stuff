#pragma once
#include "Stiva.h"

class Iterator{
	friend class Stiva; // pentru ca clasa Stiva sa aiba acces la constructorul de Iterator care e privat
	private:

		//constructorul primeste o referinta catre Stiva
		//iteratorul va referi elementul din varful stivei

		Iterator(const Stiva& stiva);  // constructorul este privat, deci un iterator se poate instantia dintr-un friend class

		//contine o referinta catre containerul pe care il itereaza
		const Stiva& stiva;
		/* aici e reprezentarea  specifica a iteratorului */

		int position;

	public:

		//reseteaza pozitia iteratorului la inceputul containerului
		void prim();

		//muta iteratorul in container
		// arunca exceptie daca iteratorul nu e valid
		void urmator();

		//verifica daca iteratorul e valid (indica un element al containerului)
		bool valid() const;

		//returneaza valoarea elementului din container referit de iterator
		//arunca exceptie daca iteratorul nu e valid
		TElem element() const;
};


