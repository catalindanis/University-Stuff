def bubbleSort(data, key=None, reverse=False):
    """
    Functia de sortare ce utilizeaza metoda bubbleSort
    :param data: obiectul de sortat (Iterable)
    :param key: functie pentru transformarea elementelor inainte de comparare
    :param reverse: modul de sortare (crescator / descrescator)
    :return: lista sortata din obiectul iterabil transmis
    """
    data = list(data)

    if key is None:
        key = lambda x: x

    sorted = False
    while not sorted:
        sorted = True
        for i in range(len(data) - 1):
            if key(data[i]) > key(data[i + 1]):
                aux = data[i]
                data[i] = data[i + 1]
                data[i + 1] = aux
                sorted = False

    if reverse:
        data.reverse()

    return data


def shellSort(data, key=None, reverse=False):
    """
    Functia de sortare ce utilizeaza metoda shellSort
    :param data: obiectul de sortat (Iterable)
    :param key: functie pentru transformarea elementelor inainte de comparare
    :param reverse: modul de sortare (crescator / descrescator)
    :return: lista sortata din obiectul iterabil transmis
    """
    data = list(data)

    if key is None:
        key = lambda x: x

    n = len(data)
    gap = n // 2

    while gap > 0:
        for i in range(gap, n):
            temp = data[i]
            j = i
            while j >= gap and key(data[j - gap]) > key(temp):
                data[j] = data[j - gap]
                j -= gap
            data[j] = temp
        gap //= 2

    if reverse:
        data.reverse()

    return data
