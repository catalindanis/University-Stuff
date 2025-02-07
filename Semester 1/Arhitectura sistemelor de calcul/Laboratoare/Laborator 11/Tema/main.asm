bits 32

extern exit, fscanf, printf, fopen, fclose
extern construire_sir_pare, construire_sir_impare
import exit msvcrt.dll
import fscanf msvcrt.dll
import printf msvcrt.dll
import fopen msvcrt.dll
import fclose msvcrt.dll

global start

segment data use32 class=data public

file_name db "date.txt", 0 
access_mode db "r", 0
descriptor dd 0
format db "%d", 0
format_afisare db "%d ", 0
urmatoarea_linie db `\n`, 0
n dd 0

sir times 100 dd 0
lg_sir dd 0

sir_pare times 100 dd 0
lg_sir_pare dd 0

sir_impare times 100 dd 0
lg_sir_impare dd 0

segment code use32 class=code public
start:
    push dword access_mode
    push dword file_name
    call [fopen]
    add esp, 4 * 2
    
    cmp eax, 0
    je final
    mov [descriptor], eax
    
    mov edi, sir
    repeta:
        push n
        push dword format
        push dword [descriptor]
        call [fscanf]
        add esp, 4 * 3
        
        cmp eax, -1
        je final_citire
        
        mov eax, [n]
        inc dword[lg_sir]
        stosd
        jmp repeta    
    final_citire:
    
    ;construire_sir_pare(n, sir, &n_pare, sir_pare)
    push dword sir_pare
    push dword lg_sir_pare
    push dword sir
    push dword [lg_sir]
    call construire_sir_pare
    add esp, 4 * 4
    
    ;construire_sir_impare(n, sir, &n_impare, sir_impare)
    push dword sir_impare
    push dword lg_sir_impare
    push dword sir
    push dword [lg_sir]
    call construire_sir_impare
    add esp, 4 * 4
    
    mov ecx, [lg_sir_pare]
    jecxz sari_afisare_pare
    mov esi, sir_pare
    afisarePare:
        push ecx
        lodsd
        push dword eax
        push dword format_afisare
        call [printf]
        add esp, 4 * 2
        pop ecx
    loop afisarePare
    sari_afisare_pare:
    
    push dword urmatoarea_linie
    call [printf]
    add esp, 4 * 1
    
    mov ecx, [lg_sir_impare]
    jecxz sari_afisare_impare
    mov esi, sir_impare
    afisareImpare:
        push ecx
        lodsd
        push dword eax
        push dword format_afisare
        call [printf]
        add esp, 4 * 2
        pop ecx
    loop afisareImpare
    sari_afisare_impare:
    
    final:
    push dword 0
    call [exit]