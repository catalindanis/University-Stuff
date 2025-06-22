#include <QAbstractListModel>
#include <Book.h>

#pragma once

string bookToString(const Book& book);

class BookListModel : public QAbstractListModel
{
	Q_OBJECT
private:
	vector<Book> books;
public:
	BookListModel(QObject* parent = nullptr) :
		QAbstractListModel(parent) {}

	void setBooks(const vector<Book>& books) {
		emit layoutAboutToBeChanged();
		this->books = books;
		emit layoutChanged();
	}

	const Book& getBook(int index) {
		return this->books[index];
	}

	int rowCount(const QModelIndex& parent = QModelIndex()) const override {
		return books.size();
	}

	QVariant data(const QModelIndex& index, int role = Qt::DisplayRole) const override {
		if (!index.isValid() || index.row() > books.size())
			return {};

		const Book& book = books[index.row()];

		if (role == Qt::DisplayRole) {
			return QString::fromStdString(bookToString(book));
		}

		return {};
	}
};
