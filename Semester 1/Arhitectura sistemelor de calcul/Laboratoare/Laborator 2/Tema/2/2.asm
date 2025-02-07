;(a+b-c)-(a+d)
;a, b, c, d - byte
bits 32

global start

extern exit
import exit msvcrt.dll

segment data use32 class=data

a DB 1
b DB 1
c DB 1
d DB 1

segment data use32 class=code
start:
    mov AL, [a] ; AL <- a
    add AL, [b] ; AL <- a + b
    sub AL, [c] ; AL <- a + b - c
    mov AH, [a] ; AH <- a
    add AH, [d] ; AH <- a + d
    sub AL, AH  ; AL <- AL - AH = (a + b - c) - (a + d)
    push dword 0
    call [exit]