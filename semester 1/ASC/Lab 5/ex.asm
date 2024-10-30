; Se dau 2 siruri de octeti A si B. Sa se construiasca sirul R care sa contina elementele lui B in ordine inversa urmate de elementele in ordine inversa ale lui A

bits 32
global start

extern exit
import exit msvcrt.dll

segment data use32 class = data
    A db 2, 1, -3, 0
    lA equ $ - A
    B db 4, 5, 7, 6, 2, 1
    lB equ $ - B
    R resb $ - A
    
segment code use32 class = code
    start:
        mov esi, A + lA - 1
        mov edi, R
        mov ecx, lA
        jecxz final_1
        _loop_1:
            mov al, [esi]
            mov [edi], al
            sub esi, 1
            add edi, 1
        loop _loop_1
        final_1:
        
        mov esi, B + lB - 1
        mov ecx, lB
        jecxz final_2
        _loop_2:
            mov al, [esi]
            mov [edi], al
            sub esi, 1
            add edi, 1
        loop _loop_2
        final_2:
        
        push dword 0
        call [exit]
    