#ifndef UNDO_H
#define UNDO_H
#include "../Repository/repo.h"


class UndoAction {
public:
	virtual void doUndo() = 0;
	virtual ~UndoAction() = default;
};

class UndoAdd final : public UndoAction {
private:
	/// reference to the repo
	Repository* repo;
	Product product;
public:
	UndoAdd(Repository* repo, const Product& product)
		: repo(repo), product(product) {}

	void doUndo() override {
		repo->remove(product);
	}
};

class UndoRemove final : public UndoAction {
private:
	/// reference to the repo
	Repository* repo;
	Product product;
public:
	UndoRemove(Repository* repo, const Product& product)
		: repo(repo), product(product) {}

	void doUndo() override {
		repo->add(product);
	}
};

class UndoChange final : public UndoAction {
private:
	Repository* repo;
	Product product;
public:
	UndoChange(Repository* repo, const Product& product)
		: repo(repo), product(product) {}

	void doUndo() override {
		for (auto& p : repo->getAllProducts()) {
			if (p.getID() == product.getID())
				p = product;
		}
	}
};


#endif //UNDO_H
