bits 32

global start

extern exit
import exit msvcrt.dll

segment data use32 class=data

a dw 1001110101011001b
b dw 0011101010001100b
c dd 0
;c = 1001 1101 0101 1001 1010 1111 1111 0011b
;c = 9Dh 59h AFh F3h => (little endian) = F3h AFh 59h 9Dh
                             
segment code use32 class=code
start:
    ;Se dau cuvintele A si B. Sa se obtina dublucuvantul C:
    ;bitii 0-4 ai lui C coincid cu bitii 11-15 ai lui A
    ;bitii 5-11 ai lui C au valoarea 1
    ;bitii 12-15 ai lui C coincid cu bitii 8-11 ai lui B
    ;bitii 16-31 ai lui C coincid cu bitii lui A
    mov ax, [a]
    shr ax, 11
    or [c], ax
    or dword[c], 111111100000b
    mov ax, [b]
    shr ax, 8
    shl ax, 12
    or [c], ax
    mov eax, 0
    mov ax, [a]
    shl eax, 16
    or [c], eax
    
    push dword 0
    call [exit]