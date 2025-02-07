bits 32

global start

extern exit, printf, scanf
import exit msvcrt.dll
import printf msvcrt.dll
import scanf msvcrt.dll

segment data use32 class=data

n resd 1
format_citire db "%d", 0
sir resb 33
format_afisare db "%s", 0

segment code use32 class=code
start:
    ;scanf(format, &n)
    push dword n
    push dword format_citire
    call [scanf]
    add esp, 4 * 2
    
    mov edi, sir
    cld
    mov ecx, 32
    repeta:
        shl dword[n], 1
        mov al, 0
        adc al, '0'
        stosb
    loop repeta
    
    ;printf(format_afisare, sir)
    push dword sir
    push dword format_afisare
    call [printf]
    add esp, 4 * 2
    
    push dword 0
    call [exit]