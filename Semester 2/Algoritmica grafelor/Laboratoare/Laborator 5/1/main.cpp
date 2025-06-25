#include <iostream>
#include <fstream>
#include <vector>
#include <queue>
#include <climits>
#include <cstring>

using namespace std;

const int MAX_V = 1005; // maxim 1000 noduri
int capacity[MAX_V][MAX_V]; // capacitatea muchiilor
vector<int> adj[MAX_V];     // lista de adiacență

int bfs(int s, int t, vector<int>& parent, int V) {
    fill(parent.begin(), parent.end(), -1);
    parent[s] = -2;
    queue<pair<int, int>> q;
    q.push({s, INT_MAX});

    while (!q.empty()) {
        int cur = q.front().first;
        int flow = q.front().second;
        q.pop();

        for (int next : adj[cur]) {
            if (parent[next] == -1 && capacity[cur][next] > 0) {
                parent[next] = cur;
                int new_flow = min(flow, capacity[cur][next]);
                if (next == t)
                    return new_flow;
                q.push({next, new_flow});
            }
        }
    }

    return 0;
}

int edmondsKarp(int s, int t, int V) {
    int flow = 0;
    vector<int> parent(V);
    int new_flow;

    while ((new_flow = bfs(s, t, parent, V))) {
        flow += new_flow;
        int cur = t;
        while (cur != s) {
            int prev = parent[cur];
            capacity[prev][cur] -= new_flow;
            capacity[cur][prev] += new_flow;
            cur = prev;
        }
    }

    return flow;
}

int main(int argc, char* argv[]) {
    ifstream fin(argv[1]);
    ofstream fout(argv[2]);

    int V, E;
    fin >> V >> E;

    for (int i = 0; i < E; ++i) {
        int u, v, c;
        fin >> u >> v >> c;
        capacity[u][v] += c;  // dacă există mai multe muchii, adunăm capacitățile
        adj[u].push_back(v);
        adj[v].push_back(u);  // adăugăm și invers pentru graful rezidual
    }

    int s = 0, t = V - 1;
    fout << edmondsKarp(s, t, V) << endl;

    return 0;
}
