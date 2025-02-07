bits 32

extern exit
import exit msvcrt.dll

global start

segment data use32 class=data

;Se dau 2 siruri de octeti S1 si S2 de aceeasi lungime. 
;Sa se construiasca sirul D astfel incat fiecare element din D 
;sa reprezinte diferenta dintre elementele de pe pozitiile 
;corespunzatoare din S1 si S2.
s1 db 1, 3, 6, 2, 3, 2
s2 db 6, 3, 8, 1, 2, 5
ls equ $ - s2
srez times ls db 0
;pentru ex : srez = -5, 0, -2, 1, 1, -3

segment code use32 class=code
start:
    mov ecx, ls ; ecx <- lungimea sirurilor
    mov edi, 0  ; edi <- 0 (index-ul pentru parcurgerea sirurilor)
    jecxz final ; daca lungimea sirului initial este 0, nu executa niciun pas
    repeta:
        mov al, [s1+edi]   ; al <- s1[edi]
        mov dl, [s2+edi]   ; dl <- s2[edi]
        sub al, dl         ; al <- al - dl
        mov [srez+edi], al ; srez[edi] <- al
        inc edi            ; edi++
    loop repeta
    final:
    push dword 0
    call [exit]