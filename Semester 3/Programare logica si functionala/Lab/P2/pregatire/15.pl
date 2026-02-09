secv_par_max([], []) :- !.
secv_par_max([H | T], Secv1) :-
	secv_par([H | T], Secv1),
	secv_par_max(T, Secv2),
	len(Secv1, Len1),
	len(Secv2, Len2),
	Len1 >= Len2, !.
secv_par_max([_ | T], Rez) :-
	secv_par_max(T, Rez).

secv_par([], []).
secv_par([H | T], [H | Rez]) :-
	0 is H mod 2, !,
	secv_par(T, Rez).
secv_par([_ | _], []).

len([], 0) :- !.
len([_ | T], Rez) :-
	len(T, Rez1),
	Rez is Rez1 + 1.

inlocuire([], []).
inlocuire([H | T], [R1 | Rez]) :-
	is_list(H), !,
	secv_par_max(H, R1),
	inlocuire(T, Rez).
inlocuire([H | T], [H | Rez]) :-
	inlocuire(T, Rez).
