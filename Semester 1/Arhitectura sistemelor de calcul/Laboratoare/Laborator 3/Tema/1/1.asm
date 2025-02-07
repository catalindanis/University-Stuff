bits 32

global start

extern exit
import exit msvcrt.dll

segment data use32 class=data

a db 25
b dw 9
c dd 8
d dq 7

segment code use32 class=code
start:
    ; (a+c)-(d+b) = 33 - 16 = 17
    ; a - byte
    ; b - word
    ; c - double word
    ; d - quad word
    ; *interpretare fara semn*
    
    mov EAX, 0 ; EAX <- 0
    mov AL, [a] ; EAX <- a
    add EAX, [c] ; EAX <- a + c
    mov EDX, 0 ; EDX:EAX <- (a+c)
    adc EDX, 0
    
    push EAX
    push EDX ; STACK <- EDX:EAX
    
    mov EDX, 0
    mov EAX, 0
    mov AX, [b] ; EDX:EAX <- b
    
    mov EBX, [d]
    mov ECX, [d+4] ; ECX:EBX <- d
    
    add EAX, EBX
    adc EDX, ECX ; EDX:EAX <- d + b
    
    pop ECX
    pop EBX ; ECX:EBX <- (a + c)
    
    sub EBX, EAX
    sbb ECX, EDX ; ECX:EBX <- (a + c) - (d + b)
    
    push dword[0]
    call exit