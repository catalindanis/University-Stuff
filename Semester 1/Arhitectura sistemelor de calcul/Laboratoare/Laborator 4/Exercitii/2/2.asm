bits 32 

global start

extern exit
import exit msvcrt.dll


segment data use32 class=data

b db 11001001b
a dw 0011110101001001b
;b = 11001101b = CDh

segment code use32 class=code
start:
    ;Sa se inlocuiasca bitii 0-3 ai octetului B cu bitii 8-11 ai cuvantului A.
    mov al, [a+1]
    and al, 00001111b
    or [b], al
    push dword 0
    call [exit]