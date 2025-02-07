bits 32

global start

extern exit, printf, scanf

import exit msvcrt.dll
import printf msvcrt.dll
import scanf msvcrt.dll

segment data use32 class=data

s db "aaabdccdac"
ls equ $ - s
c db 0
nr_aparitii dd 0
format_mesaj_citire db "Introduceti un caracter : ", 0
format_citire db "%c"
format_mesaj_afisare db "Caracterul este : %c", 10, "iar numarul de aparitii este : %d", 0

segment code use32 class=code
start:
    ;Se dă un sir de caractere (definit in segmentul de date). 
    ;Să se citească de la tastatură un caracter, să se 
    ;determine numărul de apariţii al acelui caracter în şirul 
    ;dat şi să se afişeze acel caracter împreună cu numărul de apariţii al acestuia.
    
    ;printf(format_mesaj_citire)
    push dword format_mesaj_citire
    call [printf]
    add esp, 4 * 1
    
    ;scanf(format_citire, &c)
    push dword c
    push dword format_citire
    call [scanf]
    add esp, 4 * 2
    
    mov ecx, ls
    mov esi, s
    cld
    repeta:
        lodsb 
        cmp al, [c]
        jne sari
        inc dword[nr_aparitii]
        sari:
    loop repeta
    
    ;printf(format_mesaj_afisare, c, nr_aparitii)
    push dword [nr_aparitii]
    push dword [c]
    push dword format_mesaj_afisare
    call [printf]
    
    push dword 0
    call [exit]