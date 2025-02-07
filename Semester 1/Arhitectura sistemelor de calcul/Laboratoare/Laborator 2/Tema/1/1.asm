;4+12

bits 32

global start

extern exit
import exit msvcrt.dll

segment data use32 class=data

segment data use32 class=code
start:
    
    mov AL, 4 ;  AL <- 4
    add AL, 12 ; AL <- 4 + 12 = 16

    push dword 0
    call [exit]