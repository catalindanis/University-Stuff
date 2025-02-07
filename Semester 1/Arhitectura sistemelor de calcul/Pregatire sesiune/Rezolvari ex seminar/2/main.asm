bits 32 

global start

extern exit
import exit msvcrt.dll

segment data use32 class=data

s db "catalin este frumos"
ls equ $ - s
d times ls db 0

segment code use32 class=code
start:
    mov ecx, ls
    mov esi, s
    mov edi, d
    
    jecxz final
    
    repeta:
        lodsb
        sub al, 'a'-'A'
        stosb
    loop repeta
    
    final:
    push dword 0
    call [exit]