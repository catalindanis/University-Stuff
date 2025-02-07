bits 32

global start

extern exit, fopen, fprintf, fscanf, fclose
import exit msvcrt.dll
import fopen msvcrt.dll
import fprintf msvcrt.dll
import fscanf msvcrt.dll
import fclose msvcrt.dll

segment data use32 class=data

file_name db "input.txt", 0
file_access_mode db "a+", 0
descriptor dd 0
format_citire db "%d", 0
n dd 0
format_afisare db 10, "Valoarea minima este : %d", 0
minim dd 0

segment code use32 class=code
start:
    ;Se da un fisier text. Fisierul contine numere (in baza 10) 
    ;separate prin spatii. Sa se citeasca continutul acestui fisier, 
    ;sa se determine minimul numerelor citite si sa se scrie rezultatul 
    ;la sfarsitul fisierului.
    
    ;fopen(file_name, file_access_mode)
    push dword file_access_mode
    push dword file_name
    call [fopen]
    add esp, 4 * 2
    cmp eax, 0
    je final
    mov [descriptor], eax
    
    ;fscanf(descriptor, format_citire, &n)
    push dword n
    push dword format_citire
    push dword [descriptor]
    call [fscanf]
    add esp, 4 * 3
        
    mov ebx, [n]
    mov [minim], ebx
        
    cmp eax, -1
    je gata_fisier
    
    repeta:
        ;fscanf(descriptor, format_citire, &n)
        push dword n
        push dword format_citire
        push dword [descriptor]
        call [fscanf]
        add esp, 4 * 3
        
        cmp eax, -1
        je gata_fisier
        
        mov ebx, [n]
        cmp ebx, [minim]
        jg actualizare
        mov [minim], ebx
        actualizare:
    jmp repeta
    gata_fisier:
    
    push dword [minim]
    push dword format_afisare
    push dword [descriptor]
    call [fprintf]
    add esp, 4 * 3
    
    push dword [descriptor]
    call [fclose]
    add esp, 4 * 1
    final:
    push dword 0
    call [exit]