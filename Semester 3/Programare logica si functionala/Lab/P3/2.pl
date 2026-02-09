%descSum(N: int, L: lista)
%(i, i), (i, o)
descSum(N, C) :-
    findall(L, searchSum(N, [], 0, 2, L), C).

%searchSum(N: int, L: lista, S: int, CPN: int, Res: lista)
%(i, i, i, i, i), (i, i, i, i, o)
searchSum(N, L, N, _, L) :- !.
searchSum(N, L, S, CPN, Res) :-
    prim(CPN),
    S + CPN =< N,
    CPN1 is CPN + 1,
    S1 is S + CPN,
    searchSum(N, [CPN | L], S1, CPN1, Res).
searchSum(N, L, S, CPN, Res ) :-
    S + CPN =< N,
    CPN1 is CPN + 1,
    searchSum(N, L, S, CPN1, Res).

%prim(N: int)
%(i)
prim(N) :-
    searchDiv(N, 2).

%prim(N: int, I: int)
%(i, i)
searchDiv(N, N) :- !.
searchDiv(N, I) :-
    I < N,
    N mod I =\= 0,
    I1 is I + 1,
    searchDiv(N, I1).

%descSum(1, L).
%-
%descSum(4, L).
%-
%descSum(5, L).
%[3, 2]
%descSum(10, L).
%[5, 3, 2]
%[7, 3]
%descSum(11, L).
%[11]
