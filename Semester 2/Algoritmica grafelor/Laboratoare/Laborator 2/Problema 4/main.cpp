#include <iostream>
#include <fstream>
#include <queue>
#include <vector>
using namespace std;

int main() {
    ifstream fin("graf.txt");

    int nrV;
    fin>>nrV;

    vector<vector<int>> listaAdj(nrV+1, vector<int>(0));

    int x, y;
    while(fin>>x>>y) {
        listaAdj[x].push_back(y);
    }

    fin.close();

    int sursa;
    cout<<"Introduceti nodul sursa: ";
    cin>>sursa;

    queue<pair<int, int>> coada;
    vector<bool> vizitat(nrV + 1);
    coada.push(make_pair(sursa, 0));

    while(!coada.empty()) {
        pair<int, int> v = coada.front();
        vizitat[v.first] = true;
        cout<<"v="<<v.first<<", d="<<v.second<<'\n';

        for(int i=0;i<listaAdj[v.first].size();i++) {
            if(!vizitat[listaAdj[v.first][i]]) {
                coada.push(make_pair(listaAdj[v.first][i],
                    v.second + 1));
            }
        }

        coada.pop();
    }

    return 0;
}
