.wordsize 16              ; sets the machine wordsize
.regcnt   16              ; 4 general purpose registers
.maxmem   0x400      ; max memory size is 32 bytes

.pos 0x0
main:
       ADDI x0, x0, #5
       ADDIS x0, x0, #5
       ADD x0, x0, x0
       ADDS x0, x0, x0
       SUBI x1, x1, #5
       SUBIS x1, x1, #5
       SUB x1, x1, x1
       SUBS x1, x1, x1

       ADDI x2, x2, #15
       ANDI x2, x2, #9
       AND x2, x2, x2
       ADDI x3, x3, #0
       IORI x3, x3, #9
       IOR x3, x3, x3
       ADDI x4, x4, #6
       EORI x4, x4, #9
       EOR x4, x4, x4
       ADDI x5, x5, #1
       LSL x5, x5, #3
       LSR x5, x5, #2

       ADDI x6, x6, #260
       LDUR x6, [x6, #0]
       ADDI x7, x7, #260
       LDURW x7, [x7, #0]
       ADDI x8, x8, #260
       LDURH x8, [x8, #2]
       ADDI x9, x9, #260
       LDURB x9, [x9, #3]

       ;LDUR x1, [x0, #0]
       ;ADD  x2, x2, x1
       ;B main
       ;HALT               ; halt the processor

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