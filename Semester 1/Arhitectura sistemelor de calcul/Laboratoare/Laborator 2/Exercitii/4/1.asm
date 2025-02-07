bits 32

global start

extern exit
import exit msvcrt.dll

segment data use32 class=data

a db 4
b db 3
c db 2
d dw 5

segment code use32 class=code
start:
    ;a,b,c - byte, d - word
    ;((a+b-c)*2 + d-5)*d = 50 = 00000032h
    ;mov al, [a]
    ;add al, [b]
    ;sub al, [c]
    ;mov ah, 2
    ;mul ah
    ;add ax, [d]
    ;sub ax, 5
    ;mov dx, [d]
    ;mul dx
    ;DX:AX = 50 = 00000032h
    
    ;a,b,c - byte, d - word
    ;[d-2*(a-b)+b*c]/2 = 4 r 1 = 04h r 01h
    ;mov al, [a]
    ;sub al, [b]
    ;mov ah, 2
    ;mul ah
    ;mov bx, ax
    ;mov al, [b]
    ;mov ah, [c]
    ;mul ah
    ;add ax, [d]
    ;sub ax, bx
    ;mov bl, 2
    ;div bl
    ;AL = 04h, AH = 01h
    
    ;a,b,c - byte, d - word
    ;3*[20*(b-a+2)-10*c]/5 = 0 r 0 = 0000h
    ;mov al, [b]
    ;sub al, [a]
    ;add al, 2
    ;mov ah, 20
    ;mul ah
    ;mov bx, ax
    ;mov al, [c]
    ;mov ah, 10
    ;mul ah
    ;xchg ax, bx
    ;sub ax, bx
    ;mov bl, 5
    ;div bl
    ;mov ah, 3
    ;mul ah
    ;ax = 0 = 0000h
    
    ;a,b,c - byte, d - word
    ;(a*2)+2*(b-3)-d-2*c = -1 = FFFFh
    ;mov al, 2
    ;mov ah, [a]
    ;mul ah
    ;mov bx, ax
    ;mov al, [b]
    ;sub al, 3
    ;mov ah, 2
    ;mul ah
    ;add bx, ax
    ;mov al, 2
    ;mov ah, [c]
    ;mul ah
    ;sub bx, ax
    ;sub bx, [d]
    ;mov ax, bx
    
    ;a,b,c - byte, d - word
    ;(50-b-c)*2+a*a+d = 111 = 006Fh
    ;mov al, 50
    ;sub al, [b]
    ;sub al, [c]
    ;mov ah, 2
    ;mul ah
    ;mov bx, ax
    ;mov al, [a]
    ;mul al
    ;add ax, bx
    ;add ax, [d]
    ;ax = 006Fh
    
    ;a,b,c - byte, d - word
    ;[100-10*a+4*(b+c)]-d = 75 = 004Bh
    ;mov al, [a]
    ;mov ah, 10
    ;mul ah
    ;mov bx, ax
    ;mov al, [b]
    ;add al, [c]
    ;mov ah, 4
    ;mul ah
    ;add ax, 100
    ;sub ax, bx
    ;sub ax, [d]
    ;ax = 75 = 004Bh
    
    ;a,b,c - byte, d - word
    ;[(a-b)*5+d]-2*c = 6 = 0006h
    mov al, 2
    mov ah, [c]
    mul ah
    mov bx, ax
    mov al, [a]
    sub al, [b]
    mov ah, 5
    mul ah
    add ax, [d]
    sub ax, bx
    ;ax = 6 = 0006h
    
    push dword 0
    call [exit]