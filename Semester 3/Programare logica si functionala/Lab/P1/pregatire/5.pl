eliminare([], _, []).
eliminare([H | T], E, [Rez1 | Rez]) :-
	is_list(H), !,
	eliminare(H, E, Rez1),
	eliminare(T, E, Rez).
eliminare([H | T], H, Rez) :-
	!,
	eliminare(T, H, Rez).
eliminare([H | T], E, [H | Rez]) :-
	eliminare(T, E, Rez).

aparitii([], _, 0).
aparitii([H | T], H, Rez) :-
	!,
	aparitii(T, H, Rez1),
	Rez is Rez1 + 1.
aparitii([_ | T], E, Rez) :-
	aparitii(T, E, Rez).

calculare_aparitii([], []).
calculare_aparitii([H | T], [[H, NrAp] | Rez]) :-
	aparitii([H | T], H, NrAp),
	eliminare(T, H, T1),
	calculare_aparitii(T1, Rez).


