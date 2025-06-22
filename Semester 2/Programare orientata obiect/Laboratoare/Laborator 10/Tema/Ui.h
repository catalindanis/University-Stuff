#pragma once

#include "Service.h"

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

	QPushButton* exit = new QPushButton{ "Exit" };
	QPushButton* yes = new QPushButton{ "Da" };
	QPushButton* no = new QPushButton{ "Nu" };

	QWidget* exitWidget = new QWidget{};

	QLabel* operationMessage = new QLabel{ "" };

	vector<Book> books;

	QVBoxLayout* tempButtonsLayout = new QVBoxLayout{};
	QHBoxLayout* mainLayout = new QHBoxLayout{};

	QScrollArea* scrollArea = new QScrollArea{};

	void initGUI() {
		QVBoxLayout* globalLayout = new QVBoxLayout{};
		QHBoxLayout* secondLayout = new QHBoxLayout{};
		setLayout(globalLayout);
		resize(QSize{ 1000, 400 });

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

		secondLayout->addWidget(resetButton);
		secondLayout->addWidget(operationMessage);
		secondLayout->addStretch(1);
		globalLayout->addLayout(secondLayout);

		QLabel* message = new QLabel{ "Sunteti sigur?" };

		globalLayout->addWidget(exit);
		QVBoxLayout* layout = new QVBoxLayout{};
		exitWidget->setLayout(layout);
		layout->addWidget(message);
		layout->addWidget(yes);
		layout->addWidget(no);

		books = this->service.getAllBooks();
		reloadList();
	}

	void initConnect() {
		QObject::connect(exit, &QPushButton::clicked, [&]() {
			this->exitWidget->show();
		});

		QObject::connect(yes, &QPushButton::clicked, [&]() {
			exitWidget->close();
			this->close();
		});

		QObject::connect(no, &QPushButton::clicked, [&]() {
			exitWidget->close();
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

	void reloadList() {
		QLayoutItem* item;
		QLayout* sublayout;
		QWidget* widget;
		while ((item = tempButtonsLayout->takeAt(0))) {
			if ((sublayout = item->layout()) != 0) {}
			else if ((widget = item->widget()) != 0) { widget->hide(); delete widget; }
			else { delete item; }
		}

		delete tempButtonsLayout;
		tempButtonsLayout = new QVBoxLayout{};
		mainLayout->addLayout(tempButtonsLayout);
		list->clear();
		for (const auto& book : books) {
			QListWidgetItem* item = new QListWidgetItem;
			QVariant data;
			data.setValue(book);
			item->setData(Qt::UserRole, data);
			item->setText(QString::fromStdString(bookToString(book)));

			QPushButton* tempButton = new QPushButton{ "@" };
			QWidget* tempWidget = new QWidget;

			QObject::connect(tempButton, &QPushButton::clicked, [tempWidget, book]() {
				string message = book.getType() + " : 1";
				QLabel* tempMessage = new QLabel{ message.c_str()};
				QHBoxLayout* tempLayout = new QHBoxLayout;
				tempLayout->addWidget(tempMessage);
				tempWidget->setLayout(tempLayout);
				tempWidget->show();
			});

			tempButtonsLayout->addWidget(tempButton);

			list->addItem(item);
		}
	}

	string bookToString(const Book& book) {
		return book.getTitle() + ", " + book.getAuthor() + ", " +
				book.getType() + ", " + to_string(book.getReleaseYear());
	}

public:
	Ui(Service& service) : service { service } {
		initGUI();
		initConnect();
	}
};

