bits 32

global start

extern exit
import exit msvcrt.dll

segment data use32 class=data

s1 db 1, 3, 6, 2, 3, 2
s2 db 6, 3, 8, 1, 2, 5
ls equ $-s2
d times ls db 0
;D = 7, 6, 14, 3, 5, 7

segment code use32 class=code
start:
    mov ecx, ls
    jecxz final
    repeta
        mov al, [s1 + ecx - 1]
        mov bl, [s2 + ecx - 1]
        add al, bl
        mov [d + ecx - 1], al
    loop repeta
    final:
    push dword 0
    call [exit]