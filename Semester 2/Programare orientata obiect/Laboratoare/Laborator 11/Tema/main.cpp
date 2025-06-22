#include "Ui.h"
#include "Repository.h"
#include "Validator.h"
#include "Service.h"
#include "CartService.h"
#include "Tests.h"

#include <QtWidgets/QApplication>

int main(int argc, char *argv[])
{
    runAllTests();

    QApplication a(argc, argv);
    
    FileRepository repository{ "database" };
    Validator validator;
    Service service{ repository, validator};
    CartService cart{ service };

    Ui ui{ service, cart };
    ui.show();

    return a.exec();
}
