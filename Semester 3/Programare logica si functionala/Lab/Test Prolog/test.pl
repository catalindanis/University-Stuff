%main(L: lista, N: int, Rez: lista)
%modele de flux: (i, i, o), (i, i, i)
main(L, N, Rez) :-
	elimina_pare(L, N, 0, Rez).

%elimina_pare(L: lista, N: int, Cp: int, Rez: lista)
%Cp - pe al catelea numar par am ajuns (se reseteaza la 0 cand ajunge la N)
%modele de flux: (i, i, i, o), (i, i, i, i)
elimina_pare([], _, _, []).
elimina_pare([H | T], N, Cp, Rez) :-
	H mod 2 =:= 0,
	Cp1 is Cp + 1,
	N =:= Cp1, !,
	elimina_pare(T, N, 0, Rez).
elimina_pare([H | T], N, Cp, [H | Rez]) :-
	H mod 2 =:= 0, !,
	Cp1 is Cp + 1,
	elimina_pare(T, N, Cp1, Rez).
elimina_pare([H | T], N, Cp, [H | Rez]) :-
	elimina_pare(T, N, Cp, Rez).

%model matematic
%
%main(l1...ln, N) =
%   - elimina_pare(l1...ln, N, 0)
%
%elimina_pare(l1...ln, N, Cp) =
%   - [], n == 0
%   - elimina_pare(l2...ln, N, 0), l1 % 2 == 0 SI N == Cp+1
%   - l1 (+) elimina_pare(l2...ln, N, Cp + 1), l1 % 2 == 0 SI N != Cp+1
%   - l1 (+) elimina_pare(l2...ln, N, Cp), altfel

%caz de testare
%
%main([1, 2, 3, 4, 5, 6], 2, [1, 2, 3, 5, 6]).
