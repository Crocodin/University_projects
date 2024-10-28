;Se da dublucuvantul M. Sa se obtina dublucuvantul MNew astfel:
;bitii 0-3 a lui MNew sunt identici cu bitii 5-8 a lui M
;bitii 4-7 a lui MNew au valoarea 1
;bitii 27-31 a lui MNew au valoarea 0
;bitii 8-26 din MNew sunt identici cu bitii 8-26 a lui M.

bits 32
global start

extern exit
import exit msvcrt.dll

segment data use32 class = data
    M dd 9AF10981h         ; 1001.10101111000100001001.1000.0001
    MNew resd 1
    
segment code use32 class = code
    start:
        mov eax, [M]
        and eax, 000001E0h
        ror eax, byte 5
        mov [MNew], eax      
        ; 000000000000000000000000.0000.1100
        ; bitii 0-3 a lui MNew sunt identici cu bitii 5-8 a lui M
        
        mov eax, 000000F0h
        or [MNew], eax
        ; 000000000000000000000000.1111.1100
        ; bitii 4-7 a lui MNew au valoarea 1
        
        ; 0000.0000.0000.0000.0000.0000.1111.1100
        ;      ^ pana aici au 0
        ; bitii 27-31 a lui MNew au valoarea 0
        
        mov eax, [M]
        and eax, 07FFFF00h
        
        or [MNew], eax
        ; 0000.0010.1111.0001.0000.1001.1111.1100 := 02F109FC
        ;bitii 8-26 din MNew sunt identici cu bitii 8-26 a lui M
        
        push dword 0
        call [exit]