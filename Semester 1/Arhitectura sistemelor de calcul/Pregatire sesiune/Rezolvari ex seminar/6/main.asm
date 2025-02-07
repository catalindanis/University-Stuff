bits 32

global start

extern exit
import exit msvcrt.dll

segment data use32 class=data

s db 5, 1, 3, 6, 2, 4
ls equ $-s

segment code use32 class=code
start:
    mov ecx, ls
    cmp ecx, 1
    jle final
    
    mov eax, 0
    outer_loop:
        mov ebx, eax
        add ebx, 1
        
        inner_loop:
            mov cl, [eax + s]
            mov dl, [ebx + s]
            cmp cl, dl
            jle sari
            
            mov [ebx + s], cl
            mov [eax + s], dl
            
            sari:
            inc ebx
            cmp ebx, ls-1
        jle inner_loop
        
        inc eax
        cmp eax, ls-1
    jl outer_loop
    
    final:
    push dword 0
    call [exit]