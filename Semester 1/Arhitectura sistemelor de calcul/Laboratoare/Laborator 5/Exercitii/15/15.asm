bits 32

global start

extern exit
import exit msvcrt.dll

segment data use32 class=data

A db 2, 1, 3, 3, 4, 2, 6
lsa equ $-A

B db 4, 5, 7, 6, 2, 1
lsb equ $-B

R times lsa + lsb db 0
;R: 1, 2, 6, 7, 5, 4, 1, 3, 3

segment code use32 class=code
start:
    mov edx, 0
    mov ecx, lsb
    jecxz sari
    repeta:
        mov al, [B + ecx - 1]
        mov [R + edx], al
        inc edx
    loop repeta
    sari:
    mov ecx, 0
    cmp ecx, lsa
    jge final
    repeta2:
        mov al, [A + ecx]
        test al, 1b
        jz continua
        mov [R + edx], al
        inc edx
        continua:
        inc ecx
        cmp ecx, lsa
    jl repeta2
    final:
    push dword 0
    call [exit]