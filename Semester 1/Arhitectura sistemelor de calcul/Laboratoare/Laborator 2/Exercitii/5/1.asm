bits 32

global start

extern exit
import exit msvcrt.dll

segment data use32 class=data

a db 5
b db 3
c db 2
d db 6

e dw 13
f dw 5
g dw 3
h dw 4

segment code use32 class=code
start:
    ;a,b,c,d-byte, e,f,g,h-word
    ;((a-b)*4)/c = 4
    ;mov al, [a]
    ;sub al, [b]
    ;mov ah, 4
    ;mul ah
    ;mov bl, [c]
    ;div bl
    ;al = 4 = 04h
    
    ;a,b,c,d-byte, e,f,g,h-word
    ;a*(b+c)+34 = 59 = 003Bh
    ;mov al, [b]
    ;add al, [c]
    ;mov ah, [a]
    ;mul ah
    ;add ax, 34
    ;ax = 59 = 003Bh
    
    ;a,b,c,d-byte, e,f,g,h-word
    ;a*d+b*c = 26 = 001Ah
    ;mov al, [a]
    ;mov ah, [d]
    ;mul ah
    ;mov bx, ax
    ;mov al, [b]
    ;mov ah, [c]
    ;mul ah
    ;add ax, bx
    ;ax = 26 = 001Ah
    
    ;a,b,c,d-byte, e,f,g,h-word
    ;f*(e-2)/[3*(d-5)] = 3 = 0003h
    ;mov al, [d]
    ;sub al, 5
    ;mov ah, 3
    ;mul ah
    ;mov bx, ax
    ;mov ax, [e]
    ;sub ax, 2
    ;mov dx, [f]
    ;mul dx
    ;div bx
    ;ax = 3 = 0003h
    
    ;a,b,c,d-byte, e,f,g,h-word
    ;[(a+b+c)*2]*3/g = 30 = 001Eh
    ;mov al, [a]
    ;add al, [b]
    ;add al, [c]
    ;mov ah, 2
    ;mul ah
    ;mov bx, 3
    ;mul bx
    ;mov bx, [g]
    ;div bx
    ;ax = 30 = 001Eh
    
    ;a,b,c,d-byte, e,f,g,h-word
    ;(e+f+g)/(a+b) = 1 = 01h
    ;mov ax, [e]
    ;add ax, [f]
    ;add ax, [g]
    ;mov bl, [a]
    ;add bl, [b]
    ;div bl
    ;al = 1 = 01h
    
    ;a,b,c,d-byte, e,f,g,h-word
    ;100/(e+h-3*a) = 100 = 0064h
    mov al, [a]
    mov ah, 3
    mul ah
    mov bx, ax
    mov ax, [e]
    add ax, [h]
    sub ax, bx
    mov bx, ax
    mov dx, 0
    mov ax, 100
    div bx
    ;ax = 100 = 0064h 
    
    push dword 0
    call [exit]