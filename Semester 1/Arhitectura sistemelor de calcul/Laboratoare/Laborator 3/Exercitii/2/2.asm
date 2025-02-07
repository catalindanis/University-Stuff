bits 32

global start

extern exit
import exit msvcrt.dll

segment data use32 class=data

a db -5
b dw 3
c dd -9
d dq 2

segment code use32 class=code
start:
    ;a - byte, b - word, c - double word, d - qword - Interpretare cu semn
    ;b+c+d+a-(d+c) = -2
    ;mov al, [a]
    ;cbw
    ;add ax, [b]
    ;cwde
    ;add eax, [c]
    ;cdq
    ;add eax, [d]
    ;adc edx, [d+4]
    
    ;mov ebx, eax
    ;mov ecx, edx
    
    
    ;mov eax, [c]
    ;cdq
    ;add eax, [d]
    ;adc edx, [d+4]
    
    ;xchg eax, ebx
    ;xchg edx, ecx
    
    ;sub eax, ebx
    ;sbb edx, ecx
    
    
    ;a - byte, b - word, c - double word, d - qword - Interpretare cu semn
    ;(a + b - c) + (a + b + d) - (a + b) = 9
    mov al, [a]
    cbw
    add ax, [b]
    cwde
    sub eax, [c]
    mov ebx, eax
    
    mov al, [a]
    cbw
    add ax, [b]
    cwde
    cdq
    add eax, [d]
    adc edx, [d+4]
    
    add eax, ebx
    adc edx, 0
    
    mov ebx, eax
    mov ecx, edx
    
    mov al, [a]
    cbw
    add ax, [b]
    cwde
    cdq
    
    xchg ebx, eax
    xchg ecx, edx
    
    sub eax, ebx
    sbb edx, ecx
    push dword 0
    call [exit]