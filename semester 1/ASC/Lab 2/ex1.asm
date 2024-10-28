;256/1

bits 32 
global start

extern exit
import exit msvcrt.dll  

segment data use32 class=data
    a dw 256
    b db 1

segment code use32 class=code
    start:
        mov ax, [a]
        div BYTE [b]
    
        push dword 0   
        call [exit] 