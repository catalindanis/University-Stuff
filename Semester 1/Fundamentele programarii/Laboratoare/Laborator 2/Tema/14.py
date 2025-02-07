#Tema Laborator 2
import math

def sumaDivizori(numar):
    if numar <= 0:
        return 1
    if numar == 1:
        return 0
    suma = 0
    d = 1
    while d * d <= numar:
        if d * d == numar:
            suma += d
        elif numar % d == 0:
            if d != 1:
                suma += d + numar // d
            else:
                suma += d
        d += 1
    return suma

def gasesteUrmatorulNrPerfect(numar):
    rezultat = numar+1
    while rezultat != sumaDivizori(rezultat):
        rezultat += 1
    return rezultat

def ruleazaTeste():
    assert gasesteUrmatorulNrPerfect(-100) == 6
    assert gasesteUrmatorulNrPerfect(0) == 6
    assert gasesteUrmatorulNrPerfect(6) == 28
    assert gasesteUrmatorulNrPerfect(28) == 496
    assert gasesteUrmatorulNrPerfect(496) == 8128

def main():
    ruleazaTeste()
    numar = int(input("Rezultatul trebuie sa fie mai mare decat valoarea: "))
    print(gasesteUrmatorulNrPerfect(numar))

if __name__ == "__main__":
    main()

