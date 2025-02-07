n = int(input("N = "))
numbers = []

sum_var = 0
for i in range(n):
    numbers.append(int(input(f"Valoarea #{i+1} = ")))

for elem in numbers:
    sum_var += elem

print(f"Suma este: {sum_var}")
