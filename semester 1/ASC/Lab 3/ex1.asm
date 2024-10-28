;((a + b) + (a + c) + (b + c)) - d Interpretare fara semn

bits 32 
global start        

extern exit               
import exit msvcrt.dll   

segment data use32 class=data
    a db 11h                  ; a byte
    b dw 1122h                ; a word
    c dd 11223344h            ; a dword
    d dq 1122334455667788h    ; a qword

segment code use32 class=code
    start:
        mov ax, 0
        mov al, [a]           ; ax <- a (pot trata ca fiind un word)
        add ax, [b]           ; ax <- a + b
        
        mov edx, 0
        mov dx, [b]           ; edx <- b (pot trata ca fiind un dword)
        add edx, [c]          ; edx <- b + c
        
        mov [b], ax           ; b <- a + b
        
        mov eax, 0
        mov al, [a]           ; eax <- a (pot trata ca fiind un dword)
        add eax, [c]          ; eax <- a + c
        
        add eax, edx          ; eax <- (a + c) + (b + c)
        
        mov edx, 0
        mov dx, [b]
        add eax, edx          ; eax <- ((a + b) + (a + c) + (b + c))
        mov edx, 0
        
        sub eax, [d]
        sbb edx, [d + 4]      ; edx:eax <- ((a + b) + (a + c) + (b + c)) - d

        
        
        
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
