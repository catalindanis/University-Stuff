bits 32

global start

extern exit, printf, procesare
import exit msvcrt.dll
import printf msvcrt.dll

segment data use32 class=data

a db ' '
s db "Ana are mere Ada are fructul pasiunii Gigel are ananas "
  db "Tudor are portocale si facem salata de fructe", 0
nr_secv dw 0  
format_afisare db "%c", 0
afisare_spatiu db " "
afisare_new_line db 13, 10, 0

tabela db "0123456789ABCDEF"
nr_baza_16 resb 100
 
segment code use32 class=code
start:
    mov ecx, 0
    mov esi, a
    cld
    repeta:
        lodsb
        cmp al, ' '
        jne sari
        
        push esi
        
        ;procesare(esi)
        push esi
        call procesare
        add esp, 4 * 1
        
        pop esi
        
        cmp eax, 0
        je repeta
        
        inc word [nr_secv]
        push esi
        
        mov eax, 0
        afisare:
            lodsb
            cmp al, ' '
            je gata
            
            cmp al, 0
            je gata
            
            push eax
            ;printf(format_afisare, al)
            push eax
            push format_afisare
            call [printf]
            add esp, 4 * 2
            
            pop eax
        jmp afisare
        gata:
        
        push eax
        
        ;printf(format_afisare, spatiu)
        push dword[afisare_spatiu]
        push format_afisare
        call [printf]
        add esp, 4 * 2
        
        pop eax
     
        pop esi
        
        sari:
        cmp al, 0
        je final
    jmp repeta
    final:
    
    push afisare_new_line
    call [printf]
    add esp, 4 * 1
    
    mov ebx, tabela
    mov edi, nr_baza_16
    mov ecx, 0
    
    mov ax, [nr_secv]
    impartire:
        mov dl, 16
        div dl
        inc ecx
        
        movzx dx, al
        
        mov al, ah
        xlat ; AL <- EBX[AL]
        
        stosb
        
        mov ax, dx
        cmp ax, 0
        je gata1
    jmp impartire
    gata1:    
     
    std
    mov esi, edi
    sub esi, 1
    
    afisare1:
        push ecx
        
        lodsb
        
        cld
        push eax
        push dword format_afisare
        call [printf]
        add esp, 4 * 2
        std
        
        pop ecx
    loop afisare1
    
    push dword 0
    call [exit]