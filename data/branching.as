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
       B test
       SUBIS x1, x1, #5
test:
       SUB x1, x1, x1
       SUBS x1, x1, x1
       PUSH x0
       PUSH x0
       POP

       HALT

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