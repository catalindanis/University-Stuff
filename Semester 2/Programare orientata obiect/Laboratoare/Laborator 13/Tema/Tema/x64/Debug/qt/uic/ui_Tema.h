/********************************************************************************
** Form generated from reading UI file 'Tema.ui'
**
** Created by: Qt User Interface Compiler version 6.9.0
**
** WARNING! All changes made in this file will be lost when recompiling UI file!
********************************************************************************/

#ifndef UI_TEMA_H
#define UI_TEMA_H

#include <QtCore/QVariant>
#include <QtWidgets/QApplication>
#include <QtWidgets/QMainWindow>
#include <QtWidgets/QMenuBar>
#include <QtWidgets/QStatusBar>
#include <QtWidgets/QToolBar>
#include <QtWidgets/QWidget>

QT_BEGIN_NAMESPACE

class Ui_TemaClass
{
public:
    QMenuBar *menuBar;
    QToolBar *mainToolBar;
    QWidget *centralWidget;
    QStatusBar *statusBar;

    void setupUi(QMainWindow *TemaClass)
    {
        if (TemaClass->objectName().isEmpty())
            TemaClass->setObjectName("TemaClass");
        TemaClass->resize(600, 400);
        menuBar = new QMenuBar(TemaClass);
        menuBar->setObjectName("menuBar");
        TemaClass->setMenuBar(menuBar);
        mainToolBar = new QToolBar(TemaClass);
        mainToolBar->setObjectName("mainToolBar");
        TemaClass->addToolBar(mainToolBar);
        centralWidget = new QWidget(TemaClass);
        centralWidget->setObjectName("centralWidget");
        TemaClass->setCentralWidget(centralWidget);
        statusBar = new QStatusBar(TemaClass);
        statusBar->setObjectName("statusBar");
        TemaClass->setStatusBar(statusBar);

        retranslateUi(TemaClass);

        QMetaObject::connectSlotsByName(TemaClass);
    } // setupUi

    void retranslateUi(QMainWindow *TemaClass)
    {
        TemaClass->setWindowTitle(QCoreApplication::translate("TemaClass", "Tema", nullptr));
    } // retranslateUi

};

namespace Ui {
    class TemaClass: public Ui_TemaClass {};
} // namespace Ui

QT_END_NAMESPACE

#endif // UI_TEMA_H
