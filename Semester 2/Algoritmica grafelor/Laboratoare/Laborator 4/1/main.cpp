#include <iostream>
#include <fstream>
#include <vector>
#include <set>
#include <queue>
using namespace std;

int main(int argc, char* argv[]) {
    ifstream fin(argv[1]);
    ofstream fout(argv[2]);
    int nr;
    fin >> nr;

    vector<int> tati(nr);
    vector<vector<int>> arbore(nr);
    vector<int> grade(nr, 0);

    for (int i = 0; i < nr; ++i) {
        fin >> tati[i];
        if (tati[i] != -1) {
            arbore[tati[i]].push_back(i);
            arbore[i].push_back(tati[i]);
            grade[tati[i]]++;
            grade[i]++;
        }
    }

    set<int> frunze;
    for (int i = 0; i < nr; ++i)
        if (grade[i] == 1)
            frunze.insert(i);

    vector<int> cod;
    for (int i = 0; i < nr - 2; ++i) {
        int frunza = *frunze.begin();
        frunze.erase(frunze.begin());
        for (int parinte : arbore[frunza]) {
            if (grade[parinte] > 0) {
                cod.push_back(parinte);
                grade[frunza]--;
                grade[parinte]--;
                if (grade[parinte] == 1) {
                    frunze.insert(parinte);
                }
                break;
            }
        }
    }

    fout << cod.size() << "\n";
    for (int i = 0; i < cod.size(); ++i) {
        fout << cod[i] << " ";
    }

    return 0;
}