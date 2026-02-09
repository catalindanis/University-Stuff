% 2
reverse([], []):- !.
reverse([H | T], Rez) :-
	is_list(H), !,
	reverse(T, Rez2),
	reverse(H, Rez1),
	add(Rez1, Rez2, Rez).

reverse([H | T], Rez) :-
	reverse(T, Rez1),
	add(H, Rez1, Rez).

add(E, [], [E]):- !.
add(E, [H | T], [H | Rez]) :-
	add(E, T, Rez).

% 3
subsets(L, N, Rez) :-
	findall(Rez1, subs(L, N, Rez1), Rez).

subs([H | _], 1, [H]).
subs([H | T], N, [H | Rez]) :-
	N > 1,
	N1 is N - 1,
	subs(T, N1, Rez).
subs([_ | T], N, Rez) :-
	subs(T, N, Rez).

