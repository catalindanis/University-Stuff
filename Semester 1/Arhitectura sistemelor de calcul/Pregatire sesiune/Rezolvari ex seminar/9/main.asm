bits 32

global start

extern exit, printf, scanf
import exit msvcrt.dll
import printf msvcrt.dll
import scanf msvcrt.dll

segment data use32 class=data

n resd 1
format db "%d", 0

segment code use32 class=code
start:
    ;scanf(format, &n)
    push dword n
    push dword format
    call [scanf]
    add esp, 4 * 2
    
    cmp dword[n], 0
    jge e_pozitiv
    
    NEG dword[n] ; n = 0 - n
    
    e_pozitiv:
    
    mov ecx, 0 ; nr cifre
    mov eax, [n]
    repeta:
        mov edx, 0
        mov ebx, 10
        div ebx
        inc ecx
        cmp eax, 0
    jne repeta
    
    ;printf(format, ecx)
    push ecx
    push dword format
    call [printf]
    add esp, 4 * 2
    
    push dword 0
    call [exit]