
%N - lungimea unei subliste (int)
%A, B - capetele intervalelor (int)
%Rez - lista de rezultate
%model de flux: (i, i, i, o)
main(N, A, B, Rez) :-
	findall(Sol, generare(N, A, B, 0, Sol), Rez).

%N - nr de elemente ramase de adaugat in rezultat (int)
%A - elementul curent din interval (int)
%B - capatul stang al intervalului (int)
%S - suma curenta (int)
%Rez - lista in care punem pe rand solutiile (list)
%cazul de baza - orice suma am avea de calculat, orice interval,
%daca nu mai avem elemente de pus, returnam lista vida
generare(0, _, _, _, []) :- !.

%mai avem de pus un singur element, si ne asiguram
%ca paritatea acestuia este egala cu paritatea sumei
%curente (par + par = par, impar + impar = par)
generare(1, A, B, S, [A | Rez]) :-
	A =< B,
	A mod 2 =:= S mod 2,
	A1 is A + 1,
	S1 is S + A,
	generare(0, A1, B, S1, Rez).
%mai avem de pus mai mult de 1 element, adaugam elementul curent
generare(N, A, B, S, [A | Rez]) :-
	A =< B,
	N > 1,
	A1 is A + 1,
	S1 is S + A,
	N1 is N - 1,
	generare(N1, A1, B, S1, Rez).
%mai avem de pus mai mult de 1 element, alegem sa nu adaugam elementul cu
generare(N, A, B, S, Rez) :-
	A =< B,
	A1 is A + 1,
	generare(N, A1, B, S, Rez).


%modele matematice
%main(N, A, B) =
%     U generare(N, A, B, 0)
%
%
%generare(N, A, B, S) =
%     [], N == 0
%     1. A (+) generare(N-1, A+1, B, S + A), N == 1 SI A <= B SI A mod2
%     == S mod 2
%     2. A (+) generare(N-1, A+1, B, S + A), N > 1 SI A <= B
%     3. generare(N-1, A+1, B, S), A <= B

