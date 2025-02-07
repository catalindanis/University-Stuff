bits 32

global start

extern exit, printf
import exit msvcrt.dll
import printf msvcrt.dll

segment data use32 class=data

sir dd 1234A678h, 12785634h, 1A4D3C2Bh
lg equ ($ - sir) / 4
sir_rez resw lg
format_afisare db "%d", 0
nr_biti_1 dd 0

segment code use32 class=code
start:
    mov esi, sir
    mov edi, sir_rez
    cld
    mov ecx, lg
    jecxz final
    
    repeta:
        lodsb
        lodsb
        mov bl, al
        lodsb
        lodsb
        mov ah, al
        mov al, bl
        stosw
        
        push ecx
        
        mov ecx, 16
        numara:
            shr ax, 1
            adc dword[nr_biti_1], 0
        loop numara
 
        pop ecx
    loop repeta
    
    final:
    ;printf(format_afisare, nr_biti_1)
    push dword [nr_biti_1]
    push dword format_afisare
    call [printf]
    
    push dword 0
    call [exit]