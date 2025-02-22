bits 32 

global start        

extern exit              
import exit msvcrt.dll  

  
segment data use32 class=data
    a dq 10

; our code starts here
segment code use32 class=code
    start:
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
