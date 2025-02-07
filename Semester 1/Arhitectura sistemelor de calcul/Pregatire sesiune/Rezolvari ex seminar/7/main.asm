bits 32

global start

extern exit, printf
import exit msvcrt.dll
import printf msvcrt.dll

segment data use32 class=data

s db "mihai ana cacac repede axa axaxa"
ls equ $ - s
resb 2
cuv_temp resb 100
cuv_temp_invers resb 100
lg_cuv_temp dd 0
format db "%s", 10, 0

segment code use32 class=code
start:
    mov byte[s + ls], ' '
    cld
    mov esi, s
    mov edi, cuv_temp
    repeta:
       lodsb
       cmp al, 0
       je final
       
       cmp al, ' '
       jne nu_e_spatiu
        
        push esi
        
        mov esi, cuv_temp
        mov edi, cuv_temp
        add edi, [lg_cuv_temp]
        sub edi, 1
        
        mov ecx, [lg_cuv_temp]
        jecxz nu_e_palindrom
        
        verifica:
            cld
            lodsb
            std
            scasb
            jne nu_e_palindrom
        loop verifica
        
        ;printf(format, cuv_temp)
        push dword cuv_temp
        push dword format
        call [printf]
        add esp, 4 * 2
        
        nu_e_palindrom:
        
        ;resetare cuvant_temporar
        mov dword[lg_cuv_temp], 0
        mov edi, cuv_temp
        
        mov ecx, 100
        curatare:
            mov byte[ecx + cuv_temp - 1], 0
        loop curatare
        
        pop esi
        
        jmp continua
       nu_e_spatiu:
       
        ;stocare caracter in cuvant_temporar
        stosb
        inc dword[lg_cuv_temp]
        
       continua:
       cld
    jmp repeta
    
    final:
    push dword 0
    call [exit]