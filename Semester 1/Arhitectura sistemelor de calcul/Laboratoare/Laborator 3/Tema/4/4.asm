bits 32

global start

extern exit
import exit msvcrt.dll

segment data use32 class=data

a db 102
b db 3
c dw -2
e dd 8
x dq -612

segment code use32 class=code
start:
    ; (100 + a + b * c)/(a - 100) + e + x / a
    ; a, b - byte
    ; c - word
    ; e - doubleword
    ; x - qword
    ; *interpretare cu semn*
    mov AL, [b] ; AL <- b
    cbw ; AX <- AL
    ; AX = b
    
    imul word[c] ; DX:AX <- AX * c
    ; DX:AX = b * c
    push DX
    push AX
    pop EAX
    ; EAX = b * c
    
    mov EBX, EAX ; EBX <- EAX
    ; EBX = b * c
    
    mov AL, [a] ; AL <- a
    cbw ; AX <- a
    cwde ; EAX <- a
    add EAX, EBX ; EAX <- a + b * c
    add EAX, 100 ; EAX <- 100 + a + b * c
    ; EAX = 100 + a + b * c
    
    push EAX
    
    mov AL, [a] ; AL <- a
    cbw ; AX <- a
    mov BX, AX ; BX <- a
    sub BX, 100 ; BX <- a - 100
    
    pop AX
    pop DX
    ; DX:AX = 100 + a + b * c
    
    idiv BX
    ; AX = (100 + a + b * c) / (a - 100)
    
    push AX
    
    mov AL, [a] ; AX <- a
    cbw ; AX <- a
    cwde ; EAX <- a
    mov EBX, EAX ; EBX <- a
    ; EBX = a
    
    mov EAX, [x] ; EAX <- x (dword nesemn.)
    mov EDX, [x+4] ; EDX <- x (dword semn.)
    ; EDX:EAX = x
    
    idiv EBX
    ; EAX = x / a
    
    add EAX, [e] 
    mov EBX, EAX
    ; EBX = e + x / a
    
    pop AX
    ; AX = (100 + a + b * c) / (a - 100)
    
    cwde ; EAX <- AX
    
    add EAX, EBX
    ; EAX = (100 + a + b * c) / (a - 100) + e + x / a
    
    push dword[0]
    call [exit]