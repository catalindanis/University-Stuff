(defun inordine(arb)
	(cond
		((null arb) nil)
		((atom arb) (list arb))
		(t
			(append (inordine (cadr arb)) (list (car arb)) (inordine (caddr arb)))
		)
	)
)