;[(e+f-g)+(b+c)*3]/5
;a, b, c, d - byte
;e, f, g, h - word
bits 32

global start

extern exit
import exit msvcrt.dll

segment data use32 class=data

a DB 5
b DB 8
c DB 4
d DB 1

e DW 2
f DW 3
g DW 1
h DW 4

segment data use32 class=code
start:
    
    mov AL, 3   ; AL <- 3
    mov BL, [b] ; BL <- b
    add BL, [c] ; BL <- b + c
    mul BL      ; AX <- AL * BL = 3 * (b + c)
    
    mov BX, [e] ; BL <- e
    add BX, [f] ; BL <- e + f
    sub BX, [g] ; BL <- e + f - g
    
    add AX, BX  ; AX <- AX + BX
    mov BL, 5

    div BL
    
    push dword 0
    call [exit]