#include <iostream>
#include <fstream>
#include <queue>
#include <vector>
using namespace std;

ifstream fin("in.txt");
ofstream fout("out.txt");

bool relax(int u, int v, int w, vector<int>& distance) {
    if(distance[u] != INT_MAX && distance[v] > distance[u] + w) {
        distance[v] = distance[u] + w;
        return true;
    }
    return false;
}

void init(int V, vector<int>& distance) {
    for(int i=0;i<V;i++)
        distance[i] = INT_MAX;
}

bool bellmanFord(int V, int E, int s, vector<pair<int, int>>& edges, vector<int>& weight, vector<int>& result) {
    result = vector<int>(V, 0);
    init(V, result);
    result[s] = 0;
    for(int k=1; k <= V-1; k++)
        for(int i=0; i < E; i++)
            relax(edges[i].first, edges[i].second, weight[i], result);
    for(int i=0 ;i < E; i++)
        if(relax(edges[i].first, edges[i].second, weight[i], result))
            return false;
    return true;
}

vector<int> dijkstra(int V, int E, int s, vector<pair<int, int>>& edges, vector<int>& weight) {
    vector<int> distance(V, 0);
    init(V, distance);
    distance[s] = 0;

    priority_queue<pair<int, int>, vector<pair<int, int>>, greater<>> q;
    q.push(make_pair(0, s));

    while(!q.empty()) {
        pair<int, int> p = q.top();

        for(int i=0;i<E;i++)
            if(edges[i].first == p.second) {
                if(relax(edges[i].first, edges[i].second, weight[i], distance))
                    q.push(make_pair(distance[edges[i].second], edges[i].second));
            }

        q.pop();
    }

    return distance;
}

vector<int> johnson(int V, int E, vector<pair<int, int>>& edges, vector<int>& weight) {
    vector<pair<int, int>> _edges = edges;
    int _V = V + 1;
    int _E = V + E;
    vector<int> _weight = weight;
    for(int i=0; i < V; i++) {
        _edges.push_back(make_pair(V, i));
        _weight.push_back(0);
    }

    vector<int> correction;
    if(bellmanFord(_V, _E, V, _edges, _weight, correction) == false) {
        cout<<"-1";
        exit(1);
    }

    vector<int> corrWeight;
    for(int i=0;i<E;i++)
        corrWeight.push_back(weight[i] + correction[edges[i].first] - correction[edges[i].second]);

    for(int i=0;i<E;i++) {
        //fout<<edges[i].first<<' '<<edges[i].second<<' '<<corrWeight[i]<<'\n';
    }

    for(int i=0;i<V;i++) {
        vector<int> distance = dijkstra(V, E, i, edges, corrWeight);
        for(int j = 0; j < V; j++) {
            if (distance[j] == INT_MAX) {
                fout << "INF ";
            } else {
                int realDist = distance[j] + correction[j] - correction[i];
                fout << realDist << " ";
            }
        }
        fout<<'\n';
    }

    return correction;
}

int main() {
    vector<pair<int, int>> edges;
    vector<int> weight;
    int V, E, x, y, p;
    fin>>V>>E;

    for(int i=0;i<E;i++) {
        fin>>x>>y>>p;
        edges.push_back(make_pair(x, y));
        weight.push_back(p);
    }

    vector<int> distances = johnson(V, E, edges, weight);

    fin.close();
    fout.close();
    return 0;
}
