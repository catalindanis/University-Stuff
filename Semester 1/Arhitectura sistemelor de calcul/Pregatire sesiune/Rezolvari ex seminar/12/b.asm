bits 32

global procesare

segment data use32 class=data

segment code use32 class=code
procesare:
    mov esi, [esp + 4] ; secventa
    mov ecx, 0 ; numar vocale
    mov edx, 0 ; numar consoane
    repeta:
        lodsb
        cmp al, ' '
        je gata
        
        cmp al, 0
        je gata
        
        cmp al, 'a'
        je vocala
        
        cmp al, 'A'
        je vocala
        
        cmp al, 'e'
        je vocala
        
        cmp al, 'E'
        je vocala
        
        cmp al, 'i'
        je vocala
        
        cmp al, 'I'
        je vocala
        
        cmp al, 'o'
        je vocala
        
        cmp al, 'O'
        je vocala
        
        cmp al, 'u'
        je vocala
        
        cmp al, 'U'
        je vocala
        
        inc edx
        jmp repeta
        
        vocala:
        inc ecx
        
    jmp repeta
    gata:
    
    test ecx, 1
    jnz nu_respecta
    
    test edx, 1
    jz nu_respecta
    
    mov eax, 1
    jmp final
    
    nu_respecta:
    mov eax, 0
    
    final:
    ret