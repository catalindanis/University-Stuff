#pragma once

#include "Service.h"
#include "CartService.h"

#include <QtWidgets/qwidget.h>
#include <QtWidgets/qboxlayout.h>
#include <QtWidgets/qlistwidget.h>
#include <QtWidgets/qformlayout.h>
#include <QtWidgets/qlineedit.h>
#include <QtWidgets/qpushbutton.h>
#include <QtWidgets/qlabel.h>
#include <qscrollarea.h>
#include <qvariant.h>
#include <qdebug.h>

class Ui : public QWidget {
private:
	Service& service;
	CartService& cart;

	QListWidget* list = new QListWidget;
	
	QLineEdit* titleLineEdit = new QLineEdit;
	QLineEdit* authorLineEdit = new QLineEdit;
	QLineEdit* typeLineEdit = new QLineEdit;
	QLineEdit* releaseYearLineEdit = new QLineEdit;

	QPushButton* addButton = new QPushButton{"Adauga"};
	QPushButton* deleteButton = new QPushButton{"Sterge"};
	QPushButton* updateButton = new QPushButton{"Modifica"};
	QPushButton* undoButton = new QPushButton{"Undo"};
	QPushButton* searchByTitleButton = new QPushButton{ "Cauta dupa titlu" };
	QPushButton* filterByTitleButton = new QPushButton{ "Filtrare dupa titlu" };
	QPushButton* filterByReleaseYearButton = new QPushButton{ "Filtrare dupa anul aparitiei" };
	QPushButton* sortByTitleButton = new QPushButton{ "Sortare dupa titlu" };
	QPushButton* sortByAuthorButton = new QPushButton{ "Sortare dupa autor" };
	QPushButton* sortByReleaseYearAndTypeButton = new QPushButton{ "Sortare dupa anul aparitei si gen" };
	QPushButton* resetButton = new QPushButton{ "Resetare" };

	QLabel* operationMessage = new QLabel{ "" };

	vector<Book> books;
	vector<Book> cartBooks;

	QHBoxLayout* mainLayout = new QHBoxLayout{};

	QPushButton* cartOpenButton = new QPushButton{"Cos de cumparaturi"};
	QWidget* cartWidget = new QWidget{};
	QListWidget* cartList = new QListWidget{};
	QLineEdit* cartTitleLineEdit = new QLineEdit{};
	QPushButton* cartAddButton = new QPushButton{"Adauga"};
	QPushButton* cartEmptyButton = new QPushButton{"Goleste"};
	QPushButton* cartExportButton = new QPushButton{"Export"};
	QPushButton* cartGenerateButton = new QPushButton{"Genereaza"};
	QLabel* cartOperationMessage = new QLabel{""};
	QLineEdit* generateCartLineEdit = new QLineEdit{};
	QLineEdit* exportCartLineEdit = new QLineEdit{};

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
		mainLayout->addWidget(list);
		rightLayout->addLayout(inputLayout);
		rightLayout->addLayout(buttonsLayout);
		mainLayout->addLayout(rightLayout);
		globalLayout->addLayout(mainLayout);

		secondLayout->addWidget(cartOpenButton);
		secondLayout->addWidget(resetButton);
		secondLayout->addWidget(operationMessage);
		secondLayout->addStretch(1);
		globalLayout->addLayout(secondLayout);

		QLabel* message = new QLabel{ "Sunteti sigur?" };

		books = this->service.getAllBooks();
		reloadList();

		QVBoxLayout* mainCartLayout = new QVBoxLayout{};

		QHBoxLayout* globalCartLayout = new QHBoxLayout{};
		cartWidget->setLayout(mainCartLayout);
		mainCartLayout->addLayout(globalCartLayout);
		globalCartLayout->addWidget(cartList);
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

		cartWidget->setWindowTitle(QString::fromStdString("Client"));
	}

	void initConnect() {
		QObject::connect(cartAddButton, &QPushButton::clicked, [&]() {
			string title = this->cartTitleLineEdit->text().toStdString();
			int prevLength = (int)this->cartBooks.size();
			this->cart.add(title);
			this->cartBooks = this->cart.getAllBooks();
			if (cartBooks.size() - prevLength == 1)
				cartOperationMessage->setText(QString::fromStdString("1 produs adaugat"));
			else if (cartBooks.size() - prevLength == 0)
				cartOperationMessage->setText(QString::fromStdString(cartBooks.size() - prevLength + "Niciun produs adaugat"));
			else
				cartOperationMessage->setText(QString::fromStdString(cartBooks.size() - prevLength + " produse adaugate"));
			reloadCartList();
		});

		QObject::connect(cartEmptyButton, &QPushButton::clicked, [&]() {
			this->cart.deleteAll();
			this->cartBooks = this->cart.getAllBooks();
			reloadCartList();
			this->cartOperationMessage->setText(QString::fromStdString("Cosul a fost golit"));
		});

		QPushButton::connect(cartGenerateButton, &QPushButton::clicked, [&]() {
			int noOfElements = (int)atoi(this->generateCartLineEdit->text().toStdString().c_str());
			this->cart.generateCart(noOfElements);
			int prevLength = (int)this->cartBooks.size();
			this->cartBooks = this->cart.getAllBooks();
			reloadCartList();
			if (prevLength != cartBooks.size())
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

		QObject::connect(cartOpenButton, &QPushButton::clicked, [&]() {
			cartWidget->show();
		});

		QObject::connect(addButton, &QPushButton::clicked, [&]() {
			string title = this->titleLineEdit->text().toStdString();
			string author = this->authorLineEdit->text().toStdString();
			string type = this->typeLineEdit->text().toStdString();
			int releaseYear = this->releaseYearLineEdit->text().toInt();

			try {
				this->service.addBook(title, author, type, releaseYear);
				this->operationMessage->setText("Cartea a fost adaugata cu succes!");
				this->books = this->service.getAllBooks();
				reloadList();
			}
			catch (BookCreationException e) {
				this->operationMessage->setText(e.what());
			}
			catch (BookRepositoryException e) {
				this->operationMessage->setText(e.what());
			}
		});

		QObject::connect(deleteButton, &QPushButton::clicked, [&]() {
			if (list->selectedItems().isEmpty() == false) {
				this->service.removeBook(((Book*)list->currentItem()->data(Qt::UserRole).data())->getId());
				this->books = this->service.getAllBooks();
				reloadList();
				this->operationMessage->setText("Cartea a fost stearsa cu succes!");
			}
			else
				this->operationMessage->setText("Nu este selectata o carte!");
		});

		QObject::connect(updateButton, &QPushButton::clicked, [&]() {
			if (!this->list->selectedItems().isEmpty()) {
				string title = this->titleLineEdit->text().toStdString();
				string author = this->authorLineEdit->text().toStdString();
				string type = this->typeLineEdit->text().toStdString();
				int releaseYear = this->releaseYearLineEdit->text().toInt();

				int index = ((Book*) list->currentItem()->data(Qt::UserRole).data())->getId();
				try {
					this->service.updateBook(index, title, author, type, releaseYear);
					this->operationMessage->setText("Cartea a fost actualizata cu succes!");
					this->books = this->service.getAllBooks();
					reloadList();
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
				this->books = this->service.getAllBooks();
				reloadList();
			}
			catch (exception e) {
				this->operationMessage->setText("Nu se mai poate face undo!");
			}
		});

		QObject::connect(searchByTitleButton, &QPushButton::clicked, [&]() {
			string title = titleLineEdit->text().toStdString();
			this->books = this->service.searchBooksByTitle(title);
			reloadList();
		});

		QObject::connect(filterByTitleButton, &QPushButton::clicked, [&]() {
			string title = titleLineEdit->text().toStdString();
			this->books = this->service.filterBooksByTitle(title);
			reloadList();
		});

		QObject::connect(filterByReleaseYearButton, &QPushButton::clicked, [&]() {
			int releaseYear = releaseYearLineEdit->text().toInt();
			this->books = this->service.filterBooksByReleaseYear(releaseYear);
			reloadList();
		});

		QObject::connect(sortByTitleButton, &QPushButton::clicked, [&]() {
			this->books = this->service.sortedBooksByTitle();
			reloadList();
		});

		QObject::connect(sortByAuthorButton, &QPushButton::clicked, [&]() {
			this->books = this->service.sortedBooksByAuthor();
			reloadList();
		});

		QObject::connect(sortByReleaseYearAndTypeButton, &QPushButton::clicked, [&]() {
			this->books = this->service.sortedBooksByReleaseDateAndType();
			reloadList();
		});

		QObject::connect(resetButton, &QPushButton::clicked, [&]() {
			this->books = this->service.getAllBooks();
			this->operationMessage->setText("");
			this->titleLineEdit->setText("");
			this->authorLineEdit->setText("");
			this->typeLineEdit->setText("");
			this->releaseYearLineEdit->setText("");
			reloadList();
		});
	}

	void reloadCartList() {
		cartList->clear();
		for (const auto& book : cartBooks) {
			QListWidgetItem* item = new QListWidgetItem{};
			QVariant data;
			data.setValue(book);

			item->setData(Qt::UserRole, data);
			item->setText(QString::fromStdString(bookToString(book)));
			cartList->addItem(item);
		}
	}

	void reloadList() {		
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

	string bookToString(const Book& book) {
		return book.getTitle() + ", " + book.getAuthor() + ", " +
				book.getType() + ", " + to_string(book.getReleaseYear());
	}

public:
	Ui(Service& service, CartService& cart) : service{ service }, cart{ cart } {
		initGUI();
		initConnect();
	}
};

