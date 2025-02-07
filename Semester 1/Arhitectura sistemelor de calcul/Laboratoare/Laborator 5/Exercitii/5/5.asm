bits 32

global start

extern exit
import exit msvcrt.dll

segment data use32 class=data

s db 'a', 'A', 'b', 'B', '2', '%', 'x'
l equ $-s
d times l db 0

segment code use32 class=code
start:
    mov ecx, l
    jecxz final
    mov ebx, 0
    mov edx, 0
    repeta:
        mov al, [s+ebx]
        cmp al, 'a'
        jl continua
        
        cmp al, 'z'
        jg continua
        
        mov [d+edx], al
        inc edx
        
        continua:
        inc ebx
    loop repeta
    final:
    push dword 0
    call [exit]