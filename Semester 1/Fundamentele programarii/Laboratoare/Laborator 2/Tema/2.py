from datetime import datetime

zi = int(input("Introdu ziua nasterii: "))
luna = int(input("Introdu luna nasterii: "))
an = int(input("Introdu anul nasterii: "))

data_nasterii = datetime(an, luna, zi)
data_actuala = datetime.now()

delta = data_actuala - data_nasterii

print(delta.days)


