bits 32

global _construire_sir_impare
global _construire_sir_pare

segment data use32 class=data

segment code use32 class=code
_construire_sir_pare:
    pushad
    push ebp
    mov ebp, esp
    
    cld
    mov ecx, [ebp + 8 + 8 * 4]
    jecxz final_pare
    mov esi, [ebp + 12 + 8 * 4]
    mov edx, [ebp + 16 + 8 * 4]
    mov edi, [ebp + 20 + 8 * 4]
    repeta_pare:
        lodsd
        test eax, 1
        jnz impar
        stosd
        inc dword[edx]
        impar:
    loop repeta_pare
    final_pare:
    
    mov esp, ebp
    pop ebp
    popad
    ret
    
_construire_sir_impare:
    pushad
    push ebp
    mov ebp, esp
    
    cld
    mov ecx, [ebp + 8 + 8 * 4]
    jecxz final_impare
    mov esi, [ebp + 12 + 8 * 4]
    mov edx, [ebp + 16 + 8 * 4]
    mov edi, [ebp + 20 + 8 * 4]
    repeta_impare:
        lodsd
        test eax, 1
        jz par
        stosd
        inc dword[edx]
        par:
    loop repeta_impare
    final_impare:
    
    mov esp, ebp
    pop ebp
    popad
    ret