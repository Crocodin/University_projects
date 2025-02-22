;(10*a-5*b)+(d-5*c)

bits 32 
global start

extern exit               
import exit msvcrt.dll   


segment data use32 class=data
    a db 32h
    b db 4Dh
    c db 80h
    d dw 1c87h
    calc db 5
    
segment code use32 class=code
    start:
    
        mov al, [b]
        mul byte [calc]       ; ax <- al * clacl (b * 5)
        
        mov [calc], byte 10   ; calc <- 10
        mov dx, ax            ; dx <- ax
                
        mov al, [a]
        mul byte [calc]       ; al <- al * calc (10 * a) 
        
        sub ax, dx            ; ax <- ax - dx (10*a-5*b) 
        
        mov dx, ax            ; dx <- ax (10*a-5*b)
        
        mov al, [c]
        mul byte [calc]       ; ax <- al * calc (5 * c)
        mov bx, ax            ; bx <- ax (c * 5)
        
        mov ax, [d]
        sub ax, bx            ; ax <- (d-5*c)
        
        add ax, dx            ;(10*a-5*b)+(d-5*c)
    
        push    dword 0     
        call    [exit]
