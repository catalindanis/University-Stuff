;d/[(a+b)-(c+c)]
;a, b, c - byte
;d - word
bits 32

global start

extern exit
import exit msvcrt.dll

segment data use32 class=data

a DB 12
b DB 3
c DB 5
d DW 20

segment code use32 class=code
start:
    mov BL, [a] ; BL <- a
    add BL, [b] ; BL <- a + b
    mov BH, [c] ; BH <- c
    add BH, [c] ; BH <- c + c
    sub BL, BH  ; BL <- BL - BH = (a + b) - (c + c)
    mov AX, [d] ; AX <- d
    div BL ; AL <- d / [(a + b) - (c + c)]
    push dword 0
    call [exit]