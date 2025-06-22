#pragma once
#include "Service.h"

class UndoAction {
public:
	virtual ~UndoAction() = default;
	virtual void doUndo() = 0;
};

class UndoAdd : public UndoAction {
private:
	AbstractRepository& repository;
	vector<vector<Book>> history;
	Book element;
public:
	UndoAdd(AbstractRepository& repository, Book element);
	void doUndo() override;
};

class UndoDelete : public UndoAction {
private:
	AbstractRepository& repository;
	vector<vector<Book>> history;
	int index;
	Book element;
public:
	UndoDelete(AbstractRepository& repository, int index, Book element);
	void doUndo() override;
};

class UndoUpdate : public UndoAction {
private:
	AbstractRepository& repository;
	vector<vector<Book>> history;
	int index;
	Book element;
public:
	UndoUpdate(AbstractRepository& repository, int index, Book element);
	void doUndo() override;
};