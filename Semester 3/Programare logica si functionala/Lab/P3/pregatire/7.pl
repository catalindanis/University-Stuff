aranjamente(L, N, Rez) :-
	findall(S, aranjamente-aux(L, N, S), Rez).

aranjamente-aux([H | _], 1, [H]).
aranjamente-aux([H | T], N, Sol1) :-
	N1 is N - 1,
	aranjamente-aux(T, N1, Rez),
	inserare-fiecare-poz(Rez, H, Sol1).
aranjamente-aux([_ | T], N, Rez) :-
	aranjamente-aux(T, N, Rez).

inserare-fiecare-poz([], E, [E]).
inserare-fiecare-poz([H | T], E, [E, H | T]).
inserare-fiecare-poz([H | T], E, [H | Rez]) :-
	inserare-fiecare-poz(T, E, Rez).
