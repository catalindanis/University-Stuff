w(N, N).
w(J, I) :- I < 10, I1 is I + 1, w(J, I1).
r(K, J) :- w(J, K), write(J), write(" "), fail.

p(100).
p(N) :- write(N), N1 is N - 1, p(N1), nl.

