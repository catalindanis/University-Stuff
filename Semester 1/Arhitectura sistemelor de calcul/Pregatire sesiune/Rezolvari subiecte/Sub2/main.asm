bits 32 

global start

extern exit, printf, scanf
import exit msvcrt.dll
import printf msvcrt.dll
import scanf msvcrt.dll

segment data use32 class=data

s dq 1122334455667788h, 99AABBCCDDEEFF11h, 0FF000000000000055h
  dq 0FFFEFFFFFFFFEFFFh, 450000A8000000ABh, 11113733555577ABh
lg equ $ - s
n dd 0
format_citire db "%d", 0 
format_afisare db "%xh, ", 0
sir_rez resb lg
sir_rez_biti resb lg
lungime dd 0
 
segment code use32 class=code
start:
    push dword n
    push dword format_citire
    call [scanf]
    add esp, 4 * 2
    
    mov ecx, lg
    cmp ecx, 0
    je final
    
    mov esi, s
    push sir_rez_biti
    mov edi, sir_rez
    cld
    repeta:
        lodsb
        
        mov bl, al
        mov dl, 0
        push ecx
        
        mov ecx, 8
        numara:
            shl bl, 1
            adc dl, 0
        loop numara
        pop ecx
        
        mov ebx, edi
        
        pop edi
        
        mov ah, al
        mov al, dl
        stosb
        inc dword[lungime]
        
        mov al, ah
        
        push edi
        
        mov edi, ebx
        
        stosb
        sub ecx, [n]
        add ecx, 1
        
        cmp ecx, 0
        jle gata
        push ecx
        
        mov ecx, [n]
        sub ecx, 1
        rep lodsb
        
        pop ecx
    loop repeta
    gata:
    
    mov esi, 0
    mov ecx, [lungime]
    cmp ecx, 1
    je afisare
    jl final
    sub ecx, 1
    
    outer_loop:
        mov edi, esi
        add edi, 1
        
        inner_loop:
            mov al, [sir_rez_biti + esi]
            mov bl, [sir_rez_biti + edi]
            cmp al, bl
            jge sortat
            
            mov [sir_rez_biti + esi], bl
            mov [sir_rez_biti + edi], al
            
            mov al, [sir_rez + esi]
            mov bl, [sir_rez + edi]
            
            mov [sir_rez + esi], bl
            mov [sir_rez + edi], al
        
            sortat:
            inc edi
            cmp edi, [lungime]
        jl inner_loop
        
        inc esi
    loop outer_loop
    afisare:
    
    mov ecx, [lungime]
    jecxz final
    
    mov eax, 0
    iterare:
        push ecx
        push eax
        
        mov ebx, 0
        mov bl, byte [sir_rez + eax]
        push ebx
        push dword format_afisare
        call [printf]
        add esp, 4 * 2
        
        ;mov ebx, 0
        ;mov bl, byte [sir_rez_biti + eax]
        ;push ebx
        ;push dword format_afisare
        ;call [printf]
        ;add esp, 4 * 2
        
        pop eax
        inc eax
        pop ecx
    loop iterare
    
    final:
    push dword 0
    call [exit]