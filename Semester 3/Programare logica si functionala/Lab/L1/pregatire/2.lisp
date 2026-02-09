(defun element-n(l n)
	(cond
		((null l) nil)
		((= n 1) (car l))
		(t (element-n (cdr l) (- n 1)))
	)
)

(defun apartine(l a)
	(cond
		((null l) nil)
		((listp (car l)) 
			(or (apartine (car l) a) (apartine (cdr l) a))
		)
		(t
			(or (if (equal (car l) a)
				t
				nil
			) (apartine (cdr l) a))
		)
	)
)

(defun subliste(l)
	(cond
		((or (null l) (atom l)) nil)
		(t
			(cons l (mapcan #'subliste l))
		)
	)
)

(defun multime(l)
	(cond
		((null l) nil)
		(t
			(if (= (aparitii l (car l)) 1)
				(cons (car l) (multime (cdr l)))
				(multime (cdr l))
			)
		)
	)	
)

(defun aparitii(l e)
	(cond
		((null l) 0)
		(t 
			(if (equal (car l) e)
				(+ 1 (aparitii (cdr l) e))
				(aparitii (cdr l) e)
			)
		)
	)
)

