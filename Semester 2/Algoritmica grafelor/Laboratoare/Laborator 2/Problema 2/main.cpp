#include <iostream>
#include <fstream>
#include <vector>
using namespace std;

int main() {
    ifstream fin("graf.txt");

    int nrV;
    fin>>nrV;

    vector<vector<int>> mat(nrV + 1, vector<int>(nrV + 1, 0));

    int x, y;
    while(fin>>x>>y) {
        mat[x][y] = 1;
    }

    for(int k=1;k<=nrV;k++)
        for(int i=1;i<=nrV;i++)
            for(int j=1;j<=nrV;j++)
                mat[i][j] = mat[i][j] || (mat[i][k] && mat[k][j]);

    for(int i=1;i<=nrV;i++)
        mat[i][i] = 1;

    for(int i=1;i<=nrV;i++, cout<<'\n')
        for(int j=1;j<=nrV;j++)
            cout<<mat[i][j]<<' ';
    return 0;
}
