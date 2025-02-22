; (a + b + c) - d + (b - c) Interpretare cu semn

bits 32
global start

extern exit
import exit msvcrt.dll

segment data use32 class = data
    a db 0FFh              ; byte
    b dw 1122h             ; word
    c dd 11223344h         ; dword
    d dq 1122334455667788h ; qword
    
segment code use32 class = code
    start:
        
        mov al, [a]
        cbw
        add ax, [b]
        cwde
        cdq       ; pentru a putea folosi OF cand facem (a + b + c) - d 
        add eax, [c]
        
        sub eax, [d]
        sbb edx, [d + 4]  ; edx:eax <- (a + b + c) - d
        mov [d], eax
        mov [d + 4], edx  ; d <- edx:eax = (a + b + c) - d
        
        mov ax, [b]
        cwde
        sub eax, [c]      ; eax <- b - c
        
        cdq
        add eax, [d]
        adc edx, [d + 4]  ; edx:eax <- (a + b + c) - d + (b - c)
        
        push dword 0
        call [exit]