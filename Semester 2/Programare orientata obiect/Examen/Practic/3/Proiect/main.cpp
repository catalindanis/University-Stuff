#include <QtWidgets/QApplication>
#include "Ui.h"

int main(int argc, char *argv[])
{
    QApplication app(argc, argv);
    
    Repository repo("db.txt");
    Service service(repo);
    Ui ui(service);
    ui.show();

    return app.exec();
}
