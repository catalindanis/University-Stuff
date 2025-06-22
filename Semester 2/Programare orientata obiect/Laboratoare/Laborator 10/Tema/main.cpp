#include "Ui.h"
#include "Repository.h"
#include "Validator.h"
#include "Service.h"
#include "Tests.h";

#include <QtWidgets/QApplication>

int main(int argc, char *argv[])
{
    QApplication a(argc, argv);

    qRegisterMetaType<Book*>("Book*");
    
    FileRepository repository{ "database" };
    Validator validator;
    Service service{ repository, validator};

    Ui ui{ service };
    ui.show();

    return a.exec();
}
