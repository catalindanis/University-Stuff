; noduri-pe-nivel(l1...ln, niv, target) =
;	nil, arb == null
;	list(l1), niv == target
;	noduri-pe-nivel(l2, niv + 1, target) (+) noduri-pe-nivel(l3, niv + 1, target), altfel

; noduri-niv-maxim(l1...ln) = 
;	noduri-niv-maxim-aux(l1...ln, 0)

; noduri-niv-maxim-aux(l1...ln, niv) = 
;	0, l1 == null 
;	noduri-pe-nivel(niv), len(noduri-pe-nivel(niv)) > noduri-niv-maxim-aux(l2...ln, niv + 1)
;	noduri-niv-maxim-aux(l2...ln, niv + 1)

(defun noduri-pe-nivel(arb niv target)
	(cond
		((null arb) nil)
		((= niv target) (list (car arb)))
		(t
			(append (noduri-pe-nivel (cadr arb) (+ niv 1) target) (noduri-pe-nivel (caddr arb) (+ niv 1) target))
		)
	)
)

(defun arb-max(arb niv)
	(cond
		((= (length (noduri-pe-nivel arb 0 niv)) 0) nil)
		((> 
			(length (noduri-pe-nivel arb 0 niv)) 
			(length (arb-max arb (+ niv 1)))
			) 
				(cons niv (list (noduri-pe-nivel arb 0 niv)))
		)
		(t
			(arb-max arb (+ niv 1))
		)
	)
)