def convertToIntIfPossible(number):
    """
    Functia converteste un numar real la numar intreg daca acesta are dupa virgula
    doar cifre de 0, in caz contrar numarul ramane neschimbat
    input  - numarul
    output - numarul (convertit daca e cazul)
    """
    return int(number) if number.is_integer() else number

def formatListToPrint(list):
    """
    Functia formateaza o lista de cheltuieli pentru a putea fi afisata
    fiecare cheltuiala cu proprietatile ei
    input  - lista de cheltuieli
    output - mesajul formatat (string)
    """
    result = ""
    for index in range (len(list)):
        result += str(list[index])
        if index + 1 < len(list):
            result += ",\n"
    return result