(defun inserare(l e)
	(inserare_aux l e 1)
)

(defun inserare_aux(l e p)
	(cond
		((null l) nil)
		((= (mod p 2) 0) 
			(cons (car l) (cons e (inserare_aux (cdr l) e (+ p 1))))
		)
		(t
			(cons (car l) (inserare_aux (cdr l) e (+ p 1)))
		)
	)	
)

(defun extract(l)
	(cond
		((null l) nil)
		((listp (car l)) 
			(append (extract (cdr l)) (extract (car l)))
		)
		((atom (car l)) 
			(append (extract (cdr l)) (list (car l)))
		)
	)
)

(defun cmmdc(l)
	(cond
		((null l) 0)
		((listp (car l))
			(cmmdc_aux (cmmdc (car l)) (cmmdc (cdr l)))	
		)
		(t (cmmdc_aux (car l) (cmmdc (cdr l))))
	)
)

(defun cmmdc_aux(a b)
	(cond
		((= b 0) a)
		(t (cmmdc_aux b (mod a b)))
	)
)

(defun aparitii(l a)
	(cond
		((null l) 0)
		((atom (car l))
			(if (eq a (car l))
				(+ 1 (aparitii (cdr l) a))
				(aparitii (cdr l) a)
			)
		)
		((listp (car l))
			(+ (aparitii (car l) a) (aparitii (cdr l) a))
		)
	)
)