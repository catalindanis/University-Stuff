(defun produs(l)
	(cond
		((null l) 1)
		((numberp (car l))
			(* (car l) (produs (cdr l)))
		)
		(t
			(produs (cdr l))
		)
	)
)

(defun perechi(l)
	(cond
		((null l) nil)
		(t 
			(append (per (car l) (cdr l)) 
				(perechi (cdr l)))
		)
	)
)

(defun per(e l)
	(if (null l)
		nil
		(cons (cons e (list (car l))) (per e (cdr l)))		
	)
)

(defun calculare(l)
	(cond
		((null l) nil)
		(
			(cons
				(cons (car l) (list (aparitii (car l) l)))
				(calculare (eliminare l (car l)))	
			)
		)
	)
)

(defun eliminare(l e)
	(cond
		((null l) nil)
		((eq (car l) e)
			(eliminare (cdr l) e)		
		)
		(t
			(cons (car l) (eliminare (cdr l) e))
		)
	)
)

(defun aparitii(e l)
	(cond
		((null l) 0)
		((eq (car l) e)
			(+ 1 (aparitii e (cdr l)))
		)
		(t 
			(aparitii e (cdr l))
		)
	)
)

(defun calculare(l)
	(calculare-aux (inverseaza l) nil)
)

(defun calculare-aux(l res)
	(cond
		((null l) (car res))
		((numberp (car l)) (calculare-aux (cdr l) (cons (car l) res)))
		(t
			(calculare-aux (cdr l) (cons (funcall (car l) (car res) (cadr res)) (cddr res)))
		)
	)
)

(defun inverseaza(l)
	(cond
		((null l) nil)
		(t
			(append (inverseaza (cdr l)) (list (car l)))
		)
	)
)


