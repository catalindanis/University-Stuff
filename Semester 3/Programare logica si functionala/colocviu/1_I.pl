f([], []).
f([H | T], [H | Rez]) :- H > 0, f(T, Rez), !.
f([_ | T], Rez) :- f(T, Rez).
