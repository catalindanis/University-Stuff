suma(L1, L2, Rez) :-
	lista_num(L1, R1),
	lista_num(L2, R2),
	Rez1 is R1 + R2,
	num_lista(Rez1, Rez).

lista_num(L, Rez) :-
	reverse(L, L1),
	lista_num_aux(L1, Rez).

lista_num_aux([], 0).
lista_num_aux([H | T], Rez) :-
	lista_num_aux(T, Rez1),
	Rez is Rez1 * 10 + H.

num_lista(Num, Rez) :-
	num_lista_aux(Num, L),
	reverse(L, Rez).

num_lista_aux(0, [0]) :- !.
num_lista_aux(Num, [Num]) :-
	Num =< 9, !.
num_lista_aux(Num, [Cif | Rez]) :-
	Cif is Num mod 10,
	Tail is Num div 10,
	num_lista_aux(Tail, Rez).

reverse(L, Rez) :-
	reverse_aux(L, [], Rez).

reverse_aux([], Col, Col).
reverse_aux([H | T], Col, Rez) :-
	reverse_aux(T, [H | Col], Rez).

suma_pe_lista(L, Rez) :-
	suma_pe_lista_aux(L, 0, Rez).

suma_pe_lista_aux([], S, Rez) :-
	num_lista(S, Rez).
suma_pe_lista_aux([H | T], S, Rez) :-
	is_list(H), !,
	lista_num(H, Num),
	S1 is S + Num,
	suma_pe_lista_aux(T, S1, Rez).
suma_pe_lista_aux([_ | T], S, Rez) :-
	suma_pe_lista_aux(T, S, Rez).
