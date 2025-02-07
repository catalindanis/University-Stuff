;a + b - (c + d) + 100h
;a, b, c, d - word
bits 32

global start

extern exit
import exit msvcrt.dll

segment data use32 class=data

a DW 5
b DW 7
c DW 10
d DW 3

segment data use32 class=code
start:
    mov AX, [a]  ; AX <- a
    add AX, [b]  ; AX <- a + b
    mov BX, [c]  ; BX <- c
    add BX, [d]  ; BX <- c + d
    sub AX, BX   ; AX <- AX - BX = a + b - (c + d)
    add AX, 100h ; AX <- AX + 100h = a + b - (c + d) + 100h
    push dword 0
    call [exit]