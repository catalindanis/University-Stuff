bits 32

global start

extern exit
import exit msvcrt.dll

segment data use32 class=data

a db 1234h

segment code use32 class=code
start:
    mov eax, "1234"
    push dword 0
    call [exit]
