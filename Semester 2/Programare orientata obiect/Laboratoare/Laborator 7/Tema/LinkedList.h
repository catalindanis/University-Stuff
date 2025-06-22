#pragma once
#include <vector>
using namespace std;

template <typename Element>
class Node {
private:
    Element value;
    Node* next;
    Node* previous;

public:
    Node(const Element& value) : value(value), next(nullptr), previous(nullptr) {}
    void setValue(const Element& val) {
        this->value = val;
    }
    void setNext(Node* nex) {
        this->next = nex;
    }
    void setPrevious(Node* prev) {
        this->previous = prev;
    }
    Element getValue() const {
        return this->value;
    }
    Node* getNext() const {
        return this->next;
    }
    Node* getPrevious() const {
        return this->previous;
    }
};

template<typename Element>
class LinkedList {
private:
    Node<Element>* first;
    Node<Element>* last;
    int size;

public:
    LinkedList() {
        this->first = nullptr;
        this->last = nullptr;
        this->size = 0;
    }
    ~LinkedList() {
        Node<Element>* current = this->getFirst();
        
        while (current != nullptr) {
            Node<Element>* toDelete = current;
            current = current->getNext();
            delete toDelete;
        }
    }
    void add(const Element& value) {
        Node<Element>* element = new Node<Element>(value);

        if (!this->first) {
            this->first = element;
            this->last = element;
        }
        else {
            this->last->setNext(element);
            element->setPrevious(this->last);
            this->last = element;
        }

        this->size++;
    }
    void remove(const Element& value) {
        Node<Element>* current = this->getFirst();
        while (current != nullptr) {
            if (current->getValue() == value) {
                Node<Element>* prev = current->getPrevious();
                Node<Element>* next = current->getNext();

                if (prev)
                    prev->setNext(next);
                else
                    this->first = next;

                if (next)
                    next->setPrevious(prev);
                else
                    this->last = prev;

                Node<Element>* toDelete = current;
                current = next;
                delete toDelete;
                this->size--;
            }
            else
                current = current->getNext();
        }
    }

    bool search(Element& value) {
        Node<Element>* current = this->getFirst();

        while (current != nullptr) {
            if (current->getValue() == value)
                return true;

            current = current->getNext();
        }

        return false;
    }

    void update(Element& previousValue, Element& newValue) {
        Node<Element>* current = this->getFirst();

        while (current != nullptr) {
            if (current->getValue() == previousValue) {
                current->setValue(newValue);
            }

            current = current->getNext();
        }
    }
    Node<Element>* getFirst() const {
        return this->first;
    }
    Node<Element>* getLast() const {
        return this->last;
    }
    vector<Element> getAll() const {
        vector<Element> list;
        Node<Element>* current = this->getFirst();

        while (current != nullptr) {
            list.push_back(current->getValue());
            current = current->getNext();
        }

        return list;
    }

    unsigned int getSize() const {
        return this->size;
    }

    class Iterator {
    private:
        Node<Element>* current;

    public:
        Iterator(Node<Element>* start) : current(start) {}

        Element operator*() const {
            return current->getValue();
        }

        Iterator& operator++() {
            if (current) {
                current = current->getNext();
            }
            return *this;
        }

        bool operator!=(const Iterator& other) const {
            return current != other.current;
        }
    };

    Iterator begin() const {
        return Iterator(first);
    }

    Iterator end() const {
        return Iterator(nullptr);
    }
};