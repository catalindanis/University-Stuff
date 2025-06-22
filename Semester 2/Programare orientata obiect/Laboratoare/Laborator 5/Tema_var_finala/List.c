#include "List.h"
#include <stdlib.h>


List* createEmptyList() {
	List* list = malloc(sizeof(List));
	if (list) {
		list->size = 0;
		list->capacity = 1;
		list->elements = malloc(list->capacity * sizeof(void*));
	}
	return list;
}

void resizeList(List* list) {
	list->capacity *= 2;
	void* temp = realloc(list->elements, list->capacity * sizeof(void*));
	if (temp != NULL)
		list->elements = temp;
}

void addElement(List* list, void* element) {
	if (list->capacity <= list->size)
		resizeList(list);
	list->elements[list->size] = element;
	list->size++;
}

void removeElement(List* list, int index, DestroyFct destroyElement) {
	destroyElement(list->elements[index]);
	for (int i = index; i < list->size - 1; i++)
		list->elements[i] = list->elements[i + 1];
	list->size--;
}

void updateElement(List* list, int index, void* newElement, DestroyFct destroyElement) {
	destroyElement(list->elements[index]);
	list->elements[index] = newElement;
}

void* getElement(List* list, int index) {
	return list->elements[index];
}

List* copyList(List* l, CopyElement copyElement) {
	List* copy = createEmptyList();
	for (int i = 0; i < l->size; i++)
		addElement(copy, copyElement(l->elements[i]));
	return copy;
}

void destroyList(List* list, DestroyFct destroyElement) {
	for (int i = 0; i < list->size; i++)
		destroyElement(list->elements[i]);
	free(list->elements);
	free(list);
}

void destroyDoubleList(List* list, DestroyFct destroyElement) {
	for (int i = 0; i < list->size; i++)
		destroyList(list->elements[i], destroyElement);
	free(list->elements);
	free(list);
}

