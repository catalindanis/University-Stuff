#include <iostream>
#include <fstream>
#include <queue>
using namespace std;

int mat[101][101];
int n;
bool viz[101];
int matD[101][101];

//dfs simplu
void dfs(int vf) {
    viz[vf] = true;
    for(int i=1;i<=n;i++)
        if(mat[vf][i] && !viz[i])
            dfs(i);
}

//bfs care construieste matricea de distante
void bfsD(int sursa) {
    for(int j=1;j<=n;j++)
        viz[j] = false;
    queue<pair<int, int>> q;
    q.push(make_pair(sursa, 0));
    matD[sursa][sursa] = 0;
    viz[sursa] = true;
    while(!q.empty()) {
        int c = q.front().first;
        int d = q.front().second;

        for(int i=1;i<=n;i++)
            if(mat[c][i] && !viz[i]) {
                viz[i] = true;
                matD[sursa][i] = d + 1;
                q.push(make_pair(i, matD[sursa][i]));
            }

        q.pop();
    }
}

int main() {
    //citirea grafului din fisier sub forma unei matrici de adiacenta
    ifstream fin("in.txt");

    fin>>n;
    int i, j;
    while(fin>>i>>j) {
        mat[i][j] = mat[j][i] = 1;
    }
    fin.close();

    //afisare matrice de adiacenta a grafului citit
    cout<<"Matricea citita: "<<endl;
    for(int i=1;i<=n;i++) {
        for(int j=1;j<=n;j++)
            cout<<mat[i][j]<<' ';
        cout<<'\n';
    }

    //determinare noduri izolate
    cout<<endl;
    bool exista = false;
    cout<<"Noduri izolate: ";
    for(int i=1;i<=n;i++) {
        int grad = 0;
        for(int j=1;j<=n;j++)
            grad += mat[i][j];
        if(grad == 0) {
            cout<<i<<' ';
            exista = true;
        }
    }
    if(!exista)
        printf("nu exista");

    //verificare daca graful este regulat
    int grad = 0;
    for(int i=1;i<=n;i++)
        grad += mat[1][i];

    bool regulat = true;
    for(int i=2;i<=n;i++) {
        int gradCurent = 0;
        for(int j=1;j<=n;j++)
            gradCurent += mat[i][j];
        if(gradCurent != grad)
            regulat = false;
    }

    cout<<endl;
    cout<<"Graful este "<<(regulat ? "regulat" : "neregulat")<<endl;

    //verificare daca graful este conex
    bool conex = true;
    dfs(1);
    for(int i=1;i<=n;i++)
        if(!viz[i])
            conex = false;
    cout<<"Graful "<<(conex ? "este conex" : "nu este conex")<<endl;

    //determinare matricea distantelor
    for(int i=1;i<=n;i++)
        for(int j=1;j<=n;j++)
            if(i == j) matD[i][j] = 0;
            else matD[i][j] = -1; //reprezinta infinit

    for(int i=1;i<=n;i++) {
        bfsD(i);
    }

    cout<<endl;
    cout<<"Matricea distantelor: "<<endl;
    for(int i=1;i<=n;i++) {
        for(int j=1;j<=n;j++)
            cout<<matD[i][j]<<' ';
        cout<<endl;
    }

    return 0;
}

using namespace std;