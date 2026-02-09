(defun postordine (arb)
	(cond
		((null arb) nil)
		((atom arb) (list arb))
		(t
			(append (postordine (cadr arb)) (postordine (caddr arb)) (list (car arb)))
		)
	)
)