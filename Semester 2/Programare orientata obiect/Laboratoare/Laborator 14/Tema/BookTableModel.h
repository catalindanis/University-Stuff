#include <QAbstractTableModel>
#include "Book.h"
using namespace std;

#pragma once

class BookTableModel : public QAbstractTableModel
{
	Q_OBJECT
private:
	vector<Book> books;
public:
	BookTableModel(QObject* parent = nullptr) :
		QAbstractTableModel(parent) {}

	void setBooks(const vector<Book>& books) {
		emit layoutAboutToBeChanged();
		this->books = books;
		emit layoutChanged();
	}

	int rowCount(const QModelIndex& parent = QModelIndex()) const override {
		return books.size();
	}

	int columnCount(const QModelIndex& parent = QModelIndex()) const override {
		return 4;
	}

	QVariant data(const QModelIndex& index, int role = Qt::DisplayRole) const override {
		if (role == Qt::DisplayRole) {
			const Book& book = books[index.row()];
			switch (index.column()) {
			case 0: return QString::fromStdString(book.getTitle());
			case 1: return QString::fromStdString(book.getAuthor());
			case 2: return QString::fromStdString(book.getType());
			case 3: return QString::number(book.getReleaseYear());
			}
		}
		return {};
	}

	QVariant headerData(int section, Qt::Orientation orientation, int role = Qt::DisplayRole) const override {
		if (role == Qt::DisplayRole && orientation == Qt::Horizontal) {
			switch (section) {
			case 0: return "Titlu";
			case 1: return "Autor";
			case 2: return "Gen";
			case 3: return "Anul aparitiei";
			}
		}
		return {};
	}
};

