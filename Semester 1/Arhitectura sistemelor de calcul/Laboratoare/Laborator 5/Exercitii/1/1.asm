bits 32

global start

extern exit
import exit msvcrt.dll

segment data use32 class=data

s db 1, 2, 3, 4
l equ ($-s)
d times (l-1) dw 0

segment code use32 class=code
start:
    mov ecx, l-1
    cmp ecx, 0
    jle final
    mov ebx, 0
    repeta:
        mov al, [s+ebx]
        mov dl, [s + ebx + 1]
        mul dl
        mov [d+ebx*2], ax
        inc ebx
    loop repeta
    final:
    push dword 0
    call [exit]