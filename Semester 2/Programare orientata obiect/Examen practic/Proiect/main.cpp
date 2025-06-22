#include <QtWidgets/QApplication>
#include "Tests.h"
#include "UI.h"

//Functia principala a aplicatiei
int main(int argc, char *argv[])
{
    QApplication app(argc, argv);
    
    Test::runAllTests();

    Repository repo("database.txt");
    Service service(repo);

    Ui ui(service);
    ui.show();

    return app.exec();
}
