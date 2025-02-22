;Se dau un sir care contine n reprezentari binare pe 8 biti date ca sir de caractere.
;Sa se obtina un sir care contine numerele corespunzatoare reprezentarilor binare.
;Exemplu:
;Se da: '10100111b', '01100011b', '110b', '101011b'
;Se stocheaza: 10100111b, 01100011b, 110b, 101011b

bits 32

global _asmTrans

segment code use32 class = code
    ; int asmTrans(char [])
    ; Aceasta functie converteste un sir de caractere care reprezinta un numar binar (ex: "101011b") in numarul corespunzator
    _asmTrans:
        push ebp
        mov ebp, esp

        sub esp, 4               ; Alocăm o variabilă locală pentru rezultat (32 biți)

        ; La [ebp + 8] se află adresa șirului (primul parametru)
        mov esi, [ebp + 8]       ; Adresă șir
        mov edi, esi             ; Copiem adresa pentru iterație

        pana_la_b:
            inc edi
            cmp byte [edi], 0
            je eroare            ; Am ajuns la final fara sa gasim caracterul 'b'
            cmp byte [edi], 'b'
        jne pana_la_b
        ; Acum edi pointează la caracterul 'b'

        mov dword [esp], 0       ; Inițializăm rezultatul cu 0
        mov eax, 1               ; Valoarea curentă (echivalent cu 2^0)
        mov ebx, 2               ; Baza 

        sub edi, 1               ; Mutăm pointerul la caracterul anterior ('1' sau '0')
                                 ; ex: 1010101b
                                 ;           ^ suntem aici

        repeta:
            cmp edi, esi         ; Verificăm dacă am ajuns la începutul șirului
            jb final             ; Dacă da, ieșim din buclă

            cmp byte [edi], '1'
            jne este_zero
            ; Dacă este '1', adăugăm valoarea curentă (eax) la rezultat
            add dword [esp], eax

            este_zero:
                mul ebx          ; eax *= 2 (trecem la următorul bit)
                sub edi, 1       ; Trecem la următorul caracter
        jmp repeta

        final:
            mov eax, [esp]       ; Mutăm rezultatul în eax (valoarea de returnare)
            add esp, 4           ; Eliberăm variabila locală
            
            ; Reparam segmentul de stiva
            mov esp, ebp
            pop ebp
            ret

        eroare:
            mov eax, -1           ; Returnăm -1 pentru eroare
            add esp, 4
            mov esp, ebp
            pop ebp
            ret

        
        
        
        
            