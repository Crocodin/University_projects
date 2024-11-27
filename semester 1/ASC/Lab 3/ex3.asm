; a-(7+x)/(b*b-c/d+2); a-doubleword; b,c,d-byte; x-qword

bits 32
global start

extern exit
import exit msvcrt.dll

segment data use32 class=data
    a dd 11223344h
    b db 11h
    c db 11h
    d db 11h
    x dq 88h
    
segment code use32 class=code
    start:
        mov ax, 0
        mov al, [c]     ; ax <- c
        div byte [d]    ; al <- c/d
        
        mov [d], al
        mov al, [b]
        mul al          ; ax <- b*b
        
        mov bx, 0
        mov bl, [d]     ; ax <- c/d
        sub ax, bx      ; ax <- b*b-c/d
        add ax, 2       ; ax <- b*b-c/d+2
        push ax
        ; --// striva //--
        ;    ax
        
        mov eax, [x]
        mov edx, [x + 4]; edx:eax <- x
        add eax, 7
        adc edx, 0      ; edx:eax <- 7+x
        
        mov ebx, 0
        pop bx          ; ebx <- b*b-c/d+2
        
        div ebx         ; eax <- (7+x)/(b*b-c/d+2)
        mov ebx, a      ; ebx <- a
        sub ebx, eax    ; a <- a-(7+x)/(b*b-c/d+2)
        
        push dword 0
        call [exit]
        
        