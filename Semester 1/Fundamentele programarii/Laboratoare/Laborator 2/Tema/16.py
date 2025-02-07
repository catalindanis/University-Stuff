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

def gasesteAnteriorulNrPerfect(numar):
    rezultat = numar-1
    while rezultat != sumaDivizori(rezultat) and rezultat > 6:
        rezultat -= 1
    if(rezultat < 6):
        return -1
    return rezultat

def ruleazaTeste():
    assert gasesteAnteriorulNrPerfect(-100) == -1
    assert gasesteAnteriorulNrPerfect(0) == -1
    assert gasesteAnteriorulNrPerfect(6) == -1
    assert gasesteAnteriorulNrPerfect(28) == 6
    assert gasesteAnteriorulNrPerfect(496) == 28

def main():
    ruleazaTeste()
    numar = int(input("Rezultatul trebuie sa fie mai mare decat valoarea: "))
    rezultat = gasesteAnteriorulNrPerfect(numar)
    if(rezultat != -1):
        print(gasesteAnteriorulNrPerfect(numar))
    else:
        print("Nu exista un asemenea numar!")

if __name__ == "__main__":
    main()

