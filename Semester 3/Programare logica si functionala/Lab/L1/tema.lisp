#|
11 a

main1(l1...ln) =
	- 1, n == 0
	- cmmmc(main1(l1), main1(l2...ln)), l1 e lista
	- cmmmc(l1, main1(l2...ln)), l1 e numar
	- cmmmc(1, main1(l2...ln)), altfel

cmmmc(a, b) = 
	- a * b / cmmdc(a, b)

cmmdc(a, b) = 
	- a, a = b
	- cmmdc(a - b, b), a > b
	- cmmdc(a, b - a)

(main1 '(1 2 3 4))
(main1 '(1 2 3 (4 5 a)))
(main1 '(1 2 3 (4 5 a) b 3 (d) ((5))))
|#
(defun main1(l)
	(cond 
	    ((null l) 1)
		((listp (car l)) 
			(cmmmc (main1 (car l)) (main1 (cdr l)))
		)
		((numberp (car l)) (cmmmc (car l) (main1 (cdr l))))
		(t (cmmmc 1 (main1 (cdr l))))
	)
)

(defun cmmmc(a b)
	(/ (* a b) (cmmdc a b))
)

(defun cmmdc(a b)
	(cond 
		((= a b) a)
		((> a b) (cmmdc (- a b) b))
		(t (cmmdc a (- b a)))
	)
)

#|
11 b

main2(l1...ln) = 
	- munte(l, 0), l1 < l2
	- false

munte(l1...ln, f) = 
	- true, n <= 1 si f == 1
	- false, n <= 1
	- munte(l2...ln, 0), l1 < l2 si f == 0
	- munte(l2...ln, 1), l1 > l2 si f == 0
	- munte(l2...ln, 1), l1 > l2 si f == 1
	- false

(main2 '(3 2 1))
(main2 '(1 2 3))
(main2 '(1 2 3 2 1))
|#

(defun main2(l)
	(cond 
		((< (car l) (car (cdr l)))
			(munte l 0)
		)
	)
)

(defun munte(l f)
	(cond
		((AND (<= (length l) 1) (= f 1))
			t
		)
		((<= (length l) 1)
			NIL
		)
		((AND (< (car l) (car (cdr l))) (= f 0)) 
			(munte (cdr l) 0)
		)
		((AND (> (car l) (car (cdr l))) (= f 0)) 
			(munte (cdr l) 1)
		)
		((AND (> (car l) (car (cdr l))) (= f 1)) 
			(munte (cdr l) 1)
		)
	)
)

#|
11 c

main3(l1...ln) = 
	- eliminareElement(l1...ln, maximLista(l1...ln))

eliminareElement(l1...ln, m) =
	- [l1], n == 1 si l1 e numar
	- [eliminareElement(l1, m)], n == 1 si l1 lista
	- eliminareElement(l2...ln, m), l1 e numar si l1 == m
	- l1 (+) eliminareElement(l2...ln, m), l1 e numar si l1 != m
	- eliminareElement(l1, m) (+) eliminareElement(l2...ln, m), altfel

maximLista(l1...ln) = 
	- l1, n == 1
	- maxim(l1, maximLista(l2...ln)), altfel

maxim(a, b) = 
	- a, a >= b
	- b, altfel

(main3 '(4 5 3 1 10 (5 4 3 1)))
(main3 '(1 (2 4) 3 ((4) 2)))
(main3 '(4 ((10)) 3 1 ((10 3) 2) (5 4 3 1)))
|#

(defun main3 (l)
	(eliminareElement l (maximLista l))
)

(defun eliminareElement(l m)
	(cond
		((AND (= (length l) 1) (numberp (car l)))
			(cond
				((= (car l) m)
					NIL
				)
				(t 
					(list (car l))
				)
			)
		)
		((AND (= (length l) 1) (atom (car l))) 
			(list (car l))
		)
		((AND (= (length l) 1) (listp (car l))) 
			(cond
				((NOT (NULL (eliminareElement (car l) m)))
					(list (eliminareElement (car l) m) ))
			)
		)
		((numberp (car l))
			(cond
				((= (car l) m)
					(eliminareElement (cdr l) m)
				)
				(t 
					(append (list (car l)) (eliminareElement (cdr l) m))
				)
			)
		)
		(
			(cond
				((NULL (eliminareElement (car l) m))
					(eliminareElement (cdr l) m)
				)
				(t 
					(cons (eliminareElement (car l) m) (eliminareElement (cdr l) m))
				)
			)
		)
	)
)

(defun maximLista (l)
	(cond
		((AND (= (length l) 1) (numberp (car l))) 
			(car l)
		)
		((= (length l) 1)
			(maximLista (car l))
		)
		((numberp (car l))
			(maxim (car l) (maximLista (cdr l)))
		)
		(
			(maxim (maximLista (car l)) (maximLista (cdr l)))
		)
	)
)

(defun maxim (a b)
	(cond 
		((<= a b) b)
		(t a)
	)
)

#|
11 d

main4(l1...ln) = 
	- produsLista(l1..ln)

produsLista(l1...ln) = 
	- 1, l e vida
	- l1 * produsLista(l2...ln), l1 e numar si l1 % 2 == 0
	- 1 * produsLista(l2...ln), l1 e numar si l1 % 2 != 0
	- produsLista(l1) * produsLista(l2...ln), l1 e lista
	- produsLista(l2...ln), altfel

(main4 '(1 2 3 (4)))
(main4 '((1 2) 3 (4)))
(main4 '((1 2) a (3 b) (4)))
|#

(defun main4 (l)
	(produsLista l)
)

(defun produsLista (l)
  (cond
    ((null l) 
		1
	)

    ((numberp (car l))
	 (cond 
		((= (mod (car l) 2) 0)
			(* (car l) (produsLista (cdr l)))
		)
		(t
			(* 1 (produsLista (cdr l)))
		)
	 )
     
	)

    ((listp (car l))
     (* (produsLista (car l))
        (produsLista (cdr l)))
	)

    (t 
	 (produsLista (cdr l))
	)
  )
)