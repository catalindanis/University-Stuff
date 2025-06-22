#pragma once

#include "Service.h"
#include "CartService.h"
#include "Observer.h"
#include "Observable.h"
#include "BookTableModel.h"
#include "BookListModel.h"
#include <stdlib.h>

#include <qpainter.h>
#include <QtWidgets/qwidget.h>
#include <QtWidgets/qboxlayout.h>
#include <QtWidgets/qlistwidget.h>
#include <QtWidgets/qtablewidget.h>
#include <QtWidgets/qformlayout.h>
#include <QtWidgets/qlineedit.h>
#include <QtWidgets/qpushbutton.h>
#include <QtWidgets/qlabel.h>
#include <QtWidgets/qmessagebox.h>
#include <qapplication.h>
#include <qvariant.h>
#include <qdebug.h>

string bookToString(const Book& book) {
	return book.getTitle() + ", " + book.getAuthor() + ", " +
		book.getType() + ", " + to_string(book.getReleaseYear());
}

class CartReadOnlyUI : public QWidget, Observer {
private:
	CartService& cart;

	void initGUI() {
		setLayout(new QHBoxLayout{});
		resize(QSize{ 200, 300 });
	}
protected:
	void paintEvent(QPaintEvent* ev) override {
		QPainter p{ this };

		int currentX = 0, currentY = 0, height = 20, width = 20;
		for (int i = 0; i < this->cart.getAllBooks().size(); i++) {
			if (currentX + height > this->width())
				currentX = 0, currentY += (height + 10);
			int number = rand() % 3;
			switch (number) {
			case 0:
				p.drawRect(currentX, currentY, width, height);
				break;
			case 1:
				p.drawEllipse(currentX, currentY, width, height);
				break;
			case 2:
				QPolygon triangle;
				triangle 
					<< QPoint(currentX + width / 2, currentY)
					<< QPoint(currentX, currentY + height)
					<< QPoint(currentX + width, currentY + height);
				p.drawPolygon(triangle);
				break;
			}
			currentX += (width + 10);
		}
	}
public:
	CartReadOnlyUI(CartService& cart) : cart{ cart } {
		cart.addObserver(this);
		initGUI();
	}
	
	void update() override {
		repaint();
	}
};

class CartCrudUI : public QWidget, Observer {
private:
	CartService& cart;

	QLineEdit* cartTitleLineEdit = new QLineEdit{};
	QPushButton* cartAddButton = new QPushButton{ "Adauga" };
	QPushButton* cartEmptyButton = new QPushButton{ "Goleste" };
	QPushButton* cartExportButton = new QPushButton{ "Export" };
	QPushButton* cartGenerateButton = new QPushButton{ "Genereaza" };
	QLabel* cartOperationMessage = new QLabel{ "" };
	QLineEdit* generateCartLineEdit = new QLineEdit{};
	QLineEdit* exportCartLineEdit = new QLineEdit{};

	QTableView* tableView = new QTableView{};
	BookTableModel* tableModel = new BookTableModel{};

	void initGUI() {
		QVBoxLayout* mainCartLayout = new QVBoxLayout{};

		QHBoxLayout* globalCartLayout = new QHBoxLayout{};
		this->setLayout(mainCartLayout);
		mainCartLayout->addLayout(globalCartLayout);

		globalCartLayout->addWidget(tableView);
		tableView->setModel(tableModel);
		
		QFormLayout* cartTitleFormLayout = new QFormLayout{};
		cartTitleFormLayout->addRow("Titlu", cartTitleLineEdit);

		QVBoxLayout* rightCartLayout = new QVBoxLayout{};
		globalCartLayout->addLayout(rightCartLayout);
		rightCartLayout->addLayout(cartTitleFormLayout);
		rightCartLayout->addWidget(cartAddButton);
		rightCartLayout->addWidget(cartEmptyButton);

		QHBoxLayout* generateLayout = new QHBoxLayout{};

		generateLayout->addWidget(cartGenerateButton);
		generateLayout->addWidget(generateCartLineEdit);
		generateCartLineEdit->setValidator(new QIntValidator{});

		QHBoxLayout* exportLayout = new QHBoxLayout{};

		exportLayout->addWidget(cartExportButton);
		exportLayout->addWidget(exportCartLineEdit);

		rightCartLayout->addLayout(generateLayout);
		rightCartLayout->addLayout(exportLayout);

		mainCartLayout->addWidget(cartOperationMessage);

		this->setWindowTitle(QString::fromStdString("CosCRUD"));
	}

	void initConnect() {
		QObject::connect(cartAddButton, &QPushButton::clicked, [&]() {
			string title = this->cartTitleLineEdit->text().toStdString();
			int prevLength = (int)this->cart.getAllBooks().size();
			this->cart.add(title);
			if (this->cart.getAllBooks().size() - prevLength == 1)
				cartOperationMessage->setText(QString::fromStdString("1 produs adaugat"));
			else if (this->cart.getAllBooks().size() - prevLength == 0)
				cartOperationMessage->setText(QString::fromStdString(this->cart.getAllBooks().size() - prevLength + "Niciun produs adaugat"));
			else
				cartOperationMessage->setText(QString::fromStdString(this->cart.getAllBooks().size() - prevLength + " produse adaugate"));
			});

		QObject::connect(cartEmptyButton, &QPushButton::clicked, [&]() {
			this->cart.deleteAll();
			this->cartOperationMessage->setText(QString::fromStdString("Cosul a fost golit"));
			});

		QPushButton::connect(cartGenerateButton, &QPushButton::clicked, [&]() {
			int noOfElements = (int)atoi(this->generateCartLineEdit->text().toStdString().c_str());
			int prevLength = (int)this->cart.getAllBooks().size();
			this->cart.generateCart(noOfElements);
			if (prevLength != (int)this->cart.getAllBooks().size())
				this->cartOperationMessage->setText("Cosul a fost generat");
			else
				this->cartOperationMessage->setText("Valoare invalida");
			});

		QPushButton::connect(cartExportButton, &QPushButton::clicked, [&]() {
			if (this->exportCartLineEdit->text().toStdString() != "") {
				this->cart.saveToFile(this->exportCartLineEdit->text().toStdString());
				this->cartOperationMessage->setText("Cosul a fost exportat");
			}
			else
				this->cartOperationMessage->setText("Valoare invalida");
			});
	}
public:
	CartCrudUI(CartService& cart) : cart{ cart } {
		cart.addObserver(this);
		initGUI();
		initConnect();
	}
	void update() override {
		tableModel->setBooks(this->cart.getAllBooks());
	}
};


class ManagerUI : public QWidget {
private:
	Service& service;
	CartService& cart;

	QListWidget* list = new QListWidget;

	QLineEdit* titleLineEdit = new QLineEdit;
	QLineEdit* authorLineEdit = new QLineEdit;
	QLineEdit* typeLineEdit = new QLineEdit;
	QLineEdit* releaseYearLineEdit = new QLineEdit;

	QPushButton* addButton = new QPushButton{ "Adauga" };
	QPushButton* deleteButton = new QPushButton{ "Sterge" };
	QPushButton* updateButton = new QPushButton{ "Modifica" };
	QPushButton* undoButton = new QPushButton{ "Undo" };
	QPushButton* searchByTitleButton = new QPushButton{ "Cauta dupa titlu" };
	QPushButton* filterByTitleButton = new QPushButton{ "Filtrare dupa titlu" };
	QPushButton* filterByReleaseYearButton = new QPushButton{ "Filtrare dupa anul aparitiei" };
	QPushButton* sortByTitleButton = new QPushButton{ "Sortare dupa titlu" };
	QPushButton* sortByAuthorButton = new QPushButton{ "Sortare dupa autor" };
	QPushButton* sortByReleaseYearAndTypeButton = new QPushButton{ "Sortare dupa anul aparitei si gen" };
	QPushButton* resetButton = new QPushButton{ "Resetare" };

	QLineEdit* cartTitleLineEdit = new QLineEdit{};
	QPushButton* cartAddButton = new QPushButton{ "Adauga in cos" };
	QPushButton* cartEmptyButton = new QPushButton{ "Goleste cos" };
	QLineEdit* generateCartLineEdit = new QLineEdit{};
	QPushButton* cartGenerateButton = new QPushButton{ "Genereaza cos" };

	QLabel* operationMessage = new QLabel{ "" };

	QHBoxLayout* mainLayout = new QHBoxLayout{};

	QPushButton* cartCrudOpenButton = new QPushButton{ "Cos CRUD" };
	QPushButton* cartReadOnlyOpenButton = new QPushButton{ "Cos ReadOnly" };

	CartCrudUI* cartCRUDWidget;
	CartReadOnlyUI* cartReadOnlyWidget;

	QPushButton* exitButton = new QPushButton{ "Exit" };

	QListView* listView = new QListView{};
	BookListModel* listModel = new BookListModel{};

	void initGUI() {
		QVBoxLayout* globalLayout = new QVBoxLayout{};
		QHBoxLayout* secondLayout = new QHBoxLayout{};
		setLayout(globalLayout);
		resize(QSize{ 1000, 400 });
		setWindowTitle(QString::fromStdString("Manager"));

		QVBoxLayout* rightLayout = new QVBoxLayout{};

		QFormLayout* inputLayout = new QFormLayout{};
		inputLayout->addRow("Titlu", titleLineEdit);
		inputLayout->addRow("Autor", authorLineEdit);
		inputLayout->addRow("Gen", typeLineEdit);
		releaseYearLineEdit->setValidator(new QIntValidator(1, 2025));
		inputLayout->addRow("An aparitie", releaseYearLineEdit);

		QVBoxLayout* buttonsLayout = new QVBoxLayout{};
		QHBoxLayout* firstButtonsRowLayout = new QHBoxLayout{};

		firstButtonsRowLayout->addWidget(addButton);
		firstButtonsRowLayout->addWidget(deleteButton);
		firstButtonsRowLayout->addWidget(updateButton);
		firstButtonsRowLayout->addWidget(undoButton);

		QHBoxLayout* secondButtonsRowLayout = new QHBoxLayout{};

		secondButtonsRowLayout->addWidget(searchByTitleButton);
		secondButtonsRowLayout->addWidget(filterByTitleButton);
		secondButtonsRowLayout->addWidget(filterByReleaseYearButton);

		QHBoxLayout* thirdButtonsRowLayout = new QHBoxLayout{};
		thirdButtonsRowLayout->addWidget(sortByTitleButton);
		thirdButtonsRowLayout->addWidget(sortByAuthorButton);
		thirdButtonsRowLayout->addWidget(sortByReleaseYearAndTypeButton);

		buttonsLayout->addLayout(firstButtonsRowLayout);
		buttonsLayout->addLayout(secondButtonsRowLayout);
		buttonsLayout->addLayout(thirdButtonsRowLayout);

		mainLayout->addWidget(listView);
		listView->setModel(listModel);
		listModel->setBooks(service.getAllBooks());
		//mainLayout->addWidget(list);
		
		rightLayout->addLayout(inputLayout);
		rightLayout->addStretch();
		rightLayout->addLayout(buttonsLayout);
		mainLayout->addLayout(rightLayout);
		globalLayout->addLayout(mainLayout);

		secondLayout->addWidget(cartCrudOpenButton);
		secondLayout->addWidget(cartReadOnlyOpenButton);
		secondLayout->addWidget(resetButton);
		secondLayout->addWidget(operationMessage);
		secondLayout->addStretch();
		secondLayout->addWidget(exitButton);
		globalLayout->addLayout(secondLayout);

		this->cartCRUDWidget = new CartCrudUI{ cart };
		this->cartReadOnlyWidget = new CartReadOnlyUI{ cart };

		reloadList(this->service.getAllBooks());
		this->listView->setCurrentIndex(QModelIndex{});

		rightLayout->addStretch();

		generateCartLineEdit->setValidator(new QIntValidator{});

		QFormLayout* row1 = new QFormLayout{};
		row1->addRow(cartTitleLineEdit, cartAddButton);
		row1->addRow(generateCartLineEdit, cartGenerateButton);
		row1->addRow(cartEmptyButton);

		rightLayout->addLayout(row1);
	}

	void initConnect() {

		QObject::connect(cartAddButton, &QPushButton::clicked, [&]() {
			string title = this->cartTitleLineEdit->text().toStdString();
			int prevLength = (int)this->cart.getAllBooks().size();
			this->cart.add(title);
			if (this->cart.getAllBooks().size() - prevLength == 1)
				operationMessage->setText(QString::fromStdString("1 produs adaugat"));
			else if (this->cart.getAllBooks().size() - prevLength == 0)
				operationMessage->setText(QString::fromStdString(this->cart.getAllBooks().size() - prevLength + "Niciun produs adaugat"));
			else
				operationMessage->setText(QString::fromStdString(this->cart.getAllBooks().size() - prevLength + " produse adaugate"));
			});

		QObject::connect(cartEmptyButton, &QPushButton::clicked, [&]() {
			this->cart.deleteAll();
			this->operationMessage->setText(QString::fromStdString("Cosul a fost golit"));
			});

		QPushButton::connect(cartGenerateButton, &QPushButton::clicked, [&]() {
			int noOfElements = (int)atoi(this->generateCartLineEdit->text().toStdString().c_str());
			int prevLength = (int)this->cart.getAllBooks().size();
			this->cart.generateCart(noOfElements);
			if (prevLength != (int)this->cart.getAllBooks().size())
				this->operationMessage->setText("Cosul a fost generat");
			else
				this->operationMessage->setText("Valoare invalida");
			});

		QObject::connect(exitButton, &QPushButton::clicked, [&]() {
			auto resp = QMessageBox::warning(this, "Iesire", "Sunteti sigur ca vreti sa iesiti?", 
				QMessageBox::Yes | QMessageBox::No, QMessageBox::No);
			if (resp == QMessageBox::Yes)
				QApplication::quit();
		});

		QObject::connect(cartCrudOpenButton, &QPushButton::clicked, [&]() {
			this->cartCRUDWidget->show();
		});

		QObject::connect(cartReadOnlyOpenButton, &QPushButton::clicked, [&]() {
			this->cartReadOnlyWidget->show();
		});

		QObject::connect(addButton, &QPushButton::clicked, [&]() {
			string title = this->titleLineEdit->text().toStdString();
			string author = this->authorLineEdit->text().toStdString();
			string type = this->typeLineEdit->text().toStdString();
			int releaseYear = this->releaseYearLineEdit->text().toInt();

			try {
				this->service.addBook(title, author, type, releaseYear);
				this->operationMessage->setText("Cartea a fost adaugata cu succes!");
				this->listModel->setBooks(this->service.getAllBooks());
			}
			catch (BookCreationException e) {
				this->operationMessage->setText(e.what());
			}
			catch (BookRepositoryException e) {
				this->operationMessage->setText(e.what());
			}
		});

		QObject::connect(deleteButton, &QPushButton::clicked, [&]() {
			if (listView->currentIndex().row() != -1) {
				const Book& book = this->listModel->getBook(listView->currentIndex().row());
				this->service.removeBook(book.getId());
				this->listModel->setBooks(this->service.getAllBooks());
				this->operationMessage->setText("Cartea a fost stearsa cu succes!");
			}
			else
				this->operationMessage->setText("Nu este selectata o carte!"); 
		});

		QObject::connect(updateButton, &QPushButton::clicked, [&]() {
			if (listView->currentIndex().row() != -1) {
				string title = this->titleLineEdit->text().toStdString();
				string author = this->authorLineEdit->text().toStdString();
				string type = this->typeLineEdit->text().toStdString();
				int releaseYear = this->releaseYearLineEdit->text().toInt();

				int index = this->listModel->getBook(listView->currentIndex().row()).getId();
				try {
					this->service.updateBook(index, title, author, type, releaseYear);
					this->operationMessage->setText("Cartea a fost actualizata cu succes!");
					this->listModel->setBooks(this->service.getAllBooks());
				}
				catch (BookCreationException e) {
					this->operationMessage->setText(e.what());
				}
				catch (BookRepositoryException e) {
					this->operationMessage->setText(e.what());
				}
			}
			else
				this->operationMessage->setText("Nu este selectata o carte!");
		});

		QPushButton::connect(undoButton, &QPushButton::clicked, [&]() {
			try {
				this->service.undoLastOperation();
				this->operationMessage->setText("Undo realizat cu succes!");
				this->listModel->setBooks(this->service.getAllBooks());
			}
			catch (exception e) {
				this->operationMessage->setText("Nu se mai poate face undo!");
			}
		});

		QObject::connect(searchByTitleButton, &QPushButton::clicked, [&]() {
			string title = titleLineEdit->text().toStdString();
			this->listModel->setBooks(this->service.searchBooksByTitle(title));
		});

		QObject::connect(filterByTitleButton, &QPushButton::clicked, [&]() {
			string title = titleLineEdit->text().toStdString();
			this->listModel->setBooks(this->service.filterBooksByTitle(title));
		});

		QObject::connect(filterByReleaseYearButton, &QPushButton::clicked, [&]() {
			int releaseYear = releaseYearLineEdit->text().toInt();
			this->listModel->setBooks(this->service.filterBooksByReleaseYear(releaseYear));
		});

		QObject::connect(sortByTitleButton, &QPushButton::clicked, [&]() {
			this->listModel->setBooks(this->service.sortedBooksByTitle());
		});

		QObject::connect(sortByAuthorButton, &QPushButton::clicked, [&]() {
			this->listModel->setBooks(this->service.sortedBooksByAuthor());
		});

		QObject::connect(sortByReleaseYearAndTypeButton, &QPushButton::clicked, [&]() {
			this->listModel->setBooks(this->service.sortedBooksByReleaseDateAndType());
		});

		QObject::connect(resetButton, &QPushButton::clicked, [&]() {
			this->operationMessage->setText("");
			this->titleLineEdit->setText("");
			this->authorLineEdit->setText("");
			this->typeLineEdit->setText("");
			this->releaseYearLineEdit->setText("");
			this->listModel->setBooks(this->service.getAllBooks());
		});
	}

	void reloadList(const vector<Book>& books) {		
		list->clear();
		for (const auto& book : books) {
			QListWidgetItem* item = new QListWidgetItem;
			QVariant data;
			data.setValue(book);
			item->setData(Qt::UserRole, data);
			item->setText(QString::fromStdString(bookToString(book)));

			list->addItem(item);
		}
	}

public:
	ManagerUI(Service& service, CartService& cart) : service{ service }, cart{ cart } {
		initGUI();
		initConnect();
	}
};
