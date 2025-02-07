bits 32

global start

extern exit, printf, selectie
import exit msvcrt.dll
import printf msvcrt.dll

segment data use32 class=data

sir dd 630C018Fh, 640E0563h, 6102DF07h, 6202CF00h, 6506BF02h
ls equ ($ - sir) / 4
sir_octeti resb ls * 2
format_afisare db "%xh", 0

segment code use32 class=code
start:
    
    mov esi, sir
    mov edi, sir_octeti
    mov ecx, ls
    cld
    repeta:
        lodsd
        shr eax, 8
        stosb
        shr eax, 8
        stosb
    loop repeta
    
    push ls * 2
    push dword sir_octeti
    call selectie
    add esp, 4 * 1
    
    push eax
    push dword format_afisare
    call [printf]
    add esp, 4 * 2
    
    push dword 0
    call [exit]