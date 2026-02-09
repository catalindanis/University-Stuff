(defun interclasare(l1 l2)
	(cond
		((null l1) l2)
		((null l2) l1)
		(t
			(if (< (car l1) (car l2))
				(cons (car l1) (interclasare (cdr l1) l2))
				(cons (car l2) (interclasare l1 (cdr l2)))	
			) 
		)
	)
)

(defun substituie(l e l1)
	(cond
		((null l) nil)
		((listp (car l))
			(cons (substituie (car l) e l1) (substituie (cdr l) e l1))	
		)
		(t 
			(if (eq (car l) e)
				(cons l1 (substituie (cdr l) e l1))
				(cons (car l) (substituie (cdr l) e l1))
			)
		)
	)
)

(defun suma(l1 l2)
	(+ (lista-la-numar l1) (lista-la-numar l2))
)

(defun lista-la-numar (l)
  (if (null l)
      0
      (la-numar (reverse l))
  )
)

(defun la-numar (l)
  (if (null l)
      0
      (+ (* 10 (la-numar (cdr l))) (car l)) 
  )      
)


