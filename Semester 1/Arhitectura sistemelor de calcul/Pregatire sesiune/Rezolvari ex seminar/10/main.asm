bits 32

global start

extern exit, scanf, printf
import exit msvcrt.dll
import scanf msvcrt.dll
import printf msvcrt.dll

segment data use32 class=data

n resd 1
sir resd 100
format_citire db "%d", 0
format_afisare db "%d ", 0
valoare resd 1

segment code use32 class=code
start:
    ;scanf(format_citire, &n)
    push dword n
    push dword format_citire
    call [scanf]
    add esp, 4 * 2
    
    cmp dword[n], 0
    je final
   
    mov ecx, [n]
    
    mov edi, sir
    citeste:
        push ecx
        
        ;scanf(format_citire, &valoare)
        push dword valoare
        push dword format_citire
        call [scanf]
        add esp, 4 * 2
        
        mov eax, [valoare]
        stosd
        
        pop ecx
    loop citeste
    
    mov ecx, [n]
    
    cmp ecx, 1
    jle sortat
    sub ecx, 1
    
    mov eax, 0
    outer_loop:
        push ecx
        
        mov ebx, eax
        add ebx, 1
        
        inner_loop:
            mov edx, [eax * 4 + sir]
            mov ecx, [ebx * 4 + sir]
            cmp edx, ecx
            jle e_ok
            
            mov [eax * 4 + sir], ecx
            mov [ebx * 4 + sir], edx
            
            e_ok:
            inc ebx
            cmp ebx, [n]
        jl inner_loop
        
        inc eax
        pop ecx
    loop outer_loop
    
    sortat:
    mov ecx, [n]
    mov eax, 0
    afisare:
        push ecx
        push eax
        
        ;printf(format_afisare, sir[eax])
        push dword [sir + eax * 4]
        push dword format_afisare
        call [printf]
        add esp, 4 * 2
        
        pop eax
        inc eax
        pop ecx
    loop afisare
    
    final:
    push dword 0
    call [exit]