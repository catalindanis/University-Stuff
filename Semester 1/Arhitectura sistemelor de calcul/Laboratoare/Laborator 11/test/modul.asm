bits 32

global construire_sir_pare
global construire_sir_impare

segment code use32 class=code public
construire_sir_pare:
    mov ecx, [esp+4]
    jecxz final_pare
    mov esi, [esp + 8]
    mov edx, [esp + 12]
    mov edi, [esp + 16]
    repeta_pare:
        lodsd
        test eax, 1
        jnz impar
        stosd
        inc dword[edx]
        impar:
    loop repeta_pare
    final_pare:
    ret
construire_sir_impare:
    mov ecx, [esp+4]
    jecxz final_impare
    mov esi, [esp + 8]
    mov edx, [esp + 12]
    mov edi, [esp + 16]
    repeta_impare:
        lodsd
        test eax, 1
        jz par
        stosd
        inc dword[edx]
        par:
    loop repeta_impare
    final_impare:
    ret
    