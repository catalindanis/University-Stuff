paranteze(N, Rez) :-
	findall(S, paranteze_aux(N, 0, 0, S), Rez).

paranteze_aux(N, Pd, Pi, []) :-
	N =:= Pd + Pi, !.
paranteze_aux(N, Pd, Pi, ['(' | Rez]) :-
	N - (Pi + Pd) > Pd - Pi,
	Pd1 is Pd + 1,
	paranteze_aux(N, Pd1, Pi, Rez).
paranteze_aux(N, Pd, Pi, [')' | Rez]) :-
	Pd > Pi,
	Pi1 is Pi + 1,
	paranteze_aux(N, Pd, Pi1, Rez).
