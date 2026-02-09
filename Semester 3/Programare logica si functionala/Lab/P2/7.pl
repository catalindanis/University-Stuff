%mainA(L: Lista, Cifra: int, Rez: Lista)
%(i, i, i) (i, i, o)
mainA(L, Cifra, Rez) :-
	reverse(L, L1),
	produs(L1, Cifra, 0, Rez1),
	reverse(Rez1, Rez).

%produs(L: Lista, Cifra: int, Carry: int, Rez: Lista)
%(i, i, i, i) (i, i, i, o)
produs([], _, 0, []) :- !.
produs([], _, Carry, [Carry]) :- !.
produs([H | T], Cifra, Carry, [R | Rez]) :-
	P is H * Cifra + Carry,
	R is P mod 10,
	Carry1 is P // 10,
	produs(T, Cifra, Carry1, Rez).

%reverse(L: Lista, Rez: Lista)
%(i, i) (i, o)
reverse(L, Rez) :-
	reverse_aux([], L, Rez).

%reverse_aux(Col: Lista, L: Lista, Rez: Lista)
%(i, i, i) (i, i, o)
reverse_aux(Col, [], Col) :-
	!.
reverse_aux(Col, [H | T], Rez) :-
	reverse_aux([H | Col], T, Rez).

%mainB(L: Lista, Rez: Lista)
%(i, i) (i, o)
mainB(L, Rez) :-
	substituie(L, 1, Rez).

%substituie(L: Lista, Indice: int, Rez: Lista)
%(i, i, i) (i, i, o)
substituie([], _, []) :- !.
substituie([H | T], Indice,[H1 | Rez]) :-
	is_list(H),
	R is Indice mod 2,
	R =:= 0, !,
	mainA(H, Indice, H1),
	Indice1 is Indice + 1,
	substituie(T, Indice1, Rez).
substituie([H | T], Indice,[H | Rez]) :-
	is_list(H),
	Indice1 is Indice + 1,
	substituie(T, Indice1, Rez).
substituie([H | T], Indice, [H | Rez]) :-
	\+ is_list(H),
	substituie(T, Indice, Rez).

