(defun apare(l e)
	(cond
		((atom l) (equal l e))
		(t
			(exists (mapcar #'(lambda (x) (apare x e)) l))
		)
	)
)

(defun exists(l)
	(cond
		((null l) nil)
		((car l) t)
		(t (exists (cdr l)))
	)
)