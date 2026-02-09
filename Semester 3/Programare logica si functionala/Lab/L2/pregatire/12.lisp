(defun preordine (arb)
	(cond
		((null arb) nil)
		((atom arb) (list arb))
		(
			(append (list (car arb)) (preordine (cadr arb)) (preordine (caddr arb)))
		)
	)
)