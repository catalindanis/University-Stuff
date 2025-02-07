
def prim(numar):
    if numar <= 1:
        return False
    if numar > 2 and numar % 2 == 0:
        return False
    d = 3
    while d * d <= numar:
        if numar % d == 0:
            return False
        d +=2
    return True

def gasesteUrmatorulNrPrim(numar):
    numar += 1
    while not prim(numar):
        numar += 1
    return numar

def ruleazaTeste():
    assert gasesteUrmatorulNrPrim(1) == 2
    assert gasesteUrmatorulNrPrim(2) == 3
    assert gasesteUrmatorulNrPrim(3) == 5
    assert gasesteUrmatorulNrPrim(5) == 7
    assert gasesteUrmatorulNrPrim(7) == 11
    assert gasesteUrmatorulNrPrim(11) == 13

def main():
    ruleazaTeste()
    numar = int(input("Rezultatul trebuie sa fie mai mare decat valoarea: "))
    print(gasesteUrmatorulNrPrim(numar))

if __name__ == "__main__":
    main()
