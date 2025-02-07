#6 si 8
class InvalidLength(Exception):
    '''
    Exceptia este utilizata atunci cand sirul introdus are o lungime invalida
    '''
    pass

def meniu():
    '''
    Functia afiseaza meniul
    input  -
    output - 
    '''
    print("""1.Citeste lista de numere intregi
2.Secventa maxima de elemente distincte
3.Secventa maxima care are toate elementele in intervalul [0, 10]
4.Iesire din aplicatie""")

def citesteLista(lista):
    '''
    Functia realizeaza citirea unei liste de numere intregi
    input  - lista care urmeaza sa fie citita
    output - 
    '''
    print("Introdu numarul de elemente: ")
    lista.clear()
    n = int(input())
    if n <= 0:
        raise InvalidLength
    for i in range(n):
        lista.append(int(input(f"Elementul {i+1} = ")))

def secventaMaxElementeDistincte(lista):
    '''
    Functia cauta secventa de lungime maxima care contine doar elemente distincte
    input  - lista de numere intregi, nevida
    output - perechea de indici care delimiteaza secventa
    '''
    lungimeMaxima = 1
    indexStart = indexFinal = 0
    for st in range(len(lista)):
        distincte = True
        for dr in range(st+1, len(lista)):
            for i in range(st, dr):
                if lista[i] == lista[dr]:
                    distincte = False

            if not distincte:
                break
            
            if dr - st + 1 > lungimeMaxima:
                indexStart = st
                indexFinal = dr
                lungimeMaxima = dr - st + 1
                
    return (indexStart, indexFinal)
        

def secventaMaxElementeInInterval(lista):
    '''
    Functia cauta secventa de lungime maxima care contine doar elemente din intervalul [0, 10]
    input  - lista de numere intregi, nevida
    output - perechea de indici care delimiteaza secventa / (-1, -2) daca nu exista o asemenea secventa
    '''
    indexStart = indexStartMax = -1
    indexFinal = indexFinalMax = -2
    for index in range(len(lista)):
        if lista[index] >= 0 and lista[index] <= 10:
            if indexStart == -1:
                indexStart = index
            indexFinal = index
        else:
            indexStart = -1
            indexFinal = -2

        if indexFinal - indexStart + 1 > indexFinalMax - indexStartMax + 1:
            indexFinalMax = indexFinal
            indexStartMax = indexStart
    return (indexStartMax, indexFinalMax)
    
def test():
    '''
    Functia testeaza niste perechi de valori pentru care se cunoaste deja rezultatul
    input  -
    output -
    '''
    assert secventaMaxElementeDistincte([1, 2, 3, 4, 5]) == (0, 4)
    assert secventaMaxElementeDistincte([1, 2, 2, 4, 5]) == (2, 4)
    assert secventaMaxElementeDistincte([1, 1, 1, 1, 1]) == (0, 0)
    assert secventaMaxElementeDistincte([1, 2, 3, 4, 5, 5, 2, 3, 1]) == (0, 4)
    assert secventaMaxElementeDistincte([1, 1, 2, 2, 3, 1]) == (3, 5)

    assert secventaMaxElementeInInterval([1]) == (0, 0)
    assert secventaMaxElementeInInterval([1, 2, 3]) == (0, 2)
    assert secventaMaxElementeInInterval([1, 2, 3, 11]) == (0, 2)
    assert secventaMaxElementeInInterval([-1, 2, 3]) == (1, 2)
    assert secventaMaxElementeInInterval([11]) == (-1, -2)

def afisareLista(stanga, dreapta, lista):
    '''
    Functia afiseaza elementele din lista aflate intre cei doi indici inclusiv
    input  - stanga, dreapta numere naturale
           - lista de numere intregi, nevida
    output -  
    '''
    mesaj = ""
    for i in range(stanga, dreapta+1):
        mesaj += str(lista[i]) + " "
    if mesaj != "":
        print(mesaj)
    
def interpretareComanda(lista):
    '''
    Functia interpreteaza comanda introdusa de utilizator
    input - lista de elemente intregi, nevida
    output - False daca se doreste iesirea din aplicatie, True in caz contrar
    '''
    try:
        numar = int(input())
        if numar == 1:
            citesteLista(lista)
        elif numar == 2:
            indici = secventaMaxElementeDistincte(lista)
            afisareLista(indici[0], indici[1], lista)
        elif numar == 3:
            indici = secventaMaxElementeInInterval(lista)
            afisareLista(indici[0], indici[1], lista)
        elif numar == 4:
            return False
        else:
            raise Exception
    except InvalidLength:
        print("Lungimea sirului este invalida!")
    except Exception:
        print("Format invalid")
    return True

def main():
    '''
    Functia principala a programului
    input -
    ouput - 
    '''

    lista = []

    while True:
        meniu()
        if interpretareComanda(lista) == False:
            break

    print("La revedere!")
    

if __name__ == "__main__":
    test()
    main()
