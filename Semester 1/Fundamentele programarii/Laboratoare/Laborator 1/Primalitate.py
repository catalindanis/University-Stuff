def prim(number):
    if number <= 1:
        return False
    if number > 2 and number % 2 == 0:
        return False
    d = 3
    while d * d <= number:
        if(number % d == 0):
            return False
        d += 2
    return True

number = int(input("Introdu numarul: "))
if(prim(number)):
    print("Numarul este prim!")
else:
    print("Numarul nu este prim")


