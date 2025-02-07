bits 32 ; assembling for the 32 bits architecture

global start        

extern exit, gets, printf
import exit msvcrt.dll    
import gets msvcrt.dll        
import printf msvcrt.dll

segment data use32 class=data

sir resb 100
format_citire db "%s", 0

segment code use32 class=code
start:
    ;gets(sir)
    push dword sir
    call [gets]
    add esp, 4 * 1
    
    mov eax, 0
    repeta:
        mov bl, [sir + eax]
        cmp bl, 0
        je gata
        
        cmp bl, 'z'
        ja caracter_special
        
        cmp bl, 'a'
        jae nu_e_caracter_special
        
        cmp bl, 'Z'
        ja caracter_special
        
        cmp bl, 'A'
        jae nu_e_caracter_special
        
        cmp bl, '9'
        ja caracter_special
        
        cmp bl, '0'
        jae nu_e_caracter_special
        
        caracter_special:
        mov byte[sir + eax], 'X'
        
        nu_e_caracter_special:
        inc eax
    jmp repeta
    gata:
    ;printf(format_citire, sir)
    push dword sir
    push dword format_citire
    call [printf]
    add esp, 4 * 2
    
    push dword 0      
    call [exit]      
