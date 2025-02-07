bits 32

global start

extern exit
import exit msvcrt.dll

segment data use32 class=data

s dw "abcdefgh"
ls equ ($-s)/2
b1 resb ls
b2 resb ls

segment code use32 class=code
start:
    
    mov ecx, ls
    jecxz final
    
    mov esi, s
    mov ebx, b1 ; sir octeti superiori
    mov edx, b2 ; sir octeti inferiori
    repeta:
        lodsw
        
        mov edi, edx
        stosb
        mov edx, edi
        
        mov edi, ebx
        mov al, ah
        stosb
        mov ebx, edi
    loop repeta
    
    final:
    push dword 0
    call [exit]
    
    
    
    