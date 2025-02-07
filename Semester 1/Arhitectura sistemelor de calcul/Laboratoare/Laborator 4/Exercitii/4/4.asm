bits 32

global start

extern exit
import exit msvcrt.dll

segment data use32 class=data

a dw 1001010101001110b
b dw 1101000101100110b
c dw 1010110001010110b
d dw 0
;d = 00111 + 00101 + 10101 = 7 + 5 + 21 = 33 = 21h

segment code use32 class=code
start:
    ;Se dau cuvintele A, B si C. Sa se formeze cuvantul D ca suma a numerelor reprezentate de:
    ;biţii de pe poziţiile 1-5 ai lui A
    ;biţii de pe poziţiile 6-10 ai lui B
    ;biţii de pe poziţiile 11-15 ai lui C
    
    mov al, [a]
    and al, 111110b
    shr al, 1
    add [d], al
    
    mov ax, [b]
    shr ax, 6
    and al, 11111b
    add [d], al
    
    mov al, [c+1]
    shr al, 3
    and al, 11111b
    add [d], al
    
    push dword 0
    call [exit]