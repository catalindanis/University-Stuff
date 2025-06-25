#include <iostream>
#include <fstream>
#include <queue>
#include <vector>
using namespace std;

int main() {
    ifstream fin("graf.txt");

    //citirea grafului
    int nrV;
    int x, y, sursa;

    fin>>nrV;
    vector<vector<int>> listaAdj(nrV + 1, vector<int>(0));
    vector<int> lungime(nrV + 1);
    queue<int> coada;
    vector<int> parinte(nrV + 1, -1);
    vector<bool> vizitat(nrV + 1, false);

    while(fin>>x>>y) {
        listaAdj[x].push_back(y);
    }

    cout<<"Introduceti varful sursa: ";
    cin>>sursa;

    //algoritmul lui Moore
    for(int i=1;i<=nrV;i++)
        lungime[i] = -1; //infinit
    lungime[sursa] = 0;

    coada.push(sursa);
    while(!coada.empty()) {
        int v = coada.front();
        vizitat[v] = true;
        coada.pop();

        for(int i=0;i<listaAdj[v].size();i++) {
            if(lungime[listaAdj[v][i]] == -1 && !vizitat[listaAdj[v][i]]) {
                parinte[listaAdj[v][i]] = v;
                lungime[listaAdj[v][i]] = lungime[v] + 1;
                coada.push(listaAdj[v][i]);
            }
        }
    }

    int distantaMinima = nrV;
    int varfDistantaMinima = -1;
    for(int i=1;i<=nrV;i++)
        if(i != sursa && lungime[i] < distantaMinima && lungime[i] != -1) {
            distantaMinima = lungime[i];
            varfDistantaMinima = i;
        }

    int vfC = varfDistantaMinima;
    while(vfC != -1) {
        cout<<vfC;
        vfC = parinte[vfC];
        if(vfC != -1)
            cout<<"<-";
    }
    return 0;
}
