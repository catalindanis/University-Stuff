#include <iostream>
#include <fstream>
#include <vector>

using namespace std;

ifstream fin("partial.txt");

int main() {
    int n, m;
    fin >> n >> m;

    vector<vector<int>> distance(n + 1, vector<int>(n + 1, INT_MAX));
    for (int i = 1; i <= n; i++)
        distance[i][i] = 0;

    for (int i = 1; i <= m; i++) {
        int x, y, p;
        fin >> x >> y >> p;
        distance[x][y] = p;
    }

    for (int k = 1; k <= n; k++)
        for (int i = 1; i <= n; i++)
            for (int j = 1; j <= n; j++)
                if (distance[i][k] < INT_MAX && distance[k][j] < INT_MAX)
                    distance[i][j] = min(distance[i][k] + distance[k][j], distance[i][j]);

    for (int i = 1; i <= n; i++)
        if (distance[i][i] < 0) {
            cout << "Ciclu negativ!" << endl;
            return 0;
        }

    for (int i = 1; i <= n; i++)
        for (int j = 1; j <= n; j++) {
            cout<<"Distanta dintre "<<i<< " si "<< j<< " este: ";
            if(distance[i][j] == INT_MAX)
                cout<<"INF";
            else
                cout<<distance[i][j]<<' ';
            cout<<endl;
        }
    return 0;
}