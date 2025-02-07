bits 32

global start

extern exit
import exit msvcrt.dll

segment data use32 class=data

s1 db "abcdefg"
s2 db "hijklmg"
ls equ ($ - $$) / 2
p resd 1

segment code use32 class=code
start:
    mov ecx, ls
    jecxz final
    
    repeta:
        mov eax, s1
        add eax, [p]
        
        mov ebx, s2
        add ebx, [p]
        
        mov al, [eax]
        mov bl, [ebx]
        cmp al, bl
        jz final
        
        inc dword[p]
    loop repeta
    
    final:
    push dword 0
    call [exit]