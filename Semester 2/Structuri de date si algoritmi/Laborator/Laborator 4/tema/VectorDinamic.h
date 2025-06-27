#pragma once

typedef int TElem;

class VectorDinamic {
private:
    int size;
    int capacity;
    TElem* values;

    void increaseSize();
public:
    VectorDinamic();
    void add(TElem e);
    void remove(TElem e);
    TElem get(int i) const;
    void set(int i, TElem e);
    int dim() const;
    ~VectorDinamic();
};
