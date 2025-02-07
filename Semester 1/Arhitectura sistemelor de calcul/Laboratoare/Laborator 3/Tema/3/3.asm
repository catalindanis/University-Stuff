bits 32

global start

extern exit
import exit msvcrt.dll

segment data use32 class=data

a db 102
b db 3
c dw 2
e dd 8
x dq 204

segment code use32 class=code
start:
    ; (100 + a + b * c)/(a - 100) + e + x / a
    ; a, b - byte
    ; c - word
    ; e - doubleword
    ; x - qword
    ; *interpretare fara semn*
    
    mov AX, 0 ; AX <- 0
    mov AL, [b] ; AX <- b
    mul word[c] ; DX:AX <- AX * c = b * c
    push DX
    push AX
    pop EAX
    ; EAX = b * c
    
    mov EBX, 0 ; EBX <- 0
    mov BL, [a] ; EBX <- a
    add EBX, 100 ; EBX <- a + 100
    add EAX, EBX ; EAX <- 100 + a + b * c
    ; EAX = 100 + a + b * c
    
    mov BX, 0 ; BX <- 0
    mov BL, [a] ; BX <- a
    sub BX, 100 ; BX <- a - 100
    ; BX = a - 100
    
    div BX ; AX <- EAX / BX
    ; AX = (100 + a + b * c) / (a - 100)
    
    mov EBX, 0 ; EBX <- 0
    mov BX, AX ; EBX <- AX
    
    add EBX, [e] ; EBX <- AX + e
    ;EBX = (100 + a + b * c) / (a - 100) + e
    
    mov EAX, [x] ; EAX <- x (dword nesemn.)
    mov EDX, [x + 4] ; EDX <- x (dword semn.)
    ; EDX:EAX = x
    
    mov ECX, 0 ; ECX <- 0
    mov CL, [a] ; ECX <- a
    
    div ECX ; EAX <- EDX:EAX / ECX
    ; EAX = x / a
    
    add EAX, EBX
    ; EAX = (100 + a + b * c) / (a - 100) + e + x / a
    
    push dword[0]
    call [exit]