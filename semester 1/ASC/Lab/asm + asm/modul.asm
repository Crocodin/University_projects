

bits 32

global change

segment code use32 class = code

    change:
        ; this function will be called as
        ; change(tabel, original_text, new_text)
        
        mov ESI, [ESP + 4 * 2]               ; in ESI we have original_text
        mov EDI, [ESP + 4 * 3]               ; in EDI we have new_text
        
        ; to make the conversion as fast as posible and to use the tabel format as an advantage, we will use the XLAT
        
        mov EBX, [ESP + 4 * 1]               ; in EBX we have table
        
        cld
        ; we set it to 0 so the stsob won't breake
        repet:
            lodsb                            ; now we have the caracter in al, AL <- [ESI], ESI += 1
            
            cmp AL, 0                      
            je final                         ; we are at the 0 from the asciiZ text
            
            sub Al, 'a'                      ; now the index of the caracter in the table
            xlat                             ; now in AL, is the new caracter from the table
            stosb                            ; [EDI] <- AL, EDI += 1
        jmp repet
        final:
        
        ; now at the end of [ESI] is the end of the text
        ; but the start is still new_text, this was not modified
        ; so we don't have to retrun
        
        ret
            
            