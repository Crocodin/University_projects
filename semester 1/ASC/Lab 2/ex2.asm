;(a-b-b-c)+(a-c-c-d)

bits 32 

global start        

extern exit               
import exit msvcrt.dll    

segment data use32 class=data
    a db 10
    b db 5
    c db 2
    d db 3

segment code use32 class=code
    start:
        mov al, [a]    ; al <- a
        sub al, [b]    ; al <- a - b
        sub al, [b]    ; al <- a - b - b 
        sub al, [c]    ; al <- a - b - b - c
        
        mov bl, al     ; bl <- a - b - b - c
        
        mov al, [a]    ; al <- a
        sub al, [c]    ; al <- a - c
        sub al, [c]    ; al <- a - c - c
        sub al, [d]    ; al <- a - c - c - d
        
        add bl, al     ; bl <- (a-b-b-c)+(a-c-c-d)

        push    dword 0      
        call    [exit]       
