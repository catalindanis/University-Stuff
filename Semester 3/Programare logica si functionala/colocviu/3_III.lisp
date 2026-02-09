; modele matematice
; noduri-nivel-k(arb, k) =
;	[arb1], k == 0
;	U noduri-nivel-k(arbk, k-1), k = 1...n
;

; arb - arborele (lista)
; k - nivelul pentru care cautam noduri (int)
(defun noduri-nivel-k (arb k)
	(cond
		((= k 0) (list (car arb)))
		(t
			(mapcan #'(lambda (x) (noduri-nivel-k x (- k 1))) (cdr arb)) 
		)
	)
)