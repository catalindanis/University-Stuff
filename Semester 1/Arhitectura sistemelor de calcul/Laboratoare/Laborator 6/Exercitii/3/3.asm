bits 32 

global start

extern exit
import exit msvcrt.dll

segment data use32 class=data

a dw 1432h, 7731h, 4185h, 3124h
la equ ($-a) / 2
b1 times la db 0 ; 14, 77, 41, 31
b2 times la db 0 ; 32, 31, 85, 24

segment code use32 class=code
start:
    mov esi, a
    mov edi, b1
    mov ecx, la
    cld
    jecxz final
    repeta1:
        lodsw ; ax <- a[index]
        mov al, ah
        stosb
    loop repeta1
    
    mov esi, a
    mov edi, b2
    mov ecx, la
    repeta2:
        lodsw ; ax <- a[index]
        stosb
    loop repeta2
    
    final:
    push dword 0
    call [exit]