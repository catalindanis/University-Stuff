#include "Service.h"
#include "Observer.h"
#include "qtableview.h"

#pragma once

class MusicModel : public QAbstractTableModel, public Observer {
	Q_OBJECT
private:
	Service& service;
public:
	MusicModel(Service& service, QObject* parent = nullptr) : QAbstractTableModel(parent), service(service) {}

	int rowCount(const QModelIndex& parent = QModelIndex()) const override {
		return (int)this->service.getAll().size();
	}

	int columnCount(const QModelIndex& parent = QModelIndex()) const override {
		return 5;
	}

	QVariant headerData(int section, Qt::Orientation orientation,
		int role = Qt::DisplayRole) const override {

		if(role == Qt::DisplayRole && orientation == Qt::Horizontal){
			switch (section) {
			case 0:
				return "Id";
			case 1:
				return "Titlu";
			case 2:
				return "Artist";
			case 3:
				return "Rank";
			case 4:
				return "Acelasi rank";
			}
		}

		return QVariant();
	}

	QVariant data(const QModelIndex& index, int role = Qt::DisplayRole) const override {
		if (role == Qt::DisplayRole ) {
			switch (index.column()) {
			case 0:
				return this->service.getAll()[index.row()].getId();
			case 1:
				return QString::fromStdString(this->service.getAll()[index.row()].getTitlu());
			case 2:
				return QString::fromStdString(this->service.getAll()[index.row()].getArtist());
			case 3:
				return this->service.getAll()[index.row()].getRank();
			case 4:
				return this->service.getNumberOfMusicsByRank(this->service.getAll()[index.row()].getRank());
			}
		}

		return QVariant();
	}

	void update() override {
		emit layoutAboutToBeChanged();
		emit layoutChanged();
	}
};
