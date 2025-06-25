#include <iostream>
#include <fstream>
#include <vector>
using namespace std;

/*
Problema nota 10 – timp de lucru 40 de minute
In fata blocului se joaca „n” copii, intre care exista relatii de prietenie, de forma „x y”,
mai exact copilul identificat prin numarul de ordine „x” este prieten cu cel identificat prin
numarul de ordine „y”. Relatia de prietenie este simetrica, adica daca „x” este prieten cu „y” si „y”
este prieten cu „x”. Mai mult, relatia de prietenie este si tranzitiva, concret, daca „x” este prieten
cu „y” si „y” cu „z”, atunci si „x” va fi prieten cu „z”. Sa se determine asa numitele „grupuri de
prieteni” care se formeaza intre copii. Afisati aceasta solutie. Formatul fisierului de intrare este:
n m #prima linie contine numarul de copii si numarul de relatii intre acestia
x1 y1
x2 y2
...
xn yn # exista o relatie de prietenie intre „x” si „y”
Exemplu „input.txt”:
10 7
1 2
2 3
3 4
4 5
6 10
7 8
7 9
*/

vector<vector<int>> listaAdj;
vector<bool> vizitat;

void dfs(int vf) {
    vizitat[vf] = true;
    cout<<vf<<' ';
    for(int i=0;i<listaAdj[vf].size();i++)
        if(!vizitat[listaAdj[vf][i]])
            dfs(listaAdj[vf][i]);
}

int main() {
    int n, m;
    int x, y;
    ifstream fin("input.txt");

    fin>>n>>m;
    listaAdj.resize(n+1);
    vizitat.resize(n+1);

    for(int i=1;i<=n;i++)
        vizitat[i] = false;

    for(int i=0;i<m;i++) {
        fin>>x>>y;
        listaAdj[x].push_back(y);
        listaAdj[y].push_back(x);
    }

    int index = 1;
    for(int i=1;i<=n;i++)
        if(!vizitat[i]) {
            cout<<"Grupul #"<<index++<<": ";
            dfs(i);
            cout<<'\n';
        }
    return 0;
}
