#include <iostream>
#include <fstream>
#include <vector>
using namespace std;

vector<bool> vizitat;

void dfs(vector<vector<int>> graf, int n, int v) {
    vizitat[v] = true;
    cout<<v<<' ';
    for(int i=1;i<=n;i++)
        if(graf[v][i] && !vizitat[i])
            dfs(graf, n, i);
}

int main() {
    ifstream fin("graf.txt");

    int n;
    fin>>n;

    vector<vector<int>> graf(n+1, vector<int>(n+1, 0));
    vizitat = vector<bool>(n+1, false);

    int x, y;
    while(fin>>x>>y) {
        graf[x][y] = 1;
    }

    int cnt = 1;
    for(int i=1;i<=n;i++) {
        if(!vizitat[i]) {
            cout<<"Padurea "<<cnt++<<'\n';
            dfs(graf, n, i);
            cout<<'\n';
        }
    }

    return 0;
}
