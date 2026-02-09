(defun lista(l n)
	(cond
		((and (atom l) (= n 0)) (list l))
		(t (mapcan #'(lambda(x) (lista x (- n 1))) l))
	)
)


(defun m (L)
 (cond
 ((numberp L) L)
((atom L) most-negative-fixnum)
 (t (apply #'max
(mapcar #'m L)
 )
 )
 )
)
(defun lista (L)
 (mapcan #'(lambda (L)
((lambda (v)
(cond
((= 0 (mod v 2)) (list v))
(t nil)
)
 ) (m L)
 )
 )
 L
 )
)


(defun subm (l)
	(cond
		((null l) (list nil))
		(t
		   ((lambda (s)
			(append s 
				(mapcar #'(lambda(x) (cons (car l) x))
				s)
			)
		   )
		   (subm (cdr l))
		))	
	)
)


(defun perm (l)
  (if (null l)
      (list nil) ; baza recursiei
      (mapcan (lambda (p)
                (insert-each-pos (car l) p))
              (perm (cdr l)))))


(defun insert-each-pos (e L)
  (if (null L)
      (list (list e)) ; lista vidă → doar [e]
      (cons
       (cons e L)  ; inserăm e la început
       (mapcar (lambda (sub)
                 (cons (car L) sub)) ; adaugăm primul element la fiecare sublistă generată recursiv
               (insert-each-pos e (cdr L))))))








