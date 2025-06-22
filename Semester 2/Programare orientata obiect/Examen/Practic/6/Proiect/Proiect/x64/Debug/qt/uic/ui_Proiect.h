/********************************************************************************
** Form generated from reading UI file 'Proiect.ui'
**
** Created by: Qt User Interface Compiler version 6.9.0
**
** WARNING! All changes made in this file will be lost when recompiling UI file!
********************************************************************************/

#ifndef UI_PROIECT_H
#define UI_PROIECT_H

#include <QtCore/QVariant>
#include <QtWidgets/QApplication>
#include <QtWidgets/QMainWindow>
#include <QtWidgets/QMenuBar>
#include <QtWidgets/QStatusBar>
#include <QtWidgets/QToolBar>
#include <QtWidgets/QWidget>

QT_BEGIN_NAMESPACE

class Ui_ProiectClass
{
public:
    QMenuBar *menuBar;
    QToolBar *mainToolBar;
    QWidget *centralWidget;
    QStatusBar *statusBar;

    void setupUi(QMainWindow *ProiectClass)
    {
        if (ProiectClass->objectName().isEmpty())
            ProiectClass->setObjectName("ProiectClass");
        ProiectClass->resize(600, 400);
        menuBar = new QMenuBar(ProiectClass);
        menuBar->setObjectName("menuBar");
        ProiectClass->setMenuBar(menuBar);
        mainToolBar = new QToolBar(ProiectClass);
        mainToolBar->setObjectName("mainToolBar");
        ProiectClass->addToolBar(mainToolBar);
        centralWidget = new QWidget(ProiectClass);
        centralWidget->setObjectName("centralWidget");
        ProiectClass->setCentralWidget(centralWidget);
        statusBar = new QStatusBar(ProiectClass);
        statusBar->setObjectName("statusBar");
        ProiectClass->setStatusBar(statusBar);

        retranslateUi(ProiectClass);

        QMetaObject::connectSlotsByName(ProiectClass);
    } // setupUi

    void retranslateUi(QMainWindow *ProiectClass)
    {
        ProiectClass->setWindowTitle(QCoreApplication::translate("ProiectClass", "Proiect", nullptr));
    } // retranslateUi

};

namespace Ui {
    class ProiectClass: public Ui_ProiectClass {};
} // namespace Ui

QT_END_NAMESPACE

#endif // UI_PROIECT_H
