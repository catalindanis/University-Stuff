#Programul de mai jos calculeaza valoarea constantei PI utilizand
#fractia continua generalizata
from math import pi

def calculatePI(numberOfIterations, argument = 1):
    if numberOfIterations == 0:
        return 0
    return argument * argument / ( 6 + calculatePI(numberOfIterations - 1, argument + 2))

if __name__ == '__main__':
    n = int(input("Introdu numarul de iteratii: "))
    print(f"Valoarea obtinuta: {calculatePI(n) + 3}")
    print(f"Valoarea corecta:  {pi}")

#PI = 3.141592653589793238462643383279502884197