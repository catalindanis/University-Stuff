(defun produs(a b)
	(cond
		((or (null a) (null b)) 0)
		(t
			(+ (* (car a) (car b)) (produs (cdr a) (cdr b)))
		)
	)
)

(defun adancime (l)
  (cond
    ((atom l) 0)
    ((null l) 1)
    (t
     (+ 1 (apply #'max (mapcar #'adancime l))))
  )
)

(defun sortare(l)
	(sortare-aux (multime l))
)

(defun sortare-aux(l)
	(cond
		((null l) nil)
		(t
			(inserare (sortare-aux (cdr l)) (car l))
		)
	)
)

(defun inserare(l e)
	(cond
		((null l) (list e))
		((< e (car l)) (cons e l))
		(t
			(cons (car l) (inserare (cdr l) e))
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

(defun intersectie(a b)
	(cond
		((null a) b)
		(t
			(cons (car a) (intersectie (cdr a) (sterge b (car a))))
		)
	)
)

(defun sterge(l e)
	(cond
		((null l) l)
		(t
			(if (= (car l) e)
				(sterge (cdr l) e)
				(cons (car l) (sterge (cdr l) e))
			)
		)
	)
)