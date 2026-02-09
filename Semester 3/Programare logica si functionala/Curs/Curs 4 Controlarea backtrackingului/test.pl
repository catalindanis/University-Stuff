concatenare([], L2, L2).
concatenare([H | T], L2, [H | Rez]) :-
	concatenare(T, L2, Rez).

subm(L, Rez) :-
	findall(X, submultime(L, X), Rez).

submultime([], []).
submultime([_ | T], Rez) :-
	submultime(T, Rez).
submultime([H | T], [H | Rez]) :-
	submultime(T, Rez).

g([H|_], E, [E,H]).
g([_|T], E, P):-
	g(T, E, P).

f([H|T],P):-
	g(T, H, P).
f([_|T], P):-
	f(T, P).
