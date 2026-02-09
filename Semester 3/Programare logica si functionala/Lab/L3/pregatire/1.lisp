(defun adancime(l)
	(cond
		((atom l) 0)
		(t 
			(apply #'max 
				(mapcar #'(lambda (x) (+ 1 (adancime x))) l)
			)
		)
	)
)