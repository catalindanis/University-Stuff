%sumaAlternanta(L: lista, Rez: int)
%(i, i) (i, o)
sumaAlternanta([], Rez) :-
	Rez is 0.
sumaAlternanta([E], Rez) :-
	Rez is E.
sumaAlternanta([L1, L2 | L], Rez) :-
	sumaAlternanta(L, Rez1),
	Rez is Rez1 + L1 - L2.

%sumaAlternanta([], Rez) => Rez = 0
%sumaAlternanta([1], Rez) => Rez = 1
%sumaAlternanta([1, 2], Rez) => Rez = -1
%sumaAlternanta([2, 2, 1, 1], Rez) => Rez = 0

