;(a-c)+(b-d)

bits 32 
global start        

extern exit               
import exit msvcrt.dll    

segment data use32 class=data
a dw 256
b dw 400
c dw 750
d dw 120

segment code use32 class=code
    start:
        mov ax, [a]   ; ax <- a
        sub ax, [c]   ; ax <- ax - c (a - c)
        mov [a], ax   ; a <- ax (a - c)
        
        mov ax, [b]   ; ax <- b
        sub ax, [d]   ; ax <- ax - d
        mov [b], ax   ; b <- (b - d)
        
        mov ax, [a]   ; ax <- a (a - c)
        add ax, [b]   ; ax <- ax - b ((a-c)+(b-d))
        
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
