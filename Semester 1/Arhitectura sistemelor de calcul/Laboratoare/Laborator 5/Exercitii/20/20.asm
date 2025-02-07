bits 32

global start

extern exit
import exit msvcrt.dll

segment data use32 class=data

s db 1, 4, 2, 4, 8, 2, 1, 1
ls equ $-s
d times ls db 0
;d = 1, 4, 2, 8

segment code use32 class=code
start:
    mov ecx, 0
    cmp ecx, ls
    jge final
    mov edx, 0
    repeta:
       mov al, [s + ecx]
       push ecx
       mov ecx, edx
       jecxz adauga
       cauta:
        mov bl, [d + ecx - 1]
        cmp al, bl
        je continua
       loop cauta
       adauga:
       mov [d + edx], al
       inc edx
       continua:
       pop ecx
       inc ecx
       cmp ecx, ls
    jl repeta
    final:
    push dword 0
    call [exit]