#pragma once
#include "QtWidgets/qwidget.h"
#include "QtWidgets/qboxlayout.h"
#include "QtWidgets/qtablewidget.h"
#include "QtWidgets/qpushbutton.h"
#include "QtWidgets/qlineedit.h"
#include "Service.h"

class Ui : public QWidget {
private:
	//Proprietatile ui-ului aplicatiei
	Service service;

	QHBoxLayout* mainLy = new QHBoxLayout{};
	QVBoxLayout* secondLy = new QVBoxLayout{};
	QTableWidget* table = new QTableWidget{};
	QLineEdit* input = new QLineEdit{};

	QPushButton* filtrareBrand = new QPushButton{ "Filtrare brand" };
	QPushButton* filtrareCod = new QPushButton{ "Filtrare cod" };
	QPushButton* nefiltrat = new QPushButton{ "Nefiltrat" };

	//Functia initializeaza ui-ul aplicatiei
	void initGUI() {
		this->setLayout(mainLy);
		
		table->setColumnCount(3);

		mainLy->addWidget(table);

		secondLy->addWidget(input);
		secondLy->addWidget(filtrareBrand);
		secondLy->addWidget(filtrareCod);
		secondLy->addWidget(nefiltrat);

		mainLy->addLayout(secondLy);
	}

	//Functia realizeaza conectarile dintre signal si slot-urile
	//folosite in ui-ul aplicatiei
	void connect() {
		QObject::connect(table, &QTableWidget::itemSelectionChanged, [&]() {
			QVariant data = table->currentItem()->data(Qt::UserRole);
			input->setText(QString::fromStdString(((Produs*)data.data())->getCod()));
		});

		QObject::connect(filtrareBrand, &QPushButton::clicked, [&]() {
			string input = this->input->text().toStdString();
			this->reloadList(this->service.filtrareBrand(input));
		});

		QObject::connect(filtrareCod, &QPushButton::clicked, [&]() {
			string input = this->input->text().toStdString();
			this->reloadList(this->service.filtrareCod(input));
		});

		QObject::connect(nefiltrat, &QPushButton::clicked, [&]() {
			this->reloadList(this->service.getAll());
		});
	}

	void reloadList(vector<Produs> v) {
		table->clear();
		table->setSelectionBehavior(QAbstractItemView::SelectRows);
		QStringList headers = { "Tip", "Brand", "Consum" };
		table->setHorizontalHeaderLabels(headers);
		table->setRowCount((int) v.size());

		int currentRow = 0;
		for (Produs& p: v) {
			QVariant data;
			data.setValue(p);

			QTableWidgetItem* tip = new QTableWidgetItem{};
			tip->setText(QString::fromStdString(p.getTip()));
			tip->setData(Qt::UserRole, data);

			QTableWidgetItem* brand = new QTableWidgetItem{};
			brand->setText(QString::fromStdString(p.getBrand()));

			QTableWidgetItem* consum = new QTableWidgetItem{};
			consum->setText(QString::fromStdString(p.getConsum()));

			if (tip->text().toStdString() == "frigider") {
				tip->setBackground(Qt::cyan);
				brand->setBackground(Qt::cyan);
				consum->setBackground(Qt::cyan);
			}
			else if (tip->text().toStdString() == "aragaz") {
				tip->setBackground(Qt::gray);
				brand->setBackground(Qt::gray);
				consum->setBackground(Qt::gray);
			}
			else if (tip->text().toStdString() == "cuptor") {
				tip->setBackground(Qt::magenta);
				brand->setBackground(Qt::magenta);
				consum->setBackground(Qt::magenta);
			}
			else if (tip->text().toStdString() == "masina de spalat") {
				tip->setBackground(Qt::green);
				brand->setBackground(Qt::green);
				consum->setBackground(Qt::green);
			}

			table->setItem(currentRow, 0, tip);
			table->setItem(currentRow, 1, brand);
			table->setItem(currentRow, 2, consum);
			
			currentRow++;
		}
	}

public:
	Ui(Service service) : service{ service } {
		initGUI();
		connect();
		reloadList(service.getAll());
	}
};
