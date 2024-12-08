; Se citesc de la tastatura doua numere a si b. Sa se calculeze valoarea expresiei (a/b)*k, k fiind o constanta definita in segmentul de date. Afisati valoarea expresiei (in baza 2).

bits 32
global start

extern exit, printf, scanf               ; adaugam printf si scanf ca functii externe           
import exit msvcrt.dll     
import printf msvcrt.dll     
import scanf msvcrt.dll      

segment data use32 class = data
    a resd 1
    b resd 1
    k dd 10
    
    format_citire db '%d%d', 0       ; %d <=> un numar decimal (baza 10)
    format_afisare db '%s', 0
    
    sir_binar times 32 db '0'            ; 32 bits + 1 null terminator
        db 0
    
segment code use32 class = code
    
    ; convert_ax_in_eax() parametri in AX
    convert_ax_in_eax:
        push word 0
        push ax
        pop EAX                         ; EAX = 0 : AX
        ret

    start:
        ; scanf(format, a, b)
        push dword b
        push dword a
        push dword format_citire
        call [scanf]
        add ESP, 4 * 3                   ; resetam ESP cu 3 valori (a, b, formatul)
        
        ; (a/b)*k
        mov EAX, [a]
        mov EDX, dword 0
        div word [b]                     ; rezultatul este un word
        mul dword [k]                    ; rezultatul este un dword
        ; teoretic vorbind rezultatul se afla in EAX, acesta fiind un dword (presupunem ca nu avem overflow)
        
        mov EDX, dword 0
        
        mov EDI, sir_binar + 31          ; suntem pe ultima pozitit a lui sir_binar
        STD                              ; vrem sa punem resturile impartirii lui EAX = (a/b)*k in ordine inversa
        
        mov EBX, dword 2                  ; baza 2
        loopy:
            div EBX                      ; EDX:EAX = EDX:EAX / EBX
            xchg EDX, EAX                ; in AX avem restul
            add AL, '0'                  ; avem caractere in AL acum
            STOSB                       
            xchg EDX, EAX                ; EAX revine la valoarea initiala, la cat
            mov EDX, 0
            CMP EAX, 0
        JNE loopy
        
        CLD
        ; printf(format, val)
        push sir_binar
        push format_afisare
        call [printf]
        add ESP, 4 * 2
        
        push dword 0
        call [exit]
        add ESP, 4
        
        