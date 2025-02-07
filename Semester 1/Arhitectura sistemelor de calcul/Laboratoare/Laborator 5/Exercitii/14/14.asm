bits 32

extern exit
import exit msvcrt.dll

global start

segment data use32 class=data

s db 1, 3, -2, -5, 3, -8, 5, 0
ls equ $ - s
d1 times ls db 0
d2 times ls db 0

segment code use32 class=code
start:
    mov edi, ls
    mov esi, 0
    mov ebx, 0
    mov ecx, 0
    cmp edi, esi
    je endForLoop
    forLoop:
        mov al, [s+esi]
        cmp al, 0
        jge adaugarePozitiv
        adaugareNegativ:
            mov [d2+ecx], al
            inc ecx
            jmp finalAdaugare
        adaugarePozitiv:
            mov [d1+ebx], al
            inc ebx
        finalAdaugare:
        inc esi
        cmp edi, esi
    jne forLoop
    endForLoop:
    push dword 0
    call [exit]