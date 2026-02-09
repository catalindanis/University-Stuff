(defun parcurg_st (arb nv nm)
  (cond
    ((null arb) nil)
    ((= nv (+ 1 nm)) nil)
    (t (cons (car arb)
             (cons (cadr arb)
                   (parcurg_st (cddr arb)
                               (+ nv 1)
                               (+ nm (cadr arb))))))))
(defun parcurg_d (arb nv nm)
  (cond
    ((null arb) nil)
    ((= nv (+ 1 nm)) arb)
    (t (parcurg_d (cddr arb)
                  (+ nv 1)
                  (+ nm (cadr arb))))))

(defun stang (arb)
  (parcurg_st (cddr arb) 0 0))

(defun drept (arb)
  (parcurg_d (cddr arb) 0 0))

(defun afisare (arb k)
  (cond
    ((null arb) NIL)
    ((AND (= k 0) (> 9 (car arb))) (list (car arb)))
    ((= k 0) NIL)	
    (t (append (afisare (stang arb) (- k 1))
       (afisare (drept arb) (- k 1)))))
)