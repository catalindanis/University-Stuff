def convertToIntIfPossible(number):
    """
    Functia converteste un numar real in numar intreg daca acesta are doar cifre
    de 0 dupa virgula, altfel ramane nemodificat
    :param number: numarul (float)
    :return: numarul (integer / float) in functie de caz
    """
    return int(number) if number.is_integer() else number

def formatListToPrint(list):
    """
    Functie ce formateaza o lista de cheltuieli si apoi o returneaza sub forma de mesaj
    :param list: lista de cheltuieli (List of Expense)
    :return: lista de cheltuieli formatata (String)
    """
    result = ""
    for index in range (len(list)):
        result += str(list[index])
        if index + 1 < len(list):
            result += ",\n"
    return result