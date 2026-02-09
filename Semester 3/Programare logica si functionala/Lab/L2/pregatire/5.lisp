(defun adancime(arb x)
	(cond
		((null arb) nil)
		((eq (car arb) x) 0)
		((adancime-copii (cdr arb) x)
			(+ 1 (adancime-copii (cdr arb) x))
		)
		(t nil)
	)
)

(defun adancime-copii(arb x)
	(cond
		((null arb) nil)
		((adancime (car arb) x) (adancime (car arb) x))
		(t (adancime-copii (cdr arb) x))
	)
)