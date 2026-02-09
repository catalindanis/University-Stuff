(defun cale(arb x)
	(cond
		((null arb) nil)
		((equal (car arb) x) (list (car arb)))
		((cale (cadr arb) x) (cons (car arb) (cale (cadr arb) x)))
		((cale (caddr arb) x) (cons (car arb) (cale (caddr arb) x)))
		(t nil)
	)
)