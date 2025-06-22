#include "Service.h"
#include "qtableview.h"

#pragma once

class TableModel : public QAbstractTableModel {
private:
	Service& service;
	int filterVal = -1;
public:
	TableModel(Service& service, QObject* parent = nullptr) :
		QAbstractTableModel(parent), service(service) { }
	
	int rowCount(const QModelIndex& parent = QModelIndex()) const override {
		return (int)this->service.getAll().size();
	}
	
	int columnCount(const QModelIndex& parent = QModelIndex()) const override {
		return 5;
	}

	QVariant headerData(int section, Qt::Orientation orientation, int role = Qt::DisplayRole) const override {
		if (role == Qt::DisplayRole && orientation == Qt::Horizontal) {
			switch (section) {
			case 0:
				return "Id";
			case 1:
				return "Nume";
			case 2:
				return "Tip";
			case 3:
				return "Pret";
			case 4:
				return "Numar vocale";
			}
		}

		return QVariant();
	}

	QVariant data(const QModelIndex& index, int role = Qt::DisplayRole) const override {
		int row = index.row();
		int column = index.column();

		if (role == Qt::DisplayRole) {
			switch (column) {
			case 0:
				return this->service.getAll()[row].getId();
			case 1:
				return QString::fromStdString(this->service.getAll()[row].getNume());
			case 2:
				return QString::fromStdString(this->service.getAll()[row].getTip());
			case 3:
				return this->service.getAll()[row].getPret();
			case 4:
				return this->service.getAll()[row].getNrVocale();
			}
		}

		if (role == Qt::BackgroundRole) {

			if (this->service.getAll()[row].getPret() <= this->filterVal)
				return QColor(Qt::red);
			return QColor(Qt::black);
		}

		return QVariant();
	}

	void reload(int val) {
		this->filterVal = val;
		emit layoutAboutToBeChanged();
		emit layoutChanged();
	}
};