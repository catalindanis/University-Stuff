#include <iostream>
#include <queue>
#include <unordered_map>
#include <vector>
#include <string>

using namespace std;

struct Nod {
    char caracter;
    int frecventa;
    Nod* stanga;
    Nod* dreapta;

    Nod(char c, int f) : caracter(c), frecventa(f), stanga(nullptr), dreapta(nullptr) {}
};

struct Comparare {
    bool operator()(Nod* a, Nod* b) {
        return a->frecventa > b->frecventa;
    }
};

void creare_coduri_din_arbore(Nod* radacina, string codCurent, unordered_map<char, string>& coduri) {
    if (!radacina) return;

    if (!radacina->stanga && !radacina->dreapta) {
        coduri[radacina->caracter] = codCurent;
    }

    creare_coduri_din_arbore(radacina->stanga, codCurent + "0", coduri);
    creare_coduri_din_arbore(radacina->dreapta, codCurent + "1", coduri);
}

Nod* genereaza_arbore_huffman(const string& mesaj, unordered_map<char, string>& coduri) {
    unordered_map<char, int> frecvente;
    for (char c : mesaj) {
        frecvente[c]++;
    }

    priority_queue<Nod*, vector<Nod*>, Comparare> coada;
    for (auto& pereche : frecvente) {
        coada.push(new Nod(pereche.first, pereche.second));
    }

    while (coada.size() > 1) {
        Nod* st = coada.top(); coada.pop();
        Nod* dr = coada.top(); coada.pop();
        Nod* combinat = new Nod('\0', st->frecventa + dr->frecventa);
        combinat->stanga = st;
        combinat->dreapta = dr;
        coada.push(combinat);
    }

    Nod* radacina = coada.top();
    creare_coduri_din_arbore(radacina, "", coduri);
    return radacina;
}

string codare_huffman(const string& mesaj, unordered_map<char, string>& coduri, Nod*& arbore) {
    arbore = genereaza_arbore_huffman(mesaj, coduri);
    string rezultat;
    for (char c : mesaj) {
        rezultat += coduri[c];
    }
    return rezultat;
}

string decodare_huffman(const string& cod, Nod* radacina) {
    string rezultat;
    Nod* curent = radacina;
    for (char bit : cod) {
        if (bit == '0') curent = curent->stanga;
        else            curent = curent->dreapta;

        if (!curent->stanga && !curent->dreapta) {
            rezultat += curent->caracter;
            curent = radacina;
        }
    }
    return rezultat;
}

int main() {
    string input = "Treeaaassuureee";
    unordered_map<char, string> coduri;
    Nod* arbore = nullptr;

    string comprimat = codare_huffman(input, coduri, arbore);
    string decomprimat = decodare_huffman(comprimat, arbore);

    cout << "Mesaj comprimat:   " << comprimat << endl;
    cout << "Mesaj decompresat: " << decomprimat << endl;

    return 0;
}
