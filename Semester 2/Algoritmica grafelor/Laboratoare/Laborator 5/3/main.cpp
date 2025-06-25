#include <iostream>
#include <fstream>
#include <vector>
#include <algorithm>
#include <set>
#include <map>

using namespace std;

const int MAX_V = 100010;
vector<multiset<int>> adj;
vector<int> eulerian_path;

void dfs(int u) {
    while (!adj[u].empty()) {
        int v = *adj[u].begin();
        adj[u].erase(adj[u].begin());
        adj[v].erase(adj[v].find(u)); // ștergem muchia și în sens invers
        dfs(v);
    }
    eulerian_path.push_back(u);
}

int main(int argc, char* argv[]) {

    ifstream fin(argv[1]);
    ofstream fout(argv[2]);

    int V, E;
    fin >> V >> E;

    adj.resize(V);
    vector<int> degree(V, 0);

    for (int i = 0; i < E; ++i) {
        int u, v;
        fin >> u >> v;
        adj[u].insert(v);
        adj[v].insert(u);
        degree[u]++;
        degree[v]++;
    }

    // Verificăm dacă toate nodurile au grad par
    for (int i = 0; i < V; ++i) {
        if (degree[i] % 2 != 0) {
            fout << "Nu exista ciclu eulerian\n";
            return 0;
        }
    }

    // Pornim DFS-ul dintr-un nod cu grad > 0
    int start = 0;
    while (start < V && adj[start].empty()) ++start;

    dfs(start);

    // Verificăm dacă toate muchiile au fost parcurse
    if (eulerian_path.size() != E + 1) {
        fout << "Nu exista ciclu eulerian\n";
        return 0;
    }

    reverse(eulerian_path.begin(), eulerian_path.end());
    for (size_t i = 0; i < eulerian_path.size(); ++i) {
        fout << eulerian_path[i];
        if (i + 1 < eulerian_path.size()) fout << " ";
    }
    fout << endl;

    return 0;
}
