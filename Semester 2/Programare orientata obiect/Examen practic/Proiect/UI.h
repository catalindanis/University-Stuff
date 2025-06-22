#include "QtWidgets/qwidget.h"
#include "QtWidgets/qlayout.h"
#include "QtWidgets/qformlayout.h"
#include "QtWidgets/qtablewidget.h"
#include "QtWidgets/qlistwidget.h"
#include "QtWidgets/qlineedit.h"
#include "QtWidgets/qlabel.h"
#include "QtWidgets/qpushbutton.h"
#include "Service.h"
#include "Observer.h"
#include <sstream>

#pragma once

//Fisierul contine definitia ui-ului aplicatiei

class Fereastra : public QWidget, public Observer {
private:
	//Campurile ferestrei secundare de ui
	Service& service;
	string stare;
	QListWidget* list = new QListWidget();
	QPushButton* closed = new QPushButton("closed");
	QPushButton* inprogress = new QPushButton("inprogress");
	QPushButton* open = new QPushButton("open");

	//Functia initializeaza intreaga parte de ui a ferestrei secundare
	void initGUI();
	
	//Functia initializeaza signal-urile pentru butoane
	void initConnects();

	//Functia reincarca lista cu task-urile specifice starii ferestrei
	void reload();
public:
	//Constructorul ferestrei secundare de ui
	Fereastra(string stare, Service& service) : stare(stare), service(service) {
		initGUI();
		initConnects();
	}

	//Functia care este apelata atunci cand se face o modificare in service
	void update() override {
		reload();
	}
};

class Ui : public QWidget, public Observer {
private:
	//Campurile ferestrei principale
	Service& service;

	QHBoxLayout* mainLy = new QHBoxLayout();
	QTableWidget* table = new QTableWidget();
	QLineEdit* idL = new QLineEdit();
	QLineEdit* descriereL = new QLineEdit();
	QLineEdit* stareL = new QLineEdit();
	QLineEdit* progL = new QLineEdit();

	QLineEdit* searchL = new QLineEdit();
	QPushButton* searchBtn = new QPushButton("Cauta");

	QPushButton* addBtn = new QPushButton("Adauga");

	//Functia initializeaza intreaga parte de ui a ferestrei principale
	void initGUI();

	//Functia initializeaza signal-urile pentru butoane
	void initConnects();

	//Functia reincarca tabelul cu task-urile primite ca parametru
	//tasks : noua lista de task-uri
	void reload(vector<Task> tasks);

public:

	//Constructorul ferestrei principale de ui
	Ui(Service& service) : service(service) {
		initGUI();
		initConnects();
	}

	//Functia care este apelata atunci cand se face o modificare in service
	void update() override {
		if (this->searchL->text().isEmpty())
			this->reload(this->service.getAll());
		else
			this->reload(this->service.search(this->searchL->text().toStdString()));
	}
};

