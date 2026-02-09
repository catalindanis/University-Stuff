(defun inlocuire (arb nc e)
	(cons (if (oddp nc) e (car arb))
	(mapcar #'(lambda (x) (inlocuire x (+ nc 1) e)) (cdr arb)))
)

(defun main(arb e)
	(inlocuire arb 0 e)
)