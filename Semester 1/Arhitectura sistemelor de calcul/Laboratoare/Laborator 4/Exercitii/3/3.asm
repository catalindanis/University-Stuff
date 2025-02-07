bits 32

global start

extern exit
import exit msvcrt.dll

segment data use32 class=data

a dq 0A3BCDD214DF78EABh
n db 0
;n = 3
b dd 0
; b = 1D E6 E9 0D => (little endian) 0D E9 E6 1D
c db 0
; c =  1111 0100 = F4
segment code use32 class=code
start:
    ;Se da quadwordul A. Sa se obtina numarul intreg N reprezentat de bitii 17-19 ai lui A.
    ;Sa se obtina apoi in B dublucuvantul rezultat prin rotirea spre stanga a dublucuvantului
    ;superior al lui A cu N pozitii. Sa se obtina octetul C astfel:
    ;bitii 0-2 ai lui C sunt bitii 9-11 ai lui B
    ;bitii 3-7 ai lui C sunt bitii 20-24 ai lui B
    mov al, [a+2]
    and al, 1110b
    shr al, 1
    mov [n], al
    
    mov eax, [a + 4]
    mov [b], eax
    mov cl, [n]
    rol dword[b], cl
    
    mov eax, [b]
    shr eax, 9
    and eax, 111b
    or [c], al
    
    mov eax, [b]
    shr eax, 20
    and eax, 11111b
    shl eax, 3
    or [c], al
    push dword 0
    call [exit]