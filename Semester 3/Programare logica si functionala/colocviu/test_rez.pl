main(N, K, Rez) :-
	findall(Sol, comb_aux(N, K, 1, -1, Sol), Rez).

comb_aux(_, 0, _, _, []) :- !.
comb_aux(N, K, C, U, [C | Rez]) :-
	C =< N,
	U is -1,
	C1 is C + 1,
	K1 is K - 1,
	comb_aux(N, K1, C1, C, Rez).
comb_aux(N, K, C, U, [C | Rez]) :-
	C =< N,
	U \= -1,
	D is C - U,
	0 is D mod 2,
	K1 is K - 1,
	C1 is C	+ 1,
	comb_aux(N, K1, C1, C, Rez).
comb_aux(N, K, C, U, Rez) :-
	C =< N,
	C1 is C + 1,
	comb_aux(N, K, C1, U, Rez).
