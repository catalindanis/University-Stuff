(defun nivel(arb k)
	(cond
		((null arb) nil)
		((equal (car arb) k) 0)
		(t
			((lambda (x y)
					(cond
						((not (null x)) (+ x 1))
						((not (null y)) (+ y 1))
						(t nil)
					)
			) (nivel (cadr arb) k) (nivel (caddr arb) k))
		)
	)
)