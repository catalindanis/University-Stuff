;substituie(l1...ln, e1, e2) =
;	(l1), l1 atom si l1 != e1
;	(e2), l1 == e1
;	∪ substituie(lk, e1, e2), k = 1...n

;(substituie '(1 2 3 (1 2 4 3) ((3))) 3 5)
;(substituie '(1 2 3 (1 2 4 3) ((3))) 1 5)
;(substituie '(a (b c) d (b (a b))) 'b 'z)
;(substituie '(a (b c) d) 'q 'x)

(defun substituie(l e1 e2)
	(cond
		((equal l e1) e2)
		((atom l) l)
		(t 
		   (mapcar 
		      #'(lambda (x) 
		      	(substituie x e1 e2)
		      ) 
	       	      l
		   )
		)
	)
)