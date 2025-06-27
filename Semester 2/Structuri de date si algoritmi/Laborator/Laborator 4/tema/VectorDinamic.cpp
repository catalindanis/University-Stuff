#include "VectorDinamic.h"

VectorDinamic::VectorDinamic() {
    this->capacity = 1;
    this->size = 0;
    this->values = new TElem[capacity];
}

void VectorDinamic::add(TElem e) {
    if(this->size >= this->capacity)
        increaseSize();
    this->values[this->size] = e;
}

void VectorDinamic::remove(TElem e) {
    for(int i=0;i<this->size;i++)
        if(this->values[i] == e) {
            for(int j=i; j<this->size-1;j++)
                this->values[j] = this->values[j+1];
            break;
        }
}

void VectorDinamic::set(int i, TElem e) {
    this->values[i] = e;
}


int VectorDinamic::dim() const {
    return this->size;
}

void VectorDinamic::increaseSize() {
    this->capacity *= 2;
    TElem* temp = new TElem[this->capacity];

    for(int i=0;i<this->size;i++)
        temp[i] = this->values[i];

    delete [] this->values;
    this->values = temp;
}

TElem VectorDinamic::get(int i) const {
    return this->values[i];
}

VectorDinamic::~VectorDinamic() {
    delete [] this->values;
}



