bits 32

global start

extern exit
import exit msvcrt.dll

segment data use32 class=data

s dd 127F5678h, 0ABCDABCDh
ls equ ($-s) / 4
d times ls dd 0

segment code use32 class=code
start:
    mov ecx, ls
    mov esi, s
    mov edi, d
    cld
    repeta:
        push ecx
        lodsd
        mov bh, ah
        cbw 
        mov dx, ax
        mov al, bh
        cbw
        mov cx, ax
        
        shr eax, 16
        
        mov bh, ah
        cbw 
        add dx, ax
        mov al, bh
        cbw
        add cx, ax
        
        mov ax, cx
        shl eax, 16
        mov ax, dx
        stosd
        pop ecx
    loop repeta
    push dword 0
    call [exit]