#include <iostream>
#include <fstream>
#include <queue>
#include <vector>
#include <string>
#include <algorithm>

using namespace std;

struct Node {
    char ch;
    int freq;
    Node *left, *right;

    Node(char c, int f) : ch(c), freq(f), left(nullptr), right(nullptr) {}
    Node(int f, Node* l, Node* r) : ch('\0'), freq(f), left(l), right(r) {}
};

struct cmp {
    bool operator()(Node* a, Node* b) {
        if (a->freq == b->freq) return a->ch > b->ch;
        return a->freq > b->freq;
    }
};

Node* buildTree(const vector<pair<char, int>>& freq) {
    auto cmp_tree = [](Node* a, Node* b) {
        if (a->freq == b->freq) return a->ch > b->ch;
        return a->freq > b->freq;
    };
    priority_queue<Node*, vector<Node*>, decltype(cmp_tree)> pq(cmp_tree);

    for (auto& p : freq)
        pq.push(new Node(p.first, p.second));

    while (pq.size() > 1) {
        Node* a = pq.top(); pq.pop();
        Node* b = pq.top(); pq.pop();
        char min_char = min(a->ch, b->ch);
        Node* merged = new Node(min_char, a->freq + b->freq);
        merged->left = a;
        merged->right = b;
        pq.push(merged);
    }

    return pq.top();
}

string decodeText(Node* root, const string& bits) {
    string result = "";
    Node* current = root;
    for (char bit : bits) {
        current = (bit == '0') ? current->left : current->right;
        if (!current->left && !current->right) {
            result += current->ch;
            current = root;
        }
    }
    return result;
}

int main(int argc, char* argv[]) {
    if (argc != 3) return 1;

    ifstream fin(argv[1]);
    ofstream fout(argv[2]);

    int N;
    fin >> N;
    vector<pair<char, int>> freq(N);
    for (int i = 0; i < N; ++i) fin >> ws >> freq[i].first >> freq[i].second;

    string bits;
    fin >> bits;

    Node* root = buildTree(freq);
    string decoded = decodeText(root, bits);
    fout << decoded << "\n";

    return 0;
}