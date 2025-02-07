
def prim(numar):
    '''
    Functia verifica daca un numar este prim
    input - numar (numarul efectiv pentru care verificam proprietatea)
    output - True / False (in functie de primalitate)
    '''
    if numar <= 1:
        return False
    if numar > 2 and numar % 2 == 0:
        return False
    d = 3
    while d * d <= numar:
        if numar % d == 0:
            return False
        d += 2
    return True

def nrPrimAnterior(numar):
    '''
    Functia cauta primul numar mai mic decat numarul introdus care este prim
    input - numarul fata de care rezultatul este mai mic
    output - rezultatul / -1 (daca nu exista un asemenea numar)
    '''
    numar -= 1
    while not prim(numar) and numar > 1:
        numar -= 1
    if numar == 1:
        return -1
    return numar

def test():
    assert nrPrimAnterior(2) == -1
    assert nrPrimAnterior(3) == 2
    assert nrPrimAnterior(5) == 3
    assert nrPrimAnterior(12) == 11
    assert nrPrimAnterior(15) == 13
    assert nrPrimAnterior(20) == 19

def main():
    '''
    Functia principala a programului
    input -
    ouput - 
    '''
    numar = int(input("Introdu un numar: "))
    rezultat = nrPrimAnterior(numar)
    if rezultat == -1:
        print("Nu exista un asemenea numar!")
    else:
        print(f"Rezultatul este {rezultat}")

if __name__ == "__main__":
    test()
    main()
