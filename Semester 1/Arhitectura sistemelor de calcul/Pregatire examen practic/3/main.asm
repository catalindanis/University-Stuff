bits 32

extern exit, fscanf, fopen, fclose, printf
import exit msvcrt.dll
import fscanf msvcrt.dll
import fopen msvcrt.dll
import fclose msvcrt.dll
import printf msvcrt.dll

global start

segment data use32 class=data

nume_fisier db "date.txt", 0
mod_acces db "r", 0
format_citire db "%d", 0
format_afisare db "%d, %x, %d", 10, 0
descriptor resd 1
valoare resd 1

segment code use32 class=code
start:
    ;fopen(nume_fisier, mod_acces)
    push dword mod_acces
    push dword nume_fisier
    call [fopen]
    
    cmp eax, 0
    je final
    mov [descriptor], eax
    
    ;fscanf(descriptor, format_citire, &valoare)
    push dword valoare
    push dword format_citire
    push dword [descriptor]
    call [fscanf]
    
    
    
    
    ;fclose(descriptor)
    push dword [descriptor]
    call [fclose]
    
    final:
    push dword 0
    call [exit]