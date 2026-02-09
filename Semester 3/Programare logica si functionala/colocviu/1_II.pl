candidat([H | _], H).
candidat([_ | T], Rez) :- candidat(T, Rez).

% generare(L, P, PC, K, Col, LCol, Rez).
generare(_, P, P, K, Col, K, Col) :- !.
generare(L, P, PC, K, Col, LCol, Rez) :-
	candidat(L, C),
	\+ candidat(Col, C),
	P >= PC * C,
	PC1 is PC * C,
	LCol1 is LCol + 1,
	generare(L, P, PC1, K, [C | Col], LCol1, Rez).

main(L, K, P, Rez) :-
	findall(Sol, generare(L, P, 1, K, [], 0, Sol), Rez).
