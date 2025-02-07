a = int(input("Introdu prima valoare: "))
b = int(input("Introdu a doua valoare: "))

while b != 0:
    r = a%b
    a = b
    b = r

print(f"Cel mai mare divizor comun este: {a}")
