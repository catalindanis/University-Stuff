(defun nivele (arb)
	(cond
		((null arb) 0)
		(t
			( + 1 (max (nivele (cadr arb)) (nivele (caddr arb))))
		)
	)
)