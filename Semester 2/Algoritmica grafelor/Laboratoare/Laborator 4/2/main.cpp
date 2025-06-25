#include <fstream>
#include <vector>
#include <set>
using namespace std;

int main(int argc, char* argv[]) {
    ifstream fin(argv[1]);
    ofstream fout(argv[2]);

    int m;
    fin >> m;
    int n = m + 2;

    vector<int> prufer(m);
    vector<int> grade(n, 1);

    for (int i = 0; i < m; ++i) {
        fin >> prufer[i];
        grade[prufer[i]]++;
    }

    set<int> frunze;
    for (int i = 0; i < n; ++i) {
        if (grade[i] == 1) {
            frunze.insert(i);
        }
    }

    vector<int> tati(n, -1);

    for (int i = 0; i < m; ++i) {
        int frunza = *frunze.begin();
        frunze.erase(frunze.begin());

        tati[frunza] = prufer[i];

        grade[frunza]--;
        grade[prufer[i]]--;

        if (grade[prufer[i]] == 1) {
            frunze.insert(prufer[i]);
        }
    }

    auto it = frunze.begin();
    int u = *it++;
    int v = *it;

    tati[u] = v;

    fout << n << "\n";
    for (int i = 0; i < n; ++i) {
        fout << tati[i] << " ";
    }
    fout << "\n";

    return 0;
}