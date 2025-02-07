bits 32

global start

extern exit
import exit msvcrt.dll

segment data use32 class=data

a dw 3
c db -1
b db 5
d dd -3
x dq 5
e dd -2

segment code use32 class=code
start:
    ;a,c-byte; b-word; d-doubleword; x-qword - interpretare cu semn
    ;d-(7-a*b+c)/a-6+x/2 = -4
    ;mov eax, [x]
    ;mov edx, [x+4]
    ;mov ebx, 2
    ;idiv ebx
    ;push eax
    
    ;mov al, [a]
    ;cbw
    ;imul word[b]
    ;mov bx, ax
    ;mov cx, dx
    ;add byte[c], 7
    ;mov al, [c]
    ;cbw
    ;cwd
    ;sub ax, bx
    ;sbb dx, cx
    
    ;push dx
    ;push ax
    
    ;mov al, [a]
    ;cbw
    ;mov bx, ax
    
    ;pop ax
    ;pop dx
    
    ;idiv bx
    
    ;mov bx, ax
    ;mov ax, -6
    ;sub ax, bx
    
    ;cwde
    ;pop ebx
    ;add ebx, eax
    ;mov eax, [d]
    ;add eax, ebx
    
    ;a-word; b,c-byte; e-doubleword; x-qword
    ;a*b-(100-c)/(b*b)+e+x = 14
    mov al, [b]
    imul byte[b]
    mov bx, ax
    mov al, 100
    sub al, [c]
    cbw
    cwd
    idiv bx
    
    push ax
    
    mov al, [b]
    cbw
    imul word[a]
    push dx
    push ax
    pop eax
    add eax, [e]
    cdq
    add eax, [x]
    adc eax, [x+4]
    mov ebx, eax
    mov ecx, edx
    
    pop ax
    cwde
    cdq
    
    xchg eax, ebx
    xchg edx, ecx
    
    sub eax, ebx
    sbb edx, ecx
    
    push dword 0
    call [exit]