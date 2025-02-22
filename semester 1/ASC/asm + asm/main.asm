;Sa se citeasca un sir s1 (care contine doar litere mici). Folosind un alfabet (definit in segmentul de date), determinati si afisati sirul s2 obtinut prin substituirea fiecarei litere a sirului s1 cu litera corespunzatoare din alfabetul dat.
;Exemplu:
;Alfabetul: OPQRSTUVWXYZABCDEFGHIJKLMN
;Sirul s1:  anaaremere
;Sirul s2:  OBOOFSASFS

bits 32
global start

extern exit, printf, scanf, change
import exit msvcrt.dll 
import printf msvcrt.dll
import scanf msvcrt.dll

segment data use32 class = data
    alfabet db 'OPQRSTUVWXYZABCDEFGHIJKLMN', 0
    ; reservs 100 bitest (aka. 100 carcters) for two texts
    s1 times 100 db 0                                
    s2 times 100 db 0
    format db '%s', 0

segment code use32 class = code
    start:
        ; scanf(format, a)
        push dword s1                   ; the text we what to read
        push dword format
        call [scanf]
        add ESP, 4 * 2            ; clear the stack
        ; avem textul pe care vrem sa-l modificam citit la adresa s1
        ; acum vom chema functia de modificare din modulul nostru
        
        ; change(alfabet, s1, s2)
        push dword s2
        push dword s1
        push dword alfabet
        call change
        add ESP, 4 * 3            ; clear the stack
        ; acum avem textul modificat la adresa s2
        
        ;printf(format, a)
        push dword s2                   ; siul modificat
        push dword format
        call [printf]
        add ESP, 4 * 2
        
        
        ; AM PUS ASTA AICI DEOARECE PROGRAMUL main.exe SE INCHIDE SI NU POTI SA VEZI REZULTATUL
        ; scanf(format, a)
        push dword s1                   ; the text we what to read
        push dword format
        call [scanf]
        add ESP, 4 * 2            ; clear the stack
        
        push dword 0
        call[exit]
        
        
        