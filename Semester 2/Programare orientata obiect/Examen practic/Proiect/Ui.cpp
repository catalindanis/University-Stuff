#include "UI.h"

void Ui::reload(vector<Task> tasks){
	table->clear();
	table->setRowCount(tasks.size());
	table->setColumnCount(4);

	QStringList l = { "Id", "Descriere", "Stare", "Nr programatori" };
	table->setHorizontalHeaderLabels(l);

	int row = 0;
	for (const auto& t : tasks) {
		QTableWidgetItem* id = new QTableWidgetItem{};
		id->setText(QString::number(t.getId()));

		QTableWidgetItem* descriere = new QTableWidgetItem{};
		descriere->setText(QString::fromStdString(t.getDescrere()));

		QTableWidgetItem* stare = new QTableWidgetItem{};
		stare->setText(QString::fromStdString(t.getStare()));

		QTableWidgetItem* nrProgramatori = new QTableWidgetItem{};
		nrProgramatori->setText(QString::number(t.getProgramatori().size()));

		table->setItem(row, 0, id);
		table->setItem(row, 1, descriere);
		table->setItem(row, 2, stare);
		table->setItem(row, 3, nrProgramatori);

		row++;
	}
}

void Ui::initConnects() {
	QObject::connect(addBtn, &QPushButton::clicked, [&]() {
		int id = this->idL->text().toInt();
		string descriere = this->descriereL->text().toStdString();
		string stare = this->stareL->text().toStdString();
		vector<string> programatori;

		string input = this->progL->text().toStdString();
		std::istringstream iss(input);
		std::string programator;

		while (iss >> programator) {
			programatori.push_back(programator);
		}

		this->service.add(id, descriere, programatori, stare);
		});

	QObject::connect(searchBtn, &QPushButton::clicked, [&]() {
		update();
		});
}

void Ui::initGUI() {
	this->service.addObserver(this);

	this->resize(600, 600);
	this->setLayout(mainLy);

	mainLy->addWidget(table);

	table->setSelectionMode(QAbstractItemView::SingleSelection);

	QVBoxLayout* addLy = new QVBoxLayout();

	QFormLayout* fLy = new QFormLayout();
	fLy->addRow("Id", idL);
	idL->setValidator(new QIntValidator());
	fLy->addRow("Descriere", descriereL);
	fLy->addRow("Stare", stareL);
	fLy->addRow("Programatori", progL);

	addLy->addLayout(fLy);
	addLy->addWidget(addBtn);

	QFormLayout* sLy = new QFormLayout();
	sLy->addRow("Nume programator", searchL);

	addLy->addStretch();
	addLy->addLayout(sLy);
	addLy->addWidget(searchBtn);
	mainLy->addLayout(addLy);

	reload(this->service.getAll());

	Fereastra* fereastra1 = new Fereastra("closed", this->service);
	Fereastra* fereastra2 = new Fereastra("inprogress", this->service);
	Fereastra* fereastra3 = new Fereastra("open", this->service);

	fereastra1->show();
	fereastra2->show();
	fereastra3->show();
}

void Fereastra::initGUI() {
	this->resize(500, 400);
	this->setWindowTitle(QString::fromStdString(this->stare));

	QVBoxLayout* mainLy = new QVBoxLayout();

	mainLy->addWidget(list);

	QHBoxLayout* secondLy = new QHBoxLayout();

	secondLy->addWidget(closed);
	secondLy->addWidget(inprogress);
	secondLy->addWidget(open);

	mainLy->addLayout(secondLy);

	this->setLayout(mainLy);
	this->reload();

	this->service.addObserver(this);
}

void Fereastra::initConnects() {
	QObject::connect(closed, &QPushButton::clicked, [&]() {
		if (this->list->selectedItems().count() > 0) {
			int id = stoi(this->list->selectedItems().at(0)->data(Qt::DisplayRole).toString().toStdString());

			this->service.updateStare(id, "closed");
		}
		});

	QObject::connect(inprogress, &QPushButton::clicked, [&]() {
		if (this->list->selectedItems().count() > 0) {
			int id = stoi(this->list->selectedItems().at(0)->data(Qt::DisplayRole).toString().toStdString());

			this->service.updateStare(id, "inprogress");
		}
		});

	QObject::connect(open, &QPushButton::clicked, [&]() {
		if (this->list->selectedItems().count() > 0) {
			int id = stoi(this->list->selectedItems().at(0)->data(Qt::DisplayRole).toString().toStdString());

			this->service.updateStare(id, "open");
		}
		});
}

void Fereastra::reload() {
	list->clear();

	for (const auto& t : this->service.getByStare(this->stare)) {
		QListWidgetItem* itm = new QListWidgetItem();
		itm->setData(Qt::DisplayRole, QVariant(t.getId()));
		itm->setText(QString::fromStdString(to_string(t.getId()) + " | " + t.getDescrere() + " | " + t.getStare()));
		this->list->addItem(itm);
	}
}