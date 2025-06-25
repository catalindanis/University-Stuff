#include <iostream>
#include <fstream>
#include <queue>
#include <unordered_map>
#include <vector>
#include <string>
#include <algorithm>

using namespace std;

struct Node {
    char ch;
    int freq;
    Node *left, *right;

    Node(char c, int f) : ch(c), freq(f), left(nullptr), right(nullptr) {}
};

struct cmp {
    bool operator()(Node* a, Node* b) {
        if (a->freq == b->freq) return a->ch > b->ch;
        return a->freq > b->freq;
    }
};

void getCodes(Node* root, string code, unordered_map<char, string>& codes) {
    if (!root->left && !root->right) {
        codes[root->ch] = code;
        return;
    }
    if (root->left) getCodes(root->left, code + "0", codes);
    if (root->right) getCodes(root->right, code + "1", codes);
}

int main(int argc, char* argv[]) {
    if (argc != 3) return 1;

    ifstream fin(argv[1]);
    ofstream fout(argv[2]);

    string s;
    getline(fin, s);

    unordered_map<char, int> freq;
    for (char c : s) freq[c]++;

    priority_queue<Node*, vector<Node*>, cmp> pq;
    for (auto p : freq) pq.push(new Node(p.first, p.second));

    while (pq.size() > 1) {
        Node* a = pq.top(); pq.pop();
        Node* b = pq.top(); pq.pop();
        Node* m = new Node(min(a->ch, b->ch), a->freq + b->freq);
        m->left = a; m->right = b;
        pq.push(m);
    }

    Node* root = pq.top();
    unordered_map<char, string> codes;
    getCodes(root, "", codes);

    vector<pair<char, int>> v(freq.begin(), freq.end());
    sort(v.begin(), v.end());

    fout << v.size() << "\n";
    for (auto p : v) fout << p.first << " " << p.second << "\n";

    for (char c : s) fout << codes[c];
    fout << "\n";

    return 0;
}
