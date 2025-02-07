bits 32 

global start

extern exit, printf, scanf
import exit msvcrt.dll
import printf msvcrt.dll
import scanf msvcrt.dll


segment data use32 class=data

a dw 1, 2, 3, 10, 20, 30

segment code use32 class=code
start:
    
    push dword 0
    call [exit]