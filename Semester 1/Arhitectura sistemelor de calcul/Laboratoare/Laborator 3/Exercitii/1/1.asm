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
    ;a - byte, b - word, c - double word, d - qword - Interpretare fara semn
    ;c-(a+d)+(b+d) = 7 = 00 00 00 00 00 00 00 07h
    ;mov edx, 0
    ;mov eax, 0
    ;mov al, [a]
    ;add eax, [d]
    ;adc edx, [d+4]
    
    ;mov ebx, 0
    ;mov ecx, 0
    ;mov bx, [b]
    ;add ebx, [d]
    ;adc ecx, [d+4]
    
    ;sub ebx, eax
    ;sbb ecx, edx
    
    ;mov eax, 0
    ;mov edx, 0
    
    ;mov eax, [c]
    ;add eax, ebx
    ;adc edx, ecx
    ;edx:eax = 7 = 00 00 00 00 00 00 00 07h
    
    ;a - byte, b - word, c - double word, d - qword - Interpretare fara semn
    ;(c-a-d)+(c-b)-a = 3 = 00 00 00 00 00 00 00 03h
    ;mov eax, 0
    ;mov al, [a]
    ;push dword[c]
    ;sub [c], eax
    ;mov eax, [c]
    ;mov edx, 0
    ;sub eax, [d]
    ;sbb edx, [d+4]
    
    ;pop dword[c]
    ;push edx
    ;push eax
    
    ;mov eax, 0
    ;mov ax, [b]
    ;sub [c], eax
    ;mov eax, 0
    ;mov al, [a]
    ;sub [c], eax
    ;mov eax, [c]
    ;mov edx, 0
    
    ;pop ebx
    ;pop ecx
    
    ;add eax, ebx
    ;adc edx, ecx
    ;edx:eax = 3 = 00 00 00 00 00 00 00 03h 
    
    push dword 0
    call [exit]