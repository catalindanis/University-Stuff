comb([H | _], 1, [H]).
comb([_ | T], K, Rez) :-
	comb(T, K, Rez).
comb([H | T], K, [H | Rez]) :-
	K > 1,
	K1 is K - 1,
	comb(T, K1, Rez).

inserare(E, L, [E | L]).
inserare(E, [H | T], [H | Rez]) :-
	inserare(E, T, Rez).

stergere(E, [E | T], T).
stergere(E, [H | T], [H | Rez]) :-
	stergere(E, T, Rez).
