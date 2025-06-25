#include <iostream>
#include <fstream>
#include <queue>
#include <vector>
using namespace std;

const int NO_ROWS = 21;
const int NO_COLS = 41;

vector<vector<int>> matrix(NO_ROWS + 1, vector<int>(NO_COLS + 1));
int iStart, jStart, iFinal, jFinal;
vector<vector<char>> result(NO_ROWS + 1, vector<char>(NO_COLS + 1, ' '));
queue<pair<int,int>> q;
int dI[] = {0, -1, 0, +1};
int dJ[] = {-1, 0, +1, 0};

bool inMatrix(int x, int y) {
    return x >= 1 && x <= NO_ROWS && y >= 1 && y <= NO_COLS;
}

int main()
{
    ifstream fin("labirint1.txt");

    for (int i = 1; i <= NO_ROWS; i++) {
        for (int j = 1; j <= NO_COLS; j++) {
            char c;
            fin.get(c);

            while (c == '\n') fin.get(c);

            result[i][j] = c;

            switch (c) {
                case '1':
                    matrix[i][j] = -1;
                    break;
                case 'F':
                    matrix[i][j] = 0;
                    iFinal = i;
                    jFinal = j;
                    break;
                case 'S':
                    matrix[i][j] = -2;
                    iStart = i;
                    jStart = j;
                    break;
                case ' ':
                    matrix[i][j] = -2;
                    break;
            }
        }
    }

    q.push({iFinal, jFinal});

    while (!q.empty()) {
        auto [ci, cj] = q.front();
        q.pop();

        for (int k = 0; k < 4; k++) {
            int ni = ci + dI[k];
            int nj = cj + dJ[k];

            if (inMatrix(ni, nj) && matrix[ni][nj] == -2) {
                matrix[ni][nj] = matrix[ci][cj] + 1;
                q.push({ni, nj});
            }
        }
    }

    q = queue<pair<int, int>>();
    q.push({iStart, jStart});

    while (!q.empty()) {
        auto [ci, cj] = q.front();
        q.pop();

        result[ci][cj] = '*';

        if (ci == iFinal && cj == jFinal)
            break;

        for (int k = 0; k < 4; k++) {
            int ni = ci + dI[k];
            int nj = cj + dJ[k];

            if (inMatrix(ni, nj) && matrix[ni][nj] == matrix[ci][cj] - 1) {
                q.push({ni, nj});
                break;
            }
        }
    }

    for (int i = 1; i <= NO_ROWS; i++, cout << '\n')
        for (int j = 1; j <= NO_COLS; j++)
            cout << result[i][j];

    return 0;
}
