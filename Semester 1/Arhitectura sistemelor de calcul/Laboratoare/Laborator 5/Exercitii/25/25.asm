bits 32

global start

extern exit
import exit msvcrt.dll

segment data use32 class=data

S1 db '+', '4', '2', 'a', '8', '4', 'X', '5'
ls1 equ $-S1
S2 db 'a', '4', '5'
ls2 equ $-S2
D times ls1 db 0
;D = '+', '2', '8', 'X'

segment code use32 class=code
start:
    mov ecx, 0
    cmp ecx, ls1
    jge final
    mov edx, 0
    repeta:
        mov al, [S1+ecx]
        push ecx
        mov ecx, ls2
        jecxz adauga
        cauta:
            mov bl, [S2 + ecx - 1]
            cmp al, bl
            je sari
        loop cauta
        adauga:
        mov [D + edx], al
        inc edx
        sari:
        pop ecx
        inc ecx
        cmp ecx, ls1
    jl repeta
    final:
    push dword 0
    call [exit]