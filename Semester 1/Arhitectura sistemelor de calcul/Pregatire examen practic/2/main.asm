bits 32

extern exit, fopen, fread, printf, fclose

import exit msvcrt.dll
import fopen msvcrt.dll
import fread msvcrt.dll
import printf msvcrt.dll
import fclose msvcrt.dll

global start

segment data use32 class=data

nume_fisier db "r1.txt", 0
mod_acces db "r", 0
descriptor dd 0
format db "%s", 0
text times 100 db -1

segment code use32 class=code
start:
    ;fopen(nume_fisier, mod_acces)
    push dword mod_acces
    push dword nume_fisier
    call [fopen]
    add esp, 4 * 2
    
    cmp eax, 0
    je final
    mov [descriptor], eax
    
    ;fread(text, 1, 100, descriptor)
    push dword [descriptor]
    push dword 100
    push dword 1
    push dword text
    call [fread]
    add esp, 4 * 4
    
    mov ecx, eax
    mov edi, text
    mov esi, text
    repeta:
        lodsb
        
        cmp al, 'A'
        jl gata
        
        cmp al, 'Z'
        jle e_litera
        
        cmp al, 'a'
        jl gata
        
        cmp al, 'z'
        jle e_litera
        
        jmp gata
        
        e_litera:
        
        p1:
        cmp al, 'Z'
        jne p2
        mov al, 'B'
        jmp gata
        
        p2:
        cmp al, 'y'
        jne p3
        mov al, 'a'
        jmp gata
        
        p3:
        cmp al, 'Y'
        jne p4
        mov al, 'A'
        jmp gata
        
        p4:
        add al, 2
        
        gata:
        stosb
    loop repeta
    
    ;printf(format, text)
    push dword text
    push dword format
    call [printf]
    
    final:
    
    ;fclose(descriptor)
    push dword [descriptor]
    call [fclose]
    
    push dword 0
    call [exit]