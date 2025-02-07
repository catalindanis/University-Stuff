bits 32

extern exit
import exit msvcrt.dll

global start

segment data use32 class=data

a dw 12468h

segment code use32 class=code
start:
    pop dword[a]
    push dword 0
    call [exit]
    