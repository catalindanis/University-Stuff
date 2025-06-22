#include "UndoAction.h"

UndoAdd::UndoAdd(AbstractRepository& repository, Book element) : repository(repository), element(element) {}

UndoDelete::UndoDelete(AbstractRepository& repository, int index, Book element) : repository(repository), index(index), element(element) {}

UndoUpdate::UndoUpdate(AbstractRepository& repository, int index, Book element) : repository(repository), index(index), element(element) {}

void UndoAdd::doUndo() {
	this->repository.removeBook(this->element);
}

void UndoDelete::doUndo() {
	this->repository.addBook(this->index, this->element);
}

void UndoUpdate::doUndo() {
	this->repository.updateBook(this->index, this->element);
}

