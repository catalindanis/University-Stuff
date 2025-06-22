#include <QtWidgets/QApplication>
#include "Tests.h"
#include "Ui.h"

int main(int argc, char *argv[])
{
    QApplication app(argc, argv);
    Tests::runAll();

    FileRepository repo{ "database.txt" };
    Service service{ repo };
    Ui ui{ service };
    ui.show();

    return app.exec();
}
