bits 32

extern exit, fopen, fclose, fscanf, printf, fclose

import exit msvcrt.dll
import fopen msvcrt.dll
import fclose msvcrt.dll
import fscanf msvcrt.dll
import printf msvcrt.dll
import fclose msvcrt.dll

global start

segment data use32 class=data

nume_fisier db "input.txt", 0
mod_acces_fisier db "r", 0
descriptor resd 1
format_citire db "%x", 0
format_afisare db "%x - %d", 10, 0
valoare dd -1

segment code use32 class=code
start:
    ;fopen(nume_fisier, mod_acces)
    push dword mod_acces_fisier
    push dword nume_fisier
    call [fopen]
    add esp, 4 * 2
    
    cmp eax, 0
    je final
    mov [descriptor], eax
    
    citire:
        ;fscanf(descriptor, format, &valoare)
        push dword valoare
        push dword format_citire
        push dword [descriptor]
        call [fscanf]
        add esp, 4 * 3
        
        cmp eax, -1
        je final
        
        mov eax, [valoare]
        mov ebx, 0
        mov ecx, 32
        repeta:
            test dword[valoare], 1
            jz sari
            inc ebx
            sari:
            shr dword[valoare], 1
        loop repeta
        
        ;printf(format_afisare, eax, ebx)
        push ebx
        push eax
        push dword format_afisare
        call [printf]
        add esp, 4 * 3
    jmp citire
    
    final:
    
    push dword [descriptor]
    call [fclose]
    
    push dword 0
    call [exit]