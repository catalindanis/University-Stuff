#include <iostream>
#include <fstream>
#include <vector>
#include <set>

using namespace std;

int main() {
    int n;
    int matAdiacenta[101][101];

    //citire graf din fisier
    ifstream fin("in.txt");

    fin>>n;
    int x, y;
    while(fin>>x>>y) {
        matAdiacenta[x][y] = matAdiacenta[y][x] = 1;
    }

    //afisare matrice de adiacenta
    cout<<"Matrice de adiacenta: "<<endl;
    for(int i=1;i<=n;i++) {
        for(int j=1;j<=n;j++)
            cout<<matAdiacenta[i][j]<<" ";
        cout<<endl;
    }

    //transformare matrice adiacenta in lista de adiacenta
    vector<vector<int>> listaAdiacenta = vector<vector<int>>(n+1);
    for(int i=1;i<=n;i++)
        for(int j=1;j<=n;j++)
            if(matAdiacenta[i][j] == 1)
                listaAdiacenta[i].push_back(j);

    //afisare lista de adiacenta
    cout<<endl<<"Lista de adiacenta: "<<endl;
    for(int i=1;i<=n;i++) {
        cout<<i<<": ";
        for(int j=0;j<listaAdiacenta[i].size();j++)
            cout<<listaAdiacenta[i][j]<<" ";
        cout<<endl;
    }

    //transformare lista adiacenta in matrice incidenta
    set<pair<int,int>> muchii;
    for(int i=1;i<=n;i++)
        for(int j=0;j<listaAdiacenta[i].size();j++)
            if(i < listaAdiacenta[i][j])
                muchii.insert(make_pair(i,listaAdiacenta[i][j]));

    vector<vector<int>> matIncidenta = vector<vector<int>>(n+1, vector<int>(muchii.size()+1, 0));
    int indMuchie = 1;
    for(auto m : muchii) {
        matIncidenta[m.first][indMuchie] = 1;
        matIncidenta[m.second][indMuchie] = 1;
        indMuchie++;
    }

    //afisare matrice incidenta
    cout<<endl<<"Matrice incidenta: "<<endl;
    for(int i=1;i<=n;i++) {
        for(int j=1;j<=muchii.size();j++)
            cout<<matIncidenta[i][j]<<" ";
        cout<<endl;
    }

    //transformare matrice incidenta in lista adiacenta
    listaAdiacenta = vector<vector<int>>(n+1);
    for(int j=1;j<=muchii.size();j++) {
        int x = -1, y = -1;
        for(int i=1;i<=n;i++)
            if(matIncidenta[i][j] == 1) {
                if(x == -1)
                    x = i;
                else y = i;
            }
        listaAdiacenta[x].push_back(y);
        listaAdiacenta[y].push_back(x);
    }

    //afisare lista de adiacenta
    cout<<endl<<"Lista de adiacenta: "<<endl;
    for(int i=1;i<=n;i++) {
        cout<<i<<": ";
        for(int j=0;j<listaAdiacenta[i].size();j++)
            cout<<listaAdiacenta[i][j]<<" ";
        cout<<endl;
    }

    //transformare lista adiacenta in matrice adiacenta
    for(int i=1;i<=n;i++)
        for(int j=1;j<=n;j++)
            matAdiacenta[i][j] = 0;

    for(int i=1;i<=n;i++)
        for(int j=0;j<listaAdiacenta[i].size();j++)
            matAdiacenta[i][listaAdiacenta[i][j]] = 1;

    //afisare matrice de adiacenta
    cout<<endl<<"Matrice de adiacenta: "<<endl;
    for(int i=1;i<=n;i++) {
        for(int j=1;j<=n;j++)
            cout<<matAdiacenta[i][j]<<" ";
        cout<<endl;
    }

    //transformare matrice de adiacenta in lista de muchii
    muchii = set<pair<int,int>>();
    for(int i=1;i<=n;i++)
        for(int j=i+1;j<=n;j++)
            if(matAdiacenta[i][j] == 1)
                muchii.insert(make_pair(i,j));

    //afisare lista de muchii
    cout<<endl<<"Lista de muchii: "<<endl;
    cout<<n<<endl;
    for(auto m : muchii) {
        cout<<m.first<<" "<<m.second<<endl;
    }
    return 0;
}
