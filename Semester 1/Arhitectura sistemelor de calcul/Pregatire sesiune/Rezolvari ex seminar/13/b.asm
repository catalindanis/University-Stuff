bits 32

global selectie

segment data use32 class=data

lungime dd 0
gasit db 0
valoare_curenta db 0

segment code use32 class=code
selectie:
    mov ebx, 0
    mov esi, [esp + 4]
    mov edi, esi
    mov ecx, [esp + 8]
    mov [lungime], ecx
    
    jecxz final
    
    mov ecx, 0
    cld
    repeta:
        lodsb
        
        cmp al, 0
        jl sari
        
        test al, 1
        jnz sari
        
        mov byte[gasit], 0
        mov edx, 0
        parcurge:
            cmp al, byte[edi + edx]
            jne continua
            mov byte[gasit], 1
            
            continua:
            inc edx
            cmp edx, ecx
        jl parcurge
        
        cmp byte[gasit], 0
        jnz sari
        
        movzx eax, al
        add ebx, eax
        
        sari:
        inc ecx
        cmp ecx, [lungime]
    jl repeta
    
    final:
    mov eax, ebx
    ret