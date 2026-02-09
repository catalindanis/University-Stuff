pozitii_max(L, Rez) :-
	maxim(L, Max),
	gasire_pozitii(L, Max, 1, Rez).

maxim([H], H) :- !.
maxim([H | T], H) :-
	maxim(T, Max),
	Max < H, !.
maxim([_ | T], Max) :-
	maxim(T, Max).

gasire_pozitii([], _, _, []).
gasire_pozitii([E | T], E, P, [P | Rez]) :- !,
	P1 is P + 1,
	gasire_pozitii(T, E, P1, Rez).
gasire_pozitii([_ | T], E, P, Rez) :-
	P1 is P + 1,
	gasire_pozitii(T, E, P1, Rez).

inlocuire([], []) :- !.
inlocuire([H | T], [S | Rez]) :-
	is_list(H), !,
	pozitii_max(H, S),
	inlocuire(T, Rez).
inlocuire([H | T], [H | Rez]) :-
	inlocuire(T, Rez).
