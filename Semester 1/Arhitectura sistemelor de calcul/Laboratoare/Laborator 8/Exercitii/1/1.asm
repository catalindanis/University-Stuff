bits 32

global start

extern exit, scanf, printf
import exit msvcrt.dll
import scanf msvcrt.dll
import printf msvcrt.dll

segment data use32 class=data

a dd 0
b dd 0
rezultat db 0 
format_mesaj_citire db "Introduceti un numar : ", 0
format_citire db "%d", 0
format_afisare db "Produsul este : %d", 0

segment code use32 class=code
start:
    ;printf(format_mesaj_citire)
    push dword format_mesaj_citire
    call [printf]
    add esp, 4 * 1

    ;scanf(format_citire, &a)
    push dword a
    push dword format_citire
    call [scanf]
    add esp, 4 * 2
    
    ;printf(format_mesaj_citire)
    push dword format_mesaj_citire
    call [printf]
    add esp, 4 * 1

    ;scanf(format_citire, &b)
    push dword b
    push dword format_citire
    call [scanf]
    add esp, 4 * 2
    
    mov eax, [a]
    imul dword [b]
    
    ;printf(format_afisare, a * b)
    push edx
    push eax
    push format_afisare
    call [printf]
    add esp, 4 * 3
    
    
    push dword 0
    call [exit]