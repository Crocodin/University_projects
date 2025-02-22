;[(a-d)+b]*2/c

bits 32 
global start        

extern exit            
import exit msvcrt.dll    

segment data use32 class=data
a db 4h
b db 55h
c db 8ah
d db 3h

segment code use32 class=code
    start:
        
        mov al, [a]       ; al <- a
        sub al, [d]       ; al (a) <- al (a) - d
        add al, [b]       ; al <- al + b   ==  (a-d)+b
        
        mov [a], byte 2   ; a <- 2
        mul byte [a]      ; al <- al * 2   ==  [(a-d)+b]*2
        div byte [c]      ; ax <- ax / c   ==  [(a-d)+b]*2/c

        push    dword 0      
        call    [exit]       
