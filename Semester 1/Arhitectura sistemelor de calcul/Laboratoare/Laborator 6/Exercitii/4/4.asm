bits 32

global start

extern exit
import exit msvcrt.dll

segment data use32 class=data

s DD 10203040h, 50607080h
ls equ ($-s)
d times ls db 0
;d = 56h, 3Ch, 34h, 2Bh, 1Ah, 15h, 12h, 07h

segment code use32 class=code
start:
    mov esi, s
    mov ecx, ls / 4
    mov edi, d
    cld
    jecxz final
    repeta:
        lodsd
        stosb
        shr eax, 8
        stosb
        shr eax, 8
        stosb
        shr eax, 8
        stosb
    loop repeta
    
    mov ecx, ls  
    iterare:
        push ecx
        
        mov esi, d
        mov edi, d + 1
        
        mov ecx, ls-1
        jecxz final
        compara:
            cmpsb 
            ja desc
                mov al, [esi - 1]
                mov bl, [edi - 1]
                mov [esi - 1], bl
                mov [edi - 1], al
            desc:
        loop compara
        pop ecx
    loop iterare
    final:
    push dword 0
    call [exit]