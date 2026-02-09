(defun noduri-nivel-k(arb k)
	(cond
		((null arb) nil)
		((= k 0) (list (car arb)))
		(t
			(append (noduri-nivel-k (cadr arb) (- k 1)) (noduri-nivel-k (caddr arb) (- k 1)))
		)
	)
)