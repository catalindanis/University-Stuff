generare(L, S, Rez) :-
	findall(Sol, generare_aux(L, S, Sol), Rez).

candidat([H | T], H, T).
candidat([_ | T], E, Rest) :-
	candidat(T, E, Rest).

generare_aux(_, 0, []) :- !.
generare_aux(L, S, [E | Rez]) :-
	candidat(L, E, Rest),
	S1 is S - E,
	S1 >= 0,
	generare_aux(Rest, S1, Rez).

