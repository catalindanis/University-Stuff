bits 32

global start

extern exit
import exit msvcrt.dll

segment data use32 class=data

a db 12
b dw -7
c dd 5
d dq -2

segment code use32 class=code
start:
    ; (d + d - c) - (c + c - a) + (c + a) = 1 + 22 + 7 = 30
    ; (d + d - c) - (c + c - a) + (c + a) = -9 + 2 + 17 = 10
    ; a - byte
    ; b - word
    ; c - double word
    ; d - quad word
    ; *interpretare cu semn*
    
    mov eax, [d] ; eax <- d (dword nesemn.)
    mov edx, [d+4] ; edx <- d (dword semn.)
    sub eax, [c] ; eax <- d - c
    sbb edx, 0 ; edx <- (-CF) + d (dword semn.)
    ; edx:eax = d - c
    add eax, [d] ; eax <- eax + d (dword nesemn.)
    adc edx, [d+4] ; edx <- edx + d (dword semn.)
    ; edx:eax = d + d - c
    
    push eax
    push edx
    ; edx:eax -> stack 
    
    mov eax, [c] ; eax <- c
    cdq ; edx:eax <- c
    add eax, [c] ; eax <- c + c
    adc edx, 0 ; edx <- 0 + CF
    mov ebx, eax ; ebx <- eax
    mov ecx, edx ; ecx <- edx
    ; ecx:ebx = c + c
    mov al, [a]
    cbw ; ax <- a
    cwde ; eax <- a
    cdq 
    ; edx:eax <- a
    sub ebx, eax ; ebx <- ebx - a
    sbb ecx, edx ; ecx <- ecx - edx
    ; ecx:ebx <- c + c - a
    
    mov al, [a]
    cbw ; ax <- a
    cwde ; eax <- a
    add eax, [c] ; eax <- c + a
    cdq 
    ; edx:eax <- c + a
    
    sub eax, ebx 
    sbb edx, ecx
    mov ecx, edx
    mov ebx, eax
    ; ecx:ebx <- (c + a) - (c + c - a)
    
    pop edx
    pop eax
    ; edx:eax <- d + d - c
    
    add eax, ebx
    adc edx, ecx
    ; edx:eax = (d + d - c) - (c + c - a) + (c + a)
    
    push dword[0]
    call [exit]