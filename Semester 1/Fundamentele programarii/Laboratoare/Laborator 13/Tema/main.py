
def afisare_sir_pozitii(sir, sir_pozitii):
    mesaj = ""
    for indice in sir_pozitii:
        mesaj += str(sir[indice]) + " "
    print(mesaj)

def afisare_sir(sir):
    mesaj = ""
    for element in sir:
        mesaj += str(element) + " "
    print(mesaj)

def citire_sir(sir):
    numar_elemente = int(input("Introduceti numarul de elemente: "))
    for i in range(numar_elemente):
        sir.append(int(input(f"Elementul #{i+1} = ")))

def afisare_sub_secvente_iterativ(sir):
    n = len(sir)
    stiva = [(0, [])]

    while stiva:
        index, secventa = stiva.pop()

        if len(secventa) > 0:
            afisare_sir(secventa)

        for i in range(index, n):
            if not secventa or sir[i] > secventa[-1]:
                stiva.append((i+1, secventa + [sir[i]]))


def afisare_sub_secvente_recursiv(sir, sir_rezultat):
    for indice in range(len(sir)):
        sir_rezultat.append(indice)
        if len(sir_rezultat) == 1:
            afisare_sir_pozitii(sir, sir_rezultat)
            afisare_sub_secvente_recursiv(sir, sir_rezultat)
        elif (indice > sir_rezultat[len(sir_rezultat) - 2]
            and sir[indice] > sir[sir_rezultat[len(sir_rezultat) - 2]]):
            afisare_sir_pozitii(sir, sir_rezultat)
            afisare_sub_secvente_recursiv(sir, sir_rezultat)
        sir_rezultat.pop()

def consistenta(sir):
    if len(sir) == 1:
        return True
    return (sir[-1] > sir[-2])

def solutie(sir):
    if len(sir) < 1:
        return False
    if len(sir) == 1:
        return True
    for i in range(len(sir)-1):
        if (sir[i] >= sir[i+1]):
            return False
    return True

def sub_secvente_recursiv(sir, sir_rezultat, last_index_added):
    sir_rezultat.append(0)
    for indice in range(last_index_added + 1, len(sir)):
        sir_rezultat[-1] = sir[indice]
        if consistenta(sir_rezultat):
            if solutie(sir_rezultat):
                afisare_sir(sir_rezultat)
            sub_secvente_recursiv(sir, sir_rezultat, indice)
    sir_rezultat.pop()

def main():
    sir = []
    citire_sir(sir)
    sir_rezultat = []
    sub_secvente_recursiv(sir, sir_rezultat, -1)
    #print("\n~Varianta iterativa~")
    #afisare_sub_secvente_iterativ(sir)
    #print("\n~Varianta recursiva~")
    #afisare_sub_secvente_recursiv(sir, sir_rezultat)

def incrementare(a):
    a = a + 1
    print(a)

if __name__ == "__main__":
    #main()
    a = 2
    incrementare(a)
    print(a)
