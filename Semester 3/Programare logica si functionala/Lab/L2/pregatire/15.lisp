(defun postordine(arb)
	(cond
		((null arb) nil)
		(t
		;sdr
			(append 
				(if (cdr arb) (postordine (cadr arb)) nil)
       				(if (cddr arb) (postordine (caddr arb)) nil)		
			(list (car arb)))
		)
	)
)