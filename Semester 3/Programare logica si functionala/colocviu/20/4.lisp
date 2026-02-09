(defun eliminate(l e)
	(cond 
		((null l) NIL)
		((listp (car l)) (cons (eliminate (car l) e) (eliminate (cdr l) e)))
		((equal (car l) e) (eliminate (cdr l) e))
		(t (cons (car l) (eliminate (cdr l) e)))
	)
)


