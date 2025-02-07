bits 32

global start

extern exit
import exit msvcrt.dll

segment data use32 class=data

a dw 5
b dw 1
c dw 7
d dw 3

segment code use32 class=code
start:
    ;(c+b+a)-(d+d) = 7 = 0007h
    mov ax, [c]
    add ax, [b]
    add ax, [a]
    sub ax, [d]
    sub ax, [d]
    ;ax = 10 = 000Ah
    
    ;(c+b+b)-(c+a+d) = -6 = FFFAh
    mov ax, [c]
    add ax, [b]
    add ax, [b]
    sub ax, [c]
    sub ax, [a]
    sub ax, [d]
    ;ax = -6 = FFFAh
    
    ;b+c+d+a-(d+c) = 6 = 0006h
    mov ax, [b]
    add ax, [c]
    add ax, [d]
    add ax, [a]
    sub ax, [d]
    sub ax, [c]
    ;ax = 6 = 0006h
    
    ;(a-b+c)-(d+d) = 5 = 0005h
    mov ax, [a]
    sub ax, [b]
    add ax, [c]
    sub ax, [d]
    sub ax, [d]
    ;ax = 5 = 0005h
    
    ;(a+b-c)-d = -4 = FFFCh
    mov ax, [a]
    add ax, [b]
    sub ax, [c]
    sub ax, [d]
    ;ax = -4 = FFFCh
    
    ;a-b+(c-d+a) = 13 = 000Dh
    mov ax, [a]
    sub ax, [b]
    add ax, [c]
    sub ax, [d]
    add ax, [a]
    ;ax = 13 = 000Dh
    
    
    
    push dword 0
    call [exit]