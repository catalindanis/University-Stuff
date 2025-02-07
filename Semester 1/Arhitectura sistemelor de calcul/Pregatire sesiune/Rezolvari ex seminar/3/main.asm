bits 32

global start

extern exit
import exit msvcrt.dll

segment data use32 class=data

s db 5, 25, 55, 127
ls equ $-s
d resb ls

segment code use32 class=code
start:
    mov ecx, ls
    jecxz final
    
    mov esi, s
    mov edi, d
    repeta:
        lodsb
        mov bl, 0
        push ecx
        
        mov ecx, 8
        numara:
            test al, 1
            jz sari
            inc bl
            sari:
            shr al, 1
        loop numara
        
        pop ecx
        mov al, bl
        stosb
    loop repeta
    
    final:
    push dword 0
    call [exit]