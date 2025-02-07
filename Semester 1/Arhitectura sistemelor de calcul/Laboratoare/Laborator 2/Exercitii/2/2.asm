bits 32

global start

extern exit
import exit msvcrt.dll

segment data use32 class=data

a db 5
b db 2
c db 10
d db 1

segment code use32 class=code
start:
    ;c-(a+d)+(b+d) = 7 = 07h 
    mov al, [c]
    sub al, [a]
    sub al, [d]
    add al, [b]
    add al, [d]
    ;al = 7 = 07h
    
    ;(c-a-d)+(c-b)-a = 7 = 07h
    mov al, [c]
    sub al, [a]
    sub al, [d]
    add al, [c]
    sub al, [b]
    sub al, [a]
    ;al = 7 = 07h
    
    ;(a+d+d)-c+(b+b) = 1 = 01h
    mov al, [a]
    add al, [d]
    add al, [d]
    sub al, [c]
    add al, [b]
    add al, [b]
    ;al = 1 = 01h
    
    ;a-b-d+2+c+(10-b) = 22 = 16h
    mov al, [a]
    sub al, [b]
    sub al, [d]
    add al, 2
    add al, [c]
    add al, 10
    sub al, [b]
    ;al = 22 = 16h
    
    ;(a+a)-(c+b+d) = -3 = FDh
    mov al, [a]
    add al, [a]
    sub al, [c]
    sub al, [b]
    sub al, [d]
    ;al = -3 = FDh
    
    ;(c+d+d)-(a+a+b) = 0 = 00h
    mov al, [c]
    add al, [d]
    add al, [d]
    sub al, [a]
    sub al, [a]
    sub al, [b]
    ;al = 0 = 00h
    
    ;d-(a+b)-(c+c) = -26 = E6h
    mov al, [d]
    sub al, [a]
    sub al, [b]
    sub al, [c]
    sub al, [c]
    ;al = -26 = E6h
    
    push dword 0
    call [exit]