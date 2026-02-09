(defun inlocuire (arb k e)
	(cond
		((null arb) nil)
		(t
		(if (= k 0)
			(cons e (cdr arb))
			(cons (car arb) (mapcar #'(lambda (x) (inlocuire x (- k 1) e)) (cdr arb)))
		))
	)
)