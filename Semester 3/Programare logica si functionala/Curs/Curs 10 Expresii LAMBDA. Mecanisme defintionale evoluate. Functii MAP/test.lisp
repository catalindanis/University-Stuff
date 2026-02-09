(defun modif(l)
	(cond
		((numberp l) (* 2 l))
		((atom l) l)
		(t (mapcar #'modif l))
	)
)

(defun lgm(l)
	(if (atom l) 
		0
		(max (length l) (apply #'max (mapcar #'lgm l)))
	)	
)

