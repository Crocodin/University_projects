;Se dau un nume de fisier si un text (definite in segmentul de date). Textul contine litere mici, litere mari, cifre si caractere speciale. Sa se inlocuiasca toate CIFRELE din textul dat cu caracterul 'C'. Sa se creeze un fisier cu numele dat si sa se scrie textul obtinut prin inlocuire in fisier.

bits 32
global start

; declare external functions needed by our program
extern exit, fopen, fprintf, fclose
import exit msvcrt.dll  
import fopen msvcrt.dll           ; deschiderea fisierului
import fprintf msvcrt.dll         ; scriere in fisier
import fclose msvcrt.dll          ; inchiderea fisierului 

segment data use32 class = data
    nume_fisier db "file.txt", 0  ; numele fisierului care va fi creat
    mod_acces db "w", 0           ; w - pentru scriere. daca fiserul nu exista, se va crea                                   
    descriptor_fis dd -1
    text db 'In anul 2024, pe data de 15 aprilie, la ora 14:30, Mihai a parcurs 12.345 de pasi pe munte, urcand pana la altitudinea de 2.789 metri.', 0
                                  ; textul pe care in vom modifica
    comper_number dw '9' + 1      ; '9' + 1
    comper_space dw ' '           ; is space
    new_line db `\n`, 0

segment code use32 class = code
    start:
        ; eax = fopen(nume_fisier, mod_acces)
        push dword mod_acces     
        push dword nume_fisier
        call [fopen]
        add esp, 4 * 2            ; eliberam parametrii de pe stiva
        
        mov [descriptor_fis], eax ; salvam valoarea returnata de fopen in variabila descriptor_fis
        
        ; verificam daca functia fopen a creat cu succes fisierul (daca EAX != 0)
        cmp eax, 0
        je final

        ; esi = inceputul sirului care trebuie modificat
        mov esi, text
        repeta:
            mov al, [esi]
            mov ah, byte 0
            cmp ax, [comper_number]     ; ESI - ('9' + 1), daca rez > 0 atunci ESI era un numar
            jns nu_este_numar
            cmp ax, [comper_space]
            jz nu_este_numar
            ; este un numar
                mov [esi], byte 'C'
            nu_este_numar:
            inc esi                      ; esi += 1
            cmp byte [esi], 0            ; daca esi != null nu sa terminat textul
        jnz repeta
        
        ; fprintf(descriptor_fis, text)
        push dword text
        push dword [descriptor_fis]
        call [fprintf]
        add esp, 4 * 2
        
        ; fclose(descriptor_fis)
        push dword [descriptor_fis]
        call [fclose]
        add esp, 4
        
        final:
            ; exit(0)
            push dword 0      
            call [exit] 
