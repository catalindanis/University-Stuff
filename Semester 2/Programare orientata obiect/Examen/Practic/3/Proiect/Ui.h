#include "Service.h"
#include "qwidget.h"
#include "QtWidgets/qtableview.h"
#include "QtWidgets/qlayout.h"
#include "QtWidgets/qformlayout.h"
#include "QtWidgets/qlineedit.h"
#include "QtWidgets/qpushbutton.h"
#include "QtWidgets/qmessagebox.h"
#include "QtWidgets/qslider.h"
#include "QtWidgets/qlabel.h"
#include "TableModel.h"

#pragma once

class Fereastra : public QWidget, public Observer {
	Q_OBJECT
private:
	string type;
	QLabel* value = new QLabel();
	Service& service;

	void initUi() {
		QVBoxLayout* main = new QVBoxLayout();
		this->setWindowTitle(QString::fromStdString(type));

		main->addWidget(value);

		this->setLayout(main);

		this->resize(200, 200);
		update();
	}

public:
	Fereastra(string type, Service& service) : type(type), service(service) {
		initUi();
	}

	void update() override {
		this->value->setText(QString::number(this->service.getNumberOfType(this->type)));
	}
};

class Ui : public QWidget, public Observer {
	Q_OBJECT
private:
	Service& service;
	QTableView* tableView = new QTableView();
	TableModel* model;
	QLineEdit* id = new QLineEdit();
	QLineEdit* nume = new QLineEdit();
	QLineEdit* tip = new QLineEdit();
	QLineEdit* pret = new QLineEdit();
	QPushButton* addBtn = new QPushButton("Adauga");
	QSlider* slider = new QSlider(Qt::Horizontal);
	QLabel* sliderVal = new QLabel();

	void initUi() {
		model = new TableModel(this->service);

		QHBoxLayout* main = new QHBoxLayout();
		QVBoxLayout* addLy = new QVBoxLayout();
		QFormLayout* formLy = new QFormLayout();

		formLy->addRow("Id", id);
		id->setValidator(new QIntValidator(1, 1000, this));
		formLy->addRow("Nume", nume);
		formLy->addRow("Tip", tip);
		formLy->addRow("Pret", pret);
		pret->setValidator(new QDoubleValidator(1.0, 100.0, 5, this));

		slider->setMaximum(100);

		addLy->addLayout(formLy);
		addLy->addWidget(sliderVal);
		addLy->addWidget(slider);
		addLy->addWidget(addBtn);

		tableView->setModel(model);

		main->addWidget(tableView);
		main->addLayout(addLy);
		this->setLayout(main);

		for (const auto& p : this->service.getNumberOfTypes()) {
			Fereastra* fereastra = new Fereastra(p.first, this->service);
			this->service.addObserver(fereastra);

			fereastra->show();
		}
	}

	void initConnects() {
		QObject::connect(addBtn, &QPushButton::clicked, [&]() {
			try {
				int id = this->id->text().toInt();
				string nume = this->nume->text().toStdString();
				string tip = this->tip->text().toStdString();
				double pret = this->pret->text().toDouble();
				this->service.add(id, nume, tip, pret);
			}
			catch (string& err) {
				QMessageBox::warning(this, "Eroare", err.c_str());
			}
		});

		QObject::connect(slider, &QSlider::valueChanged, [&]() {
			this->sliderVal->setText(QString::number(this->slider->value()));
			update();
		});
	}
public:
	Ui(Service& service) : service(service) {
		service.addObserver(this);
		initUi();
		initConnects();
	}

	void update() override {
		this->repaint();
		this->model->reload(this->slider->value());
	}
};

