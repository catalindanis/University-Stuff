#include "qwidget.h"
#include "QtWidgets/qlayout.h"
#include "QtWidgets/qlineedit.h"
#include "QtWidgets/qslider.h"
#include "QtWidgets/qlabel.h"
#include "QtWidgets/qformlayout.h"
#include "QtWidgets/qpushbutton.h"
#include "QtWidgets/qmessagebox.h"
#include "qdebug.h"
#include "qpainter.h"
#include "Model.h"

#pragma once

class Ui : public QWidget, public Observer {
private:
	Service& service;
	QTableView* view = new QTableView{};
	MusicModel* model;
	QLineEdit* title = new QLineEdit{};
	QSlider* slider = new QSlider{Qt::Horizontal};
	QPushButton* del = new QPushButton{"Sterge"};
	QPushButton* updateB = new QPushButton{"Actualizeaza"};

	void initLayout() {
		this->resize(1000, 500);
		QWidget* el = new QWidget{};
		QHBoxLayout* ly = new QHBoxLayout{};

		view->setModel(model);
		view->setSelectionBehavior(QAbstractItemView::SelectRows);
		view->setSelectionMode(QAbstractItemView::SingleSelection);

		QFormLayout* form = new QFormLayout{};
		form->addRow("Titlu", title);
		form->addRow("Rank", slider);

		QVBoxLayout* v = new QVBoxLayout{};
		v->addStretch();
		v->addLayout(form);

		QHBoxLayout* h = new QHBoxLayout{};
		h->addWidget(del);
		h->addWidget(updateB);

		v->addLayout(h);

		slider->setMinimum(0);
		slider->setMaximum(10);

		QVBoxLayout* v1 = new QVBoxLayout{};
		v1->addStretch();
		v1->addWidget(view);

		ly->addLayout(v1);
		ly->addLayout(v);
		v1->addStretch();
		v->addStretch();

		view->setMaximumHeight(200);
		

		this->setLayout(ly);
	}

	void initConnections() {
		QObject::connect(updateB, &QPushButton::clicked, [&]() {
			if (this->view->selectionModel()->selectedRows().count() > 0) {
				int id = this->model->data(this->model->index(this->view->selectionModel()->currentIndex().row(), 0)).toInt();
				string titlu = this->title->text().toStdString();
				int rank = this->slider->value();
				this->service.update(id, titlu, rank);
			}
		});

		QObject::connect(view->selectionModel(), &QItemSelectionModel::selectionChanged, [&]() {
			title->setText(this->model->data(this->model->index(this->view->selectionModel()->currentIndex().row(), 1)).toString());
			slider->setValue(this->model->data(this->model->index(this->view->selectionModel()->currentIndex().row(), 3)).toInt());
		});

		QObject::connect(del, &QPushButton::clicked, [&]() {
			if (this->view->selectionModel()->selectedRows().count() > 0) {
				QVariant id = this->model->data(
					this->model->index(this->view->selectionModel()->currentIndex().row(), 0),
					Qt::DisplayRole);
				try {
					this->service.remove(id.toInt());
				}
				catch (exception& e) {
					QMessageBox::warning(this, 
						QString::fromStdString("Eroare"),
						QString::fromStdString(e.what()));
				}
			}
		});
	}

	void paintEvent(QPaintEvent* event) override {
		QPainter p{ this };

		QPoint tl{ 20, 20 };
		p.drawEllipse(tl, 20, 20);
		p.drawEllipse(tl, 25, 25);
	}

public:
	Ui(Service& service) : service{ service }
	{
		model = new MusicModel(service, this);
		service.addObserver(model);
		service.addObserver(this);
		initLayout();
		initConnections();
	}

	void update() override {
		repaint();
	}
};