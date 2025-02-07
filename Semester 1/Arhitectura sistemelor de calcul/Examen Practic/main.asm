bits 32 

global start        

extern exit, fopen, fclose, fprintf, fscanf
import exit msvcrt.dll   
import fopen msvcrt.dll
import fclose msvcrt.dll
import fprintf msvcrt.dll
import fscanf msvcrt.dll

segment data use32 class=data

sir db "abcdefgh"
nume_fisier db "input.txt", 0
mod_acces_input db "r", 0
descriptor dd 0
format_citire db "%d", 0
n resd 1
nume_fisier_iesire db "output-i.txt", 0
mod_acces_output db "w", 0
descriptor_temporar resd 1
format_afisare db "%s", 0
caracter db 0, 0

;Se da in data segment un sir de exact 8 caractere si numele unui fisier. 
;Fisierul dat contine un numar de la 0 la 7. Sa se citeasca acel numar (fie n numarul citit). 
;Sa se creeze n fisiere, fiecare avand numele output-i.txt, unde i=0,n. 
;Sa se scrie in fiecare fisier ultimele (i+1) caractere din sirul dat in ordine inversa.

segment code use32 class=code
    start:
        ;fopen(nume_fisier, mod_acces_input)
        push dword mod_acces_input
        push dword nume_fisier
        call [fopen]
        add esp, 4 * 2
        
        cmp eax, 0
        je eroare_deschidere_fisier
        
        mov [descriptor], eax
        
        ;fscanf(descriptor, format_citire, n)
        push dword n
        push dword format_citire
        push dword [descriptor]
        call [fscanf]
        add esp, 4 * 3
        
        mov ecx, 0
        cmp ecx, [n]
        jg final
   
        repeta:
            mov ebx, ecx
            add bl, '0'
            mov [nume_fisier_iesire + 7], bl
            mov ebx, ecx
            push ecx
            
            ;fopen(nume_fisier_iesire, mod_acces_output)
            push dword mod_acces_output
            push dword nume_fisier_iesire
            call [fopen]
            add esp, 4 * 2
            
            cmp eax, 0
            je eroare_deschidere_fisier_output
            
            mov [descriptor_temporar], eax
            
            mov ecx, ebx
            add ecx, 1
            mov esi, sir + 7
            
            iterare:
                push ecx
                std
                lodsb
                cld
                
                mov [caracter], al
                
                ;fprintf(descriptor_temporar, format_afisare, caracter)
                push dword caracter
                push dword format_afisare
                push dword [descriptor_temporar]
                call [fprintf]
                add esp, 4 * 3
                
                pop ecx
            loop iterare
            
            ;fclose(descriptor_temporar)
            push dword [descriptor_temporar]
            call [fclose]
            add esp, 4 * 1
            
            eroare_deschidere_fisier_output:
            
            pop ecx
            inc cl
            cmp cl, [n]
        jle repeta
        
   
        final:
   
        ;fclose(descriptor)
        push dword [descriptor]
        call [fclose]
        add esp, 4 * 1
        
        eroare_deschidere_fisier:
        push    dword 0     
        call    [exit]       
