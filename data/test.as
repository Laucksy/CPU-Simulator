.wordsize 16              ; sets the machine wordsize
.regcnt    4              ; 4 general purpose registers
.maxmem   0x10000000      ; max memory size is 32 bytes

.pos 0x0
main:
       ADDI x2, x2, #20   ; store 20 in register x2
       ;MOVZ x0, data      ; store pointer at data to x0
       LDUR x1, [x0, #0]  ; load data at start into register x1
       ADD  x2, x2, x1    ; add 0xAB to 10 and store in register x2
       B main
       HALT               ; halt the processor

.pos 0x100                ; set image location to 0x100
.align 8                  ; align data to an 8-byte boundry

data:
        .double 0x0AB     ; place 0xAB in a 8-byte location
        .single 0x0AB     ; place 0xAB in a 4-byte location
        .half   0x0AB     ; place 0xAB in a 2-byte location
        .byte   0x0AB     ; place 0xAB in a 1-byte location

.pos 0x200                ; set the image location to 0x200
stack:
        .double 0xDEF     ; start the stack here and create an 8-byte data value