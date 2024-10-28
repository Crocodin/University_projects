; a-(7+x)/(b*b-c/d+2); a-doubleword; b,c,d-byte; x-qword  //   interpretare cu semn

bits 32
global start

extern exit
import exit msvcrt.dll

segment data use32 class = data:
    a dd -1FA32h
    b db 11h
    c db -1h
    d db 3h
    x dq -4F2A112233h
    
segment code use32 class = code:
    start:
        mov al, [b]
        imul al           ; ax <- b*b
        
        push ax
        
        mov al, [c]       
        cbw               ; ax <- c
        idiv [d]          ; ax <- c/d
        add ax, byte 2    ; ax <- c/d+2
        
        pop dx
        sub dx, ax        ; dx <- b*b-c/d+2
        
        mov ax, dx
        cwde
        mov ebx, eax      ; ebx <- b*b-c/d+2
        
        mov eax, [x]
        mov edx, [x + 4]
        add eax, 7
        adc edx, 0        ; edx:eax <- 7+x
        
        idiv ebx          ; eax <- (7+x)/(b*b-c/d+2)
        
        mov edx, [a]
        sub edx, eax      ; edx <- a-(7+x)/(b*b-c/d+2)
    
        push dword 0
        call [exit]