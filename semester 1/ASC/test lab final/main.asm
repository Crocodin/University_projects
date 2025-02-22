
bits 32

global start

extern exit, scanf, fopen, fscanf, fprintf, fclose
import exit msvcrt.dll
import scanf msvcrt.dll
import fopen msvcrt.dll
import fscanf msvcrt.dll
import fprintf msvcrt.dll
import fclose msvcrt.dll

segment data use32 class = data
    format_string db '%s', 0         ; s- pentru cuvine
    format_numar db '%d', 0          ; d - pentru int-uri
    
    fisier_afisare db 'output.txt', 0
    mod_acces_afisrae db "w", 0      ; w - pentru scriere. daca fiserul nu exista, se va crea 
    descriptor_fis_afis dd -1
    
    nume_fisier times 20 db 0        ; fisierul este de maxim 20 caractere
    mod_acces db 'r', 0              ; r - pentru citire
    descriptor_fis dd -1
    
    caracter_special dd 0            ; daca nr de litere este mai mic decat n, vom completa cu cs 
    n_numar dd 0                     ; numarul de litere din fiecare cuvant, pe care le vom afisa
    
    text times 100 db 0              ;
    cuvant times 100 db 0
    
segment code use32 class = code
    start:
        CLD
        
        push dword nume_fisier       ; citim numele fisierului
        push dword format_string
        call [scanf]
        add esp, 4 * 2               ; eliberam stiva
        
        push dword caracter_special  ; citim caracterul special
        push dword format_string
        call [scanf]
        add esp, 4 * 2
        
        push dword n_numar           ; citim numarul
        push dword format_numar
        call [scanf]
        add esp, 4 * 2
        
        ; eax = fopen(nume_fisier, mod_acces)
        push dword mod_acces     
        push dword nume_fisier
        call [fopen]
        add esp, 4 * 2               
        
        mov [descriptor_fis], eax    ; salvam valoarea returnata de fopen in variabila descriptor_fis
        
        ; verificam daca functia fopen a deschis cu succes fisierul (daca EAX != 0)
        cmp eax, 0
        je .final
        
        ; deschidem fisierul de afisare 
        push dword mod_acces_afisrae     
        push dword fisier_afisare
        call [fopen]
        add esp, 4 * 2               
        
        mov [descriptor_fis_afis], eax    ; salvam valoarea returnata de fopen in variabila descriptor_fis
        
        cmp eax, 0
        je .final
        
        ; fscanf(descriptor_fis, format, text)
        .repet:
            push dword text
            push dword format_string
            push dword [descriptor_fis]
            call [fscanf]
            add esp, 4 * 3
            
            cmp EAX, -1                 ;
            je .final
            
            mov ECX, [n_numar]
            mov ESI, text
            
            .afisare:
                cmp byte [esi], 0         ; daca cuvantul s-a terminat
                jne .next
                mov byte[esi], '+'
                .next:
                inc ESI
            loop .afisare
            
            mov byte[ESI], ' '             ; caracterul spatiu
            inc ESI
            mov byte[ESI], 0               ; finalul noului cuvant
            
            ; fprintf(descriptor_fis, text)
            push dword text
            push dword [descriptor_fis_afis]
            call [fprintf]
            add esp, 4 * 2
            
            mov EDI, text
            mov ECX, 100
            mov al, 0
            repe stosb 
            
        jmp .repet
            
        .final:
        
        ; fclose(descriptor_fis)
        push dword [descriptor_fis]
        call [fclose]
        add esp, 4
        
        push dword [descriptor_fis_afis]
        call [fclose]
        add esp, 4
        
        push dword 0
        call [exit]

