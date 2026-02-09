(defun reuniune(m1 m2)
	(interclasare (sortare m1) (sortare m2))
)

(defun interclasare(l1 l2)
	(cond
		((null l1) l2)
		((null l2) l1)
		((= (car l1) (car l2)) (interclasare (cdr l1) l2))
		((< (car l1) (car l2)) (cons (car l1) (interclasare (cdr l1) l2)))
		(t (cons (car l2) (interclasare l1 (cdr l2))))
	)
)

(defun sortare(l)
	(cond
		((null l) nil)
		((insert (sortare (cdr l)) (car l)))
	)	
)

(defun insert(l e)
	(cond
		((null l) (list e))
		((<= e (car l))
			(cons e l)
		)
		(t
			(cons (car l) (insert (cdr l) e))
		)
	)
)


(defun pozitii_min(l)
	(pozitii l (min_lista l) 1)
)

(defun pozitii(l e pc)
	(cond
		((null l) nil)
		((= e (car l)) (cons pc (pozitii (cdr l) e (+ pc 1))))
		(t (pozitii (cdr l) e (+ pc 1)))
	)
)

(defun min_lista(l)
	(if (null l)
		most-positive-fixnum
		(min (min_lista (cdr l)) (car l))	
	)
)