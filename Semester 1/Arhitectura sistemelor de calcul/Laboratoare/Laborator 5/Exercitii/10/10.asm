bits 32

global start

extern exit
import exit msvcrt.dll

segment data use32 class=data

S1 db '+', '2', '2', 'b', '8', '6', 'X', '8'
ls1 equ $-S1

S2 db 'a', '4', '5'
ls2 equ $-S2

D times ls2 + ls1 / 2 db 0
;D: '5', '4', 'a', '2','b', '6', '8'

segment code use32 class=code
start:
    mov edx, 0
    mov ecx, ls2
    jecxz sari
    repeta:
        mov al, [S2 + ecx - 1]
        mov [D + edx], al
        inc edx
    loop repeta
    sari:
    mov ecx, 1
    cmp ecx, ls1
    jg final
    repeta1:
        test ecx, 1b
        jnz continua
        mov al, [S1 + ecx - 1]
        mov [D + edx], al
        inc edx
        continua:
        inc ecx
        cmp ecx, ls1
    jle repeta1
    final:
    push dword 0
    call [exit]