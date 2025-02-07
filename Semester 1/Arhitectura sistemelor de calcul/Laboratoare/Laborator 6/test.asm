bits 32

extern exit
import exit msvcrt.dll

global start

segment data use32 class=data

s dw 2, 1
ls equ $-s
lc db 0
incmax db 0
lmax db 1

segment code use32 class=code
start:
    ;Dandu-se un sir de cuvinte,
    ;sa se calculeze cea mai lunga 
    ;secventa de cuvinte ordonate crescator din acest sir.
    mov ecx, 0 ; ecx reprezinta pozitia de unde incepe secventa curenta
    cmp ecx, ls ; compar ecx cu ls inainte de a executa un set de pasi
    jge final ; daca lungimea sirului este <=0 atunci sar direct la finalul programului
    cmp ecx, 2 ; daca lungimea sirului este chiar 1, atunci stim rezultatul
    je lungime_1
    reindexare:
        mov esi, s ; punem in sirul sursa sirul s
        add esi, ecx ; setam pozitia de la care incepem cautarea
        mov ebx, ecx ; folosim ebx ca index in bucla interioara
        cmp ebx, ls-2 ; comparam initial indexul curent cu lungimea sirului
        jge sari ; daca avem doar 1 element in subsirul curent nu are rost sa verificam conditia
        mov byte[lc], 1
        verificare_conditie:
            lodsw ; AX <- s[ecx]
            mov DX, AX ; DX <- s[ecx]
            lodsw ; AX <- s[ecx+1]
            sub ESI, 2 
            cmp DX, AX ; compar s[ecx] cu s[ecx+1]
            jg iesire ; daca nu respecta conditia => ies din bucla
            inc byte[lc] ; incrementez lungimea curenta a secventei
            add ebx, 2 ; incrementez indicele din bucla interioara
            cmp ebx, ls-2 ; verific conditia din bucla interioara
        jl verificare_conditie
        iesire:
        mov al, [lc] ; pun in al lungimea curenta a secventei
        cmp al, [lmax] ; compar lungimea curenta a secventei cu lungimea maxima de pana acum
        jle sari ; daca lungimea curenta e mai mica, nu ne intereseaza
                 ; in caz contrar, actualizez variabilele de maxim cu valorile curente
        mov [lmax], al
        mov [incmax], cl
        sari:
        add ecx, 2 ; crestem din 2 in 2 (lucram pe word)
        cmp ecx, ls ; eticheta reindexare se repeta doar cat timp ecx < ls
    jl reindexare
    mov eax, 0 ; zerorizez eax
    mov al, [incmax] ; pun in eax indicele de start a secventei maxime
    mov cl, 1 ; initializez cl pentru a-l putea folosi la shiftarea spre dreapta
    shr eax, cl ; impart cu 2 valoarea din eax
    mov ecx, 0 ; zerorizez ecx
    mov ecx, [lmax] ; pun in ecx lungimea maxima
    jmp final ; daca am ajuns aici trebuie sa sarim la final pentru a nu executa
              ; cazul sirului de lungime 1
    lungime_1: ; daca lungimea sirului este 1 => cea mai lunga secventa este chiar elementul
        mov eax, 0
        mov ecx, 1
    final:
    push dword 0
    call [exit]