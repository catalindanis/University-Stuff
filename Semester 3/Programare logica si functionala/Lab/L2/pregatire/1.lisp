(defun cale(arb x)
	(cond 
		((null arb) nil)
		((eq (car arb) x) (list x))
		((cale-copii (cdr arb) x)
			(cons (car arb) (cale-copii (cdr arb) x))
		)
		(t nil)
	)
)

(defun cale-copii(lista x)
	(cond
		((null lista) nil)
		((cale (car lista) x))
		(t  (cale-copii (cdr lista) x))
	)
)	