; diferenta(a1...an, b2...bn) =
;	inversare(diferenta-aux(inversare(a1...an), inversare(b2...bn), 0))
;
; diferenta-aux(a1...an, b2...bn, c) = 
;	nil, a1...an == null
;	(a1 - c) (+) (a2...an), b2...bn == null
;	(a1 + 10 - b1 - c) (+) diferenta-aux(a2...an, b2...bn, 1), a1 - c < b1
;	(a1 - b1 - c) (+) diferenta-aux(a2...an, b2...bn, 0), altfel
; 
; inversare(l1...ln) = 
;	nil, l1...ln == null
;	inversare(l2...ln) (+) l1, altfel
;
; sterge-zerouri(l1...ln) = 
;	(0), l1...ln == null
;	l1...ln, l1 > 0
;	sterge-zerouri(l2...ln), altfel

; (diferenta '(6 9) '(9))
; (diferenta '(6 8) '(1 9))
; (diferenta '(6 5 4) '(4 5 6))
; (diferenta '(1 2 5) '(1 6))
; (diferenta '(1 2 3) '(2 4))

; l1 - primul termen al diferentei (lista)
; l2 - al doilea termen al diferentei (lista)
(defun diferenta(l1 l2)
	(sterge-zerouri (inversare (diferenta-aux (inversare l1) (inversare l2) 0)))
)

; l1 - primul termen al diferentei (lista)
; l2 - al doilea termen al diferentei (lista)
; c - transportul matematic de la ultima operatie efectuata (int)
(defun diferenta-aux (l1 l2 c)
	(cond
		((null l1) nil)
		((null l2) (cons (- (car l1) c) (cdr l1))

		)
		(t
			(if (< (- (car l1) c) (car l2))
				(cons (- (+ 10 (car l1)) (car l2) c) (diferenta-aux (cdr l1) (cdr l2) 1))
				(cons (- (car l1) (car l2) c) (diferenta-aux (cdr l1) (cdr l2) 0))	
			)
		)
	)
)

; l - lista de inversat (lista)
(defun inversare (l)
	(cond
		((null l) nil)
		(t 
			(append (reverse (cdr l)) (list (car l)))
		)
	)
)

; l - lista din care se sterg toate zerourile de la inceput pana la gasirea primului atom numeric > 0 (lista)
(defun sterge-zerouri (l)
	(cond
		((null l) (list 0))
		(t
			(if (> (car l) 0)
				l
				(sterge-zerouri (cdr l))
			)
		)
	)
)