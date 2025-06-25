#include <iostream>
#include <fstream>
#include <vector>
using namespace std;

/*
Problema nota 10 – timp de lucru 40 de minute
Se da un graf neorientat neponderat. Sa se determine daca acest graf
este aciclic. Hint: determinati daca, dupa o parcurgere in adancime raman
muchii din graf prin care nu ati trecut. Raspunsul va fi de forma „DA”
(graful este aciclic), respectiv „NU” (graful are cel putin un ciclu).
Date de intrare:
n m # prima linie contine numarul de noduri si numarul de muchii
x1 y1
x2 y2
...
xn yn # exista o muchie intre „x” si „y”
Exemplu „input.txt”:
7 7
1 2
2 3
3 4
4 1
1 5
5 6
5 7
*/

vector<vector<int>> listaAdj;
vector<bool> viz;

bool hasCycle(int node, int parent) {
    viz[node] = true;
    for(int i=0;i<listaAdj[node].size();i++) {
        if(!viz[listaAdj[node][i]])
            return hasCycle(listaAdj[node][i], node);
        else if(listaAdj[node][i] != parent)
            return true;
    }
    return false;
}

int main() {
    ifstream fin("input.txt");

    int n, m;
    int x, y;
    fin>>n>>m;

    listaAdj.resize(n + 1);
    for(int i=0;i<=n;i++)
        viz.push_back(false);

    bool are_ciclu = true;

    for(int i=1;i<=n;i++) {
        bool contine_ciclu =
            hasCycle(i, i);
        are_ciclu = are_ciclu || contine_ciclu;
    }

    if(are_ciclu)
        cout<<"NU";
    else cout<<"DA";

    return 0;
}
