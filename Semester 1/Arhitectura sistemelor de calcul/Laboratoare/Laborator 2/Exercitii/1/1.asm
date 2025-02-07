bits 32

global start

extern exit, scanf, printf
import exit msvcrt.dll
import printf msvcrt.dll
import scanf msvcrt.dll

segment data use32 class=data

a db 5
c equ 2

segment code use32 class=code
start:
    mov al, c
    push dword 0
    call [exit]
