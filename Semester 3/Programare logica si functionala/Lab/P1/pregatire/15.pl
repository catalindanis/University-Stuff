verificare_nr_pare(L) :-
	verificare_aux(L, 0).

verificare_aux([], Cnt) :-
	0 is Cnt mod 2, !.
verificare_aux([_ | T], Cnt) :-
	Cnt1 is Cnt + 1,
	verificare_aux(T, Cnt1).

eliminare_min_prim(L, Rez) :-
	gaseste_minim(L, Minim),
	elimina_prima_aparitie(L, Minim, Rez).

gaseste_minim([H], H).
gaseste_minim([H | T], H) :-
	gaseste_minim(T, M1),
	M1 > H, !.
gaseste_minim([H | T], M1) :-
	gaseste_minim(T, M1),
	M1 =< H.

elimina_prima_aparitie([], _, []) :- !.
elimina_prima_aparitie([H | T], H, T) :- !.
elimina_prima_aparitie([H | T], E, [H | Rez]) :-
	elimina_prima_aparitie(T, E, Rez).

