#include <iostream>
#include <fstream>
#include <vector>
#include <queue>
#include <algorithm>
#include <cstring>

using namespace std;

const int MAX_V = 1005;

int capacity[MAX_V][MAX_V];  // capacitatea arcelor
int flow[MAX_V][MAX_V];      // fluxul curent
int height[MAX_V];           // inalțimea nodurilor
int excess[MAX_V];           // excesul de flux
vector<int> adj[MAX_V];      // lista de adiacenta

int V;

void push(int u, int v) {
    int send = min(excess[u], capacity[u][v] - flow[u][v]);
    if (send <= 0 || height[u] <= height[v]) return;

    flow[u][v] += send;
    flow[v][u] -= send;
    excess[u] -= send;
    excess[v] += send;
}

void relabel(int u) {
    int minHeight = INT_MAX;
    for (int v : adj[u]) {
        if (capacity[u][v] - flow[u][v] > 0)
            minHeight = min(minHeight, height[v]);
    }
    if (minHeight < INT_MAX)
        height[u] = minHeight + 1;
}

void discharge(int u, queue<int>& active) {
    for (size_t i = 0; i < adj[u].size() && excess[u] > 0; ++i) {
        int v = adj[u][i];
        push(u, v);
    }

    if (excess[u] > 0) {
        relabel(u);
        for (int v : adj[u]) push(u, v);
        if (excess[u] > 0) active.push(u);  // rămâne activ
    }
}

int preflowPush(int s, int t) {
    memset(flow, 0, sizeof(flow));
    memset(excess, 0, sizeof(excess));
    memset(height, 0, sizeof(height));

    height[s] = V;
    for (int v : adj[s]) {
        flow[s][v] = capacity[s][v];
        flow[v][s] = -capacity[s][v];
        excess[v] = capacity[s][v];
        excess[s] -= capacity[s][v];
    }

    queue<int> active;
    for (int i = 0; i < V; ++i) {
        if (i != s && i != t && excess[i] > 0) active.push(i);
    }

    while (!active.empty()) {
        int u = active.front();
        active.pop();
        discharge(u, active);
    }

    int max_flow = 0;
    for (int v = 0; v < V; ++v)
        max_flow += flow[s][v];

    return max_flow;
}

int main(int argc, char* argv[]) {

    ifstream fin(argv[1]);
    ofstream fout(argv[2]);

    int E;
    fin >> V >> E;

    for (int i = 0; i < E; ++i) {
        int u, v, c;
        fin >> u >> v >> c;
        if (capacity[u][v] == 0) {
            adj[u].push_back(v);
            adj[v].push_back(u);
        }
        capacity[u][v] += c;
    }

    int s = 0, t = V - 1;
    fout << preflowPush(s, t) << endl;

    return 0;
}
