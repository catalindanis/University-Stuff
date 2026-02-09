inserare([], E, 1, [E]).
inserare([H | T], E, 1, [E, H | T]) :- !.
inserare([H | T], E, P, [H | Rez]) :-
	P1 is P - 1,
	inserare(T, E, P1, Rez).

cmmdc_lista([], 0) :- !.
cmmdc_lista([H | T], Rez) :-
	is_list(H), !,
	cmmdc_lista(H, Rez1),
	cmmdc_lista(T, Rez2),
	cmmdc(Rez1, Rez2, Rez).
cmmdc_lista([H | T], Rez) :-
	cmmdc_lista(T, Rez1),
	cmmdc(H, Rez1, Rez).


cmmdc(A, 0, A) :- !.
cmmdc(A, B, Rez) :-
	B1 is A mod B,
	cmmdc(B, B1, Rez).
