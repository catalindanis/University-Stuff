from enum import Enum

class ExpenseType(Enum):
    """
    Clasa defineste tipul unei cheltuieli
    Foloseste ca si clasa parinte Enum, pentru a prelua atributele acesteia
    """
    MANCARE = 1
    INTRETINERE = 2
    IMBRACAMINTE = 3
    TELEFON = 4
    ALTELE = 5