bits 32

global start

extern exit
import exit msvcrt.dll

segment data use32 class=data

;     1   2   3   4   5

s1 db 7, 33, 55, 19, 46
ls1 equ $-s1
s2 db 33, 21, 7, 13, 27, 19, 55, 1, 46 
ls2 equ $-s2
d times ls2 db 0

segment code use32 class=code
start:
    mov ecx, ls2
    mov edi, d
    mov esi, s2
    jecxz final
    cld
    repeta:
        push ecx
        lodsb ; al <- s2[index]
        mov bl, al
        
        mov al, 0
        mov edx, 0
        cmp edx, ls1
        jge dupa
        cauta:
            mov cl, [s1 + edx]
            cmp cl, bl
            je gasit
            inc edx
            cmp edx, ls1
        jl cauta
        jmp dupa
        gasit:
        mov al, dl
        inc al
        dupa:
        stosb
        pop ecx
    loop repeta
    final:
    push dword 0
    call [exit]