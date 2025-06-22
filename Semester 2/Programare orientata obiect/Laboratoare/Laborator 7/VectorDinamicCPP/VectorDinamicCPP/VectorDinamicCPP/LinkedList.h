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
    Node(const Element value) : value(value), next(nullptr), previous(nullptr) {}
    void setValue(const Element val) {
        this->value = val;
    }
    void setNext(Node* nex) noexcept{
        this->next = nex;
    }
    void setPrevious(Node* prev) noexcept {
        this->previous = prev;
    }
    Element getValue() const noexcept {
        return this->value;
    }
    Node* getNext() const noexcept {
        return this->next;
    }
    Node* getPrevious() const noexcept {
        return this->previous;
    }
};

template<typename Element>
class LinkedList {
private:
    Node<Element>* first;
    Node<Element>* last;
    int length;

    void clear() noexcept {
        Node<Element>* current = this->first;
        while (current != nullptr) {
            Node<Element>* toDelete = current;
            current = current->getNext();
            delete toDelete;
        }
        this->first = nullptr;
        this->last = nullptr;
        this->length = 0;
    }

    void copyFrom(const LinkedList& other) {
        Node<Element>* current = other.first;
        while (current != nullptr) {
            this->add(current->getValue());
            current = current->getNext();
        }
    }

public:
    LinkedList() noexcept : first(nullptr), last(nullptr), length(0) {}

    ~LinkedList() {
        clear();
    }

    LinkedList(const LinkedList& other) : first(nullptr), last(nullptr), length(0) {
        copyFrom(other);
    }

    LinkedList& operator=(const LinkedList& other) {
        if (this != &other) {
            clear();
            copyFrom(other);
        }
        return *this;
    }

    void add(const Element value) {
        Node<Element>* element = new Node<Element>(value);
        if (!this->first) {
            this->first = element;
            this->last = element;
        } else {
            this->last->setNext(element);
            element->setPrevious(this->last);
            this->last = element;
        }
        this->length++;
    }

    void remove(const Element& value) {
        Node<Element>* current = this->first;
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

                delete current;
                this->length--;
                return; // Exit after removing the node
            } else {
                current = current->getNext();
            }
        }
    }

    bool search(const Element& value) const {
        Node<Element>* current = this->first;
        while (current != nullptr) {
            if (current->getValue() == value)
                return true;
            current = current->getNext();
        }
        return false;
    }

    void update(const Element& previousValue, const Element& newValue) {
        Node<Element>* current = this->first;
        while (current != nullptr) {
            if (current->getValue() == previousValue) {
                current->setValue(newValue);
                return;
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
        Node<Element>* current = this->first;
        while (current != nullptr) {
            list.push_back(current->getValue());
            current = current->getNext();
        }
        return list;
    }

    Element get(int position) const noexcept {
        Node<Element>* current = this->first;
        int index = 0;
        while (current != nullptr) {
            if (position == index)
                return current->getValue();
            index++;
            current = current->getNext();
        }

        return current->getValue();
    }

    unsigned int size() const noexcept {
        return this->length;
    }

    class Iterator {
    private:
        Node<Element>* current;

    public:
        Iterator(Node<Element>* start) noexcept : current(start) {}

        Element operator*() const noexcept {
            return current->getValue();
        }

        Iterator& operator++() noexcept {
            if (current) {
                current = current->getNext();
            }
            return *this;
        }

        bool operator!=(const Iterator& other) const noexcept {
            return current != other.current;
        }
    };

    Iterator begin() const noexcept {
        return Iterator(first);
    }

    Iterator end() const noexcept {
        return Iterator(nullptr);
    }
};