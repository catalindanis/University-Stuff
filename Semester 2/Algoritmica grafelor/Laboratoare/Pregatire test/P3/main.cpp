#include <iostream>
#include <fstream>
#include <queue>
#include <vector>
using namespace std;

// Problema nota 10-timp de lucru 40 de minute
// Se da o configuratie de case (c1, c2, ... cn). n - reprezentand numarul total de
// case (n<=1000). Exista drumuri unice unidirectionale intre diferite case avand o
// anumita lungime L (de exemplu tripletul ci cj L inseamna ca avem un drum intre ci
// si cj cu lungimea L). Un vizitatator trebuie sa ajunga de casa cx la casa cy.
// (cx si cy fiind cunoscute). Gasiti o solutie optima pentru vizitator. Afisati aceasta
// solutie.

int n, m;
vector<vector<pair<int, int>>> listaAdj;
vector<int> dist, par;

void init(int s) {
    for(int i=1;i<=n;i++) {
        dist[i] = INT_MAX;
        par[i] = NULL;
    }
    dist[s] = 0;
}

struct Compare {
    bool operator()(pair<int, int> a, pair<int, int> b) {
        return a.second > b.second;
    }
};

bool relax(int u, int v, int w) {
    if(dist[u] != INT_MAX && dist[v] > dist[u] + w) {
        dist[v] = dist[u] + w;
        par[v] = u;
        return true;
    }
    return false;
}

void dijsktra(int s) {
    init(s);
    priority_queue<pair<int, int>, vector<pair<int, int>>, Compare> q;
    q.push(make_pair(s, dist[s]));

    while(!q.empty()) {
        pair p = q.top();
        int x = p.first;
        for(int i=0;i<listaAdj[x].size();i++) {
            int y = listaAdj[x][i].first;
            int w = listaAdj[x][i].second;
            if(relax(x, y, w))
                q.push(make_pair(y, dist[y]));
        }
        q.pop();
    }
}

int main() {
    ifstream fin("input.txt");

    int x, y, p;
    fin>>n>>m;

    listaAdj.resize(n + 1);
    dist.resize(n + 1);
    par.resize(n + 1);
    for(int i=0;i<m;i++) {
        fin>>x>>y>>p;
        listaAdj[x].push_back(make_pair(y, p));
    }

    int cx, cy;
    fin>>cx>>cy;

    dijsktra(cx);

    cout<<"Distanta = "<<dist[cy];
    return 0;
}
