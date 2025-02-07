bits 32

global start

extern exit
import exit msvcrt.dll

segment data use32 class=data

a dw 1
b db 1
c dd 3
e dd 2
x dq 20

segment code use32 class=code
start:
    ;a,b,c-byte; e-doubleword; x-quadword - fara semn
    ;x-(a*b*25+c*3)/(a+b)+e = 1 
    ;mov al, [a]
    ;mul byte[b]
    ;mov dx, 25
    ;mul dx
    ;push dx
    ;push ax
    ;pop ebx
    ;mov eax, 0
    ;mov al, [c]
    ;mov ah, 3
    ;mul ah
    ;add eax, ebx
    
    ;push eax
    ;pop ax
    ;pop dx
    
    ;mov bl, [a]
    ;add bl, [b]
    ;mov bh, 0
    ;div bx
    
    ;mov bx, ax
    ;mov eax, 0
    ;mov ax, bx
    
    ;add eax, [e]
    ;mov edx, 0
    
    ;mov ebx, [x]
    ;mov ecx, [x+4]
    
    ;xchg eax, ebx
    ;xchg edx, ecx
    
    ;sub eax, ebx
    ;sbb edx, ecx
    
    ;a-word; b-byte; c-doubleword; x-qword - fara semn
    ;(a*a+b+x)/(b+b)+c*c = 20
    
    mov ax, [a]
    mul word[a]
    push dx
    push ax
    pop eax
    mov ebx, 0
    mov bl, [b]
    add eax, ebx
    mov edx, 0
    add eax, [x]
    adc edx, [x+4]
    mov ebx, 0
    mov bl, [b]
    add bl, [b]
    div ebx
    mov ecx, 0
    mov ebx, eax
    mov eax, [c]
    mul dword[c]
    
    add eax, ebx
    adc edx, ecx
    
    push dword 0
    call [exit]