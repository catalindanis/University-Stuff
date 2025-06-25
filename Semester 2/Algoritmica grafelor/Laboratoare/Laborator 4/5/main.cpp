#include <iostream>
#include <fstream>
#include <vector>
#include <algorithm>

using namespace std;

struct Edge {
    int u, v, w;
    bool operator<(const Edge& other) const {
        return w < other.w;
    }
};

vector<int> parent;

int find(int x) {
    if (parent[x] != x)
        parent[x] = find(parent[x]);
    return parent[x];
}

bool unite(int x, int y) {
    int xr = find(x);
    int yr = find(y);
    if (xr == yr) return false;
    parent[yr] = xr;
    return true;
}

int main(int argc, char* argv[]) {
    ifstream fin(argv[1]);
    ofstream fout(argv[2]);

    int V, E;
    fin >> V >> E;

    vector<Edge> edges(E);
    for (int i = 0; i < E; ++i) {
        fin >> edges[i].u >> edges[i].v >> edges[i].w;
    }

    sort(edges.begin(), edges.end());
    parent.resize(V);
    for (int i = 0; i < V; ++i)
        parent[i] = i;

    int cost = 0;
    vector<pair<int, int>> mst;

    for (auto& e : edges) {
        if (unite(e.u, e.v)) {
            cost += e.w;
            mst.push_back({e.u, e.v});
        }
    }

    fout << cost << '\n';
    fout << mst.size() << '\n';
    for (auto& p : mst)
        fout << p.first << ' ' << p.second << '\n';

    return 0;
}
