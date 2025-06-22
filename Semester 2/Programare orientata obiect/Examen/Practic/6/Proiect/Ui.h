#include "QtWidgets/qwidget.h"
#include "QtWidgets/qlayout.h"
#include "QtWidgets/qcombobox.h"
#include "qpainter.h"
#include <QMouseEvent>
using namespace std;

#pragma once

class Ui : public QWidget {
private:
	QComboBox* cb = new QComboBox();
	vector<QRect> rect;

	void initUi() {
		QHBoxLayout* ly = new QHBoxLayout();

		cb->addItem("Test");
		cb->addItem("dawd");
		cb->addItem("tedas");

		ly->addWidget(cb);

		this->setLayout(ly);
	}

	void initConnect() {
		QObject::connect(cb, &QComboBox::currentIndexChanged, [&](int index) {
			qDebug() << index;
		});
	}

public:
	Ui() {
		initUi();
		initConnect();
	}

	QRect r{ 10, 10, 20, 20 };

	void paintEvent(QPaintEvent* event) override {
		QPainter p{ this };

		
		p.drawEllipse(r);
	}

	void mousePressEvent(QMouseEvent* event) override {
		if (r.contains(event->pos())) {
			qDebug() << "Rectangle clicked!";
		}
	}
};