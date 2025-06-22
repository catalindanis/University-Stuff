#include "ui.h"
#include "Tests.h"
#include <QtWidgets/QApplication>

int main(int argc, char *argv[])
{
    QApplication a(argc, argv);

    Tests tests;
    tests.runAllTests();

    Repository repository{ "aplicatie.txt" };
    Service service{ repository };

    Ui ui{ service };
    ui.show();

    return a.exec();
}
