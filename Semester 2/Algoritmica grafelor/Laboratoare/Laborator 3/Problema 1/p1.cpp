#include <fstream>
#include <iostream>
#include <climits>
#include <vector>

using namespace std;

vector<pair<pair<int, int>, int>> adjList;
vector<int> distances;
vector<int> parent;

void init(int n) {
    for(int i=0;i<n;i++) {
        distances[i] = INT_MAX;
        parent[i] = NULL;
    }
}

bool relax(int x, int y, int i) {
     if(distances[x] != INT_MAX && distances[y] > distances[x] + adjList[i].second) {
         distances[y] = distances[x] + adjList[i].second;
         parent[y] = x;
         return true;
     }
    return false;
}

bool bellmanford(int n, int m, int s) {
    init(n);
    distances[s] = 0;
    for(int i=1;i<m;i++)
        for(int i=0;i<adjList.size();i++) {
            //if(relax(adjList[i].first.first, adjList[i].first.second, i))
                //cout<<"Relaxez muchia: "<<adjList[i].first.first<<' '<< adjList[i].first.second<<'\n';
            relax(adjList[i].first.first, adjList[i].first.second, i);
        }

    for(int i=0;i<adjList.size();i++)
        if(relax(adjList[i].first.first, adjList[i].first.second, i))
            return false;

    return true;
}

int main(int argc, char* argv[]) {
    ifstream fin(argv[1]);
    ofstream fout(argv[2]);

    int n, m, s;
    fin>>n>>m>>s;

    int x, y, p;
    for(int i=0; i<m; i++) {
        fin>>x>>y>>p;
        adjList.push_back(make_pair(make_pair(x, y), p));
    }

    // for(int i=0;i<m;i++)
    //     fout<<adjList[i].first.first<<' '<<adjList[i].first.second<<' '<<adjList[i].second<<'\n';

    distances = vector<int>(n);
    parent = vector<int>(n);
    if(bellmanford(n, m, s) == false) {
        fout<<"Exista un ciclu de pondere negativa!";
        return 1;
    }

    for(int i=0;i<n;i++)
        if(distances[i] == INT_MAX)
            fout<<"INF ";
        else fout<<distances[i]<<' ';

    fin.close();
    fout.close();
    return 0;
}
