bits 32

global start

extern exit
import exit msvcrt.dll

segment data use32 class=data

A dq 8877665544332211h
N resb 1
B resd 1
C resb 1

segment code use32 class=code
start:
    ;Se da quadwordul A. Sa se obtina numarul intreg N reprezentat de bitii 35-37 ai lui A.
    ;Sa se obtina apoi in B dublucuvantul rezultat prin rotirea spre dreapta 
    ;a dublucuvantului inferior al lui A cu N pozitii. Sa se obtina octetul C astfel:
    ;bitii 0-3 ai lui C sunt bitii 8-11 ai lui B
    ;bitii 4-7 ai lui C sunt bitii 16-19 ai lui B
    
    mov EAX, [A+4] ; EAX <- dword-ul high din A
    ; EAX: 32, 33, 34, 35, 36, 37 ..., 62, 63 (bitii din A)
    SHR EAX, 3 ; EAX: 35, 36, 37, ..., 62, 63, ... (bitii din A) 
    AND EAX, 111b ; EAX: 35, 36, 37 (bitii din A)
    mov [N], EAX; ; N <- numarul intreg N reprezentat de bitii 35, 36, 37 ai lui A
    
    mov EBX, [A] ; EBX <- dword-ul low din A   
    mov CL, [N] ; CL <- N
    ror EBX, CL ; EBX <- dword-ul low din A rotit spre dreapta de N ori
    mov [B], EBX ; B <- dword-ul low din A rotit spre dreapta de N ori
    
    mov CL, [B+1] ; CL <- bitii 8, 9, 10, ... , 15 din B
    AND CL, 1111b ; CL <- bitii 8, 9, 10, 11 din B
    mov DL, [B+2] ; BL <- bitii 16, 17, ... , 23 din B
    AND DL, 1111b ; DL <- bitii 16, 17, 18, 19 din B
    SHL DL, 4 ; mutarea celor 4 valori de pe bitii 0, 1, 2, 3 pe 4, 5, 6, 7 in DL
    OR CL, DL ; CL <- bitii 8, 9, 10, 11, 16, 17, 18, 19 din B
    mov [C], CL ; C <- bitii 8, 9, 10, 11, 16, 17, 18, 19 din B
    
    push dword 0
    call [exit]