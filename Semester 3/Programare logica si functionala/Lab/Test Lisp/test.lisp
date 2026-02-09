;(defun diferenta-aux (l1 l2 c))

(defun diferenta(l1 l2)
	(diferenta-aux (reverse l1) (reverse l2) 0)
)

(defun reverse (l)
	(cond
		((null l) nil)
		(t 
			(append (reverse (cdr l)) (list (car l)))
		)
	)
)