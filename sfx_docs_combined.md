
Chapter 1 Introduction to Super FX™

The Super FX is a Graphic Support processing Unit (GSU) designed to greatly improve the Super NES graphics and mathematical functions through the use of the following special features.

## 1.1 FEATURES

### 1.1.1 RISC-LIKE INSTRUCTIONS
Instructions which are utilized often consist of only one byte and are executed in one cycle in an instruction cache.

### 1.1.2 HIGH SPEED CLOCK OPERATION
The current version of the Super FX operates at a clock speed of 10.74MHz. This is six times as fast as the Super NES CPU.

### 1.1.3 BUILT-IN INSTRUCTION CACHE
A 512-byte cache RAM is installed in order to perform the instructions at high speed. (Refer to “Cache RAM”.)

### 1.1.4 SUPER NES CPU’S MEMORY MAY BE USED
The Super FX uses game pak ROM and RAM which is currently used by the Super NES CPU. (Refer to “Memory Mapping”.)

### 1.1.5 INDEPENDENT ROM AND RAM BUSES
The Super FX can access game pak ROM and RAM in parallel. Program processing speed is maximized, as buffers are provided to read from ROM and write to RAM. (Refer to “Program Execution”.)

### 1.1.6 PARALLEL OPERATIONS WITH SUPER NES CPU
The Super NES CPU and Super FX may execute processing in parallel. Thus, high speed operations can be performed.

### 1.1.7 GRAPHICS FUNCTION
A fast plot process can be performed by specifying a coordinate corresponding with the Super NES PPU format. (Refer to “Bitmap Emulation”, under “Super FX Special Functions”.)

### 1.1.8 PIPELINE PROCESSING
Pipeline processing reduces the number of processing cycles and enables high speed operation. (Refer to “Pipeline Processing”, under “Instruction Set General Description”.)

## 1.2 SPECIAL CONVENTIONS

Unless otherwise specified, addresses will be written with a 2 digit hexadecimal bank number and a 4 digit hexadecimal address separated by a colon (:). The following example demonstrates this convention.

`3F:0000H`

In this example “3F” represents the bank number, while “0000” represents the hexadecimal address.

## 1.3 SYSTEM CONFIGURATION

The GSU is installed on each game pak with ROM and RAM as demonstrated below. The Super NES CPU and the GSU share game pak ROM and RAM. Additional ROM for the Super NES CPU and back-up RAM may also be installed.

{{BEGIN_DIAGRAM}}
This diagram, labeled "Figure 2-1-1 Super FX System Configuration," illustrates the hardware architecture of the Super FX system. It shows two main components: the "SUPER NES GAME PAK" and the "SUPER NES CONTROL DECK." The Game Pak contains "Game Pak ROM" and "Game Pak RAM," which are connected to the GSU via separate data and address buses (labeled "D" and "A"). The GSU is also connected to "Back-up RAM" and "SUPER NES CPU ROM." The "SUPER NES CPU" is located in the Control Deck and is connected to the GSU via a "HOST" bus (labeled "H" and "D"). The diagram demonstrates that the GSU and the Super NES CPU share access to the Game Pak's ROM and RAM, but the diagram does not show the bus arbitration mechanism that prevents simultaneous access. The diagram also shows that the GSU can access the back-up RAM and the Super NES CPU ROM independently.
{{END_DIAGRAM}}

## 1.4 SYSTEM OPERATION

Although the Super NES CPU and GSU share game pak ROM and RAM, the processors can not access either simultaneously. The GSU has a flag, controlled by the Super NES CPU program, which determines whether the CPU or GSU have access to game pak ROM and/or RAM. This is demonstrated in the following figure.

{{BEGIN_DIAGRAM}}
This diagram, labeled "Figure 2-1-2 Game Pak ROM/RAM Bus Diagram," illustrates the bus arbitration mechanism between the Super NES CPU and the GSU. The diagram shows that the GSU has a "Switch" control that determines which processor has access to the game pak ROM and RAM. The diagram also shows that the GSU can access the W-RAM, back-up RAM, and Super NES ROM independently of the game pak ROM and RAM. The diagram demonstrates that the GSU can process instructions stored in game pak ROM and store results in game pak RAM, while the Super NES CPU can access the W-RAM, back-up RAM, and Super NES ROM. The diagram also shows that the GSU can generate an interrupt to the Super NES CPU when it completes its processing.
{{END_DIAGRAM}}

## 1.5 EXAMPLE OF USAGE

### 1.5.1 RESET SUPER NES
When the Super NES is reset, the GSU is also reset. In this condition the game pak ROM and RAM busses are connected to the Super NES CPU. The program stored in game pak ROM is processed by the Super NES CPU. The GSU is idle during this period.

### 1.5.2 WRAM
The Super NES CPU is used to move the program from game pak ROM to the work RAM (WRAM) mounted within the Super NES Control Deck. The Super NES CPU may then be operated by this WRAM program.

### 1.5.3 ACTIVATION OF GSU
The GSU flag is set by the Super NES CPU. This allows the GSU to process instructions stored in game pak ROM and store results in game pak RAM.

### 1.5.4 GSU STOP COMMAND
When the GSU completes the desired processing, a stop command is executed. The GSU stops processing and generates an interrupt to the Super NES CPU. This notifies the Super NES CPU that the GSU has completed its processing.

### 1.5.5 GSU DISCONNECT
When the GSU stops, game pak ROM and RAM busses are again connected to the Super NES CPU. This permits the Super NES CPU to process the results of the GSU's computations.

### 1.5.6 EXAMPLE SUMMARY
This process may have been used, for example, to produce game video data. These programming steps are then repeated, as necessary, to accomplish the programmer's desired result.

### 1.5.7 CURRENT CONSUMPTION
A game pak which contains the Super FX is required to have a built-in safety program to prevent it from operating in excess of the maximum current rating of the AC Adapter. For example, a game pak which contains the Super FX can not be used with Multi Player 5 because this would exceed the maximum current rating. A program must be included within the game pak which will check accessory IDs and activate the Super FX only if an acceptable accessory is connected. If an accessory ID other than those acceptable is detected, a warning message must be displayed and the Super FX must halt.

Some accessories may be used, depending upon the size of ROM and RAM included in the game pak and the Super FX operating frequency. The user should contact Nintendo’s Licensee Support Group for assistance, in advance, if use of an accessory other than the standard controller is desired.


---


## 2.1 GSU FUNCTIONAL BLOCK DIAGRAM

The GSU is comprised of the following 6 functional blocks. These are demonstrated in the figure below.

{{BEGIN_DIAGRAM}}
This diagram illustrates the functional architecture of the GSU (Graphics Super Unit). It shows the interconnection between its core components:

*   **SUPER NES CPU INTERFACE**: This is the primary communication hub. It manages data and instruction transfers between the Super NES CPU and the GSU's internal components. It also controls the activation of the GSU and manages interrupts to the CPU.
*   **INSTRUCTION CONTROLLER**: This block is responsible for fetching, decoding, and managing the execution of instructions. It receives instructions from the CPU interface or from the cache/pipeline decoder and directs them to the appropriate functional units. The note indicates that pipeline and cache circuits are used to enable high-speed instruction execution.
*   **CACHE PIPELINE DECODER**: This component works in conjunction with the Instruction Controller. It decodes instructions and manages the cache, which is used to store frequently accessed data and instructions for faster access.
*   **GENERAL REGISTERS (16 BIT X 16)**: These are the primary working registers of the GSU. They hold data and addresses for arithmetic and logical operations. The diagram shows them connected to the Instruction Controller and the Operator.
*   **OPERATOR**: This block executes the 16-bit arithmetic and logical operations on the data stored in the General Registers. It is connected to the General Registers and the Instruction Controller.
*   **GAME PAK ROM CONTROLLER**: This controller manages data transfer between the GSU and the game pak's ROM (Read-Only Memory). It loads instructions from the ROM into the GSU's internal registers. The note mentions a ROM buffering system to enable efficient execution.
*   **GAME PAK RAM CONTROLLER**: This controller manages data transfer between the GSU and the game pak's RAM (Random Access Memory). It loads instructions from RAM into the GSU's internal registers and handles data transfers between RAM and GSU registers. The note mentions a RAM buffering system for efficient execution.
*   **PLOT Circuitry**: This is a component within the Game Pak RAM Controller. It is responsible for bitmap emulation, which is a key function for rendering graphics.

The diagram also shows the external buses:
*   **TO SUPER NES CPU BUS**: The primary bus for communication with the main CPU.
*   **TO GAME PAK RAM BUS**: The bus for accessing the game pak's RAM.
*   **TO GAME PAK ROM BUS**: The bus for accessing the game pak's ROM.

The diagram shows a bidirectional flow of data and control signals between these components, indicating a tightly integrated system designed for high-speed graphics processing.
{{END_DIAGRAM}}

## 2.1.1 SUPER NES CPU INTERFACE

The Super NES CPU Interface performs the following functions:

1.  Controls data transfer between the Super NES CPU, game pak ROM/RAM, and the general registers.
2.  Controls instruction data transfer between Super NES CPU and the cache.
3.  Controls activation of GSU.
4.  Controls interrupt to Super NES CPU.

## 2.1.2 INSTRUCTION CONTROLLER

This controls fetch instructions, decode instructions, and various other blocks based upon these instructions; loaded from game pak ROM, game pak RAM, or the cache.

Note: Pipeline and cache circuits enable high speed execution of instructions.

## 2.1.3 GAME PAK ROM CONTROLLER

The game pak ROM controller performs the following functions:

1.  Controls data transfer between the Super NES CPU and game pak ROM.
2.  Loads instructions from game pak ROM to the GSU.
3.  Transfers data from the game pak ROM to the GSU internal registers.

Note: Data transfer from the game pak ROM to the GSU is accomplished using a ROM buffering system. This enables instructions from the game pak RAM and cache to be executed and operated in an array.

## 2.1.4 GAME PAK RAM CONTROLLER

The game pak RAM controller functions as follows:

1.  Controls data transfer between the Super NES CPU and game pak RAM.
2.  Loads instructions from game pak RAM to the GSU.
3.  Transfers data between game pak RAM and GSU internal registers.
4.  Bitmap emulation.

Note: Data transfer from the game pak RAM to the GSU is accomplished using a RAM buffering system. This enables instructions from the game pak ROM and cache to be executed and operated in an array.

## 2.1.5 GENERAL REGISTERS

These registers are used for general operations and data transfer.

Note: The GSU is equipped with sixteen, 16-bit registers. All GSU operations are performed using the general registers.

## 2.1.6 OPERATOR

The Operator executes 16-bit arithmetic operations and logical operations.

## 2.2 REGISTERS

A list of GSU internal registers is provided in the table below.

| FUNCTIONAL GROUP | REGISTER NAME |
| :--- | :--- |
| General Registers Group | General Register R0 ~ R13, ROM Address Pointer R14, Program Counter R15, Status/Flag Register SFR |
| Registers Related to Memory Operations | Program Bank Register PBR, Game Pak ROM Bank Register ROMBR, Game Pak RAM Bank Register RAMBR, Cache Base Register CBR |
| Plot Related Registers | Screen Base Register SCBR, Screen Mode Register SCMR, Color Register COLR, Plot Option Register POR |
| Other Registers | Back-up RAM Register BRAMR, Version Code Register VCR, CONFIG Register CFGR, Clock Select Register CLSR |

Table 2-2-1 Registers Listed by Functional Group

## 2.2.1 GENERAL REGISTERS

### 2.2.1.1 R0 ~ R13

These registers are used to execute various instructions as GSU General Registers during GSU operation. There are special functions available for some instructions (refer to “GSU Internal Register Configuration”). These can also be accessed by the Super NES CPU when the GSU is in the idle state.

### 2.2.1.2 R14

This register functions as a data pointer for game pak ROM during GSU operation. Data addressed in this register is automatically stored in the ROM buffer. As with R0 ~ R13, this register may be used as a GSU general register. It can also be accessed by the Super NES CPU when the GSU is in the idle state.

### 2.2.1.3 R15

This register is the GSU Program Counter. If an address is written to this register from the Super NES CPU, while the GSU is idle, the GSU will be activated.

### 2.2.1.4 STATUS/FLAG REGISTER (SFR)

The “flags” in this register indicate GSU status and operation results. This register can be referenced by the Super NES CPU even while the GSU is operating.

## 2.2.2 REGISTERS RELATED TO MEMORY OPERATIONS

### 2.2.2.1 PROGRAM BANK REGISTER (PBR)

This register specifies the memory bank when an instruction is read. Its value must be assigned from the Super NES CPU before the GSU is activated. This is changed during GSU operation using the LJMP instruction.

### 2.2.2.2 GAME PAK ROM BANK REGISTER (ROMBR)

This register specifies the game pak ROM bank when data are read from the game pak ROM using the ROM buffering system. Its value is changed during GSU operation using the ROMB instruction.

### 2.2.2.3 GAME PAK RAM BANK REGISTER (RAMBR)

This register specifies the game pak RAM bank when data are read/written from/to the game pak RAM. Its value is changed during GSU operation using the RAMB instruction.

### 2.2.2.4 CACHE BASE REGISTER (CBR)

This register specifies the starting address when loading data from the game pak ROM or RAM to the cache RAM. The value for CBR is updated during GSU operation whenever the CACHE instruction or LJMP instruction is executed.

## 2.2.3 PLOT RELATED REGISTERS

### 2.2.3.1 SCREEN BASE REGISTER (SCBR)

This register is used to specify the start address in the character data storage area. Its value must be assigned from the Super NES CPU prior to activating the GSU.

### 2.2.3.2 SCREEN MODE REGISTER (SCMR)

This register assigns the color and screen mode when PLOT processing is performed. Its value must be assigned from the Super NES CPU prior to activating the GSU.

### 2.2.3.3 COLOR REGISTER (COLR)

This register specifies the color when PLOT processing is performed. Its value is changed during GSU operation using the COLOR instruction or GETC instruction. It cannot be accessed from the Super NES CPU.

### 2.2.3.4 PLOT OPTION REGISTER (POR)

This register assigns the mode when executing the COLOR, GETC, or PLOT instructions. When these instructions are used, the value of the plot option register must be assigned before execution, using the CMODE instruction.

## 2.2.4 OTHER REGISTERS

### 2.2.4.1 B-RAM REGISTER (BRAMR)

Back-up RAM enable/disable can be controlled by this register. The register’s value must be assigned from the Super NES CPU.

### 2.2.4.2 VERSION CODE REGISTER (VCR)

This assigns the GSU version code. Its value can be read only from the Super NES CPU.

### 2.2.4.3 CONFIG REGISTER (CFGR)

This register assigns the execution speed for GSU multiplication instructions and enables/disables the interrupt signal to the Super NES CPU. Its value must be assigned from the Super NES CPU prior to GSU activation.

### 2.2.4.4 CLOCK SELECT REGISTER (CLSR)

This register is used to assign the operating frequency for the Super FX. Its value must be assigned from the Super NES CPU prior to activation of the Super FX.

## 2.3 INSTRUCTION SET

There are 98 instructions available in the GSU. These instructions and their functions are given in the following table.

| CLASSIFICATION | INSTRUCTION | FUNCTION |
| :--- | :--- | :--- |
| DATA TRANSFER INSTRUCTIONS | From game pak ROM (ROM buffer) to register | GETB Get byte from ROM buffer |
| | | GETBH Get high byte from ROM buffer |
| | | GETBL Get low byte from ROM buffer |
| | | GETBS Get signed byte from ROM buffer |
| | | GETC Get byte from ROM to color register |
| | From game pak RAM to register | LDW (Rm) Load word data from RAM |
| | | LDB (Rm) Load byte data from RAM |
| | | LM Rn, (xx) Load word data from RAM using 16 bits |
| | | LMS Rn, (yy) Load word data from RAM, short address |
| | From register to game pak RAM (RAM buffer) | STW (Rm) Store word data to RAM |
| | | STB (Rm) Store byte data to RAM |
| | | SM (xx), Rn Store word data to RAM using 16 bits |
| | | SMS (yy), Rn Store word data to RAM, short address |
| | | SBK Store word data, last RAM address used |
| | From register to register | MOVE Rn, Rn' Move word data |
| | | MOVES Rn, Rn' Move word data and set flags |
| | Immediate data to register | IWT Rn, #xx Load immediate word data |
| | | IBT Rn, #pp Load immediate byte data |
| | Arithmetic Operation Instructions | ADD Rn Add |
| | | ADD #n Add |
| | | ADC Rn Add with carry |
| | | ADC #n Add with carry |
| | | SUB Rn Subtract |
| | | SUB #n Subtract |
| | | SBC Rn Subtract with carry |
| | | CMP Rn Compare |
| | | MULT Rn Signed multiply |
| | | MULT #n Signed multiply |
| | | UMULT Rn Unsigned multiply |
| | | UMULT #n Unsigned multiply |
| | | FMULT Fractional signed multiply |
| | | LMULT 16x16 signed multiply |
| | | DIV2 Divide by 2 |
| | | INC Rn Increment |
| | | DEC Rn Decrement |

Table 2-2-2 Instruction Set (Sheet 1)


---



## 3.1 SUPER NES CPU MEMORY MAP

The figure on the following page depicts the memory map for the Super NES CPU. Refer to this figure while reading the sub-paragraphs below.

### 3.1.1 GSU INTERFACE

This area (A) is mapped to address 3000H ~ 32FFH in banks 00H ~ 3FH and 80H ~ BFH. (Refer to “GSU Internal Register Configuration”.)

### 3.1.2 GAME PAK ROM

Game pak ROM (B) is mapped to 2 Mbytes starting from 00:8000H. Two Mbytes from 40:0000H (B') are used for the ROM image. This image is stored in blocks of 32 Kbytes, as indicated on the memory map by circled numbers (i.e., area; ①' is the image of area ①, ②' is the image of area ②, and so forth).

### 3.1.3 GAME PAK RAM

Game pak RAM (C) is mapped to 128 Kbytes starting from 70:0000H. Eight Kbytes from address 6000H (C') in each of banks 00~3F and 80~BF are used for RAM image.

### 3.1.4 BACK-UP RAM

Back-up RAM (D) is mapped to 128 Kbytes from 78:0000H.

### 3.1.5 SUPER NES CPU ROM

Six Mbyte of ROM (E) is mapped from 80:8000H.

{{BEGIN_DIAGRAM}}
This diagram, labeled "SUPER NES CPU MEMORY MAP," illustrates the memory layout of the Super NES CPU. The vertical axis represents the bank address (ranging from 00H to FFH), while the horizontal axis represents the memory address (ranging from 0000H to FFFFH). The diagram is divided into several distinct regions, each representing a different type of memory or hardware component.

The topmost region, labeled "GAME PAK ROM (B)", spans from address 0000H to 3FFFH and is divided into four sections marked ①, ②, ③, and ④. These sections correspond to the ROM image blocks, as described in the text. The region labeled "GAME PAK ROM IMAGE (B')" is located at 4000H to 7FFFH and represents the actual ROM image data.

The next region, labeled "GAME PAK RAM (C)", spans from 8000H to BFFFH. It is subdivided into two sections: "GAME PAK RAM IMAGE (C')" at 6000H to 7FFFH and "GAME PAK RAM (C)" at 8000H to BFFFH. The "GAME PAK RAM IMAGE (C')" section represents the RAM image data, while the "GAME PAK RAM (C)" section represents the actual RAM.

The region labeled "BACK-UP RAM (D)" spans from C000H to DFFFH.

The region labeled "SUPER NES CPU RAM" spans from E000H to FFFFH.

The region labeled "SUPER NES CPU ROM (E)" spans from 8000H to 807FFFH.

The region labeled "GSU (A)" spans from 3000H to 32FFH.

The region labeled "Super NES W-RAM" spans from 2000H to 3FFFH.

The diagram also includes a note that the GSU can access memory using three bank registers: Program Bank Register (PBR), ROM Bank Register (ROMBR), and RAM Bank Register (RAMBR).

{{END_DIAGRAM}}

## 3.2 GSU MEMORY MAPPING

The GSU memory map is depicted on the following page.

### 3.2.1 GAME PAK ROM

The game pak ROM (A) is mapped to 2 Mbytes starting from 00:8000H. Two Mbytes from 40:0000H (A') are used for the ROM image. This image is stored in blocks of 32 Kbytes, as indicated on the memory map by circled numbers (i.e., area; ①' is the image of area ①, ②' is the image of area ②, and so forth). Other areas should not be used for this purpose.

### 3.2.2 GAME PAK RAM

Game pak RAM (B) is mapped to 128 Kbytes starting from 70:0000H. When the GSU accesses memory, it specifies bank addresses using three bank registers. These are; Program Bank Register (PBR), ROM Bank Register (ROMBR), and RAM Bank Register (RAMBR).

{{BEGIN_DIAGRAM}}
This diagram, labeled "SUPER FX MEMORY MAP," illustrates the memory layout of the Super FX chip. The vertical axis represents the bank address (ranging from 00H to FFH), while the horizontal axis represents the memory address (ranging from 0000H to FFFFH). The diagram is divided into several distinct regions, each representing a different type of memory or hardware component.

The topmost region, labeled "GAME PAK ROM (A)", spans from 0000H to 3FFFH. It is subdivided into four sections marked ①, ②, ③, and ④. These sections correspond to the ROM image blocks, as described in the text. The region labeled "GAME PAK ROM IMAGE (A')" is located at 4000H to 7FFFH and represents the actual ROM image data.

The next region, labeled "GAME PAK RAM (B)", spans from 8000H to BFFFH.

The diagram also includes a note that the PBR can be used to specify any bank address that is mapped. The ROMBR can only be used to specify banks 00H to 5FH.

{{END_DIAGRAM}}```

---


Chapter 4 GSU Internal Register Configuration

The GSU internal registers will be described in detail in this chapter. Although many of these registers may be accessed from the Super NES CPU, none can be accessed in this way during operation of the GSU, with the exception of the Status/Flag Register (SFR) and Version Code Register (VCR). In addition, when addressing the 16-bit registers from the Super NES CPU, the low byte must be accessed first.

All addresses denoted with (***) can be accessed in banks 00H ~ 3FH and 80H ~ BFH.

## 4.1 GENERAL REGISTERS (R0 ~ R13)

Access from Super NES CPU: R/W
Register Size: 16 bits
GSU Access Method: Various transfer instructions (LDW (Rn)) Various Operation Instructions (ADD Rn) Other Instructions

| Register Name | Super NES CPU Address | Special Functions | Initial Value |
|---|---|---|---|
| R0 | **:**3000H, 3001H | Default source/destination register | Invalid |
| R1 | **:**3002H, 3003H | PLOT instruction, X coordinate | 0000H |
| R2 | **:**3004H, 3005H | PLOT instruction, Y coordinate | 0000H |
| R3 | **:**3006H, 3007H | | Invalid |
| R4 | **:**3008H, 3009H | LMULT instruction, lower 16 bits | Invalid |
| R5 | **:**300AH, 300BH | | Invalid |
| R6 | **:**300CH, 300DH | FMULT and LMULT instructions, multiplication | Invalid |
| R7 | **:**300EH, 300FH | MERGE instruction, source 1 | Invalid |
| R8 | **:**3010H, 3011H | MERGE instruction, source 2 | Invalid |
| R9 | **:**3012H, 3013H | | Invalid |
| R10 | **:**3014H, 3015H | | Invalid |
| R11 | **:**3016H, 3017H | LINK instruction destination register | Invalid |
| R12 | **:**3018H, 3019H | LOOP instruction counter | Invalid |
| R13 | **:**301AH, 301BH | LOOP instruction branch | Invalid |

Table 2-4-1 GSU General Registers

For LINK and LOOP special functions refer to “Instruction Execution”, for other special functions refer to the instruction name in the chapter titled “Description of Instructions”.

## R0

{{BEGIN_DIAGRAM}}
Figure 2-4-1 Example of General Register

This diagram illustrates the bit layout of the general register R0. The register is 16 bits wide, divided into two 8-bit bytes. The upper byte (D15-D8) corresponds to the address 3001H, and the lower byte (D7-D0) corresponds to the address 3000H. This structure allows for 16-bit access to the register, with the low byte being accessed first when addressing 16-bit registers from the Super NES CPU.
{{END_DIAGRAM}}

## 4.2 GAME PAK ROM ADDRESS POINTER (R14)

Access from Super NES CPU: R/W
Super NES CPU Addresses: **:**301CH, 3011DH
Register Size: 16 bits
GSU Access Method: Various transfer instructions (LDW (Rn)) Various operation instructions (ADD Rn) Other instructions

{{BEGIN_DIAGRAM}}
This diagram illustrates the bit layout of the Game Pak ROM Address Pointer register R14. The register is 16 bits wide, divided into two 8-bit bytes. The upper byte (D7-D0) corresponds to the address 301DH, and the lower byte (D7-D0) corresponds to the address 301CH. The bits are labeled as A15 to A0, indicating that this register holds a 16-bit address for the Game Pak ROM. This register is used to specify the game pak ROM address when data are loaded from the game pak ROM to an internal register. Typically, the ROM buffering system will be used for this process.
{{END_DIAGRAM}}

## 4.3 PROGRAM COUNTER (R15)

Access from Super NES CPU: R/W
Super NES CPU Addresses: **:**301EH, 3011FH
Register Size: 16 bits
Default Address: 0000H
GSU Access Method: Various branching instructions (JMP Rn) Other instruction

{{BEGIN_DIAGRAM}}
This diagram illustrates the bit layout of the Program Counter register R15. The register is 16 bits wide, divided into two 8-bit bytes. The upper byte (D7-D0) corresponds to the address 301FH, and the lower byte (D7-D0) corresponds to the address 301EH. The bits are labeled as PC15 to PC0, indicating that this register holds a 16-bit address for the program counter. R15 is the GSU program counter. If its value is changed by a transfer instruction or operation instruction, the program jumps to the address of the new value.
{{END_DIAGRAM}}

## 4.4 STATUS/FLAG REGISTER (SFR)

Access from Super NES CPU: R/W
Super NES CPU Addresses: **:**3030H, 3031FH
Register Size: 16 bits
Default Address: 0000H

{{BEGIN_DIAGRAM}}
This diagram illustrates the bit layout of the Status/Flag Register (SFR). The register is 16 bits wide, divided into two 8-bit bytes. The upper byte (D7-D0) corresponds to the address 3031H, and the lower byte (D7-D0) corresponds to the address 3030H. The bits are labeled as IRQ, *, *, B, IH, IL, ALT2, ALT1 for the upper byte, and *, R, G, OV, S, CY, Z, * for the lower byte. The asterisks indicate that these bits are 0 when the register is read. The table below provides a description of each flag.

| Flag | Description |
|---|---|
| Z | Zero flag |
| CY | Carry flag |
| S | Sign flag |
| OV | Overflow flag |
| G | Go flag (set to 1 when the GSU is running) |
| R | Set to 1 when reading ROM using R14 address. |
| ALT1 | Mode set-up flag for the next instruction |
| ALT2 | Mode set-up flag for the next instruction |
| IL | Immediate lower 8-bit flag |
| IH | Immediate higher 8-bit flag |
| B | Set to 1 when the WITH instruction is executed. |
| IRQ | Interrupt flag |

Table 2-4-2 GSU Status Register Flags

The Status/Flag register indicates the status of the GSU. It may be accessed from the Super NES CPU during GSU operation to determine GSU status.
{{END_DIAGRAM}}

## 4.5 PROGRAM BANK REGISTER (PBR)

Access from Super NES CPU: R/W
Super NES CPU Addresses: **:**3034H
Register Size: 8 bits
Default Address: Undefined
GSU Access Method: LJMP instruction

{{BEGIN_DIAGRAM}}
This diagram illustrates the bit layout of the Program Bank Register (PBR). The register is 8 bits wide, divided into two 4-bit bytes. The bits are labeled as A23 to A16, indicating that this register holds an 8-bit value for the program bank. The program bank register specifies the memory bank register to be accessed when the GSU is loading the program code.
{{END_DIAGRAM}}

## 4.6 GAME PAK ROM BANK REGISTER (ROMBR)

Access from Super NES CPU: R
Super NES CPU Addresses: **:**3036H
Register Size: 8 bits
Default Address: Undefined
GSU Access Method: ROMB instruction

{{BEGIN_DIAGRAM}}
This diagram illustrates the bit layout of the Game Pak ROM Bank Register (ROMBR). The register is 8 bits wide, divided into two 4-bit bytes. The bits are labeled as A23 to A16, indicating that this register holds an 8-bit value for the game pak ROM bank. The game pak ROM bank register specifies the game pak ROM bank when loading data from game pak ROM using the ROM buffering system.
{{END_DIAGRAM}}

## 4.7 GAME PAK RAM BANK REGISTER (RAMBR)

Access from Super NES CPU: R
Super NES CPU Addresses: **:**303CH
Register Size: 1 bit
Default Address: Undefined
GSU Access Method: RAMB instruction

{{BEGIN_DIAGRAM}}
This diagram illustrates the bit layout of the Game Pak RAM Bank Register (RAMBR). The register is 1 bit wide, with the bit labeled as A16. The register is used to specify the game pak RAM bank when data are read/written between game pak RAM and the GSU internal registers. The RAMB instruction specifies bank 70H or 71H for game pak RAM access. The bit is 0 when this register is read.
{{END_DIAGRAM}}

## 4.8 CACHE BASE REGISTER (CBR)

Access from Super NES CPU: R
Super NES CPU Addresses: **:**303EH, 303FH
Register Size: 12 bits
Default Address: 0000H
GSU Access Method: LJMP, CACHE instructions

{{BEGIN_DIAGRAM}}
This diagram illustrates the bit layout of the Cache Base Register (CBR). The register is 12 bits wide, divided into two 6-bit bytes. The upper byte (D7-D0) corresponds to the address 303FH, and the lower byte (D7-D0) corresponds to the address 303EH. The bits are labeled as A15 to A8 for the upper byte, and A7 to A0 for the lower byte. The lower byte has two bits marked with asterisks, indicating that these bits are 0 when the register is read. The cache base register specifies the starting address when data are loaded from game pak ROM or RAM to the cache RAM.
{{END_DIAGRAM}}

## 4.9 SCREEN BASE REGISTER (SCBR)

Access from Super NES CPU: W
Super NES CPU Addresses: **:**3038H
Register Size: 8 bits
Default Address: Undefined
GSU Access Method: None

{{BEGIN_DIAGRAM}}
This diagram illustrates the bit layout of the Screen Base Register (SCBR). The register is 8 bits wide, divided into two 4-bit bytes. The bits are labeled as A17 to A10, indicating that this register holds an 8-bit value for the screen base address. The screen base register is used to specify the start address in the character data storage area.
{{END_DIAGRAM}}

## 4.10 SCREEN MODE REGISTER (SCMR)

Access from Super NES CPU: W
Super NES CPU Addresses: **:**303AH
Register Size: 6 bits
Default Address: 00H
GSU Access Method: None

{{BEGIN_DIAGRAM}}
This diagram illustrates the bit layout of the Screen Mode Register (SCMR). The register is 6 bits wide, divided into two 3-bit bytes. The bits are labeled as HT1, RON, RAN, HT0 for the upper byte, and MD1, MD0 for the lower byte. The register specifies the color gradient and screen height during PLOT processing and controls game pak ROM and RAM bus assignments.

### 4.10.1 SCREEN HEIGHT

| Ht 1 | Ht 0 | Mode |
|---|---|---|
| 0 | 0 | 128 (pixels) |
| 0 | 1 | 160 (pixels) |
| 1 | 0 | 192 (pixels) |
| 1 | 1 | OBJ mode |

Table 2-4-3 Screen Height

### 4.10.2 COLOR GRADIENT

| Mod 1 | Mod 0 | Mode |
|---|---|---|
| 0 | 0 | 4-color mode |
| 0 | 1 | 16-color mode |
| 1 | 0 | Not used |
| 1 | 1 | 256-color mode |

Table 2-4-4 Color Gradient

### 4.10.3 ROM/RAM ENABLE FLAGS

When:
RON = 0, the Super NES CPU has game pak ROM bus access.
1, the GSU has game pak ROM bus access.
RAN = 0, the Super NES CPU has game pak RAM bus access.
1, the GSU has game pak RAM bus access.
{{END_DIAGRAM}}

## 2.2.2 BW-RAM ACCESS

The Super NES CPU and SA-1 CPU share all areas of BW-RAM and can freely access it (two-phase access).

The SA-1 CPU accesses BW-RAM at 5.37 MHz and the Super NES CPU accesses BW-RAM at 2.68 MHz.

### 2.2.2.1 ONLY SA-1 CPU USES BW-RAM

{{BEGIN_DIAGRAM}}
This diagram illustrates the timing of BW-RAM access when only the SA-1 CPU is using it. The SA-1 CPU accesses BW-RAM at 5.37 MHz. The diagram shows a sequence of Read, Read, No Access, Write, Read, No Access operations. The SA-1 CPU has exclusive access to BW-RAM during these operations.
{{END_DIAGRAM}}

### 2.2.2.2 SUPER NES CPU ACCESSES BW-RAM DURING SA-1 CPU OPERATIONS

{{BEGIN_DIAGRAM}}
This diagram illustrates the timing of BW-RAM access when the Super NES CPU accesses it during SA-1 CPU operations. The SA-1 CPU accesses BW-RAM at 5.37 MHz. The diagram shows a sequence of C-CPU, C-CPU, S-CPU, C-CPU, C-CPU, S-CPU operations. The Super NES CPU has access to BW-RAM during the S-CPU operations.
{{END_DIAGRAM}}

### 2.2.2.3 BOTH PROCESSORS ACCESS BW-RAM (2-PHASE ACCESS)

{{BEGIN_DIAGRAM}}
This diagram illustrates the timing of BW-RAM access when both the Super NES CPU and SA-1 CPU are accessing it. The SA-1 CPU accesses BW-RAM at 5.37 MHz. The diagram shows a sequence of S-CPU, C-CPU, S-CPU, C-CPU, S-CPU, C-CPU operations. The Super NES CPU has access to BW-RAM during the S-CPU operations.
{{END_DIAGRAM}}

## 4.13 BACK-UP RAM REGISTER (BRAMR)

Access from Super NES CPU: W
Super NES CPU Addresses: **:**3033H
Register Size: 1 bit
Default Address: 00H
GSU Access Method: None

{{BEGIN_DIAGRAM}}
This diagram illustrates the bit layout of the Back-Up RAM Register (BRAMR). The register is 1 bit wide, with the bit labeled as BRAM Flag. The register is used to enable or disable the Back-Up RAM. When BRAM Flag = 0, BRAM is disabled. When BRAM Flag = 1, BRAM is enabled. Data becomes “protected” when the BRAM flag is reset (“0”) after saving data to the Back-up RAM.
{{END_DIAGRAM}}

## 4.14 VERSION CODE REGISTER (VCR)

Access from Super NES CPU: R
Super NES CPU Addresses: **:**303BH
Register Size: 8 bit
Default Address: Undefined
GSU Access Method: None

{{BEGIN_DIAGRAM}}
This diagram illustrates the bit layout of the Version Code Register (VCR). The register is 8 bits wide, divided into two 4-bit bytes. The bits are labeled as VC7 to VC0, indicating that this register holds an 8-bit value for the version code. The version code register permits the user to read the GSU version code.
{{END_DIAGRAM}}

## 4.15 CONFIG REGISTER (CFGR)

Access from Super NES CPU: W
Super NES CPU Addresses: **:**3037H
Register Size: 8 bit
Default Address: 00H
GSU Access Method: None

{{BEGIN_DIAGRAM}}
This diagram illustrates the bit layout of the Config Register (CFGR). The register is 8 bits wide, divided into two 4-bit bytes. The bits are labeled as IRQ, -, MS0, -, -, -, -, -. The MS0 bit is used for Multiplier Speed Selection. When MS0 = 0, the multiplier operates in Standard Speed Mode. When MS0 = 1, the multiplier operates in High Speed Mode. The IRQ Mask Flag is equal to 1 when the GSU interrupt request is masked. The CONFIG register selects the operating speed of the multiplier in the GSU and sets up a mask for the interrupt signal.

Note: When the Super FX operates at 21 MHz (when the CLSR flag of the Clock Select Register is “1”), MS0 flag should be fixed at “0”.
{{END_DIAGRAM}}

## 4.16 CLOCK SELECT REGISTER (CLSR)

Access from Super NES CPU: W
Super NES CPU Addresses: **:**3039H
Register Size: 1 bit
Default Address: 00H
GSU Access Method: None

{{BEGIN_DIAGRAM}}
This diagram illustrates the bit layout of the Clock Select Register (CLSR). The register is 1 bit wide, with the bit labeled as CLSR Flag. The register assigns the Super FX operating frequency. When CLSR Flag = 0, Super FX operates at 10.7 MHz. When CLSR Flag = 1, Super FX operates at 21.4 MHz.
{{END_DIAGRAM}}

# Chapter 5 GSU Program Execution

## 5.1 STARTING THE GSU

The GSU is placed in the idle state when the Super NES control deck is reset. The GSU is started by writing to its internal program counter (R15) from the Super NES. The GSU programs operate on the game pak ROM, RAM, or cache RAM, but the GSU activation method differs depending upon which memory is accessed. The various methods are described below.

### 5.1.1 STARTING GSU PROGRAM IN GAME PAK ROM

The GSU is started by the following method when the GSU program is to operate in the game pak ROM.

#### 5.1.1.1 BUS CONTROL

In order for the Super NES CPU to pass game pak ROM bus access to the GSU, the Super NES CPU program used to start the GSU in an area other than the game pak ROM (such as WRAM) is transferred to the GSU and the GSU jumps to that program.

However, if the optional ROM for the Super NES is being used, the GSU can be started by running the start program in Super NES ROM, making the above transfer unnecessary.

#### 5.1.1.2 REGISTER ADDRESSING

In the Super NES CPU program for starting the GSU, first assign the following registers.

- PBR (Super NES CPU Address, **:3034H)
- SCBR (Super NES CPU Address, **:3038H)
- SCMR (Super NES CPU Address, **:303AH)

Note: RON absolutely must be set to “1”.

- CFGR (Super NES CPU Address, **:3037H)
- CLSR (Super NES CPU Address, **:3039H)

Subsequently, when the lead address of the GSU program is written from the Super NES CPU to R15 (Super NES CPU address, **:301EH), the GSU can be started from that address.

An example of the program required for starting the GSU from the Super NES is demonstrated on the following page.


mem8
lda #clock data
sta 3039H ;Sets operating frequency
sta 3037H ;Sets CONFIG register
lda #screen base
sta 3038H ;Sets screen base
lda #program bank
sta 3034H ; Sets program code bank
lda #screen size mode
ora 18H ; Sets RON, RAN flag, screen size, and color number
sta 303aH
mem16
rep #0010000B
lda #program address
sta 301EH ; Sets program counter

## 5.1.2 STARTING GSU PROGRAM IN GAME PAK RAM

The following procedure is used to start the GSU when its program is to operate in game pak RAM.

### 5.1.2.1 TRANSFER GSU PROGRAM

The Super NES CPU first transfers the GSU program from the game pak ROM to game pak RAM. If the GSU will not be using game pak ROM, the Super NES CPU does not need to pass the game pak ROM bus access to the GSU.

### 5.1.2.2 REGISTER ADDRESSING

In the Super NES CPU program for starting the GSU, first assign the following registers.

- PBR (Super NES CPU Address, **:3034H)
- SCBR (Super NES CPU Address, **:3038H)
- SCMR (Super NES CPU Address, **:303AH)

Note: RAN absolutely must be set to “1”.

- CFGR (Super NES CPU Address, **:3037H)
- CLSR (Super NES CPU Address, **:3039H)

Subsequently, when the lead address of the GSU program is written from the Super NES CPU to R15 (Super NES CPU address, **:301EH), the GSU can be started from that address.

## 5.1.3 STARTING GSU PROGRAM IN CACHE RAM

The following procedure is used to start the GSU when its program is to operate in cache RAM.

### 5.1.3.1 TRANSFER GSU PROGRAM

The Super NES CPU first transfers the GSU program from the game pak ROM to cache RAM. If the GSU will not be using game pak ROM or RAM, the Super NES CPU does not need to pass the game pak ROM or RAM bus access to the GSU.

### 5.1.3.2 REGISTER ADDRESSING

In the Super NES CPU program for starting the GSU, first assign the following registers.

- PBR (Super NES CPU Address, **:3034H)
- SCBR (Super NES CPU Address, **:3038H)
- SCMR (Super NES CPU Address, **:303AH)
- CFGR (Super NES CPU Address, **:3037H)
- CLSR (Super NES CPU Address, **:3039H)

Subsequently, when the lead address of the GSU program is written from the Super NES CPU to R15 (Super NES CPU address, **:301EH), the GSU can be started from that address.

## 5.2 STOPPING THE GSU

The following two methods may be used to stop the GSU.

- GSU auto-stop using the STOP instruction
- Forced stop from the Super NES CPU using the GO flag

### 5.2.1 GSU AUTO-STOP USING STOP INSTRUCTION

The STOP instruction is one of the instructions in the GSU instruction set. When the GSU reads the STOP instruction, it resets the GO flag, sends an interrupt (IRQ) to the Super NES CPU (to inform the CPU that processing is complete), and goes into the idle state.

The value in R15 after the GSU has executed a STOP instruction varies depending upon the instruction that was executed immediately prior to the STOP instruction.

| Instruction Type | Value of R15 |
| :--- | :--- |
| Transfer Data to R15 | R15 Data + 1 |
| Jump or Branch | Jump or branch destination address + 1 |
| CACHE Instruction | Address of STOP instruction + 1 |
| Other Instruction | Address of STOP instruction + 1 |

## 5.2.2 FORCED STOP FROM SUPER NES CPU USING GO FLAG

The GSU can be forceably stopped by writing a “0” from the Super NES CPU to the GO flag in the status/flag register (Super NES CPU address, **:3030H). This clears the data in the cache and resets the cache base register to 0000H.

## 5.3 MEMORY ACCESS FROM SUPER NES CPU DURING GSU OPERATION

If a “0” is written from the Super NES CPU to the RON flag in the status/flag register (Super NES CPU address, **:303AH) during GSU operation, the GSU will shift to WAIT status when it requires game pak ROM access. This makes it temporarily possible to access game pak ROM from the Super NES CPU.

The WAIT status is subsequently canceled by writing a “1” to RON from the Super NES CPU. This causes the GSU to resume processing. In a similar manner, game pak RAM can be temporarily accessed by the Super NES CPU, using the RAN flag in the screen mode register.

## 5.4 INTERRUPTS

### 5.4.1 SUPER NES CPU INTERRUPT VECTOR

Game pak ROM access from the Super NES CPU is inhibited during GSU operation and when the RON flag is “1”. If an interrupt (NMI) is generated to the Super NES CPU under these conditions, an interrupt vector from the game pak ROM will not be available for the Super NES CPU. This will cause an error. In order to avoid this problem, when a Super NES CPU interrupt vector is read, the GSU outputs a dummy vector on the data bus. The table below expresses the relationship between the Super NES CPU interrupt vector addresses and the dummy vectors. By placing interrupt routines in all the memories except the game pak ROM and encoding a jump instruction to each of the interrupt routines at WRAM addresses 00:0104H, 00:0100H, 00:0108H, and 00:010CH, interrupt processing can be executed without accessing the game pak ROM.

| Interrupt Vector Address | Dummy Vector |
| :--- | :--- |
| 00:FFE4 | 00:0104 |
| 00:FFE6 | 00:0100 |
| 00:FFE8 | 00:0100 |
| 00:FFEA | 00:0108 |
| 00:FFEE | 00:010C |

Table 2-5-1 Dummy Interrupt Vector Addresses

Note: If the game pak ROM is accessed from the Super NES CPU during GSU operation when GO and RON are “1”, the dummy data can be read using the value of the lower 4 bits of that address. This will generate the dummy addresses described above. The table below demonstrates this.

| Lower 4 Bits of Address | Dummy Data |
| :--- | :--- |
| 0H, 2H, 6H, 8H, CH | 00H |
| 4H | 04H |
| AH | 08H |
| EH | 0CH |
| Other | 01H |

Table 2-5-2 Dummy Data

### 5.4.2 INTERRUPT FROM GSU TO SUPER NES CPU

The STOP instruction generates an IRQ from the GSU to the Super NES CPU. Therefore, the Super NES CPU can continue its own processing without having to periodically monitor the GSU for the end of its routine. Since there are instances in which an IRQ is generated for some other reason, the Super NES CPU must determine if the GSU was the source of the IRQ. There is an IRQ flag at bit 15 of the GSU status register. If this flag is “1”, the IRQ was generated by the completion of GSU processing. When bit 15 of this status register is read, the bit is reset to “0”. The IRQ output by the GSU can be disabled by setting bit 7 in the CONFIG register to “1”.

Chapter 6 Instruction Execution

6.1 READING INSTRUCTION CODE

6.1.1 EXECUTION IN GAME PAK ROM/RAM
The GSU executes a program by reading the instruction codes from the game pak ROM or RAM at the addresses specified by the PBR and program counter (R15). The contents of the PBR determines whether the instruction code is to be read from game pak ROM or RAM (refer to "Memory Mapping").

The RON flag must be set (1) when an instruction code is read from game pak ROM. If the RON flag is reset (0), the GSU will be placed in the WAIT state when a game pak ROM instruction code is loaded. Likewise, the RAN flag must be set (1) when an instruction code is read from game pak RAM. If the RAN flag is reset (0), the GSU will be placed in the WAIT state when a game pak RAM instruction code is loaded.

6.1.2 EXECUTION IN CACHE RAM
If the GSU's program counter (R15) is in a cache area determined by the cache base register and the data in the cache are valid, the GSU will read the instruction code from the cache RAM and execute it. When a program is being executed in the cache, even if RON or RAN is reset (0), the GSU will not stop when an instruction code is loaded. Consequently, it becomes possible to access the game pak ROM or RAM from the Super NES CPU.

6.2 PIPELINE PROCESSING
The GSU employs a "pipeline" for high-speed operation. This "pipeline" is a mechanism that, in parallel with the execution of an instruction, loads the next step and prepares it as the next instruction. The program counter (R15) indicates the next address following the instruction currently being executed.

Normally, it is not particularly necessary to be aware of this processing, but it must be considered when using instructions that change the program counter (R15), such as branch or jump instructions. When a branching process is executed, the instruction code at the next address is loaded into the pipeline. This instruction code is then executed in parallel with a load of the instruction code at the branch destination address into the pipeline. This is demonstrated in example 1 on the following page.

(Example 1)
BNE FROG
INC R1
:
:
FROG: ADD R2

When the program in Example 1 is executed, the INC instruction will be executed regardless of the presence of a branch instruction, since it is loaded into the pipeline while processing the BNE instruction.

Note: Be especially careful when placing an instruction of 2 bytes or more after an instruction that changes the program counter.

(Example 2)
BNE LOP1
BRA LOP2
:
:
LOP1: TO R1

When the program in Example 2 is executed, the program jumps to LOP1 when the Z flag is 0, but the first byte of the code "BRA LOP2" has already been loaded into the pipeline. Therefore, the code 11H at the jump destination "TO R1" will be processed as the offset value of the BRA instruction, causing "BRA ****" to be executed instead of "TO R1".

Note: The value for **** = LOP1+1+11H.

In this situation, a NOP instruction should be inserted after the BNE instruction, as shown below.

(Example 3)
BNE LOP1
NOP
BRA LOP2
:
:
LOP1: TO R1

6.3 PROGRAM COUNTER
The GSU program counter is assigned to R15. When the value for R15 is changed by an instruction, the program jumps to the address indicated by that value.

(Example 4)
IWT R0,#0010H
IWT R4,#0020H
IWT R15,#Address
NOP
:
:
Address: ADD R4
INC R3

In example 4, the program jumps to the specified address at the IWT instruction on the third line. Due to pipeline processing, the ADD instruction in the 7th line will be executed after the NOP instruction in the 4th line is executed. In addition, the address following the instruction currently being executed can be identified by moving the contents of R15 to another register.

6.4 FLAG PREFIXES
In the GSU, the action of the next instruction code to be executed varies depending upon the values of the status flags (ALT1, ALT2, B), set by instructions such as the ALT1 instruction.

(Example 5)
The instruction code 53H will perform the processing shown below depending upon the values for ALT1 and ALT2.

When ALT1=0, ALT2=0 Sreg+R3→Dreg (ADD R3)
When ALT1=1, ALT2=0 Sreg+R3+CY→Dreg (ADC R3)
When ALT1=0, ALT2=1 Sreg+3→Dreg (ADD #3)
When ALT1=1, ALT2=1 Sreg+3+CY→Dreg (ADC #3)

(Example 6)
The instruction code 11H will perform the processing shown below depending on the value of the B flag.

When B=0 Set Dreg to R1 (TO R1)
When B=1 Sreg→R1 (MOVE R1,Rn n=value for Sreg)

The ALT1 instruction is used to set the ALT1 flag to 1. Likewise, the ALT 2 instruction is used to set the ALT2 flag to 1. The ALT3 instruction sets both the ALT1 flag and ALT2 flag. The WITH instruction is used to set the B flag.

Normally, the flags which were set by these instructions are cleared after the next instruction is executed. The flags are not cleared when the next instruction is a FROM, TO, WITH, ALT1, ALT2, ALT3, or a branch instruction.

For instance, since the TO and FROM instructions become MOVE and MOVES instructions, respectively; when the B flag is set, these flags will be cleared after the instructions are executed. They will also be cleared after the execution of a NOP instruction.

Since ALT1, ALT2, and ALT3 instructions are used in combination with the next instruction, they do not need to be thought of as independent instructions. For instance, there is no need to be specifically aware that "if ADD R3 is executed after setting the ALT1 flag with an ALT1 instruction, the instruction becomes ADC R3". The process can simply be seen as the two-byte instruction "ADC R3". In the assembler, as well, it is normally unnecessary to specifically code an ALT1 instruction or to write a MOVE instruction as a WITH instruction and a TO instruction.

However, as demonstrated in the following examples, these things need to be kept in mind when accelerating program processing by effectively using the pipeline.

(Example 7)
IWT R3,#100H
LOP1: ADC R0 ; ALT1+ADD R0
PLOT
:
DEC R3
BNE LOP1
NOP

Due to pipeline processing, the code following a branching instruction will be executed regardless of the presence of a branch. In Example 7, the NOP instruction after the BNE instruction will always be executed, but this program can be substituted as demonstrated below.

(Example 8)
IWT R3,#100H
ALT1
NEWLOP1: ADD R0
PLOT
:
DEC R3
BNE NEWLOP1
ALT1

In this example, the branch destination "ADC R0" is divided into "ALT1" and "ADD R0". ALT1 is placed after BNE, changing the address of the branch destination. Thus, the pipeline code at the time of the branch becomes useful.

A different situation is demonstrated below.

(Example 9)
IWT R3,#100H
LOP2: PLOT
:
MOVE R4,R5 ; WITH R5+TO R4
DEC R3
BNE LOP2
NOP

This program can be substituted as shown in Example 10.

(Example 10)
IWT R3,#100H
LOP2: PLOT
:
DEC R3
WITH R5
BNE LOP2
TO R4

In example 10, "MOVE R4,R5" is split into "WITH R5" and "TO R4". This kind of rewrite is possible because the B flag is not changed by the branch instruction.

6.5 REGISTER PREFIXES
Most of the GSU instructions use a source register (Sreg) and destination register (Dreg). The Sreg indicates the general register used for the source of the instruction, while the Dreg indicates the general register used to store the result. The Sreg and Dreg can be assigned in the GSU using the TO, FROM and WITH register prefix instructions. The Sreg is assigned using the FROM instruction and the Dreg using the TO instruction. The Sreg and Dreg can both be assigned using the WITH instruction. The Sreg and Dreg return to the default R0 when any instruction other than TO, FROM, WITH, ALT, or a branch is executed.

If a TO instruction or FROM instruction follows a WITH instruction, as demonstrated below, they will be executed as MOVE or MOVES instructions, causing Sreg and Dreg to return to the defaults after the instructions are executed. These registers also return to the defaults after a NOP instruction is executed.

(Example 11)
The program used to execute R3=R4-R5 is as follows.

TO R3
FROM R4
SUB R5

The operation R0=R4-R5 can be performed by executing the following program, omitting the TO instruction.

FROM R4
SUB R5

The operation R0=R0-R5 can be performed using the following program. The FROM instruction is omitted.

SUB R5

After a normal instruction has been executed, with the exception of TO, FROM, WITH, ALT, or a branch, Sreg and Dreg are both assigned the default register (R0). Consequently, in the following program, the initial SUB instruction will execute R3=R4-R5, but the second SUB instruction will execute R0=R0-R5.

TO R3
FROM R4
SUB R5
SUB R5

The WITH instruction not only assigns Sreg and Dreg, but also sets the B flag within the status/flag register. The TO and FROM instructions act as different instructions when the B flag is set.

* When a TO instruction is next, it performs a MOVE instruction (instruction to move between registers).
* When a FROM instruction is next, it performs a MOVES instruction (instruction to move between registers and set flags according to the data loaded).

6.6 LOOP
The LOOP instruction is provided for efficient loop processing in the GSU. The LOOP instruction decrements the value in R12 by 1 and, when the result is not 0, loads the address in R13 into the program counter. When the result is 0, the next instruction is executed without branching.

Consequently, when performing loop processing using the LOOP instruction, it is necessary to store the loop count number in R12 and the loop return destination address in R13.

(Example 12)
IWT R14,#DATA ;R14=ROM Address for Read Data
IWT R12,#0100H ;R12=Loop Count Number
MOVE R13,R15 ;R13=REPEAT (Loop Back Address)
REPEAT:
GETB
INC R14
LOOP ;R12=R12-1. IF (R12<>0) THEN PC=R13
PLOT

6.7 SUBROUTINES
The GSU does not have any instructions for making subroutine calls. Therefore, when using a subroutine, it will be necessary to specify the return destination address in the program.

(Example 13)
A000 FB 07 A0 IWT R11,#RETURN
A003 FF 03 A1 IWT R15,#SUB1 ;Jump to SUB1
A006 01 NOP ;Dummy
A007 D0 RETURN: INC R0 ;Return Address
:
:
A103 96 SUB1: ASR
A104 96 ASR
A105 2B 1F MOVE R15,R11 ;Return to Main Routine
A107 01 NOP ;Dummy

In Example 13, the program jumps to the subroutine after the return address in R11 has been specified. In the subroutine, the program finally returns to the main program by loading the value for R11 to the program counter (R15).

The LINK instruction is used in the GSU for specifying the return address. LINK adds a value from 1 to 4, depending upon the operand, to the address of the instruction following LINK. The result is stored in R11.

(Example 14)
The call side of the routine in Example 13 can be rewritten as follows using the LINK instruction.

A000 94 LINK #4 ;R11=A005
A001 FF 03 A1 IWT R15,#SUB1 ;Jump to SUB1
A004 01 NOP
A005 D0 RETURN: INC R0 ;Return Address

6.8 CACHE RAM
A 512-byte instruction cache is built into the Super FX. Because instruction code is read six times as fast as reading from game pak ROM or RAM, a program in cache RAM runs at high speed. If a program is run in cache memory, access to the game pak ROM or RAM can be performed at the same time the instruction is executed. Therefore, a program can be executed at a higher speed.

6.8.1 USING CACHE INSTRUCTIONS
The CACHE instruction is used to control the cache. If the CACHE instruction is executed, any subsequent instruction codes will be sequentially loaded into the cache RAM whether they are loaded from game pak ROM or game pak RAM.

For instance, if the CACHE instruction is executed immediately prior to loop processing, the program can be made to operate in the cache RAM beginning with the second repetition.

Program loops exceeding 512 bytes in size will not perform efficiently since the portion not handled in cache RAM will always be executed in game pak ROM or game pak RAM. Dividing the program into several loops so that the loops fit within the 512 byte limit will enable higher speed operation when the CACHE instruction is executed immediately prior to these loops.

6.8.2 CACHE OPERATION
When the CACHE instruction is executed, the beginning address for data to be loaded from game pak ROM or RAM to cache RAM is stored in the CBR (cache base register). The cache area will be 512 bytes beginning with the address stored in the CBR. The 512-byte cache area is further divided into 32 blocks of 16 bytes each. A "cache flag" is assigned to each of these 32 blocks.

When the program counter indicates the cache area, the cache flag that corresponds with that address is read. If the cache flag is not set, the instructions are loaded to cache RAM while the program executes in game pak ROM or RAM. The cache flag is set when the 16-byte block has been entirely loaded with instruction code. If the cache flag has already been set, the program is executed in cache RAM. The cache flags are all reset when the CACHE instruction is executed.

Since the low 4 bits of the CBR are fixed at 0, the beginning address stored in the CBR after execution of a CACHE instruction will be the value of the address following the CACHE instruction with its low 4 bits set to 0 (XXX0H). If the low 4 bits of the address following the CACHE instruction are other than 0, the program jumps to the address in the CBR and loads the code from the game pak ROM or RAM into the cache RAM, after the CACHE instruction is executed.

If a branch occurs before all 16 bytes of instruction code in a block can be loaded (before the cache flag is set), the program will branch after the remaining instruction code in that block has been entirely loaded. This operation is the same within the same block. If the program has branched to an address other than the block header address (XXX0H), the code between the block header address and the branch address will be loaded before the instruction at the branch address is executed. Refer to the illustration on the following page.

{{BEGIN_DIAGRAM}}
Figure 2-6-1 Load to Cache RAM While Branching

Game Pak ROM or RAM
XXX0H
Branch Statement
XXXFH
YYY0H
Branch Destination
YYYFH

After loading □ into the cache, the program will branch.
After loading □ into the cache, the program will execute the branch destination instruction..
{{END_DIAGRAM}}

Since the CBR does not have any bank information, when an LJMP instruction is executed, all cache flags are cleared and the CBR is reset to a value with the low 4 bits of the jump destination address at 0 (XXX0H). This operation is the equivalent of executing another CACHE instruction.

In addition, when the Super NES CPU writes a 0 to the GO flag of the GSU's status/flag register (a forced end if the GSU is operating), all of the cache flags are cleared and the CBR value is set to 0000H. If the GSU is stopped by a STOP instruction, the contents of the CBR, cache flags and cache RAM are all saved. Consequently, when the GSU is restarted, a 0 must be written to the GO flag to reset the CBR and cache flags.

6.8.3 CACHE RAM ACCESS FROM THE SUPER NES
It is possible for the Super NES CPU to read and write to the GSU's cache RAM. The cache RAM is divided into 512-byte addresses from 3100H in any of banks 00H~3FH or 80H~BFH in the Super NES memory map. When the GSU is not operating, data can be freely read and written from/to the Super NES CPU.

However, the CBR does not necessarily comply with address 3100H in the Super NES memory map. Caution should be observed when reading cache memory contents after the CACHE instruction has been executed. The address in the CBR cache RAM complies with the address indicated by the value of the low 9 bits of the CBR. Therefore, the CBR address on the Super NES is calculated as follows.

CBR address on Super NES = 3100H + (CBR AND 01FFH)

When cache data is loaded from the CBR complied address to 32FFH, continuous data is loaded from 3100H to the CBR complied address minus 1.

For example; when the CBR is C3A0H,

Instruction Memory Address Super NES Complied Address
C3A0H~C3FFH 32A0H~32FFH
C400H~C59FH 3100H~329FH

When writing data from Super NES CPU to cache RAM, instructions must be written in 16-byte blocks. If data are written only part way through the 16 bytes, the flag will not be set for that block. In this case, the GSU will process as though cache data did not exist in that block. To set the cache flag, write any data to the XXXFH address of that block.

6.8.4 GSU EXCLUSIVE OPERATION IN CACHE RAM
By activating the GSU after code has been written from the Super NES CPU to the cache RAM, it is possible to operate the program exclusively in cache RAM. The CBR value is stored from the Super NES CPU by resetting the GO flag. This causes the CBR value to become 0000H. The program addresses in cache are normally 0000H through 01FFH, so the GSU is activated with addresses in this range stored in the program counter.

Please be aware that, even when a STOP instruction is executed, the next code has been loaded into the pipeline. If the address of the STOP instruction is XXXFH, the GSU will try to read code from external RAM unless the cache flag for the block containing the next address (XXX0H) has been set.

## 7.1 GAME PAK ROM DATA

The GSU uses a function called the “ROM buffering system” as a method of loading data from game pak ROM during program execution. Using the ROM buffering system, register R14 is assigned as the address pointer to game pak ROM. When a value is set in register R14, the game pak ROM data at the address specified by ROMBR and register R14 are loaded to an internal buffer called the “ROM buffer”.

### 7.1.1 GSU PROGRAM RUNNING IN CACHE RAM OR GAME PAK RAM

When the program is running in cache RAM or game pak RAM, game pak ROM data can be loaded in parallel with the execution of instructions. Therefore, it is most efficient to sandwich several instructions between an instruction that changes R14 and a GETB instruction.

Care is required when performing the following operations while data are being loaded into the ROM buffer.

- If the value for R14 is updated, the initial loading process is interrupted and a new loading process is started.
- If a ROMB instruction is fetched, the program will wait until the data are loaded into the ROM buffer. The ROMBR value will be changed after data is loaded and program execution will resume.
- If a GETB or similar instruction is fetched, the program will pause while the data is loaded into the ROM buffer.

In the following examples, it is presumed that the program is being executed in cache RAM and bit 0 of the CLSR is “1” (Super FX operating frequency is 21.4 MHz).

### CAUTIONS

If cache instructions are executed immediately after the value is set at R14, while the program is running on cache RAM, the proper value is not read to the ROM buffer. Please use caution when reading data from ROM.

- During 21.7 MHz operation, do not insert a CACHE instruction during the first 7 machine cycles after an instruction that changes the content of R14.
- During 10.7 MHz operation, do not insert a CACHE instruction during the first 4 machine cycles after an instruction that changes the content of R14.

---

## (Example 1)

| Cycle | Instruction     | Comment               |
|-------|-----------------|-----------------------|
| 2     | MOVE R14,R1     | ;Start Fetching       |
| 5     | GETB            | ;Get The Byte Into R0 |
| 1     | TO R1           |                       |
| 1     | FROM R2         |                       |
| 1     | ADD R3          | ;Perform R1=R2+R3     |
| 1     | TO R4           |                       |
| 1     | FROM R5         |                       |
| 1     | ADD R6          | ;Perform R4=R5+R6     |
| 1     | ADD R8          | ;R0=R0+R8             |

Fourteen cycles are required to execute the program in the previous example. Since R0 is not used until the last instruction, the GETB instruction can be moved to the line before “ADD R8”, as demonstrated below.

---

## (Example 2)

| Cycle | Instruction     | Comment               |
|-------|-----------------|-----------------------|
| 2     | MOVE R14,R1     | ;Start Fetching       |
| 1     | TO R1           |                       |
| 1     | FROM R2         |                       |
| 1     | ADD R3          | ;Perform R1=R2+R3     |
| 1     | TO R4           |                       |
| 1     | FROM R5         |                       |
| 1     | ADD R6          | ;Perform R4=R5+R6     |
| 1     | GETB            | ;Get The Byte Into R0 |
| 1     | ADD R8          | ;R0=R0+R8             |

Only 10 cycles are required to execute this program. Read timing for game pak ROM access is as follows.

- Operating frequency 21.4 MHz: 5 cycles
- Operating frequency 10.7 MHz: 3 cycles

---

## 7.1.2 GSU PROGRAM RUNNING IN GAME PAK ROM

When the GSU program is running in game pak ROM, it is necessary to use the ROM buffering system even when loading game pak ROM data. The instruction following a change in register R14 will not begin execution until the ROM buffer is loaded.

---

## 7.2 GAME PAK RAM DATA

The GSU uses a function called the “RAM buffering system” as a method of loading data from game pak RAM during program execution. Using the RAM buffering system, the game pak RAM address and data to be written are moved to an internal buffer. The operation of writing to RAM is started by executing a STB, STW, SM, SMS, or SBK instruction.

### 7.2.1 GSU PROGRAM RUNNING IN CACHE RAM OR GAME PAK ROM

When the program is running in cache RAM or game pak ROM, its write data will be written to game pak RAM while the subsequent program is being executed. Therefore, it is most efficient to sandwich several instructions between STW instructions.

Care is required when performing the following operations while writing to game pak RAM.

- Execution of a command that updates the register which was used as the address in a STB or STW instruction will have absolutely no effect on the write operation to game pak RAM and will not wait.
- If a RAMB instruction is fetched, the program will wait until the data are written to game pak RAM. The RAMBR value will be changed after the write is completed and execution of the program will resume.
- If a STW instruction is fetched, the program will wait until the data are written to game pak RAM.

In the following examples, it is presumed that the program is being executed in cache RAM and bit 0 of the CLSR is “1” (Super FX operating frequency is 21.4 MHz).

---

## (Example 3)

| Cycle | Instruction     | Comment               |
|-------|-----------------|-----------------------|
| 1     | FROM R8         | ;Store R8 Into (R10)  |
| 1     | STW (R10)       |                       |
| 10    | STW (R11)       | ;Store R0 Into (R11)  |
| 1     | TO R1           |                       |
| 1     | FROM R2         |                       |
| 1     | ADD R3          | ;Perform R1=R2+R3     |
| 1     | FROM R5         |                       |
| 1     | ADD R6          | ;Perform R0=R5+R6     |

---

## Seventeen cycles are required to execute the program in the previous example. Since the value for R0 is not changed until the last instruction, the second STW instruction can be moved to the line immediately before that instruction. This is demonstrated on the following page.

---

## (Example 4)

| Cycle | Instruction     | Comment               |
|-------|-----------------|-----------------------|
| 1     | FROM R8         |                       |
| 1     | STW (R10)       | ;Store R8 Into (R10)  |
| 1     | TO R1           |                       |
| 1     | FROM R2         |                       |
| 1     | ADD R3          | ;Perform R1=R2+R3     |
| 7     | STW (R11)       | ;Store R0 Into (R11)  |
| 1     | FROM R5         |                       |
| 1     | ADD R6          | ;Perform R0=R5+R6     |

Only 14 cycles are required to execute the program in Example 4. This is more efficient that Example 3, a wait period of 2 cycles is still required to write to game pak RAM.

---

## 7.2.2 GSU PROGRAM RUNNING IN GAME PAK RAM

When the GSU program is running in game pak RAM, it is necessary to use the RAM buffering system described above even when writing game pak RAM data. The instruction following a STB or similar instruction is executed after completion of the write operation to game pak RAM.

---

## 7.3 BULK PROCESSING

Normally during bulk processing, data are loaded from game pak RAM, some processing is performed, and a process is executed to return the data to the same address. Waste can be avoided if the process can be completed without having to specify the address in RAM a second time.

When an instruction that performs a data transfer between the game pak RAM and an internal register is executed in the GSU, the game pak RAM address used in that instruction will be stored in memory. The SBK instruction stores the RAM address in which the register contents are stored. Since it does not require an operand, it can be executed more quickly than the SM or SMS instructions. The difference is demonstrated in the following two examples.

---

## (Example 5)

In the following example the SBK instruction is not used. In this case, word data have been read from game pak RAM address 1234H, the register contents are incremented, and again written to 1234H.

| Cycle | Instruction     | Comment               |
|-------|-----------------|-----------------------|
| 14    | LM R0,(1234H)    | ;R0←(1234H)           |
| 1      | INC R0          | ;R0←R0+1              |
| 4      | SM (1234H),R0    | ;(1234H)←R0           |

Nineteen cycles are required to execute the above program. If the SBK instruction is used, the following occurs.

---

## (Example 6)

| Cycle | Instruction     | Comment               |
|-------|-----------------|-----------------------|
| 14    | LM R0,(1234H)    | ;R0←(1234H)           |
| 1      | INC R0          | ;R0←R0+1              |
| 1      | SBK             | ;(1234H)←R0           |

In this example, only 16 cycles are required. The memory required to handle the program is also decreased.

# Chapter 8 GSU Special Functions

The GSU performs various special functions to realize high-speed operations. These functions are described below.

## 8.1 BITMAP EMULATION

Since a character mapping system is used with the Super NES PPU, its CPU can not efficiently perform processing such as; placing a point, drawing a line or painting a plane (bitmap graphics). Prior to display on the screen, this data must be converted to character data. Thereby, emulating the bitmap data.

The GSU is equipped with functions that support “Plot Processing”. These functions, “place a point of a specified color at a specified coordinate position.” Consequently; after setting the screen mode (CMODE instruction), the color data (COLOR, GETC instructions), and the X,Y coordinates; the PLOT instruction is performed.

In this manner, the GSU converts plotted (bitmaped) data to character data which can be utilized by the Super NES PPU and writes them to game pak RAM. In order to be displayed on screen, character data produced in the game pak RAM must be transferred by the Super NES CPU to the V-RAM of the Super NES.

## 8.1.1 SET SCREEN MODE

To begin GSU plot processing, screen mode assignments must be made. This is performed using the screen mode register (SCMR) and the screen base register (SCBR). The plot options are assigned using the CMODE instruction.

### 8.1.1.1 SCREEN MODE REGISTER (SCMR)

The GSU conversion process from bitmapped data to character data requires a screen mode selection. This determines how the characters will be aligned and the bit mode to be used. This is performed by assigning a mode to the SCMR using the Super NES CPU.

The GSU has 4 modes. A BG character array may be selected with screen heights of 128 dot, 160 dot and 192 dot. The fourth mode is an OBJ character array.

The character data conversion processing by the GSU is performed assuming that the character array is aligned as demonstrated in the following figures for BG 128 dot, BG 160 dot, BG 192 dot, or OBJ; respectively. Consequently, when the converted data are used as BG or OBJ character data for the Super NES, it is necessary to assign the screen mode and store the screen data in the VRAM.

{{BEGIN_DIAGRAM}}
Figure 2-8-1 128 Dot High BG Character Array (numbers are hexadecimal)

This diagram illustrates the memory layout for a 128-dot high background (BG) character array. The array is 256 dots wide and 128 dots high. The memory addresses are shown in hexadecimal, starting from 000 and ending at 1FF. The array is divided into 16 rows, each 8 dots high, and 32 columns, each 8 dots wide. The "SC Data" label indicates the area where the character data is stored. The diagram shows that the data is organized in a grid, with each cell representing a dot in the character array. The addresses are grouped in sets of 3, corresponding to the 32 columns. The diagram also shows that the data is stored in a row-major order, with the first row starting at address 000 and the last row ending at address 1FF.

{{END_DIAGRAM}}

{{BEGIN_DIAGRAM}}
Figure 2-8-2 160 Dot High BG Character Array (numbers are hexadecimal)

This diagram illustrates the memory layout for a 160-dot high background (BG) character array. The array is 256 dots wide and 160 dots high. The memory addresses are shown in hexadecimal, starting from 000 and ending at 27F. The array is divided into 20 rows, each 8 dots high, and 32 columns, each 8 dots wide. The "SC Data" label indicates the area where the character data is stored. The diagram shows that the data is organized in a grid, with each cell representing a dot in the character array. The addresses are grouped in sets of 3, corresponding to the 32 columns. The diagram also shows that the data is stored in a row-major order, with the first row starting at address 000 and the last row ending at address 27F.

{{END_DIAGRAM}}

{{BEGIN_DIAGRAM}}
Figure 2-8-3 192 Dot High BG Character Array (numbers are hexadecimal)

This diagram illustrates the memory layout for a 192-dot high background (BG) character array. The array is 256 dots wide and 192 dots high. The memory addresses are shown in hexadecimal, starting from 000 and ending at 2FF. The array is divided into 24 rows, each 8 dots high, and 32 columns, each 8 dots wide. The "SC Data" label indicates the area where the character data is stored. The diagram shows that the data is organized in a grid, with each cell representing a dot in the character array. The addresses are grouped in sets of 3, corresponding to the 32 columns. The diagram also shows that the data is stored in a row-major order, with the first row starting at address 000 and the last row ending at address 2FF.

{{END_DIAGRAM}}

{{BEGIN_DIAGRAM}}
Figure 2-8-4 OBJ Character Array (numbers are hexadecimal)

This diagram illustrates the memory layout for an object (OBJ) character array. The array is 256 dots wide and 256 dots high. The memory addresses are shown in hexadecimal, starting from 000 and ending at 3FF. The array is divided into 32 rows, each 8 dots high, and 32 columns, each 8 dots wide. The "SC Data" label indicates the area where the character data is stored. The diagram shows that the data is organized in a grid, with each cell representing a dot in the character array. The addresses are grouped in sets of 3, corresponding to the 32 columns. The diagram also shows that the data is stored in a row-major order, with the first row starting at address 000 and the last row ending at address 3FF.

{{END_DIAGRAM}}

To calculate the total number of bytes of character data required, the following formula is derived from the bit mode and the screen height and width.

Total number of bytes of character data =
(Number of dots high/8) X (Number of dots wide/8) X (8n)

Where n equals the number of bits per dot (2,4, or 8).

### 8.1.1.2 SCREEN BASE REGISTER (SCBR)

The start address of the area in game pak RAM where character data will be handled must be assigned in advance from the Super NES CPU. This information is stored in the SCBR.

The start address is calculated using the following formula.

(Start Address) = 70:0000H+SCBRx400H

## 8.1.5 PLOT DATA ADDRESS CALCULATION METHODS

The addresses to which plot data are written are determined using the following data.

*   X and Y coordinates are specified by the low bytes of R₁ and R₂.
*   The screen color mode and height mode are specified by the SCMR.
*   SCBR

The following examples demonstrate the method of calculating this address. In the calculations below, “X[7:3]” indicates the value of bit 7 through 3 for the value of X. The expression “X4,” indicates the value of bit 4 for X.

1.  Calculate the character number (CN) containing the specified coordinates. CN is the value of SC data in the character arrays previously described.

### (a) Height, 128 Dot Mode

CN [9:0] = (X[7:3] x 10H) + Y[7:3]

```
X7  X6  X5  X4  X3
+  Y7  Y6  Y5  Y4  Y3
CN9 CN8 CN7 CN6 CN5 CN4 CN3 CN2 CN1 CN0
```

### (b) Height, 164 Dot Mode

CN [9:0] = (X[7:3] x 14H) + Y[7:3]

```
X7  X6  X5  X4  X3
+  Y7  Y6  Y5  Y4  Y3
CN9 CN8 CN7 CN6 CN5 CN4 CN3 CN2 CN1 CN0
```

### (c) Height, 192 Dot Mode

CN [9:0] = (X[7:3] x 18H) + Y[7:3]

```
X7  X6  X5  X4  X3
+  Y7  Y6  Y5  Y4  Y3
CN9 CN8 CN7 CN6 CN5 CN4 CN3 CN2 CN1 CN0
```

### (d) OBJ Mode

CN [9:0] = (Y[7] x 200H) + (X[7] x 100H) + (Y[6:3] x 10H) + Y[6:3]

```
X7  X6  X5  X4  X3
+  Y7  Y6  Y5  Y4  Y3
CN9 CN8 CN7 CN6 CN5 CN4 CN3 CN2 CN1 CN0
```

2.  The addresses to be written to are then calculated as follows.

A[19:0] = (CN[9:0] x CHAR_SIZE)
+ (SB[7:0] x 4000H)
+ (Y[2:0] x 2)
+ (PL[2] x 200H) + (PL[1] x 100H) + PL[0]

Where CHAR_SIZE is the number of bytes used for one character. This is 16 bytes for 4 color mode, 32 bytes for 16 color mode, and 64 bytes for 256 color mode. The expression “PL[2:0]” indicates a plane number. The expression “SB[7:0]” indicates the value stored at the SCBR. The following examples demonstrate this calculation.

### (a) 4 Color Mode

```
SB7 SB6 SB5 SB4 SB3 SB2 SB1 SB0
CN9 CN8 CN7 CN6 CN5 CN4 CN3 CN2 CN1 CN0
+  Y2  Y1  Y0
A19 A18 A17 A16 A15 A14 A13 A12 A11 A10 A9 A8 A7 A6 A5 A4 A3 A2 A1 A0
```

### (b) 16 Color Mode

```
SB7 SB6 SB5 SB4 SB3 SB2 SB1 SB0
CN9 CN8 CN7 CN6 CN5 CN4 CN3 CN2 CN1 CN0
+  Y2  Y1  Y0
A19 A18 A17 A16 A15 A14 A13 A12 A11 A10 A9 A8 A7 A6 A5 A4 A3 A2 A1 A0
```

### (c) 256 Color Mode

```
SB7 SB6 SB5 SB4 SB3 SB2 SB1 SB0
CN9 CN8 CN7 CN6 CN5 CN4 CN3 CN2 CN1 CN0
+  Y2  Y1  Y0
A19 A18 A17 A16 A15 A14 A13 A12 A11 A10 A9 A8 A7 A6 A5 A4 A3 A2 A1 A0
```


---


## 8.2 MULTIPLICATION INSTRUCTIONS

The 4 multiplication instructions shown below are available in the GSU.

- **MULT** instruction
  Signed 8 bits x Signed 8 bits → Signed 16 bits
  Low 8 bits of Sreg
  Low 8 bits of operand
  Dreg

- **UMULT** instruction
  Unsigned 8 bits x Unsigned 8 bits → Unsigned 16 bits
  Low 8 bits of Sreg
  Low 8 bits of operand
  Dreg

- **LMULT** instruction
  Signed 16 bits x Signed 16 bits → Signed 32 bits
  Sreg
  R6
  High Dreg Low R4

- **FMULT** instruction
  Signed 16 bits x Signed 16 bits → Signed 32 bits
  Sreg
  R6
  High Dreg

There is an 8 bit x 8 bit multiplier built into the GSU. Since this multiplier is used only once with the MULT and UMULT instructions, these instructions can be executed at high speed. A 16 bit x 16 bit answer is calculated, for the LMULT and FMULT instructions, by performing an 8 bit x 8 bit multiplication 4 times.

The execution speed of each multiplication instruction can be changed using bit 5 of the CFGR. Normally, the standard speed mode (bit 5=0) is used. When the Super FX operates at 10.7 MHz (when bit 0 of the CLSR is "0"), the high speed mode (bit 5=1) can be used. If R4 is specified as the destination register with the LMULT instruction, the high 16 bits of the operation results are stored in R4.

### CAUTION

If R4 is specified as the destination register with the FMULT instruction, the operation results will not be stored in R4 and the results will be lost. Do not specify R4 as the destination register for the FMULT instruction.

## 8.2.1 INTERNAL PROCESSING OF FMULT AND LMULT

For LMULT and FMULT instructions, 16 bit x 16 bit multiplication is performed by repeating an 8 bit x 8 bit multiplication circuit whose signed and unsigned numbers could both be present 4 times. The processing flow for the FMULT and LMULT instructions is explained below. The FMULT and LMULT instructions share the circuit, but notice that there are processes that can only be performed by the LMULT instruction.

Initially, an 18 bit buffer used to hold the partial results during multiplication, called the partial product buffer, is cleared.

The first multiplication is performed.
Low 8 bits of Sreg (unsigned) x Low 8 bits of R6 (unsigned)
→ 16 bit result (unsigned)

The high 8 bits of the result are stored in the low 8 bits of the partial product buffer. For LMULT, the low 8 bits of the result are stored in the low 8 bits of R4.

The second multiplication is performed.
High 8 bits of Sreg (signed) x Low 8 bits of R6 (unsigned)
→ 16 bit result (signed)

The result is expanded to 18 bits with the sign and added to the partial product buffer.

The third multiplication is performed.
Low 8 bits of Sreg (unsigned) x High 8 bits of R6 (signed)
→ 16 bit result (signed)

The result is expanded to 18 bits with the sign and added to the partial product buffer. For LMULT, the low 8 bits of the partial product buffer are further stored in the high 8 bits of R4.

The fourth multiplication is performed.
High 8 bits of Sreg (signed) x High 8 bits of R6 (signed)
→ 16 bit result (signed)

The result (16 bits) is added to the high 10 bits of the partial product buffer. For LMULT if the Dreg is R4, the value in the partial product buffer is stored in R4. If the Dreg is not R4, the value of the partial product buffer is stored in the Dreg.

If R4 is specified as the destination register for the LMULT instruction when performing the above processing, the high 16 bits of the operation result will be stored in R4. However, if R4 is specified as the destination register for the FMULT instruction, the operation result will not be stored as the value for R4.


---

For example, when the value 11H is stored in the SCBR, in 4-bit mode, with a height of 128 dots, width of 192 dots;

(Start Address) = 70:0000H+11Hx400H = 70:4400H

(Total number of bytes of character data)
= (128/8)x(192/8)x(8x4) = 3000H

game pak RAM addresses 70:4400H through 70:73FFH are used for the character data area.

8.1.1.3 CMODE INSTRUCTION

The CMODE instruction must be stored in the plot option register (POR) to enable the PLOT instruction and COLOR or GETC instructions to be selected. The relationship between plot processing and the CMODE instruction is covered in more detail under “Plot Function and CMODE”, later in this chapter.

8.1.2 SET COLOR (COLOR, GETC)

The color data used in plot processing must be stored in the GSU’s color register (COLR) using the COLOR instruction or the GETC instruction. If the COLOR instruction is used, the value for the source register is stored, while the GETC instruction stores the value for the ROM buffer.

8.1.3 PLOT PROCESSING (PLOT)

The PLOT instruction plots the color data, stored by the COLOR or GETC instruction, to the X and Y coordinates stored in general registers R1 and R2. The X coordinate value must be in R1 and the Y coordinate value in R2. Color data plotted by the PLOT instruction are converted to character data and written to the game pak RAM.

Since it would be inefficient to perform a direct write to game pak RAM for each PLOT instruction, caching is performed in an 8-bit (1 pixel) x 8-bit memory inside the GSU. This corresponds with the 1 vertical pixel x 8 horizontal pixel blocks into which the screen is divided. This memory is called the “pixel cache” and the blocks that are cached are called “character blocks”.

There are two pixel cache memories in the GSU. The color data produced by the PLOT instruction is cached in the “primary pixel cache.” These data are copied to the “secondary pixel cache,” then written from the “secondary pixel cache” to game pak RAM. Each pixel cache has an 8-bit flag called the primary and secondary bit-pend flags. These indicate whether or not the color data in each pixel cache is valid.

When the PLOT instruction is executed, the offset address of game pak RAM where color data are written is calculated from the value in bit 7 through bit 3 of the X coordinate (R1) and the value in bit 7 through bit 0 of the Y coordinate (R2). These values are held in the GSU. When another PLOT instruction is executed, the GSU compares the new coordinate values to those stored. If the coordinates have not changed, plotting is performed to the same character block (stored in secondary cache) is written to game pak RAM.

The flow of GSU plot processing will be demonstrated below using two cases. In the first description, the character block which was stored by the previous PLOT instruction is to be written. The second case demonstrates plotting to a different block.

8.1.3.1 PLOTTING TO SAME CHARACTER BLOCK

Color data are written to the pixel cache and the corresponding bit-pend flag is set. When all of the bit-pend flags are set (all 8 pixels of the cache block have been written), write processing to game pak RAM is performed in the following manner.

First, the contents of the primary pixel cache and the primary bit-pend flag are transferred to the secondary pixel cache and secondary bit-pend flag. If the contents of the secondary pixel cache are in the process of being written to the game pak RAM, this process is placed in WAIT status until the secondary pixel cache is empty.

After transfer processing, all of the primary bit-pend flags are cleared. Then the GSU executes the instruction following the PLOT instruction. Since the primary pixel cache can be used, the next instruction could be a PLOT instruction without requiring a WAIT status. Parallel with the execution of the next instruction, the GSU converts the color data in the secondary pixel cache into character data and writes them to the game pak RAM.

8.1.3.2 PLOTTING TO A DIFFERENT CHARACTER BLOCK

The contents of the primary pixel cache and the primary bit-pend flag are transferred to the secondary pixel cache and secondary bit-pend flag. If the contents of the secondary pixel cache are in the process of being written to the game pak RAM, this process is placed in WAIT status until the secondary pixel cache is empty. Thereafter, color data are written to the primary pixel cache and the corresponding bit-pend flag is set.

The GSU then executes the instruction following the PLOT instruction. Parallel with the execution of this instruction, the GSU converts the color data in the secondary pixel cache to character data and writes it to game pak RAM.

The data in the corresponding character block are read from the game pak RAM and converted back, while the color data correspond with the flags which are not set in the secondary bit-pend flag are set in the secondary pixel cache. The GSU then converts the color data in the secondary pixel cache into character data and writes them to the game pak RAM.

Thus, the operation of writing to game pak RAM using two pixel caches can be performed in parallel with the execution of instructions, making PLOT processing very efficient. In addition, since the PLOT instruction increments the value for R1 after processing, there is no need to specify coordinates when writing the pixels continuously toward the right.

CAUTION

Do not change the setting of the screen mode, described under “Set Screen Mode,” during plot operations. Also, when screen plot processing is completed, execute the RPIX instruction to write all of the data contained in the pixel caches to the game pak RAM.

(Example 1)

The following program is executed under the following conditions.

SCBR=00H, Color Mode=256, and Screen Mode=BG 128 dot high

```
IBT  R1,#0
IBT  R2,#0  ;Set the plot starting coordinate to (0,0)
IBT  R0,#0
CMODE
IBT  R0,#15H
COLOR  ;Load 15H to the color register
PLOT
PLOT  ;Plot (0,0) through (2,0)
IBT  R0,#36H
COLOR  ;Load 36H to the color register
PLOT
PLOT
PLOT
PLOT  ;Plot (3,0) through (7,0)
```

The primary pixel cache becomes the cache RAM for the character block from coordinates (0,0) through (7,0). When the program is executed, the following values are stored in the primary pixel cache and the primary bit-pend flag.

{{BEGIN_DIAGRAM}}
This diagram illustrates the primary pixel cache and its associated bit-pend flag for a character block from coordinates (0,0) to (7,0). The cache is organized as an 8x8 grid, with each cell representing a pixel. The values in the cache are shown as 15/15/15/36/36/36/36/36, indicating the color data for each pixel. The primary bit-pend flag is shown as a row of 8 ones, indicating that all 8 pixels in the cache block have been written. The diagram also shows the corresponding game pak RAM addresses for each pixel, ranging from 70:0000H to 70:0031H.
{{END_DIAGRAM}}

Since all 8 pixels in a character block are set with the final PLOT instruction, they are transferred from the primary pixel cache to the secondary pixel cache and the game pak RAM write begins. This process clears the primary bit-pend flags and the primary pixel cache is released.

(Example 2)

Continuing from Example 1, the following program is executed.

```
IBT  R0,#4AH  ;Load 4AH to the color register
COLOR
PLOT
PLOT
PLOT
PLOT  ;Plot (8,0) through (11,0)
IBT  R1,#10H  ;Change X coordinate to 16
PLOT  ;Plot (16,0)
```

The primary pixel cache becomes the cache for the character data from coordinates (8,0) through (15,0). Immediately after the 4th PLOT instruction is executed, the primary pixel cache and primary bit-pend flags are as shown below.

{{BEGIN_DIAGRAM}}
This diagram illustrates the primary pixel cache and its associated bit-pend flag for a character block from coordinates (8,0) to (15,0). The cache is organized as an 8x8 grid, with each cell representing a pixel. The values in the cache are shown as 4A/4A/4A/4A, indicating the color data for each pixel. The primary bit-pend flag is shown as a row of 8 bits, with the first 4 bits set to 1 and the last 4 bits set to 0, indicating that the first 4 pixels in the cache block have been written. The diagram also shows the corresponding game pak RAM addresses for each pixel, ranging from 70:0080H to 70:00B1H.
{{END_DIAGRAM}}

Since the last PLOT instruction writes to a different character block, RAM write processing is performed. First, a transfer is performed from the primary pixel cache and primary bit-pend flag to the secondary pixel cache and secondary bit-pend flag. Then, game pak RAM write processing is performed, but the pixels in the secondary pixel cache which have not been plotted are written after a game pak RAM read operation has been executed.

{{BEGIN_DIAGRAM}}
This diagram illustrates the secondary pixel cache and its associated bit-pend flag for a character block from coordinates (8,0) to (15,0). The cache is organized as an 8x8 grid, with each cell representing a pixel. The values in the cache are shown as 4A/4A/4A/4A/10/12/12/10, indicating the color data for each pixel. The secondary bit-pend flag is shown as a row of 8 bits, with the first 4 bits set to 1 and the last 4 bits set to 0, indicating that the first 4 pixels in the cache block have been written. The diagram also shows the corresponding game pak RAM addresses for each pixel, ranging from 70:0080H to 70:00B1H.
{{END_DIAGRAM}}

8.1.3.3 RPIX INSTRUCTION

The RPIX instruction reads the character block containing the specified coordinates from game pak RAM into the pixel cache and performs processing to calculate the pixel values after the contents of the pixel cache have been written to the game pak RAM. When the screen drawing routine is complete, it is advisable to execute the RPIX instruction to insure that all of the PLOT data have been written.

If consecutive RPIX instructions are executed, game pak RAM read data processing will always be performed because the instruction does not discern whether or not there are color data at the specified coordinates in the pixel cache.

CAUTION

Even when consecutive RPIX instructions read color data from the same character block, data will always be read from the game pak RAM.

8.1.4 PLOT FUNCTION AND CMODE

The CMODE instruction is used to determine how the color register value will be handled by the PLOT instruction. The modes which can be specified with CMODE are shown in the table below.

{{BEGIN_DIAGRAM}}
This table shows the functions of the CMODE instruction. The table has 5 rows and 5 columns. The first row is the BIT column, which shows the bit number of the CMODE instruction. The second row is the Flag Name column, which shows the name of the flag. The third row is the Operation when 0 column, which shows the operation when the flag is 0. The fourth row is the Operation when 1 column, which shows the operation when the flag is 1. The fifth row is the Related Instructions column, which shows the related instructions. The table shows the following flags: Transparent Flag, Dither Flag, High Nibble Flag, Freeze High Nibble Flag, and OBJ Mode Flag. The table shows the following operations: Do not PLOT color 0, PLOT color 0, PLOT value of low 4 bits of color register, Alternately PLOT high 4 bits and low 4 bits of color register, Set value of low 4 bits in color register, Set value of high 4 bits in color register, Set all 8 bits in color register, Set only low 4 bits in color register with high 4 bits fixed, Set mode with SCMR (ht0,ht1), OBJ mode. The table shows the following related instructions: PLOT, PLOT, COLOR, GETC, COLOR, GETC, PLOT, RPIX.
{{END_DIAGRAM}}

The PLOT instruction is related to bit 3, but it is also used during PLOT processing for selecting the number of bits to be used (0=8 Bit, 1=4 Bit) for transparent processing.

8.1.4.1 BIT 0

The Super NES has multiple hardware BG screens. When one BG screen is laid over another BG screen, the 0 portions of the color in the top BG screen become “transparent” and the colors of the bottom BG are displayed. The GSU uses color mode 0 to perform this function.

When Bit 0=0 and all of the effective COLR bits are 0, the PLOT circuit refreshes only the X coordinate and no PLOT operation is performed. Normal PLOT operation is performed for anything other than 0.

8.1.4.2 BIT 1

When the number of colors that can be displayed at once is low (16 color mode), techniques can be used to apparently increase the number of colors through dither processing. The GSU is able to process this with extreme ease. The example below demonstrates the difficulties encountered when this function is not used.

(Example 3)

Routine for drawing a horizontal line of a specified length from a specified coordinate using two alternating specified colors.

```
R1:Start X position
R2:Start Y position
R3:Color 1
R4:Color 0
R12:Line length

;LOOP return address
MOVE  R13,R15  ;Set LOOP return address.
;LOOP return address
FROM  R1
XOR  R2
AND  #1  ;Execute [R0=(R1 XOR R2)And 1].
BNE  DOPLOT
FROM  R3  ;When not zero, set R3 (color 1) to Sreg.
FROM  R4  ;When zero, set R4 (color 0) to Sreg.
DOPLOT: COLOR  ;Set value of Sreg in COLR.
PLOT
LOOP
NOP
```

Thus, if only the plotting functions are used, it takes time to determine which of the two colors to PLOT at a specified time. The bit 1 dither flag may be used to efficiently perform this type of drawing process. The dither mode is only functional in 4 color mode and 16 color mode.

When dither mode is set, the PLOT circuit checks the bit 0 value of the result when an XOR operation is performed on R1 (X coordinate) and R2 (Y coordinate). If the resultant bit 0=0, the low 4 bits of the COLR register are used as the color data for the PLOT instruction. However, if the resultant bit 0=1, the high 4 bits of the COLR register are used.

When the program in the previous example is written using the CMODE instruction, only the PLOT instruction is looped, as demonstrated below.

(Example 4)

```
IBT  R0,#2  ;Set to transparent and dither mode.
CMODE
FROM  R3
ADD  R3
ADD  R0
ADD  R0  ;Shift low 4 bits of COLOR1 to high 4 bits.
ADD  R4  ;Add value of R4 (COLOR0) to R0.
COLOR  ;Set COLR.
MOVE  R13,R15
;LOOP return address
LOOP
PLOT  ;Plot pixel.
```

Since the processing to determine whether or not a color is transparent is performed in parallel with the generation of plot data, dithering cannot be performed between a transparent color and a normal color. This mode can also be used in the 4 color mode.

8.1.4.3 BIT 2

To efficiently perform rotation/enlargement/reduction of OBJ data, a system is used in which each pixel of color data is stored at one address. When displaying a 16 color OBJ, half of the memory is wasted using this method. Memory may be conserved by storing two pixels of color data together in one byte. However, this requires a method for extracting two pixels of color data from one byte of data. Bit 2 of CMODE is used by the GSU to perform this function.

When the COLOR or GETC instruction is executed with bit 2 of CMODE set, the high 4 bits of the source register are written to the color register. If different OBJ data are stored in the high 4 bits and low 4 bits of the same memory area, this function permits the packed 8-bit data to be used without shift processing. This mode can also be used in 4 color mode.

8.1.4.4 BIT 3

If the COLOR or GETC instruction is executed in 256 color mode with bit 3 of CMODE set, only the low 4 bits of the COLR register can be written to the color register. The high 4 bits are fixed. This function enables the high 4 bits of the color register to be used in place of a palette in 256 color mode. In other words, characters of different colors can be drawn by plotting 16 color mode data while changing the value of the high 4 bits of the color register.

8.1.4.5 BIT 4

When bit 4 of CMODE is set, the mode which enables character data to be produced for OBJ. When this bit is 0, the mode is specified by HT0,HT1 of the SCMR. When switching the OBJ mode by changing this bit, it will be necessary to use the RPIX instruction to write the data to the game pak RAM which have already been written to the pixel caches.

{{BEGIN_DIAGRAM}}
This diagram shows the plot operations assigned by CMODE. The diagram has two main sections: "COLOR Instruction/GETC Instruction" and "PLOT instruction processing: Generates plot data". The "COLOR Instruction/GETC Instruction" section shows how the COLOR or GETC instruction writes to the COLOR REGISTER. The "PLOT instruction processing: Generates plot data" section shows how the PLOT instruction generates plot data. The diagram also shows the "PLOT Instruction Processing Transparent Mode Processing" section, which shows how the PLOT instruction processes transparent mode. The diagram shows the following inputs: Z[7:4], Z[3:4], Bit 2, Bit 3, SCMR Color Mode, Bit 3, COLR[7:0]. The diagram shows the following outputs: COLOR[7:4], COLOR[3:0], PLOT Enable/Disable. The diagram shows the following operations: Write inhibited by Bit 3, SELECT, AND, XOR, PLOT processing not performed.
{{END_DIAGRAM}}

---


## Description of Instructions

This chapter provides a detailed description of each instruction and its function. ROM and RAM execution times listed for each instruction refer to the game pak ROM and RAM. Special indicators and symbols are used throughout this chapter. These are defined in the following 3 tables.

### 9.1 OPERAND DESCRIPTIONS

| INDICATOR | DESCRIPTION |
| :--- | :--- |
| R₀ | Indicates internal register R₀. |
| Rₙ | A 16-bit general use register. |
| Rₙ' | A 16-bit general use register. |
| (Rₘ) | Indicates the value stored in the memory location specified by the contents of register Rₘ. |
| (xx) | Indicates the value stored in the memory location specified by the 16-bit value xx. |
| (yy) | Indicates the value stored in the memory location specified by the 9-bit value yy. (0≤yy≤510) |
| #n | Indicates 4-bit immediate data. |
| #xx | Indicates 16-bit immediate data. (0≤xx≤65535) |
| #pp | Indicates 8-bit immediate data. (-128≤pp≤127) |
| e | 1-byte data -128≤e≤127, that expresses the displacement in the relative addressing mode. |

### 9.2 FLAG DESCRIPTIONS

| SYMBOL | DESCRIPTION |
| :--- | :--- |
| 1 | Set |
| 0 | Reset |
| * | Set or reset according to results. |
| - | No change |

### 9.3 OPERATOR FUNCTIONS

| INDICATOR | DESCRIPTION |
| :--- | :--- |
| R₀ | Indicates internal register R₀. |
| Rₙ, Rₙ' | A 16-bit general use register specified by n. |
| (Rₘ) | Indicates a value stored in a memory location specified by the contents of register Rₘ. |
| (xx) | Indicates a value stored in a memory location specified by the 16-bit value xx. |
| (yy) | Indicates a value stored in a memory location specified by the 9-bit value yy. |
| #n | Indicates 4-bit immediate data. |
| #xx | Indicates 16-bit immediate data. (0≤xx≤65535) |
| #pp | Indicates 8-bit immediate data. (-128≤pp≤127) |
| e | 1-bit data (-128≤e≤127), that expresses displacement in the relative addressing mode. |
| S_reg | Source register |
| D_reg | Destination register |
| High-Byte | Upper byte of 16-bit data |
| Low-Byte | Lower byte of 16-bit data |
| → | Indicates direction of movement of data |
| + | Add |
| - | Subtract |
| * | Multiply |
| Rₙ, #n | 1's compliment |
| ALT1 | ALT1 Flag |
| ALT2 | ALT2 Flag |
| CY | Carry Flag |
| O/V | Overflow Flag |
| Z | Zero Flag |
| S | Sign Flag |
| B | B Flag |
| GO | Go Flag |


---

ADC Rn

Operation: S_reg + R_n + CY Flag → D_reg (n=0~15)

Description: This instruction adds the source register, the operand, and the carry flag. The result is stored in the destination register.

Source and destination registers are specified in advance using a WITH, FROM, or TO instruction. When not specified, these registers default to R0.

The operand can be any of registers R0~R15.

Flags affected:

| B | ALT1 | ALT2 | O/V | S | CY | Z |
|---|------|------|-----|---|----|---|
| 0 | 0    | 0    | *   | * | *  | * |

B: Reset
ALT1: Reset
ALT2: Reset
O/V: Set on signed overflow.
S: Set if result is negative, else reset
CY: Set on unsigned carry, else reset
Z: Set if result is zero.

Opcode:

(MSB) (LSB)
ADC Rn
0 0 1 1 1 0 1 (3DH)
0 1 0 1 n (0H~FH) (5nH)

Machine Cycles:
ROM execution time 6 cycles
RAM execution time 6 cycles
Cache RAM execution time 2 cycles

Example:
```
ADC R1 ; R0+R1+CY→R0
WITH R2 ; Set the source/destination registers to R2
ADC R3 ; R2+R3+CY→R2
ADC R2 ; R0+R2+CY→R0
```

---

ADC #n

Operation: S_reg + #n + CY Flag → D_reg (n=0~15)

Description: This instruction adds the source register, the immediate data specified by the operand #n, and the carry flag. The result is stored in the destination register.

Source and destination registers are specified in advance using a WITH, FROM, or TO instruction. When not specified, these registers default to R0.

The operand can be immediate data from 0~15.

Flags affected:

| B | ALT1 | ALT2 | O/V | S | CY | Z |
|---|------|------|-----|---|----|---|
| 0 | 0    | 0    | *   | * | *  | * |

B: Reset
ALT1: Reset
ALT2: Reset
O/V: Set on signed overflow.
S: Set if result is negative, else reset
CY: Set on unsigned carry, else reset
Z: Set if result is zero, else reset.

Opcode:

```
(MSB)           (LSB)
ADC #n          0 0 1 1 1 1 1 1
                 0 1 0 1 n (0H~FH)
```

Machine Cycles:
ROM execution time 6 cycles
RAM execution time 6 cycles
Cache RAM execution time 2 cycles

Example:
```
ADC  #9H      ; R0+0009H+CY→R0
FROM R3       ; Set the source register to R3
ADC  #5H      ; R3+0005H+CY→R0
ADC  #0AH     ; R0+000AH+CY→R0
```

---

ADD Rn

Operation: S_reg + R_n → D_reg (n=0~15)

Description: This instruction adds the source register and the register specified by the operand R_n. The result is stored in the destination register.

Source and destination registers are specified in advance using a WITH, FROM, or TO instruction. When not specified, these registers default to R0.

The operand can be any of registers R0~R15.

Flags affected:

| B | ALT1 | ALT2 | O/V | S | CY | Z |
|---|------|------|-----|---|----|---|
| 0 | 0    | 0    | *   | * | *  | * |

B: Reset
ALT1: Reset
ALT2: Reset
O/V: Set on signed overflow.
S: Set if result is negative, else reset
CY: Set on unsigned carry, else reset
Z: Set if result is zero.

Opcode:

(MSB) (LSB)
ADD Rn [0] [1] [0] [1] n (0H~FH) (5nH)

Machine Cycles:
ROM execution time 3 cycles
RAM execution time 3 cycles
Cache RAM execution time 1 cycle

Example: Under the following conditions:
S_reg: R0, D_reg: R0, R0=4283H, R4=2438H
R0=66BBH when ADD R4 is executed.

```
ADD  R4    ; R0+R4→R0
TO   R5    ; Set the destination register to R5
ADD  R6    ; R0+R6→R5
ADD  R3    ; R0+R3→R0
```

---

ADD #n

Operation: S_reg + #n → D_reg (n=0~15)

Description: This instruction adds the source register to the immediate data specified by the operand #n. The result is stored in the destination register.

Source and destination registers are specified in advance using a WITH, FROM, or TO instruction. When not specified, these registers default to R0.

The operand can be immediate data from 0-15.

Flags affected:

| B | ALT1 | ALT2 | O/V | S | CY | Z |
|---|------|------|-----|---|----|---|
| 0 | 0    | 0    | *   | * | *  | * |

B: Reset
ALT1: Reset
ALT2: Reset
O/V: Set on signed overflow, else reset.
S: Set if result is negative, else reset
CY: Set on unsigned carry, else reset.
Z: Set on zero result, else reset.

Opcode:

(MSB) (LSB)
ADD #n
```
0 0 1 1 1 1 0
0 1 0 1 n (0H~FH)
```
(3EH) (5nH)

Machine Cycles:
ROM execution time 6 cycles
RAM execution time 6 cycles
Cache RAM execution time 2 cycles

Example: Under the following conditions:
S_reg: R4, D_reg: R7, R4=3682H
R7 is 368AH when ADD #8H is executed.

```
ADD #8H    ; R4+0008H→R7
WITH R7    ; Set the source and destination registers to R7
ADD #2H     ; R7+0002H→R7
ADD R7      ; R0+R7→R0
```

---

ALT1
Flag Prefix Instruction

Operation: 1 → ALT1 Flag

Description: ALT1 is a prefix instruction used in combination with the instruction which follows. When ALT1 is executed, the Super FX sets the ALT1 flag in bit 8 of the status flag register (3030, 3031H). The ALT1 flag specifies the mode for the next instruction.

Flags affected:
| B | ALT1 | ALT2 | O/V | S | CY | Z |
|---|------|------|-----|---|----|---|
| - |  1   |  -   |  -  | - |  - | - |
ALT1: Set

Opcode:
(MSB)           (LSB)
ALT1  0 0 1 1 1 0 1  (3DH)

Machine Cycles:
ROM execution time     3 cycles
RAM execution time     3 cycles
Cache RAM execution time 1 cycles

Example: Execution of the ALT1 instruction sets the ALT1 flag. Various instructions can be executed, depending upon the instruction which follows the ALT1 prefix.

(Refer to, “ALT1 ($3D) +”, in the Super FX Opcode Matrix at the end of this chapter.)

---

ALT2
FLAG PREFIX INSTRUCTION
Operation: 1 → ALT2 Flag
Description: ALT2 is a prefix instruction used in combination with the instruction which follows. When ALT2 is executed, the Super FX sets the ALT2 flag in bit 9 of the status flag register (3030, 3031H). The ALT2 flag specifies the mode for the next instruction.
Flags affected:
| B | ALT1 | ALT2 | O/V | S | CY | Z |
|---|------|------|-----|---|----|---|
| - |  -   |  1   |  -  | - |  - | - |
Opcode:
(MSB)           (LSB)
ALT2  0 0 1 1 1 1 0  (3EH)
Machine Cycles:
ROM execution time     3 cycles
RAM execution time     3 cycles
Cache RAM execution time  1 cycles
Example: Execution of the ALT2 instruction sets the ALT2 flag. Various instructions can be executed, depending upon the instruction which follows the ALT2 prefix.
(Refer to, “ALT2 ($3E) +”, in the Super FX Opcode Matrix at the end of this chapter.)

---

ALT3

FLAG PREFIX INSTRUCTION

Operation:
1 → ALT1 Flag
1 → ALT2 Flag

Description:
ALT3 is a prefix instruction used in combination with the instruction which follows. When ALT3 is executed, the Super FX sets the ALT1 and ALT2 flags in bits 8 and 9 of the status flag register (3030, 3031H).

These flags specify the mode for the next instruction.

Flags affected:
| B | ALT1 | ALT2 | O/V | S | CY | Z |
|---|------|------|-----|---|----|---|
| - |  1   |  1   |  -  | - |  - | - |
ALT1: Set
ALT2: Set

Opcode:
(MSB)           (LSB)
ALT3  0 0 1 1 1 1 1 1  (3FH)

Machine Cycles:
ROM execution time     3 cycles
RAM execution time     3 cycles
Cache RAM execution time  1 cycles

Example:
Execution of the ALT3 instruction sets the ALT 1 and ALT 2 flags. Various instructions can be executed, depending upon the instruction which follows the ALT3 prefix.

(Refer to, “ALT3 ($3F) +”, in the Super FX Opcode Matrix at the end of this chapter.)

---

AND Rn

Operation: S_reg AND R_n → D_reg (n=1~15)

Description: This instruction performs logical AND on corresponding bits of the source register and the operand R_n. The result is stored in the destination register.

Source and destination registers are specified in advance using a WITH, FROM, or TO instruction. When not specified, these registers default to R_0.

The operand can be any of registers R_1~R_15.

Flags affected:

| B | ALT1 | ALT2 | O/V | S | CY | Z |
|---|------|------|-----|---|----|---|
| 0 | 0    | 0    | -   | * | -  | * |

B: Reset
ALT1: Reset
ALT2: Reset
S: Set if result is negative, else reset
Z: Set on zero result, else reset.

Opcode:
(MSB) (LSB)
ADD R_n
[0] [1] [1] [1] n (1H~FH) (7nH)

Machine Cycles:
ROM execution time 3 cycles
RAM execution time 3 cycles
Cache RAM execution time 1 cycles

Example:
```
AND R8     ; R0 AND R8 → R0
(163AH) (00FFH) → (003AH)

FROM R9    ; Set the source register to R9
TO R10      ; Set the destination register to R10
AND R7      ; R9 AND R7 → R10
(55AAH) (FF00H) → (5500H)
```

---

AND #n

Operation: Sreg AND #n→ Dreg (n=1~15)

Description: This instruction performs logical AND on corresponding bits of the source register and the immediate data specified by the operand #n. The result is stored in the destination register.

Source and destination registers are specified in advance using a WITH, FROM, or TO instruction. When not specified, these registers default to R₀.

The operand can be immediate data from 1~15.

Flags affected:

| B | ALT1 | ALT2 | O/V | S | CY | Z |
|---|------|------|-----|---|----|---|
| 0 | 0    | 0    | -   | * | -  | * |

B: Reset
ALT1: Reset
ALT2: Reset
S: Set if result is negative, else reset
Z: Set on zero result, else reset.

Opcode:

(MSB) (LSB)
ADD #n
```
0 0 1 1 1 1 0
0 1 1 1 n (1H~FH)
```
(3EH) (7nH)

Machine Cycles:
ROM execution time 6 cycles
RAM execution time 6 cycles
Cache RAM execution time 2 cycles

Example:
When register R₀ is, "3E5DH (0011 1110 0101 1101B)",
```
AND #6H
```
will result in,
R₀ = "0004H (0000 0000 0000 0100B)".

---

ASR

Operation:
The diagram depicts an arithmetic shift right operation. The 16-bit source register (S_reg) is shifted one bit to the right. The bit that was originally at position D0 (least significant bit) is moved into the carry flag (CY). The bit at position D15 (most significant bit) remains unchanged. The result of the shift is stored in the destination register (D_reg).

Description:
This instruction shifts all bits in the source register one bit to the right. Bit 0 goes into the carry flag and bit 15 is unaffected. The result is stored in the destination register.

Source and destination registers are specified in advance using a WITH, FROM, or TO instruction. When not specified, these registers default to R0.

Flags affected:
| B | ALT1 | ALT2 | O/V | S | CY | Z |
|---|------|------|-----|---|----|---|
| 0 | 0    | 0    | -   | * | *  | * |

B: Reset
ALT1: Reset
ALT2: Reset
S: Set if result is negative, else reset
CY: Set if bit 0 in the source register is "1", else reset
Z: Set on zero result, else reset.

Opcode:
(MSB) (LSB)
ASR
1 0 0 1 0 1 1 0 (96H)

Machine Cycles:
ROM execution time 3 cycles
RAM execution time 3 cycles
Cache RAM execution time 1 cycles

Example:
Under the following conditions,
S_reg: R10, D_reg: R1
CY bit 15 bit0
0 R10 0 1 0 0 1 1 1 0 1 1 1 0 1 1 (4F7BH)

When ASR is executed, the carry flag and R1 are:
CY bit15 bit0
1 R1 0 0 1 0 0 1 1 1 0 1 1 1 0 1 (27BDH)

---

BCC e

Operation:
If CY Flag=0
then R₁₅+e→R₁₅
(e= -128 ~+127)
R₁₅ identifies the next address for the BCC instruction (2 bytes)

Description:
If the carry flag is "0", add "e" to the contents of the program counter R₁₅ and JUMP to the address indicated by the resulting value in the program counter.

If the carry flag is "1", do not jump.

The relative offset can be -128 to +127 bytes from the address following the code for "e".

If the decision results in a JUMP, the next instruction to be executed will already be in the instruction pipeline of the processor. For this reason one byte from the pipeline will be executed before the instruction at the branch destination is executed. (The execution time for this instruction is not included in the machine cycles listed below.)

Flags affected:
| B | ALT1 | ALT2 | O/V | S | CY | Z |
|---|------|------|-----|---|----|---|
| - | -    | -    | -   | - | -  | - |

Opcode:
(MSB) (LSB)
BCC e
0 0 0 0 1 1 0 0 (0CH)
← e (00H~FFH) → Relative address

Note:
The number "e" (number, label, formula) which shows the jump destination is given in the assembler as an operand.

Machine Cycles:
ROM execution time 6 cycles
RAM execution time 6 cycles
Cache RAM execution time 2 cycles

Example:
In the following example, the carry flag is zero and the program jumps forward 5 bytes from the execution address of the instruction.

BCC $+5H

The relationship between the program and the program counter is as follows:

PC ADDRESS Object Code
51E 0C (BCC $+5H)
51F 03
520 ←PC before jump Execute instruction at address 520 and jump.
521
522 ←PC after jump
523
. .

---

BCS e

Operation:
If CY Flag=1
then R₁₅+e→R₁₅
(e = -128~+127)
R₁₅ identifies the next address for the BCS instruction (2 bytes)

Description:
If the carry flag is "1", add "e" to the program counter R₁₅ and JUMP to the address indicated by the resulting value in the program counter.

If the carry flag is "0", do not jump.

The relative offset can be -128 to +127 bytes from the address following the code for "e".

If the decision results in a JUMP, the next instruction to be executed will already be in the instruction pipeline of the processor. For this reason one byte from the pipeline will be executed before the instruction at the branch destination is executed. (The execution time for this instruction is not included in the machine cycles listed below.)

Flags affected:
| B | ALT1 | ALT2 | O/V | S | CY | Z |
|---|------|------|-----|---|----|---|
| - | -    | -    | -   | - | -  | - |

No flags affected

Opcode:
(MSB) (LSB)
BCS e
```
0 0 0 0 1 1 0 1
```
(0DH)
Relative address

Note:
The number "e" (number, label, formula) which shows the jump destination is given in the assembler as an operand.

Machine Cycles:
ROM execution time 6 cycles
RAM execution time 6 cycles
Cache RAM execution time 2 cycles

Example:
In the following example, the carry flag is set and the program jumps backward 1 byte from the execution address of the instruction.

BCS $-1H

The relationship between the program and the program counter is as follows:

PC ADDRESS Object Code
42D ←PC after jump
42E 0D (BCS $-1H)
42F FD
430 ←PC before jump Execute instruction at address 430 and jump.
431
. .
. .

---

BEQ e

Operation:
If Z Flag=1
then R₁₅+e→R₁₅
(e= -128~+127)
R₁₅ identifies the next address for the BEQ instruction (2 bytes)

Description:
If the zero flag is "1", add "e" to the program counter R₁₅ and JUMP to the address indicated by the resulting value in the program counter.

If the zero flag is "0", do not jump.

The relative offset can be -128 to +127 bytes from the address following the code for "e".

If the decision results in a JUMP, the next instruction to be executed will already be in the instruction pipeline of the processor. For this reason one byte from the pipeline will be executed before the instruction at the branch destination is executed. (The execution time for this instruction is not included in the machine cycles listed below.)

Flags affected:
B ALT1 ALT2 O/V S CY Z
- - - - - - -

Opcode:
(MSB) (LSB)
0 0 0 0 1 0 0 1 (09H)
BEQ e ← e (00H~FFH) → Relative address

Note:
The number "e" (number, label, formula) which shows the jump destination is given in the assembler as an operand.

Machine Cycles:
ROM execution time 6 cycles
RAM execution time 6 cycles
Cache RAM execution time 2 cycles

Example:
In the following example, the zero flag is set and the program jumps ahead 5 bytes from the execution address of the instruction.

BEQ $+5H

The relationship between the program and program counter is as follows:

PC ADDRESS Object Code
15FD
15FE 09 □ (BEQ $+5H)
15FF 03
1600 ←PC before jump Execute instruction at address 1600 and jump.
1601
1602
1603 ←PC after jump

---

BGE e

Operation:
If (S XOR O/V)=0
then R₁₅+e→R₁₅
(e = -128~+127)
R₁₅ identifies the next address for the BGE instruction (2 bytes)

Description:
If the sign flag and the overflow flag are equal, add “e” to the program counter R₁₅ and JUMP to the address indicated by the resulting value in the program counter.

If the values are different, do not jump.

The relative offset can be -128 to +127 bytes from the address following the code for “e”.

If the decision results in a JUMP, the next instruction to be executed will already be in the instruction pipeline of the processor. For this reason one byte from the pipeline will be executed before the instruction at the branch destination is executed. (The execution time for this instruction is not included in the machine cycles listed below.)

Flags affected:
| B | ALT1 | ALT2 | O/V | S | CY | Z |
|---|------|------|-----|---|----|---|
| - | -    | -    | -   | - | -  | - |
No flags affected

Opcode:
(MSB) (LSB)
BGE e
0 0 0 0 0 1 1 1 (07H)
← e (00H~FFH) → Relative address

Note:
The number “e” (number, label, formula) which shows the jump destination is given in the assembler as an operand.

Machine Cycles:
ROM execution time 6 cycles
RAM execution time 6 cycles
Cache RAM execution time 2 cycles

Example:
In the following example, the sign flag and over flag are set and the program jumps backward 3 bytes from the execution address of the instruction.

BGE $-3H

The relationship between the program and program counter is as follows:

PC ADDRESS Object Code
22FA ←PC after jump
22FB
22FC
22FD
22FE 07 FB (BGE $-3H)
22FF ←PC before jump
2300 Execute instruction at address 2300 and jump.

---

BIC Rn

Operation:
S_reg AND ¬Rn → D_reg
(n=1~15)

Description:
This instruction performs logical AND on corresponding bits of the source register and the 1's complement of register specified in the operand Rn. The result is stored in the destination register.

The source and destination registers are specified in advance using a WITH, FROM, or TO instruction. When not specified, these registers default to R0.

The operand can be any of registers R1~R15.

Flags affected:

| B | ALT1 | ALT2 | O/V | S | CY | Z |
|---|------|------|-----|---|----|---|
| 0 | 0    | 0    | -   | * | -  | * |

B: Reset
ALT1: Reset
ALT2: Reset
S: Set if result is negative, else reset
Z: Set on zero result, else reset.

Opcode:
(MSB) (LSB)
BIC Rn
```
0 0 1 1 1 1 0 1
0 1 1 1 n (1H~FH)
```
(3DH) (7nH)

Machine Cycles:
ROM execution time 6 cycles
RAM execution time 6 cycles
Cache RAM execution time 2 cycles

Example: Under the following conditions:
S_reg: R2, D_reg: R0
R2=75CEH (0111 0101 1100 1110B),
R1=3846H (0011 1000 0100 0110B)
R0 is 4588H (0100 0101 1000 1000B) when
BIC R1
is executed.

---

BIC #n

Operation: Sreg AND ¬n → Dreg (n=1~15)

Description: This instruction performs logical AND on corresponding bits of the source register and the 1's complement of the immediate data specified in the operand #n. The result is stored in the destination register.

The source and destination registers are specified in advance using a WITH, FROM, or TO instruction. When not specified, these registers default to R0.

The operand can be immediate data from 1~15.

Flags affected:

| B | ALT1 | ALT2 | O/V | S | CY | Z |
|---|------|------|-----|---|----|---|
| 0 | 0    | 0    | -   | * | -  | * |

B : Reset
ALT1 : Reset
ALT2 : Reset
S : Set if result is negative, else reset
Z : Set on zero result, else reset.

Opcode:

(MSB) (LSB)
BIC #n
0 0 1 1 1 1 1 (3FH)
0 1 1 1 n (1H~FH) (7nH)

Machine Cycles:
ROM execution time 6 cycles
RAM execution time 6 cycles
Cache RAM execution time 2 cycles

Example: Under the following conditions:

Sreg: R4, Dreg: R5
R4= 364BH (0011 0110 0100 1011B)
R5 is 3640H (0011 0110 0100 0000B) when
BIC #F
is executed.

---

BLT e

Operation:
If (S XOR O/V)=1
then R₁₅+e→R₁₅
(e= -128~+127)
R₁₅ identifies the next address for the BLT instruction (2 bytes)

Description:
If the sign flag and the overflow flag are different, add “e” to the program counter R₁₅ and read the next instruction at the location indicated by the resulting value in the program counter.

If the values are the same, do not jump.

The relative offset can be -128 ~ +127 bytes from the address following the code for “e”.

If the decision results in a JUMP, the next instruction to be executed will already be in the instruction pipeline of the processor. For this reason one byte from the pipeline will be executed before the instruction at the branch destination is executed. (The execution time for this instruction is not included in the machine cycles listed below.)

Flags affected:
B ALT1 ALT2 O/V S CY Z
- - - - - - -

Opcode:
(MSB) (LSB)
BLT e
0 0 0 0 0 1 1 0 (06H)
← e (00H~FFH) → Relative address

Note:
The number “e” (number, label, formula) which shows the jump destination is given in the assembler as an operand.

Machine Cycles:
ROM execution time 6 cycles
RAM execution time 6 cycles
Cache RAM execution time 2 cycles

Example:
In the following example, the sign flag is set and the overflow flag is reset. The program jumps forward 4 bytes from the execution address of the instruction.

BLT $+4H

The relationship between the program and program counter is as follows:

PC ADDRESS Object Code
BBD BBE 06 (BLT $+4H)
BBF 02 ←PC before jump Execute instruction at address BC0 and jump.
BC0
BC1 ←PC after jump
BC2

---

BMI e

Operation:
If S Flag = 1
then R₁₅ + e → R₁₅
(e = -128 ~ +127)
R₁₅ identifies the next address for the BMI instruction (2 bytes)

Description:
If the sign flag is "1", add "e" to the program counter R₁₅ and read the next instruction at the location indicated by the resulting value in the program counter.
If the sign flag is "0", do not jump.
The relative offset can be -128 ~ +127 bytes from the address following the code for "e".
If the decision results in a JUMP, the next instruction to be executed will already be in the instruction pipeline of the processor. For this reason one byte from the pipeline will be executed before the instruction at the branch destination is executed. (The execution time for this instruction is not included in the machine cycles listed below.)

Flags affected:
B ALT1 ALT2 O/V S CY Z
- - - - - - -

Opcode:
(MSB) (LSB)
0 0 0 0 1 0 1 1 (0BH)
BMI e
← e (00H~FFH) →
Relative address

Note:
The number "e" (number, label, formula) which shows the jump destination is given in the assembler as an operand.

Machine Cycles:
ROM execution time 6 cycles
RAM execution time 6 cycles
Cache RAM execution time 2 cycles

Example:
In the following example, the sign flag is set and the program jumps forward 5 bytes from the execution address of the instruction.
BMI $+5H

The relationship between the program and program counter is as follows:

PC ADDRESS Object Code
57D
57E 0B (BMI $+5H)
57F 03
580 ←PC before jump Execute instruction at address 580 and jump.
581
582
583 ←PC after jump

---

BNE e

Operation:
If Z Flag = 0
then R₁₅ + e → R₁₅
(e = -128 ~ +127)
R₁₅ identifies the next address for the BNE instruction (2 bytes)

Description:
If the zero flag is "0", add "e" to the program counter R₁₅ and read the next instruction at the location indicated by the resulting value in the program counter.

If the zero flag is "1", do not jump.

The relative offset can be -128 ~ +127 bytes from the address following the code for "e".

If the decision results in a JUMP, the next instruction to be executed will already be in the instruction pipeline of the processor. For this reason one byte from the pipeline will be executed before the instruction at the branch destination is executed. (The execution time for this instruction is not included in the machine cycles listed below.)

Flags affected:
B ALT1 ALT2 O/V S CY Z
- - - - - - -

No flags affected

Opcode:
(MSB) (LSB)
BNE e
0 0 0 0 1 0 0 0 (08H)
← e (00H~FFH) →
Relative address

Note:
The number "e" (number, label, formula) which shows the jump destination is given in the assembler as an operand.

Machine Cycles:
ROM execution time 6 cycles
RAM execution time 6 cycles
Cache RAM execution time 2 cycles

Example:
In the following example, the zero flag is reset and the program jumps backward 2 bytes from the execution address of the instruction.

BNE $-2H

The relationship between the program and program counter is as follows:

PC ADDRESS Object Code
35FB ←PC after jump Execute instruction at address 3600 and jump.
35FC
35FD
35FE 08 □ (BNE $-2H)
35FF
3600 ←PC before jump
3601

---

BPL e

Operation:
If S Flag = 0
then R₁₅ + e → R₁₅
(e = -128 ~ +127)
R₁₅ identifies the next address for the BPL instruction (2 bytes)

Description:
If the sign flag is "0", add "e" to the program counter R₁₅ and read the next instruction at the location indicated by the resulting value in the program counter.

If the sign flag is "1", do not jump.

The relative offset can be -128 ~ +127 bytes from the address following the code for "e".

If the decision results in a JUMP, the next instruction to be executed will already be in the instruction pipeline of the processor. For this reason one byte from the pipeline will be executed before the instruction at the branch destination is executed. (The execution time for this instruction is not included in the machine cycles listed below.)

Flags affected:
| B | ALT1 | ALT2 | O/V | S | CY | Z |
|---|------|------|-----|---|----|---|
| - | -    | -    | -   | - | -  | - |

Opcode:
(MSB) (LSB)
BPL e
0 0 0 0 1 0 1 0 (0AH)
← e (00H~FFH) →
Relative address

Note:
The number "e" (number, label, formula) which shows the jump destination is given in the assembler as an operand.

Machine Cycles:
ROM execution time 6 cycles
RAM execution time 6 cycles
Cache RAM execution time 2 cycles

Example:
In the following example, the sign flag is reset and the program jumps forward 4 bytes from the execution address of the instruction.

BPL $+4H

The relationship between the program and program counter is as follows:

PC ADDRESS Object Code
95D
95E 0A (BPL $+4H)
95F 02
960 ←PC before jump Execute instruction at address 960 and jump.
961
962 ←PC after jump
963

---

BRA e

Operation: R15+e→R15 (e= -128~+127)
R15 identifies the next address for the BRA instruction (2 bytes)

Description:
Regardless of the status of the flags, add "e" to the program counter R15 and read the next instruction at the location indicated by the resulting value in the program counter.

The relative offset can -128 ~ +127 bytes from the address following the code for "e".

When a JUMP occurs, the next instruction to be executed will already be in the instruction pipeline of the processor. For this reason one byte from the pipeline will be executed before the instruction at the branch destination is executed. (The execution time for this instruction is not included in the machine cycles listed below.)

Flags affected:
B ALT1 ALT2 O/V S CY Z
- - - - - - -

No flags affected

Opcode:
(MSB) (LSB)
0 0 0 0 0 1 0 1 (05H)
BRA e
← e (00H~FFH) →
Relative address

Note:
The number "e" (number, label, formula) which shows the jump destination is given in the assembler as an operand.

Machine Cycles:
ROM execution time 6 cycles
RAM execution time 6 cycles
Cache RAM execution time 2 cycles

Example:
In the following example, the program jumps backward to the execution address of the instruction.

BRA $0H

The relationship between the program and program counter is as follows:

PC ADDRESS Object Code
B0FC
B0FD
B0FE 05 ←PC after jump ← Execute
B0FF FE (BRA $0H) instruction at
B100 ←PC before jump address B100
B101 and jump.

---

BVC e

Operation:
If O/V Flag=0
then R₁₅+e→R₁₅
(e= -128~+127)
R₁₅ identifies the next address for the BVC instruction (2 bytes)

Description:
If the overflow flag is "0", add "e" to the program counter R₁₅ and read the next instruction at the location indicated by the resulting value in the program counter.

If the overflow flag is "1", do not jump.

The relative offset can be -128 ~ +127 bytes from the address following the code for "e".

If the decision results in a JUMP, the next instruction to be executed will already be in the instruction pipeline of the processor. For this reason one byte from the pipeline will be executed before the instruction at the branch destination is executed. (The execution time for this instruction is not included in the machine cycles listed below.)

Flags affected:
| B | ALT1 | ALT2 | O/V | S | CY | Z |
|---|------|------|-----|---|----|---|
| - | -    | -    | -   | - | -  | - |
No flags affected

Opcode:
(MSB) (LSB)
BVC e
```
0 0 0 0 1 1 1 0
```
(0EH)
Relative address
e (00H~FFH)

Note:
The number "e" (number, label, formula) which shows the jump destination is given in the assembler as an operand.

Machine Cycles:
ROM execution time 6 cycles
RAM execution time 6 cycles
Cache RAM execution time 2 cycles

Example:
In the following example, the overflow flag is reset and the program jumps forward 4 bytes from the execution address of the instruction.

BVC $+4H

The relationship between the program and program counter is as follows:

PC ADDRESS Object Code
288D
288E 0E □ (BVC $+4H)
288F 02
2890 ←PC before jump Execute instruction at address 2890 and jump.
2891
2892 ←PC after jump
2893

---

BVS e

Operation:
If O/V Flag=1
then R₁₅+e→R₁₅
(e = -128~+127)
R₁₅ identifies the next address for the BVS instruction (2 bytes)

Description:
If the overflow flag is "1", add "e" to the program counter R₁₅ and read the next instruction at the location indicated by the resulting value in the program counter.

If the overflow flag is "0", do not jump.

The relative offset can be -128 ~ +127 bytes from the address following the code for "e".

If the decision results in a JUMP, the next instruction to be executed will already be in the instruction pipeline of the processor. For this reason one byte from the pipeline will be executed before the instruction at the branch destination is executed. (The execution time for this instruction is not included in the machine cycles listed below.)

Flags affected:
| B | ALT1 | ALT2 | O/V | S | CY | Z |
|---|------|------|-----|---|----|---|
| - | -    | -    | -   | - | -  | - |
No flags affected

Opcode:
(MSB) (LSB)
BVS e
0 0 0 0 1 1 1 1
← e (00H~FFH) →
Relative address

Note:
The number "e" (number, label, formula) which shows the jump destination is given in the assembler as an operand.

Machine Cycles:
ROM execution time 6 cycles
RAM execution time 6 cycles
Cache RAM execution time 2 cycles

Example:
In the following example, the overflow flag is set and the program jumps backward 2 bytes from the execution address of the instruction.

BVS $-2H

The relationship between the program and program counter is as follows:

PC ADDRESS Object Code
68B ←PC after jump Execute instruction at address 690 and jump.
68C
68D
68E 0F (BVS $-2H)
68F FC
690 ←PC before jump
691

---

CACHE

Operation:
If CACHE BASE REGISTER <> (R₁₅ & 0FFF0H)
then (R₁₅ & 0FFF0H) → CACHE BASE REGISTER

Description:
When the cache base register is equal to the address with the lower 4 bits of the program counter at 0, nothing occurs. When it is not equal to this address, reset all cache flags and set the cache base register to that value.

Flags affected:
| B | ALT1 | ALT2 | O/V | S | CY | Z |
|---|------|------|-----|---|----|---|
| 0 | 0    | 0    | -   | - | -  | - |

B : Reset
ALT1 : Reset
ALT2 : Reset

Opcode:
(MSB) (LSB)
CACHE 0 0 0 0 0 0 1 0 (02H)

Machine Cycles:
ROM execution time 3~4 cycles
RAM execution time 3~4 cycles
Cache RAM execution time 1 cycle

---

CMODE
Operation: S_reg (b4~b0) → PLOT OPTIONS REGISTER
Description: This instruction loads the lower 5 bits of the source register into the plot options register. The instruction is used to specify the PLOT, COLOR, and GETC execution modes.
Bit 0 - Transparency Flag
0 = Transparency ON
If transparency is on and the color register is "0", the plot circuit only changes the X coordinate. When transparency is on and the color register is other than "0", the normal plotting operation is performed.
1 = Transparency OFF
The normal plotting operation is performed when transparency is off.
Bit 1 - Dither Flag
Bit 1 is only valid in the 16-color mode. When Bit 1 is "1" and the values of bit 0 in registers R1 and R2 are the same, the lower 4 bits in the color register are plotted. When bit 0 of registers R1 and R2 are different, the upper 4 bits in the color register are plotted.
Note: When transparency is on and the 4 bits to be plotted are "0", only the X coordinate is changed.
Bit 2 - Upper 4 Bits Color
Bit 2 is valid in the 16-color and 256-color modes. In the 256-color mode, Bit 3 must be set to a logic "1".
When Bit 2 is "1", the upper 4 bits in the source register are stored in the lower 4 bits of the color register while processing the COLOR and GETC instructions. This allows the data for two pixels to be stored in one byte.
Bit 3 - 256 Color Mode Only
Set Bit 3, "1", in the 256-color mode to fix the upper 4 bits of the color register while processing the COLOR and GETC instructions and change the lower 4 bits only.
Bit 4 - Sprite Mode
Set Bit 4, "1", to specify the bitmap in the sprite mode.

Flags affected:
B ALT1 ALT2 O/V S CY Z
0 0 0 - - - -
B : Reset
ALT1 : Reset
ALT2 : Reset

Opcode:
(MSB) (LSB)
CMODE 0 0 1 1 1 0 1 (3DH)
0 1 0 0 1 1 0 (4EH)

Machine Cycles:
ROM execution time 6 cycles
RAM execution time 6 cycles
Cache RAM execution time 2 cycles

Example:
Under the following conditions,
Sreg: R0, R0= 0002H
the transparency and dithering modes are set when
CMODE
is executed.

---

CMP Rn

Operation: Sreg - Rn (n=0~15)

Description: This instruction subtracts the operand Rn from the source register and sets the flags accordingly. The result of the subtraction is not stored.

The source register is specified in advance using a FROM or WITH instruction. When not specified, the source register defaults to R0.

The operand can be R0~R15.

Flags affected:

| B | ALT1 | ALT2 | O/V | S | CY | Z |
|---|------|------|-----|---|----|---|
| 0 | 0    | 0    | *   | * | *  | * |

B : Reset
ALT1 : Reset
ALT2 : Reset
O/V : Set on overflow, else reset
S : Set when the result is negative, else reset.
CY : Set on unsigned borrow, else reset.
Z : Set on zero result, else reset

Opcode:

(MSB) (LSB)
CMP Rn
0 0 1 1 1 1 1 (3FH)
0 1 1 0 n (0H~FH) (6nH)

Machine Cycles:
ROM execution time 6 cycles
RAM execution time 6 cycles
Cache RAM execution time 2 cycles

Example: Under the following conditions,

Sreg: R1, R1=8000H, R3=2FFFH

the overflow and carry flags are set and sign and zero flags are reset when

```
CMP R3
```

is executed.

---

COLOR
Operation: S_reg → Color register
Description: This instruction loads the lower 8 bits of the source register into the color register as the color value.
Note: The value in the color register is stored in the color matrix (8 rows x 8 columns) with the PLOT instruction. When the PLOT instruction has been executed eight times or either of registers R₁ or R₂ is changed, the data is changed automatically to character data format and stored in the game pak RAM.
Flags affected:
| B | ALT1 | ALT2 | O/V | S | CY | Z |
|---|---|---|---|---|---|---|
| 0 | 0 | 0 | - | - | - | - |
B: Reset
ALT1: Reset
ALT2: Reset
Opcode:
(MSB) (LSB)
COLOR 0 1 0 0 1 1 1 0 (4EH)
Machine Cycles:
ROM execution time 3 cycles
RAM execution time 3 cycles
Cache RAM execution time 1 cycles
Example: Under the following conditions:
S_reg: R₆, R₆= 9830H
the color register becomes 30H when
COLOR
is executed.

---

DEC Rn
Operation: Rn - 1 → Rn (n=0~14)
Description: This instruction decrements the register specified in the operand Rn by 1 and stores the result back in the same register. The register used can be R0-R14.
Flags affected:
| B | ALT1 | ALT2 | O/V | S | CY | Z |
|---|---|---|---|---|---|---|
| 0 | 0 | 0 | - | * | - | * |
B: Reset
ALT1: Reset
ALT2: Reset
S: Set when the result is negative, else reset.
Z: Set on zero result, else reset
Opcode: (MSB) (LSB)
DEC Rn [1] [1] [1] [0] n (0H~EH) (EnH)
Machine Cycles:
ROM execution time 3 cycles
RAM execution time 3 cycles
Cache RAM execution time 1 cycles
Example: Under the following conditions:
R9 = A3F7H
when the following instruction is executed
DEC R9
R9 becomes A3F6H.

---

DIV2

Operation:
If (S_reg) = -1 then 0 → (D_reg)
else ASR (S_reg) → (D_reg)

Description:
This instruction automatically shifts all bits in the source register right one place. The result is stored in the destination register. (Refer to ASR instruction for details.) If the source register data is FFFFH, the result stored in the destination register is 0000H.

The source and destination registers are specified in advance using a FROM, WITH, or TO instruction. When not specified, these registers default to R0.

Flags affected:

| B | ALT1 | ALT2 | O/V | S | CY | Z |
|---|------|------|-----|---|----|---|
| 0 | 0    | 0    | -   | * | *  | * |

B : Reset
ALT1 : Reset
ALT2 : Reset
S : Set when the result is negative, else reset.
CY : Set when Bit 0 of the source register is "1" and reset when "0".
Z : Set on zero result, else reset

Opcode:
(MSB) (LSB)
DIV2
```
0 0 1 1 1 1 0 1
1 0 0 1 0 1 1 0
```
(3DH)
(96H)

Machine Cycles:
ROM execution time 6 cycles
RAM execution time 6 cycles
Cache RAM execution time 2 cycles

Example:
Under the following conditions,
S_reg: R7, D_reg: R2
CY 0
R7: Bit15 0 1 0 0 0 1 1 0 0 1 1 0 1 (4635H)
Bit0

becomes
CY 1
R2: Bit15 0 0 1 0 0 1 1 0 0 1 1 0 1 0 (231AH)
Bit0

when
DIV2
is executed.

---

FMULT

Operation:
The diagram illustrates the data flow for the FMULT instruction. It shows that the 16-bit value from the source register (S_reg) is multiplied by the 16-bit value in register R6. The result is a 32-bit signed integer, split into an upper 16 bits and a lower 16 bits. The upper 16 bits are stored in the destination register (D_reg). The lower 16 bits are not stored but are used to set the carry flag (CY). The diagram also indicates that the destination register is implicitly R4 if not otherwise specified, though the text clarifies that R4 is not a valid destination.

Description:
This instruction performs a 16 x 16-bit signed multiplication with the source register and R6. The upper 16 bits of the 32-bit result are stored in the destination register. Bit 15 of the 32-bit result becomes the carry flag.

The source and destination registers are specified in advance using a FROM, WITH, or TO instruction. When not specified, these registers default to R0.

Note: Any register, R0~R15, except R4 may be assigned as the destination register.

Flags affected:
| B | ALT1 | ALT2 | O/V | S | CY | Z |
|---|---|---|---|---|---|---|
| 0 | 0 | 0 | - | * | * | * |

B: Reset
ALT1: Reset
ALT2: Reset
S: Set when the result is negative, else reset.
CY: Set when Bit 15 of the result is "1" and reset when "0".
Z: Set if the upper 16 bits of result are zero, else reset.

Opcode:
(MSB) (LSB)
FMULT
1 0 0 1 1 1 1 (9FH)

Machine Cycles:
ROM execution time: 11 or 7 cycles
RAM execution time: 11 or 7 cycles
Cache RAM execution time: 8 or 4 cycles

Note: The number of machine cycles depends on the CFGR register.

Example:
Under the following conditions,
S_reg: R5, D_reg: R2, R5= 4AAAH, R6= DAABH
R2 becomes F51CH and the carry flag and sign flag are set when
FMULT
is executed.

---

FROM Rn

REGISTERS PREFIX INSTRUCTION

Operation:
If B = 0 then set S_reg to R_n (n=0~15)
else R_n → D_reg

Description:
This instruction specifies which of the registers, R_0~R_15, is to be used as the source register. If the B flag is set, the contents of the specified operand R_n are stored in the destination register D_reg, which is specified using the WITH instruction. (Refer to the MOVES instruction.)

Flags affected:
B ALT1 ALT2 O/V S CY Z
- - - - - - -

Opcode:
(MSB) (LSB)
FROM R_n 1 0 1 1 n (0H~FH) (BnH)

Machine Cycles:
ROM execution time 3 cycles
RAM execution time 3 cycles
Cache RAM execution time 1 cycles

Example:
Execute
FROM R_2
to set R_2 as the source register.

To perform R_2 + R_3 = R_0, write:
```
FROM R_2    ;Sets the source register to R_2
ADD R_3      ;Executes R_2 + R_3 → R_0
```

---

GETB

Operation:
The diagram illustrates the data flow for the GETB instruction. The ROM Buffer (D7-D0) provides the lower 8 bits of data. This data is written into the lower 8 bits (D7-D0) of the destination register (Dreg). The upper 8 bits (D15-D8) of the destination register are reset to 00H. The arrow from the ROM Buffer to the Dreg indicates the data transfer direction.

Description:
This instruction loads one byte of data stored in the ROM buffer into the lower 8 bits of the destination register and resets the upper 8 bits of the destination register. Register R₁₄ is the ROM address pointer when data is loaded from the game pak ROM into the ROM buffer. Using the value stored at R₁₄ for the game pak ROM address, data is read from game pak ROM to the ROM buffer.

Banks are specified in advance using the ROMB instruction. However, changing banks using the ROMB instruction does not in itself trigger a ROM load.

The destination register is specified in advance using a WITH or TO instruction. When not specified, this register defaults to R₀.

Flags affected:
| B | ALT1 | ALT2 | O/V | S | CY | Z |
|---|---|---|---|---|---|---|
| 0 | 0 | 0 | - | - | - | - |

B: Reset
ALT1: Reset
ALT2: Reset

Opcode:
(MSB) (LSB)
GETB 1 1 1 0 1 1 1 (EFH)

Machine Cycles:
ROM execution time 3~8 cycles
RAM execution time 3~9 cycles
Cache RAM execution time 1~6 cycles

Note:
Because the ROM buffer is used, the number of execution cycles varies with each program.

Example:
Under the following conditions,
ROM buffer=0075H, Dreg:R₀
R₀ becomes 0075H when
GETB
is executed.

---

GETBH

Operation:
The diagram illustrates the data flow for the GETBH instruction. The ROM buffer (D7 to D0) provides the high byte (D15 to D8) to the destination register (Dreg). Simultaneously, the low byte (D7 to D0) of the source register (Sreg) is transferred to the low byte (D7 to D0) of the destination register (Dreg). This operation effectively combines the high byte from ROM with the low byte from the source register to form a 16-bit value in the destination register.

Description:
This instruction loads the data contained in the ROM buffer to the high byte of the destination register and the low byte of the source register to the low byte of the destination register.

The source and destination registers are specified in advance using a WITH, FROM, or TO instruction. When not specified, these registers default to R0.

Note:
Refer to the GETB instruction and "Memory Mapping" for information to load data from game pak ROM to the ROM buffer.

Flags affected:
| B | ALT1 | ALT2 | O/V | S | CY | Z |
|---|---|---|---|---|---|---|
| 0 | 0 | 0 | - | - | - | - |

B: Reset
ALT1: Reset
ALT2: Reset

Opcode:
(MSB) (LSB)
```
0 0 1 1 1 1 0 1  (3DH)
1 1 1 0 1 1 1 1  (EFH)
```

Machine Cycles:
ROM execution time: 6~10 cycles
RAM execution time: 6~9 cycles
Cache RAM execution time: 2~6 cycles

Note:
Because the ROM buffer is used, the number of execution cycles varies with each program.

Example:
Under the following conditions,
(ROM buffer) = 75H, Sreg: R2, Dreg: R6, R2= 4ABDH

R6 becomes 75BDH when
GETBH
is executed.

---

GETBL

Operation:
The diagram illustrates the data flow for the GETBL instruction. The ROM buffer (D7-D0) provides the lower byte of data. The source register (Sreg, D15-D8) provides the upper byte of data. These two bytes are combined and written into the destination register (Dreg, D15-D0), with the lower byte from the ROM buffer going into the lower byte of Dreg and the upper byte from Sreg going into the upper byte of Dreg.

Description:
This instruction loads the data contained in the ROM buffer to the low byte of the destination register and the high byte of the source register to the high byte of the destination register.

The source and destination registers are specified in advance using a WITH, FROM, or TO instruction. When not specified, these registers default to R0.

Note:
Refer to the GETB instruction and "Memory Mapping" for information to load data from game pak ROM to the ROM buffer.

Flags affected:
| B | ALT1 | ALT2 | O/V | S | CY | Z |
|---|---|---|---|---|---|---|
| 0 | 0 | 0 | - | - | - | - |

B: Reset
ALT1: Reset
ALT2: Reset

Opcode:
(MSB) (LSB)
GETBL
0 0 1 1 1 1 1 0 (3EH)
1 1 1 0 1 1 1 1 (EFH)

Machine Cycles:
ROM execution time: 6~10 cycles
RAM execution time: 6~9 cycles
Cache RAM execution time: 2~6 cycles

Note:
Because the ROM buffer is used, the number of execution cycles varies with each program.

Example:
Under the following conditions,
(ROM buffer) = 75H, Sreg: R2, Dreg: R6, R2= 4ABDH
R6 is 4A75H when
GETBL
is executed.

---

GETBS

Operation:
The diagram illustrates the data flow for the GETBS instruction. Data from the ROM Buffer is loaded into the destination register (Dreg). Specifically, the low byte (D0-D7) of the ROM Buffer is loaded into the low byte (D0-D7) of Dreg. Additionally, the data from Bit 7 of the ROM Buffer is loaded into Bits 8-15 of Dreg. This effectively combines the 8-bit value from the ROM buffer with the 8-bit value from Bit 7 of the ROM buffer to form a 16-bit value in the destination register.

Description:
This instruction loads the data contained in the ROM buffer to the low byte of the destination register and the data contained in Bit 7 of the ROM buffer to Bits 8~15 of the destination register.

The destination register is specified in advance using a WITH or TO instruction. When not specified, this register defaults to R0.

Note:
Refer to the GETB instruction and “Memory Mapping” for information to load data from game pak ROM to the ROM buffer.

Flags affected:
| B | ALT1 | ALT2 | O/V | S | CY | Z |
|---|---|---|---|---|---|---|
| 0 | 0 | 0 | - | - | - | - |

B, ALT1, ALT2 : Reset

Opcode:
(MSB) (LSB)
GETBS
0 0 1 1 1 1 1 1 (3FH)
1 1 1 0 1 1 1 1 (EFH)

Machine Cycles:
ROM execution time 6~10 cycles
RAM execution time 6~9 cycles
Cache RAM execution time 2~6 cycles

Note:
Because the ROM buffer is used, the number execution cycles varies with each program.

Example:
Under the following conditions,
(ROM buffer) = 85H, Dreg: R8
R8 becomes FF85H when
GETBS
is executed.

---

GETC

Operation: (ROM buffer) → (COLOR register)

Description: This instruction loads the data contained in the ROM buffer into the color register as color data.

Note: Refer to the GETB instruction and "Memory Mapping" for information to load data from game pak ROM to the ROM buffer. Refer to COLOR and "Bitmap Emulation" for information concerning the color register and how to plot.

Flags affected:
| B | ALT1 | ALT2 | O/V | S | CY | Z |
|---|------|------|-----|---|----|---|
| 0 | 0    | 0    | -   | - | -  | - |

Opcode:
```
(MSB)       (LSB)
GETC  1 1 0 1 1 1 1  (DFH)
```

Machine Cycles:
- ROM execution time: 3~10 cycles
- RAM execution time: 3~9 cycles
- Cache RAM execution time: 1~6 cycles

Note: Because the ROM buffer is used, the number of execution cycles varies with each program.

Example:
Under the following conditions,
```
(ROM buffer) = 4BH
```
4BH is loaded to the color register when
```
GETC
```
is executed.

---

HIB

Operation:
The diagram illustrates the data flow for the HIB instruction. The high byte (D15-D8) of the source register (S_reg) is transferred to the lower byte (D7-D0) of the destination register (D_reg). Simultaneously, the upper byte of the destination register is set to 00H (all zeros).

Description:
This instruction loads the high byte of the source register into the low byte of the destination register. The high byte of the destination register is loaded with 00H.
The source and destination registers are specified in advance using a WITH, FROM, or TO instruction. When not specified, these registers default to R0.

Flags affected:
| B  | ALT1 | ALT2 | O/V | S   | CY  | Z   |
|----|------|------|-----|-----|-----|-----|
| 0   | 0    | 0    | -   | *   | -   | *   |

B: Reset
ALT1: Reset
ALT2: Reset
S: Set if a negative number is loaded to the low byte of the destination register, else reset.
Z: Set if zero is loaded to low byte of the destination register, else reset.

Opcode:
(MSB) (LSB)
HIB  1 1 0 0 0 0 0 0  (C0H)

Machine Cycles:
ROM execution time: 3 cycles
RAM execution time: 3 cycles
Cache RAM execution time: 1 cycles

Example:
Under the following conditions,
S_reg: R11, D_reg=R1, R11= 8A43H

R1 becomes 008AH and the sign flag is set when
HIB
is executed.

---

IBT Rn, #pp

Operation:
The diagram illustrates how the immediate data `pp` (a signed 8-bit value ranging from -128 to +127) is loaded into register `Rn`. The immediate data is split into two parts: the low 7 bits (D6-D0) are loaded into the low byte of `Rn` (bits D7-D0), and the most significant bit (D7) of the immediate data is loaded into the high byte of `Rn` (bits D15-D8). This effectively loads a 16-bit value into `Rn`, where the high byte is sign-extended from the immediate data.

Description:
This instruction loads one byte of immediate data (hexadecimal) into the low byte of register `Rn`. Bit 7 of the immediate data is loaded into bits 8 through 15 of `Rn`.

Flags affected:
| B | ALT1 | ALT2 | O/V | S | CY | Z |
|---|---|---|---|---|---|---|
| 0 | 0 | 0 | - | - | - | - |

B, ALT1, ALT2: Reset

Opcode:
(MSB) (LSB)
`IBT Rn, #pp`
```
1 0 1 0 n (0H~FH)
```
The opcode is 4 bits long, with the format shown above. The first 4 bits are fixed as `1010` (AnH). The register number `n` is encoded as a 4-bit value (0H~FH), which corresponds to the register `Rn` (n=0~15). The immediate data `pp` is encoded as an 8-bit value (00H~FFH), which is interpreted as a signed integer in the range -128 to +127.

Machine Cycles:
- ROM execution time: 6 cycles
- RAM execution time: 6 cycles
- Cache RAM execution time: 2 cycles

Example:
Since hexadecimal numbers are handled in the assembler as integers, without signs, a hexadecimal number of 80H or greater that is entered as an operand is processed as a number greater than +128, exceeding the range -128~+127. When this occurs, the assembler will specify the low byte as the immediate data of the IBT instruction.

```
IBT R8, #4     ... 0004H → R8
IBT R8, #-128  ... FF80H → R8
IBT R8, #0A4H  ... FFA4H → R8
```

---

INC Rn

Operation: Rn + 1 → Rn (n = 0~14)

Description: This instruction increments the contents of the register specified in the operand Rn by one and stores the result back into the same register.

The operand can be R0~R14.

Flags affected:

| B | ALT1 | ALT2 | O/V | S | CY | Z |
|---|---|---|---|---|---|---|
| 0 | 0 | 0 | - | * | - | * |

B : Reset
ALT1 : Reset
ALT2 : Reset
S : Set if result is negative, else reset.
Z : Set on zero result, else reset.

Opcode:
(MSB) (LSB)
INC Rn 1 0 1 1 n (0H~EH) (DnH)

Machine Cycles:
ROM execution time 3 cycles
RAM execution time 3 cycles
Cache RAM execution time 1 cycles

Example:
When register R12 is 65B1H, R12 becomes 65B2H when
INC R12
is executed.

---

IWT Rn, #xx

Operation:
#xx (2-byte hexadecimal immediate data) → Rn
(n = 0~15, #xx=0~65535)

Description:
This instruction loads two bytes of immediate data, #xx (hexadecimal), to the register specified in the operand, Rn.

Flags affected:
| B | ALT1 | ALT2 | O/V | S | CY | Z |
|---|---|---|---|---|---|---|
| 0 | 0 | 0 | - | - | - | - |

B : Reset
ALT1 : Reset
ALT2 : Reset

Opcode:
(MSB) (LSB)
ITW Rn, #xx
| 1 | 1 | 1 | 1 | n (0H~FH) | (FnH) |
|---|---|---|---|---|---|
| x (00H~FFH) | (Lower Byte) |
| x (00H~FFH) | (Upper Byte) |

The two-byte immediate data in the op code is loaded low byte first, followed by the high byte.

Machine Cycles:
ROM execution time 9 cycles
RAM execution time 9 cycles
Cache RAM execution time 3 cycles

Example:
Register R0 becomes 4583H when
```
IWT R0, #4583H
```
is executed.

---

JMP Rn

Operation: Rn → R15 (PC) (n=8~13)

Description: This instruction loads the contents of the register specified in the operand Rn to R15 (program counter) and initiates a program fetch from the resulting location specified by the program counter.

The next instruction to be executed will already be in the instruction pipeline of the processor. For this reason one byte from the pipeline will be executed before the instruction at the branch destination is executed. (The execution time for this instruction is not included in the machine cycles listed below.)

The operand can be register R8~R13.

Flags affected:

| B | ALT1 | ALT2 | O/V | S | CY | Z |
|---|---|---|---|---|---|---|
| 0 | 0 | 0 | - | - | - | - |

B : Reset
ALT1 : Reset
ALT2 : Reset

Opcode:
(MSB) (LSB)
JMP Rn
1 0 0 1 n (8H~DH) (9nH)

Machine Cycles:
ROM execution time 3 cycles
RAM execution time 3 cycles
Cache RAM execution time 1 cycles

Example:
When register R10 is 0555H and the following program is executed,

```
PC      Opcode
0444H   JMP R10
0445H   INC R10
...
```

the jump destination is 0555H.

---

LDB (Rm)

Operation:
(Rm) → D_reg (Low Byte)  (m=0~11)
00H → D_reg (High Byte)

Description:
This instruction loads one byte of data located at the game pak RAM address contained in the register specified in the operand Rm and stores this data in the destination register. The upper byte of the destination register is loaded with 00H.

Use the RAMB instruction to set the RAM bank. (Refer to RAMB.)

The destination register is specified in advance using a WITH or TO instruction. When not specified, this register defaults to R0.

Flags affected:

| B | ALT1 | ALT2 | O/V | S | CY | Z |
|---|------|------|-----|---|----|---|
| 0 | 0    | 0    | -   | - | -  | - |

B : Reset
ALT1 : Reset
ALT2 : Reset

Opcode:
(MSB)  (LSB)
LDB (Rm)
```
0 0 1 1 1 1 0 1
0 1 0 0 m (0H~BH)
```
(3DH)  (4mH)

Machine Cycles:
ROM execution time 11 cycles
RAM execution time 13 cycles
Cache RAM execution time 6 cycles

Note:
The GSU waits while the data is loaded from game pak RAM.
The cycles required for this are included in the execution times given above.

Example:
Under the following conditions,
D_reg=R7, R1= 3482H,  (70:3482H)= 51H
RAMBR:70H

and when the following program is executed,
LDB  (R1)
R7 becomes 0051H.

---

LDW (Rm)

Operation:
(Rm) → D_reg (Low Byte)  (m=0~11)
(Rm±1) → D_reg (High Byte)
When the contents of Rm is:
even, (Rm+1)
odd, (Rm-1)
is loaded to the high byte.

Description:
The word data located in the game pak RAM address that equals the contents of register Rm are stored in the destination register. The game pak RAM address bank is specified using the RAMB instruction (refer to RAMB).

The destination register is specified in advance using a WITH or TO instruction. When not specified, this register defaults to R0.

Flags affected:
| B | ALT1 | ALT2 | O/V | S | CY | Z |
|---|------|------|-----|---|----|---|
| 0 | 0    | 0    | -   | - | -  | - |

B : Reset
ALT1 : Reset
ALT2 : Reset

Opcode:
(MSB)  (LSB)
LDW (Rm)  0 1 0 0  m (0H~BH)  (4mH)

Machine Cycles:
ROM execution time  10 cycles
RAM execution time  12 cycles
Cache RAM execution time  7 cycles

Note:
While a load is performed from the game pak ROM, the GSU is in the WAIT state. This execution time is included in the above machine cycles.

Example:
Under the following conditions,
D_reg:R5, R3=6480H, (70:6480H)=C0H, RAMBR=70H
and when the following program is executed,
```
LDW  (R3)
```
the register R5 becomes C02EH.

---

LEA Rn, xx

Operation: Rn ← xx (n=0~15, xx=0~65535)

Description: This instruction loads two bytes of immediate data, #xx (hexadecimal), to the register specified in the operand Rn.

Flags affected:
| B | ALT1 | ALT2 | O/V | S | CY | Z |
|---|---|---|---|---|---|---|
| 0 | 0 | 0 | - | - | - | - |

B : Reset
ALT1 : Reset
ALT2 : Reset

Opcode:
(MSB) (LSB)
LEA Rn, xx
| 1 | 1 | 1 | 1 | n (0H~FH) | (FnH) |
|---|---|---|---|---|---|
| x (00H~FFH) | (Lower Byte) |
| x (00H~FFH) | (Upper Byte) |

The two-byte immediate data in the op code is loaded low byte first, followed by the high byte.

Machine Cycles:
ROM execution time 9 cycles
RAM execution time 9 cycles
Cache RAM execution time 3 cycles

Example:
Register R3 becomes 4853H when
```
LEA R3, #4853H
```
is executed.

---

LINK #n

Operation: R₁₅ + #n → R₁₁ (n=1~4)
R₁₅ contains address following LINK instruction

Description: This instruction adds the operand #n to the value contained in register R₁₅ (program counter) and stores the result in register R₁₁. Operand #n can be a number from 1~4. This instruction can be used to specify a return address in register R₁₁ when jumping to a subroutine.

Flags affected:
| B | ALT1 | ALT2 | O/V | S | CY | Z |
|---|---|---|---|---|---|---|
| 0 | 0 | 0 | - | - | - | - |

B : Reset
ALT1 : Reset
ALT2 : Reset

Opcode:
(MSB) (LSB)
LINK #n [1 0 0 1] n (1H~4H) (9nH)

Machine Cycles:
ROM execution time 3 cycles
RAM execution time 3 cycles
Cache RAM execution time 1 cycles

Example: Under the following conditions,
R₁₅: 4368H

and when the following program is executed,

```
4368   LINK #4
4369   IWT R₁₅, #74FFH
436C   NOP
436B   IBT R₁, #12H
```

register R₁₁ becomes 4369H + 2=436BH

---

LJMP Rn

Operation:
Rn → R15 (PC)  (n=8~13)
Sreg → Program Bank Register (PBR)

Description:
This instruction loads the register specified as operand, Rn, into the program counter, R15 and loads the lower byte of the source register to the program bank register. This allows the program to jump to addresses in different banks.

The next instruction to be executed will already be in the instruction pipeline of the processor. For this reason one byte from the pipeline will be executed before the instruction at the branch destination is executed. (The execution time for this instruction is not included in the machine cycles listed below.)

The operand can be any of registers R8~R13.

Flags affected:
| B | ALT1 | ALT2 | O/V | S | CY | Z |
|---|------|------|-----|---|----|---|
| 0 | 0    | 0    | -   | - | -  | - |

Opcode:
(MSB)           (LSB)
LJMP Rn         0 0 1 1 1 0 1
                 1 0 0 1 n (8H~DH)

Machine Cycles:
ROM execution time     6 cycles
RAM execution time     6 cycles
Cache RAM execution time 2 cycles

Example:
Under the following conditions,
R1:0001H
the program jumps from 00:8006H to 01:0002H when the following program is executed.

| Bank | :Address | Syntax        |
|------|----------|---------------|
| 00   | :8000H   | IWT R10, #0002H |
| 00   | :8003H   | FROM R1       |
| 00   | :8004H   | LJMP R10      |
| 00   | :8006H   | NOP           |

---

LMS Rn, (yy)

Operation:
RAM (yy) → Rn (low byte)  (n=0~15, yy=0~510*)
RAM (yy+1) → Rn (high byte)

*Note: Selectable RAM address (yy) must be an even number.

Description:
This instruction uses a short address method to perform the LM instruction. The address is shortened by reducing the number of bytes in the instruction opcode. The instruction loads data from the game pak RAM address equal to the immediate number yy and stores the data in register Rn. The selectable game pak RAM address may be an even number of 0~510. The RAMB instruction is used to specify the bank of the RAM address.

Flags affected:
| B | ALT1 | ALT2 | O/V | S | CY | Z |
|---|---|---|---|---|---|---|
| 0 | 0 | 0 | - | - | - | - |

B : Reset
ALT1 : Reset
ALT2 : Reset

Opcode:
(MSB)  (LSB)
```
0 0 1 1 1 0 1
1 0 1 0 n (0H~FH)
kk (00H~FFH)
```
(3DH)
(AnH)
(Address)

[Short address method]
This method is used by LMS, SMS, and other instructions to reduce the number of bytes in the instruction opcode. Only one byte is used. The actual game pak RAM address is twice that of the address code. The relationship between yy in the above syntax and kk in the opcode is:
yy = kk x 2

Machine Cycles:
ROM execution time 17 cycles
RAM execution time 17 cycles
Cache RAM execution time 10 cycles

Note:
The GSU waits while data is loaded from game pak RAM. The execution time required for this is included in the machine cycles given above.

Example:
Under the following conditions,
(70:1AAH) = 32H, (70:1ABH) = 92H, RAMBR:70H
register R3 becomes 9232H when the following program is executed:

Syntax      Opcode
LMS R3, (1AAH) 3D A3 D5

---

LMULT

Operation:
The diagram illustrates a 16 x 16-bit signed multiplication. The source register (Sreg) and register R6 are multiplied. The 32-bit result is split into two 16-bit words: the upper word is stored in the destination register (Dreg), and the lower word is stored in register R4. The carry flag (CY) is set if the most significant bit (Bit 15) of R6 is set.

Description:
This instruction performs 16 x 16-bit signed multiplication using the source register and register R6. The upper 16 bits of the result are stored in the destination register, and the lower 16 bits are stored in R4. If Bit 15 of R6 is set, the carry flag is also set to "1".

The source and destination registers are specified in advance using a WITH, FROM, or TO instruction. When not specified, the source and destination registers default to R0. If R4 is specified as the destination register, the result will be invalid.

Flags affected:
| B  | ALT1 | ALT2 | O/V | S   | CY   | Z    |
|----|------|------|-----|-----|------|------|
| 0  | 0    | 0    | -   | *   | *    | *    |

B: Reset
ALT1: Reset
ALT2: Reset
S: Set if the result is negative, else reset
CY: Set if Bit 15 of R6 is "1", reset if "0"
Z: Set if the destination register result is zero, else reset.

Opcode:
(MSB) (LSB)
LMULT
0 0 1 1 1 1 0 1 (3DH)
1 0 0 1 1 1 1 1 (9FH)

Machine Cycles:
ROM execution time: 10 or 14 cycles
RAM execution time: 10 or 14 cycles
Cache RAM execution time: 5 or 9 cycles

Note:
The number of cycles varies depending upon the CFGR register setting.

Example:
Under the following conditions,
Sreg: R9, Dreg: R8
R9 = B556H, R6 = DAABH
the register R8 becomes 0AE3H and R4 5C72H when
LMULT
is executed.

---

LM Rn, (xx)

Operation:
RAM (xx) → Rn (low byte)  (n=0~15, xx=0~65535)
RAM (xx±1) → Rn (high byte)
When the value of xx is:
even, (xx+1)
odd, (xx-1)
is loaded to the high byte.

Description:
This instruction loads the data contained in the game pak RAM address specified in the second operand xx and stores the data in the register specified in the first operand Rn. The RAMB instruction is used to specify the bank of the RAM address.

Flags affected:

| B | ALT1 | ALT2 | O/V | S | CY | Z |
|---|------|------|-----|---|----|---|
| 0 | 0    | 0    | -   | - | -  | - |

B : Reset
ALT1 : Reset
ALT2 : Reset

Opcode:
(MSB)  (LSB)
```
0 0 1 1 1 1 0 1  (3DH)
1 1 1 1 n (0H~FH) (FnH)
x (00H~FFH)       (ADRS Lower Byte)
x (00H~FFH)       (ADRS Upper Byte)
```

Machine Cycles:
ROM execution time     20 cycles
RAM execution time     21 cycles
Cache RAM execution time 11 cycles

Note:
While a load is performed from the game pak RAM, the GSU is in the WAIT state. This execution time is included in the above machine cycles.

Example:
Under the following conditions,
(70:BACCH) = 28H, (70:BACDH) = 96H, RAMBR=70H
register R9 becomes 9628H when the following program is executed:
```
LM  R9, (0BACCH)
```

---

LOB

Operation:
The diagram illustrates the data flow for the LOB instruction. The source register (S_reg) is 16 bits wide, split into an Upper Byte (D15-D8) and a Lower Byte (D7-D0). The destination register (D_reg) is also 16 bits wide, split into an Upper Byte (D15-D8) and a Lower Byte (D7-D0). The operation copies the Lower Byte from S_reg to the Lower Byte of D_reg. The Upper Byte of D_reg is set to 00H (zero).

Description:
This instruction loads the lower byte of the source register to the low byte of the destination register. The high byte of the destination register is loaded with 00H.
The source and destination registers are specified in advance using a WITH, FROM, or TO instruction. When not specified, the source and destination registers default to R0.

Flags affected:
| B  | ALT1 | ALT2 | O/V | S   | CY  | Z   |
|----|------|------|-----|-----|-----|-----|
| 0   | 0    | 0    | -   | *   | -   | *   |

B: Reset
ALT1: Reset
ALT2: Reset
S: Set if the low byte of the source register is negative, else reset.
Z: Set if low byte of the source register is zero, else reset.

Opcode:
(MSB)       (LSB)
LOB          1 0 0 1 1 1 0  (9EH)

Machine Cycles:
ROM execution time     3 cycles
RAM execution time     3 cycles
Cache RAM execution time 1 cycle

Example:
Under the following conditions,
S_reg: R10, D_reg: R12, R10= FB23H
the register R12 becomes 0023H when
LOB
is executed.

---

LOOP
Operation: R₁₂ - 1 → R₁₂
If Z Flag=0 then R₁₃→ R₁₅ (PC)

Description: This instruction decrements R₁₂ by 1. If the result does not set the zero flag, the contents of R₁₃ are loaded into R₁₅ and the program is fetched from the resulting location specified by the program counter.

If the zero flag is set, the program counter is incremented and the next instruction is executed.

The instruction at the address following the LOOP instruction is already loaded into the pipeline. The branch is taken after this instruction is executed.

Flags affected:
| B | ALT1 | ALT2 | O/V | S | CY | Z |
|---|---|---|---|---|---|---|
| 0 | 0 | 0 | - | * | - | * |

B : Reset
ALT1 : Reset
ALT2 : Reset
S : Set if the register R₁₂ is negative, else reset.
Z : Set if the register R₁₂ is zero, else reset.

Opcode:
(MSB) (LSB)
LOOP 0 0 1 1 1 0 0 (3CH)

Machine Cycles:
ROM execution time 3 cycles
RAM execution time 3 cycles
Cache RAM execution time 1 cycles

Example:
In the following program,
```
00:8014  INC R7
00:8015  INC R6
00:8016  LOOP
00:8017  NOP
00:8018  ADD R4
```
if R₁₃ is 8014H and R₁₂ is other than 0001H, the program jumps to 00:8014H after the NOP instruction is executed. If R₁₂ is 0001H, the jump does not happen and the instruction ADD is executed.

---

LSR

Operation:
The diagram depicts a logical shift right operation. The 16-bit source register (S_reg) is shifted right by one bit position. The bit that was originally at position 0 (least significant bit) is moved to the carry flag (CY). The bit that was originally at position 15 (most significant bit) is replaced with a 0. The result of the shift is stored in the destination register (Dreg).

Description:
This instruction shifts all bits in the source register one bit to the right and stores the result in the destination register. Bit 15 becomes "0" and the value of Bit 0 is stored in the carry flag.

The source and destination registers are specified in advance using a WITH, FROM, or TO instruction. When not specified, the source and destination registers default to R0.

Flags affected:
| B | ALT1 | ALT2 | O/V | S | CY | Z |
|---|---|---|---|---|---|---|
| 0 | 0 | 0 | - | 0 | * | * |

B : Reset
ALT1 : Reset
ALT2 : Reset
S : Reset
CY : Set if Bit 0 in source register is "1", else reset
Z : Set on zero result, else reset.

Opcode:
(MSB) (LSB)
LSR
0 0 0 0 0 0 1 1 (03H)

Machine Cycles:
ROM execution time 3 cycles
RAM execution time 3 cycles
Cache RAM execution time 1 cycles

Example:
Under the following conditions,
S_reg: R8, D_reg: R0

bit 15 bit 0
R8: 1 0 1 1 0 1 0 0 1 1 1 1 (B53FH)

LSR execution results in:

bit15 bit0 CY
R0: 0 1 0 1 1 0 1 0 0 1 1 1 1 (5A9FH) 1

---

MOVEB Rn, (Rn')

MACRO INSTRUCTION

Operation:
(Rn') → Rn (Low Byte)  (n=0~15, n'=0~11)
00H → Rn (High Byte)

Conditions:
If n=0:
then, use only LDB instruction,
else, use TO instruction and LDB instruction.

Description:
This instruction loads one byte of data located at the game pak
RAM address equal to the contents of register Rn', specified by
the second operand and stores this data in the register specified
in the first operand. The high byte of the destination register is
loaded with 00H. The register identified in the second operand is
selectable from R0~R11. The RAMB instruction is used to specify
the game pak RAM bank (refer to "RAMB").

This macro instruction is stored in memory as "LDB (Rm)" or "TO
Rn" + "LDB (Rm)." The assembler automatically recognizes
whether or not the TO instruction is required. When n does not
equal 0, the TO instruction is added. Refer to "LDB (Rm)" and
"TO Rn" for machine cycles, flags affected, and opcode.

Example:
Under the following conditions,
R1=3482H, (70:3482H)=51H, RAMBR=70H
when the following program is executed,
MOVEB R7, (R1)  ; (R1)→R7 (Low Byte)  (TO R7+LDB (R1))
;00H→R7 (High Byte)
register R7 becomes 0051H.

Also, under the following conditions,
R3=3581H, (70:3581H)=9AH, RAMBR=70H
when the following program is executed,
MOVEB R0, (R3)  ; (R3)→R0 (Low Byte)  (LDB (R3))
;00H→R0 (High Byte)
register R0 becomes 009AH.

---

MOVEB (Rn'), Rn

MACRO INSTRUCTION

Operation: Rn (low byte) → (Rn') (n=1~15, n'=0~11)

Conditions: If n=0:
then, use only STB instruction,
else, use FROM instruction and STB instruction.

Description: This instruction stores the contents of the low byte of register Rn specified in the second operand at the game pak RAM address equal to the contents of register Rn', specified in the first operand. The register identified in the first operand is selectable from R0~R11. The RAMB instruction is used to specify the game pak RAM bank (refer to "RAMB").

This macro instruction is stored in memory as "STB (Rm)" or "FROM Rn" + "STB (Rm)." The assembler automatically recognizes whether or not the FROM instruction is required. When n does not equal 0, the FROM instruction is added. Refer to "STB (Rm)" and "FROM Rn" for machine cycles, flags affected, and opcode.

Example: Under the following conditions,

R5=3843H, R11=94F1H, RAMBR=71H

when the following program is executed,

```
MOVEB  (R11), R5  ;R5 (Low Byte)→(R11)  (FROM R5+STB (R11))
```

the result is (71:94F1H)=43H.

Also, under the following conditions,

R0=89E0H, R3=438BH, RAMBR=70H

when the following program is executed,

```
MOVEB  (R3), R0  ;R0 (Low Byte)→(R3)  (STB (R3))
```

the result is (70:438BH)=43H.

---

MOVES Rn, Rn'

Operation: Rn' → Rn (n, n' = 0~15)

Description: This instruction loads the contents of register Rn', specified in the second operand, to register Rn, specified in the first operand. Flags are set according to the data loaded. (Refer to MOVE Rn, Rn'.)

Flags affected:

| B | ALT1 | ALT2 | O/V | S | CY | Z |
|---|---|---|---|---|---|---|
| 0 | 0 | 0 | * | * | - | * |

B : Reset
ALT1 : Reset
ALT2 : Reset
O/V : Set if Bit 7 is "1", else reset
S : Set if Bit 15 is "1", else reset
Z : Set when data is zero, else reset

Opcode:

(MSB) (LSB)
MOVES Rn, Rn'
| 0 | 0 | 1 | 0 | n' (0H~FH) | (2n' H) |
| 1 | 0 | 1 | 1 | n (0H~FH) | (BnH) |

Machine Cycles:
ROM execution time 6 cycles
RAM execution time 6 cycles
Cache RAM execution time 2 cycles

Example: When R7 is 4983H and

```
MOVES R10, R7
```

is executed, the register R10 becomes 4983H and the overflow flag is set.

---

MOVEW

MACRO INSTRUCTION

Operation:
(R_n') → R_n (Low Byte)  (n=0~15, n'=0~11)
(R_n ±1) → R_n (High Byte)

Note:
If the contents of R_n' are even, store the address equal to the contents of (R_n'+1) in the high byte of R_n. If the contents of R_n' are odd, store the address equal to (R_n'-1) in the high byte of R_n.

Conditions:
If n=0:
then, use only LDW instruction,
else, use TO instruction and LDW instruction.

Description:
This instruction loads hexadecimal data from the game pak RAM address equal to the contents of register R_n' specified in the second operand and stores it into register R_n specified by the first operand. The game pak RAM address bank is specified using the RAMB instruction (refer to RAMB).

This macro instruction is stored in memory as “LDW (R_m)” or “TO R_n” + “LDW (R_m).” The assembler automatically recognizes whether or not the TO instruction is required. When n is not equal to 0, the TO instruction is added. Refer to “LDW (R_m)” and “TO R_n” for machine cycles, flags affected, and opcode.

Example:
Under the following conditions,
R_3=6480H, (71:6480H)=2EH, (71:6481H)=C0H,
RAMBR=71H

and when the following program is executed,
```
MOVEW  R5, (R3)  ;(R3)→R5(Low Byte)  (TO R5 + LDB (R3))
              ;(R3+1)→R5(High Byte)
```
register R_5 becomes C02EH.

Also, under the following conditions,
R_6=0822H, (70:0822H)=43H, (70:0823H)=96H,
RAMBR=70H

and when the following program is executed,
```
MOVEW  R0, (R6)  ;(R6)→R0(Low Byte)  (LDB (R6))
              ;(R6+1)→R0(High Byte)
```
register R_0 becomes 9643H.

---

MOVEW (R_n'), R_n

MACRO INSTRUCTION

Operation:
R_n (low byte) → (R_n')  (n=0~15, n'=0~11)
R_n (high byte) → (R_n' ±1)

Note: If the contents of R_n' are even, store the high byte of R_n into the address equal to the contents of (R_n'+1). If the contents of R_n' are odd, store the high byte of R_n into the address equal to the contents of (R_n'-1).

Conditions:
If n=0:
then, use only STW instruction,
else, use FROM instruction and STW instruction.

Description:
This instruction stores the contents (hexadecimal data) of register R_n specified in the second operand into the game pak RAM address which is equal to the value of register R_n' specified in the first operand. The game pak RAM address bank is specified using the RAMB instruction (refer to RAMB). The operand n' can be a register from R_0~R_11.

This macro instruction is stored in memory as "STW (R_m)" or "FROM R_n" + "STW (R_m)." The assembler automatically recognizes whether or not the FROM instruction is required. When n is not equal to 0, the FROM instruction is added. Refer to "STW (R_m) and "FROM R_n" for machine cycles, flags affected, and opcode.

Example:
Under the following conditions,
R_9=BFA3H, R_10=4444H, RAMBR=71H
and when the following program is executed,

```
MOVEW  (R_10), R_9
;R_9(Low Byte)→(R_10)  (FROM R_9+STW (R_10))
;R_9(High Byte)→(R_10+1)
```

the result is (71:4444H)=A3H, (71:4445H)=BFH.

Also, under the following conditions,
R_0=3151H, R_6=92A0H, RAMBR=71H
and when the following program is executed,

```
MOVEW  (R_6), R_0
;R_0 (Low Byte)→(R_6)  (STW (R_6))
;R_0 (High Byte)→(R_6+1)
```

the result is (71:92A0H)=51H, (71:92A1H)=31H.

---

MOVE Rn, Rn'

Operation: Rn' → Rn (n, n' = 0~15)

Description: This instruction loads the contents of register Rn', specified in the second operand, to register Rn, specified in the first operand.

Flags affected:
| B | ALT1 | ALT2 | O/V | S | CY | Z |
|---|---|---|---|---|---|---|
| 0 | 0 | 0 | - | - | - | - |

B : Reset
ALT1 : Reset
ALT2 : Reset

Opcode:
(MSB) (LSB)
MOVE Rn, Rn'
| 0 | 0 | 1 | 0 | n' (0H~FH) | (2n'H) |
| 0 | 0 | 0 | 1 | n (0H~FH) | (1nH) |

Machine Cycles:
ROM execution time 6 cycles
RAM execution time 6 cycles
Cache RAM execution time 2 cycles

Example:
Under the following conditions,
R14= 4983H, R8= 9264H
the register R8 becomes 4983H when
```
MOVE R8, R14
```
is executed.

---

MOVE Rn, #xx

MACRO INSTRUCTION

Operation: #xx → Rn
(n = 0~15, #xx=-32768~65535)
(if unsigned, #xx=0~65535)

Conditions: IF (-128≤xx≤127):
then, use an IBT instruction
(if unsigned, (0≤xx≤127) or (65408≤xx≤65535))
else, use an IWT instruction.

Description: This instruction directly loads hexadecimal immediate data into register Rn, specified in the first operand. This is a macro instruction and is stored in memory as “IWT Rn, #xx” or “IBT Rn, #pp.” The assembler automatically recognizes whether this should be replaced with an IBT instruction or IWT instruction, depending upon the value of immediate data.

If immediate data is -128 ~ 127 (unsigned, 0~127 or 65408~65535), it is replaced with an IBT instruction. Otherwise, it is replaced with an IWT instruction. Refer to “IBT Rn, #pp” or “IWT Rn, #xx” for machine cycles, flags affected, and opcode.

Example:
```
MOVE R8, #070H    ;0070H→R8    (IBT R8, #070H)
MOVE R8, #0A4H;   00A4H→R8    (IWT R8, #0A4H)
MOVE R8, #-128;   FF80→R8     (IBT R8, #-128)
```

---

MOVE Rn, (xx)

MACRO INSTRUCTION

Operation:
(xx) → Rn (low byte)      (n=0~15, xx=0~FFFFH)
(xx±1) → Rn (high byte)

Note: When the value xx is even, the contents of (xx+1) are loaded to the high byte of Rn. When the value of xx is odd, the contents of (xx-1) are loaded to the high byte of Rn.

Conditions: If (0000H≤xx≤01FFH) and xx is even: then, use an LMS instruction else, use an LM instruction.

Description: This instruction loads hexadecimal data contained in the game pak RAM address specified in the second operand and stores the data in register Rn, specified in the first operand.. The RAMB instruction is used to specify the bank of the game pak RAM address (refer to RAMB).

This is a macro instruction and is stored in memory as "LM Rn, (xx)" or "LMS Rn, (yy)." The assembler automatically recognizes whether it should be replaced with an LM instruction or an LMS instruction, depending upon the value of the game pak RAM address specified.

When the game pak RAM address is an even number of 0~1FFH, it is replaced with an LMS instruction. Otherwise, it is replaced with an LM instruction. Refer to "LM Rn, (xx)" or "LMS Rn, (yy)" for machine cycles, flags affected, and opcode.

Example: Under the following conditions,

(70:BACCH) = 28H, (70:BACDH) = 96H, RAMBR=70H

the register R9 becomes 9628H when the following program is executed:

```
MOVE  R9, (0BACCH)    ;(70:BACCH)→R9(Low Byte)   (LM R9, (0BACCH))
                      ;(70:BACDH)→R9(High Byte)
```

Also, under the following conditions,

(71:01AAH) = 32H, (71:01ABH) = 92H, RAMBR=71H

the register R3 becomes 9232H when the following program is executed:

```
MOVE  R3, (1AAH)      ;(71:01AAH)→R3(Low Byte)   (LMS R3, (01AAH))
                      ;(71:01ABH)→R3(High Byte)
```

---

MOVE (xx), Rn

MACRO INSTRUCTION

Operation:
Rn (low byte) → (xx)  (n=0~15, xx=0~FFFFH)
Rn (high byte) → (xx±1)

Note: If the value of xx is even, store the high byte of Rn at (xx+1). If the value of xx is odd, store the high byte of Rn at (xx-1).

Conditions: If (0000H≤xx≤01FFH) and xx is even: then, use an SMS instruction, else, use an SM instruction.

Description: This instruction stores the contents (hexadecimal data) of register Rn specified in the second operand in the game pak RAM address specified in the first operand. The RAMB instruction is used to specify the bank of the game pak RAM address (refer to "RAMB").

This macro instruction is stored in memory as "SM (xx), Rn" or "SMS (yy), Rn." The assembler automatically recognizes whether it should be replaced with an SM instruction or an SMS instruction, depending upon the value of the game pak RAM address specified.

When the game pak RAM address is an even number of 0~1FFH, it is replaced with an SMS instruction. Otherwise, it is replaced with an SM instruction. Refer to "SM (xx), Rn" and "SMS (yy), Rn" for machine cycles, flags affected, and opcode.

Example: Under the following conditions,

R9: BACDH, and RAMBR=71H

when the following program is executed,

```
MOVE  (9CDEH), R9
```

;R9 (Low Byte)→(71:9CDEH)  (SM (9CDEH), R9)
;R9 (High Byte)→(71:9CDFH)

the result is (71:9CDEH)=CDH, (71:9CDFH)=BAH

Also, under the following conditions,

R2: 3248H, and RAMBR=70H

when the following program is executed,

```
MOVE  (136H), R2
```

;R2 (Low Byte)→(70:0136H)  (SMS (136H), R2)
;R2 (High Byte)→(70:0137H)

the result is (70:0136H)=48H, (70:0137H)=32H

---

MULT Rn

Operation: S_reg (low byte) * R_n (low byte) → D_reg (n=0~15)

Description: This instruction performs 8 x 8-bit signed multiplication using the low byte of the source register and the low byte of register R_n. The result is stored in the destination register.

The source and destination registers are specified in advance using a FROM, WITH, or TO instruction. When not specified, the source and destination registers default to R_0.

The operand can be a register R_0~R_15.

Flags affected:

| B | ALT1 | ALT2 | O/V | S | CY | Z |
|---|------|------|-----|---|----|---|
| 0 | 0    | 0    | -   | * | -  | * |

B: Reset
ALT1: Reset
ALT2: Reset
S: Set when the result is negative, else reset.
Z: Set on zero result, else reset.

Opcode:

(MSB) (LSB)
MULT R_n
[1] [0] [0] [0] n (0H~FH) (8nH)

Machine Cycles:
ROM execution time: 3 or 5 cycles
RAM execution time: 3 or 5 cycles
Cache RAM execution time: 1 or 2 cycles

Note: The number of cycles depends upon the CFGR register.

Example: Under the following conditions,

S_reg: R_5, D_reg: R_2
R_5= 52CFH, R_1= 63CFH

the register R_2 becomes 0961H when

```
MULT R_1
```

is executed.

---

MULT #n

Operation: S_reg (low byte) * #n → D_reg (n=0~15)

Description: This instruction performs 8 x 8-bit signed multiplication using the low byte of the source register and the immediate data specified in the operand #n. The result is stored in the destination register.

The source and destination registers are specified in advance using a FROM, WITH, or TO instruction. When not specified, the source and destination registers default to R0.

The operand can be immediate data from 0~15.

Flags affected:

| B | ALT1 | ALT2 | O/V | S | CY | Z |
|---|------|------|-----|---|----|---|
| 0 | 0    | 0    | -   | * | -  | * |

B: Reset
ALT1: Reset
ALT2: Reset
S: Set when the result is negative, else reset.
Z: Set on zero result, else reset.

Opcode:

(MSB) (LSB)
```
  0 0 1 1 1 1 0
  1 0 0 0 n (0H~FH)
```

(3EH) (8nH)

Machine Cycles:
ROM execution time: 6 or 8 cycles
RAM execution time: 6 or 8 cycles
Cache RAM execution time: 2 or 3 cycles

Note: The number of cycles depends upon the CFGR register.

Example: Under the following conditions,

S_reg: R3, D_reg: R4, R3= 95C6H

the register R4 becomes FDF6H when

MULT #9

is executed.

---

NOP
Operation: PC ← PC+1
Description: This instruction causes the processor to idle for one cycle and increment the program counter by one.
Flags affected:
| B | ALT1 | ALT2 | O/V | S | CY | Z |
|---|---|---|---|---|---|---|
| 0 | 0 | 0 | - | - | - | - |
B : Reset
ALT1 : Reset
ALT2 : Reset
Opcode:
(MSB) (LSB)
NOP 0 0 0 0 0 0 0 1 (01H)
Machine Cycles:
ROM execution time 3 cycles
RAM execution time 3 cycles
Cache RAM execution time 1 cycles

---

NOT

Operation: Sreg → Dreg

Description: This instruction calculates the 1's complement of the source register and stores the result in the destination register.

The source and destination registers are specified in advance using a FROM, WITH, or TO instruction. When not specified, the source and destination registers default to R0.

Flags affected:

| B | ALT1 | ALT2 | O/V | S | CY | Z |
|---|------|------|-----|---|----|---|
| 0 | 0    | 0    | -   | * | -  | * |

B: Reset
ALT1: Reset
ALT2: Reset
S: Set when the result is negative, else reset.
Z: Set on zero result, else reset.

Opcode:
(MSB) (LSB)
NOT
0 1 0 0 1 1 1 (4FH)

Machine Cycles:
ROM execution time 3 cycles
RAM execution time 3 cycles
Cache RAM execution time 1 cycles

Example:
Under the following conditions,

Sreg: R9, Dreg: R13

Bit15 Bit0
R9: 1 0 1 1 0 1 1 1 0 1 1 0 0 1 0 0 (B764H)

the execution of

NOT

results in:

Bit15 Bit0
R13: 0 1 0 0 1 0 0 0 1 0 0 1 1 0 1 1 (489BH)

---

OR Rn

Operation: S_reg OR R_n → D_reg (n=1~15)

Description: This instruction performs logical bit-wise OR on corresponding bits of the source register and the register specified in the operand R_n. The result is stored in the destination register.

The source and destination registers are specified in advance using a FROM, WITH, or TO instruction. When not specified, the source and destination registers default to R_0.

The operand can be a register R_1~R_15.

Flags affected:

| B | ALT1 | ALT2 | O/V | S | CY | Z |
|---|------|------|-----|---|----|---|
| 0 | 0    | 0    | -   | * | -  | * |

B: Reset
ALT1: Reset
ALT2: Reset
S: Set when the result is negative, else reset.
Z: Set on zero result, else reset.

Opcode:

(MSB) (LSB)
OR R_n
1 1 0 0 n (1H~FH) (CnH)

Machine Cycles:
ROM execution time: 3 cycles
RAM execution time: 3 cycles
Cache RAM execution time: 1 cycles

Example: Under the following conditions,

S_reg: R_4, D_reg: R_5

R_4: Bit15 0 1 1 0 0 1 1 0 1 1 0 1 0 0 (6368H)
R_2: Bit15 0 0 0 1 0 1 1 0 1 0 0 1 1 0 (168CH)

the register R_5 becomes:

R_5: Bit15 0 1 1 1 1 1 1 1 1 0 1 1 0 0 (77ECH)

when

OR R_2

is executed.

---

OR #n

Operation: Sreg OR #n → Dreg (n=1~15)

Description: This instruction performs logical bit-wise OR on corresponding bits of the source register and the immediate data specified in the operand #n. The result is stored in the destination register.

The source and destination registers are specified in advance using a FROM, WITH, or TO instruction. When not specified, the source and destination registers default to R0.

Flags affected:

| B | ALT1 | ALT2 | O/V | S | CY | Z |
|---|------|------|-----|---|----|---|
| 0 | 0    | 0    | -   | * | -  | * |

B: Reset
ALT1: Reset
ALT2: Reset
S: Set when the result is negative, else reset.
Z: Set on zero result, else reset.

Opcode:

(MSB) (LSB)
OR #n
```
0 0 1 1 1 1 0
1 1 0 0 n (1H~FH)
```
(3EH) (CnH)

Machine Cycles:
ROM execution time 6 cycles
RAM execution time 6 cycles
Cache RAM execution time 2 cycles

Example:
Under the following conditions,
Sreg: R7, Dreg: R5

Bit15 Bit0
R7: 0 1 0 1 1 1 1 1 0 1 0 0 0 1 0 (5FA2H)

the register R5 becomes:

Bit15 Bit0
R5: 0 1 0 1 1 1 1 1 0 1 0 0 1 1 1 (5FA7H)

when

OR #5H

is executed.

---

PLOT

Description: This instruction plots the color code specified by the COLOR or GETC instruction to locations X and Y specified by R₁ and R₂. After plotting, R₁ will be incremented.

Flags affected:
| B | ALT1 | ALT2 | O/V | S | CY | Z |
|---|---|---|---|---|---|---|
| 0 | 0 | 0 | - | - | - | - |

B : Reset
ALT1 : Reset
ALT2 : Reset

Opcode:
(MSB) (LSB)
PLOT 0 1 0 0 1 1 0 0 (4CH)

Machine Cycles:
ROM execution time 3~48 cycles
RAM execution time 3~51 cycles
Cache RAM execution time 1~48 cycles

Note: Because this instruction uses the RAM buffer, the number of machine cycles varies depending upon the program.

---

RAMB

Operation: S_reg → RAMBR

Description: This instruction moves the low byte of the source register into the game pak RAM bank register in order to specify the game pak RAM bank when transferring data between game pak RAM and multi-purpose registers. Note that the SCBR is used with the RAMBR to specify the bank for plotting. The game pak RAM bank register can only be changed with the RAMB instruction. The initial value of this register is invalid.

The source register is specified in advance using a FROM or WITH instruction. When not specified, the register defaults to R0.

Flags affected:
```
B  ALT1  ALT2  O/V  S  CY  Z
0   0     0     -   -   -   -
```
B: Reset
ALT1: Reset
ALT2: Reset

Opcode:
(MSB)           (LSB)
RAMB 0 0 1 1 1 1 1 0 (3EH)
        1 1 0 1 1 1 1 1 (DFH)

Machine Cycles:
ROM execution time 6 cycles
RAM execution time 6 cycles
Cache RAM execution time 2 cycles

Example: Under the following conditions,
S_reg: R3, R3= 0170H
the RAM bank register becomes 70H when
RAMB
is executed.

---


ROL
Operation:
The diagram illustrates a circular left shift operation. The 16-bit source register (S_reg) is shifted left by one bit. The bit at position D15 (the most significant bit) is moved into the carry flag (CY). The carry flag's previous value is moved into bit 0 (the least significant bit) of the destination register (D_reg). The result is stored in D_reg.

Description:
This instruction shifts all bits in the source register one bit to the left. Bit 15 is shifted to the carry flag and the carry flag is shifted to Bit 0. The result is stored in the destination register.

The source and destination registers are specified in advance using a WITH, FROM, or TO instruction. When not specified, the source and destination registers default to R0.

Flags affected:
B: Reset
ALT1: Reset
ALT2: Reset
O/V: -
S: Set if result is negative, else reset.
CY: Set if Bit 15 in source register is "1", else reset.
Z: Set on zero result, else reset.

Opcode:
(MSB) (LSB)
0 0 0 0 0 1 0 0 (04H)

Machine Cycles:
ROM execution time: 3 cycles
RAM execution time: 3 cycles
Cache RAM execution time: 1 cycle

Example:
Under the following conditions,
S_reg: R8, D_reg: R4
CY: 1
R8: 0 0 0 1 1 1 0 1 0 1 0 0 1 0 1 1 (1D4BH)

executing ROL results in:
CY: 0
R4: 0 0 1 1 0 1 0 1 0 0 1 0 1 1 1 (3A97H)

---

ROMB
Operation: S_reg → ROMBR
Description: This instruction moves the low byte of the source register into the game pak ROM bank register in order to specify the game pak ROM bank when loading data from game pak ROM. The game pak ROM bank register can only be changed with the ROMB instruction, but the contents can not be read. The initial value of this register is invalid.

The source register is specified in advance using a FROM or WITH instruction. When not specified, the source register defaults to R0.

Flags affected:
| B | ALT1 | ALT2 | O/V | S | CY | Z |
|---|------|------|-----|---|----|---|
| 0 | 0    | 0    | -   | - | -  | - |

Opcode:
(MSB) (LSB)
ROMB 0 0 1 1 1 1 1 (3FH)
      1 1 0 1 1 1 1 (DFH)

Machine Cycles:
ROM execution time 6 cycles
RAM execution time 6 cycles
Cache RAM execution time 2 cycles

Example:
Under the following conditions,
S_reg: R5, R5= 0046H
the ROMBR becomes 46H when
ROMB
is executed.

---

ROR

Operation:
The diagram illustrates a right rotation operation. The 16-bit source register (Sreg) is rotated right by one bit. Bit 0 (least significant bit) is moved to the carry flag (CY), and the previous value of the carry flag is moved into bit 15 (most significant bit) of the destination register (Dreg). The result is stored in Dreg.

Description:
This instruction shifts all bits in the source register one bit to the right. Bit 0 is shifted to the carry flag and the carry flag is shifted to Bit 15. The result is stored in the destination register.

The source and destination registers are specified in advance using a WITH, FROM, or TO instruction. When not specified, the source and destination registers default to R0.

Flags affected:
| B | ALT1 | ALT2 | O/V | S | CY | Z |
|---|---|---|---|---|---|---|
| 0 | 0 | 0 | - | * | * | * |

B : Reset
ALT1 : Reset
ALT2 : Reset
S : Set if result is negative, else reset.
CY : Set if Bit 0 in source register is "1", else reset.
Z : Set on zero result, else reset.

Opcode:
(MSB) (LSB)
ROR
1 0 0 1 0 1 1 (97H)

Machine Cycles:
ROM execution time 3 cycles
RAM execution time 3 cycles
Cache RAM execution time 1 cycles

Example:
Under the following conditions,
Sreg: R10, Dreg: R12

CY bit15 bit0
1 R10: 0 0 0 1 1 0 1 0 1 0 0 1 0 1 1 (1D4BH)

executing ROR results in:

CY bit15 bit0
1 R12: 1 0 0 1 1 0 1 0 1 0 0 1 0 1 (8EA5H)

---

RPIX
Operation: PIXEL COLOR from game pak RAM → Dreg
Description: This instruction loads the color data stored in game pak RAM and stores it in the destination register. Because data in game pak RAM is in the PPU format, it is first read to the color matrix and subsequently stored in the destination register. The data is then read from game pak RAM to the color matrix.
Flags affected:
| B | ALT1 | ALT2 | O/V | S | CY | Z |
|---|---|---|---|---|---|---|
| 0 | 0 | 0 | - | * | - | * |
B: Reset
ALT1: Reset
ALT2: Reset
S: Set when the result is negative, else reset.
Z: Set on zero result, else reset.
Opcode:
(MSB) (LSB)
RPIX
0 0 1 1 1 0 1 (3DH)
0 1 0 0 1 1 0 0 (4CH)
Machine Cycles:
ROM execution time 24~80 cycles
RAM execution time 24~78 cycles
Cache RAM execution time 20~74 cycles

---

SBC Rn

Operation: S_reg - R_n - CY_Flag → D_reg (n=0~15)

Description: This instruction subtracts the contents of the register specified in the operand and the carry flag from the source register and stores the result in the destination register.

Source and destination registers are specified in advance using a WITH, FROM, or TO instruction. When not specified, the source and destination registers default to R0.

Flags affected:

| B | ALT1 | ALT2 | O/V | S | CY | Z |
|---|------|------|-----|---|----|---|
| 0 | 0    | 0    | *   | * | *  | * |

B : Reset
ALT1 : Reset
ALT2 : Reset
O/V : Set on signed overflow, else reset
S : Set when the result is negative, else reset.
CY : Set on unsigned overflow, else reset
Z : Set on zero result, else reset

Opcode:

(MSB) (LSB)
SCB Rn
```
0 0 1 1 1 1 0 1
0 1 1 0 n (0H~FH)
```
(3DH) (6nH)

Machine Cycles:
ROM execution time 6 cycles
RAM execution time 6 cycles
Cache RAM execution time 2 cycles

Example: Under the following conditions:
S_reg: R4, D_reg: R6, R4=5682H, R5=3609H, CY Flag=1
register R6 becomes 2079H and the carry flag is reset when
SBC R5
is executed.

---

SBK

Operation: S_reg → (Last game pak RAM address used)

Description: The game pak RAM address accessed when data is transferred between game pak RAM and a multi-purpose register, for example the LD and ST instructions, is buffered internally. When data is to be stored to the last accessed game pak RAM address, this buffer is used so that the address does not have to be specified again in the op code. This is called "bulk processing".

This instruction uses bulk processing to store the word data contained in the source register to RAM.

The source register is specified in advance using a WITH or FROM instruction. When not specified, the register defaults to R0.

Flags affected:

| B | ALT1 | ALT2 | O/V | S | CY | Z |
|---|------|------|-----|---|----|---|
| 0 | 0    | 0    | -   | - | -  | - |

Opcode: (MSB) (LSB)
SBK 1 0 0 1 0 0 0 0 (90H)

Machine Cycles:
ROM execution time 3~8 cycles
RAM execution time 7~11 cycles
Cache RAM execution time 1~6 cycles

Example: Under the following conditions,
(70:3230H)=51H, (70:3231H)=49H, RAMBR=70H
executing,

```
LM     R1, (3230H)
INC    R1
SBK
```

will result in R1=4952H, (70:3230H)=52H, and (70:3231H)=49H.

---

SEX

Operation:
The diagram illustrates the data flow for the SEX instruction. The source register (Sreg) is a 16-bit register split into an Upper Byte (D15-D8) and a Lower Byte (D7-D0). The destination register (Dreg) is also a 16-bit register. The operation copies the Lower Byte of Sreg directly into the Lower Byte of Dreg (D7-D0). Simultaneously, it copies Bit 7 of the Upper Byte of Sreg (which is the sign bit of the lower byte) into Bits 8-15 of Dreg. This effectively sign-extends the 8-bit value from the lower byte of the source register to a 16-bit value in the destination register.

Description:
This instruction performs signed expansion of the low byte of the source register, converts it to word data and stores it in the destination register. This means that Bit 7 of the source register is stored in Bits 8 ~ 15 of the destination register. The low byte is loaded directly from the source register to the destination register. The source and destination registers are specified in advance using a WITH, FROM, or TO instruction. When not specified, the source and destination registers default to R0.

Flags affected:
| B  | ALT1 | ALT2 | O/V | S  | CY | Z  |
|----|------|------|-----|----|----|----|
| 0   | 0    | 0    | -   | *  | -  | *  |

- B: Reset
- ALT1: Reset
- ALT2: Reset
- S: Set if the result is negative, else reset.
- Z: Set on zero result, else reset.

Opcode:
(MSB) (LSB)
SEX 1 0 0 1 0 1 0 1 (95H)

Machine Cycles:
- ROM execution time: 3 cycles
- RAM execution time: 3 cycles
- Cache RAM execution time: 1 cycles

Example:
Under the following conditions,
Sreg: R5, Dreg: R1, R5= 9284H
the register R1 becomes FF84H when
SEX
is executed.

---

SMS (yy), Rn

Operation:
Rn (low byte) → (yy)
Rn (high byte) → (yy+1)
(n=0~15, yy=0~510*)
*Note: Selectable RAM address (yy) must be an even number.

Description:
Similar to SM, this instruction loads word data from register Rn, specified in the second operand, and stores it in the game pak RAM address equal to the value specified in the first operand, yy. The selectable address is an even number 0~510. The bank is specified with the RAMB instruction. This instruction uses the short address method to reduce the number of bytes in the instruction code.

Flags affected:
| B | ALT1 | ALT2 | O/V | S | CY | Z |
|---|------|------|-----|---|----|---|
| 0 | 0    | 0    | -   | - | -  | - |

B : Reset
ALT1 : Reset
ALT2 : Reset

Opcode:
(MSB) (LSB)
```
0 0 1 1 1 1 0
1 1 1 1 n (0H~FH)
kk (00H~FFH)
```
(3EH)
(AnH)
(Address)

[Short address method]
This method is used by LMS, SMS and other instructions to reduce the number of bytes in the instruction code. One byte is used for the address. The selectable address may be an even number 0~510. The relationship between yy in the syntax and kk in the opcode is:
yy = kk x 2

Machine Cycles:
ROM execution time 9~14 cycles
RAM execution time 13~17 cycles
Cache RAM execution time 3~8 cycles

Note:
Because this instruction uses the RAM buffer, the number of machine cycles varies depending upon the program.

Example:
Under the following conditions,
Register R11= ABCDH, RAMBR=71H
the following program is execution,
Syntax          Opcode
SMS  (194H), R11  3E AB CA
will result in (71:0194H) = CDH, (71:0195H) = ABH. The relationship between syntax and opcode is as shown above.

---

SM (xx), Rn

Operation:
Rn (low byte) → (xx)
Rn (high byte) → (xx+1)
(n=0~15, xx=0~65535)
When the contents of Rn are even, the high byte is stored at address (Rn+1);
When the contents of Rn are odd, the high byte is stored at address (Rn-1).

Description:
This instruction stores the contents of register Rn, specified in the second operand, to the game pak RAM address which equals the value of (xx), the first operand. The RAM bank must be specified with the RAMB instruction. (Refer to RAMB.)

Flags affected:
| B | ALT1 | ALT2 | O/V | S | CY | Z |
|---|---|---|---|---|---|---|
| 0 | 0 | 0 | - | - | - | - |

B : Reset
ALT1 : Reset
ALT2 : Reset

Opcode:
(MSB) (LSB)
```
0 0 1 1 1 1 0  (3EH)
1 1 1 1 n (0H~FH)  (FnH)
x (00H~FFH)      (ADRS Lower Byte)
x (00H~FFH)      (ADRS Upper Byte)
```

Machine Cycles:
ROM execution time 12~17 cycles
RAM execution time 16~20 cycles
Cache RAM execution time 4~9 cycles

Note:
Because this instruction uses the RAM buffer, the number of cycles varies depending upon the program.

Example:
Under the following conditions,
R4=438CH and RAMBR=70H
the following program execution,
SM (0B492H), R4
will result in (70:B492H) =8CH, (70:B493H) = 43H.

---

STB (Rm)

Operation: S_reg (low byte) → (R_m) (m=0~11)

Description: This instruction stores the low byte of the source register in the game pak RAM address equal to the value in the register specified in the operand. The operand can be a register R0~R11. The game pak RAM bank must be specified with the RAMB instruction.

The source register is specified in advance using a WITH or FROM instruction. When not specified, the register defaults to R0.

Flags affected:

| B | ALT1 | ALT2 | O/V | S | CY | Z |
|---|------|------|-----|---|----|---|
| 0 | 0    | 0    | -   | - | -  | - |

B: Reset
ALT1: Reset
ALT2: Reset

Opcode:

(MSB) (LSB)
STB (Rm)
0 0 1 1 1 1 0 1 (3DH)
0 0 1 1 m (0H~BH) (3mH)

Machine Cycles:
ROM execution time 6~9 cycles
RAM execution time 8~14 cycles
Cache RAM execution time 2~5 cycles

Note: Because this instruction uses the RAM buffer, the number of machine cycles varies depending upon the program.

Example: Under the following conditions,
S_reg:R5, R5=216CH, R8=9A34H, RAMBR=70H
and when the following program is executed,
```
STB (R8)
```
the result is (70:9A34H)=6CH.

---

STOP

Operation: 0 → Go flag

Description: This instruction resets the GSU GO flag and stops the processor. When this instruction is executed and the GSU stops, the Super NES IRQ signal is initiated.

Flags affected:

| B | ALT1 | ALT2 | O/V | S | CY | Z |
|---|---|---|---|---|---|---|
| 0 | 0 | 0 | - | - | - | - |

B : Reset
ALT1 : Reset
ALT2 : Reset

Opcode:
(MSB) (LSB)
STOP
`0 0 0 0 0 0 0 0` (00H)

Machine Cycles:
ROM execution time 3 cycles
RAM execution time 3 cycles
Cache RAM execution time 1 cycles

---

STW (Rm)

Operation:
S_reg (low byte) → (R_m)
S_reg (high byte) → (R_m + 1)
When the contents of R_m are even, the high byte is stored at address (R_m+1);
When the contents of R_m are odd, the high byte is stored at address (R_m-1).

Description:
This instruction stores the contents of the source register into the game pak RAM address specified in the operand, R_m. The RAM bank must be specified with the RAMB instruction. The operand can be a register from R_0~R_11.

The source register is specified in advance using WITH or FROM. When not specified, the register defaults to R_0.

Flags affected:
```
B  ALT1  ALT2  O/V  S  CY  Z
0   0     0     -    -  -   -
```
B : Reset
ALT1 : Reset
ALT2 : Reset

Opcode:
(MSB)           (LSB)
STW (R_m)       0 0 1 1  m (0H~BH)  (3mH)

Machine Cycles:
ROM execution time     3~8 cycles
RAM execution time     7~11 cycles
Cache RAM execution time  1~6 cycles

Note:
Because this instruction uses the RAM buffer, the number of cycles varies depending upon the program.

Example:
Under the following conditions,
S_reg:R_10, R_10=9326H, R_2:5872H, RAMBR=70H
and when the following program is executed,
STW (R_2)
the result is (70:5872H)=26H, (70:5873H)=93H.

---

SUB Rn
Operation: S_reg - R_n → D_reg (n=0~15)
Description: This instruction subtracts the contents of the register specified in the operand from the source register and stores the result in the destination register.

Source and destination registers are specified in advance using a WITH, FROM, or TO instruction. When not specified, the source and destination registers default to R0.

The operand can be any of registers R0~R15.

Flags affected:
| B | ALT1 | ALT2 | O/V | S | CY | Z |
|---|------|------|-----|---|----|---|
| 0 | 0    | 0    | *   | * | *  | * |

B : Reset
ALT1 : Reset
ALT2 : Reset
O/V : Set on signed overflow, else reset.
S : Set if the result is negative, else reset
CY : Set on unsigned overflow, else reset (Set on adder overflow.)
Z : Set if result is zero.

Opcode:
(MSB) (LSB)
SUB Rn 0 1 1 0 n (0H~FH) (6nH)

Machine Cycles:
ROM execution time 3 cycles
RAM execution time 3 cycles
Cache RAM execution time 1 cycles

Example:
Under the following conditions:
S_reg: R5, D_reg: R4, R5=735AH, R8=426BH
the register R4 becomes 30EFH when
SUB R8
is executed.

---

SUB #n

Operation: S_reg - #n → D_reg (n=0~15)

Description: This instruction subtracts the immediate data specified in the operand from the contents of the source register and stores the result in the destination register.

The source and destination registers are specified in advance using a WITH, FROM, or TO instruction. When not specified, the source and destination registers default to R0.

The operand can be immediate data from 0-15.

Flags affected:

| B | ALT1 | ALT2 | O/V | S | CY | Z |
|---|------|------|-----|---|----|---|
| 0 | 0    | 0    | *   | * | *  | * |

B : Reset
ALT1 : Reset
ALT2 : Reset
O/V : Set on signed overflow, else reset.
S : Set if the result is negative, else reset
CY : Set on unsigned borrow, else reset
Z : Set if result is zero.

Opcode:

(MSB) (LSB)
```
0 0 1 1 1 1 0
0 1 1 0 n (0H~FH)
```
(3EH) (6nH)

Machine Cycles:
ROM execution time 6 cycles
RAM execution time 6 cycles
Cache RAM execution time 2 cycles

Example: Under the following conditions:
S_reg: R0, D_reg: R0, R0=329BH
the register R0 becomes 3291H when
SUB #10
is executed.

---

SWAP
Operation:
S_reg (low byte) → D_reg (high byte)
S_reg (high byte) → D_reg (low byte)

Description:
This instruction swaps the low byte and high byte of the source register and stores the result in the destination register.

The source and destination registers are specified in advance using a FROM, WITH, or TO instruction. When not specified, the source and destination registers default to R0.

Flags affected:
| B | ALT1 | ALT2 | O/V | S | CY | Z |
|---|------|------|-----|---|----|---|
| 0 | 0    | 0    | -   | * | -  | * |

B : Reset
ALT1 : Reset
ALT2 : Reset
S : Set when the result is negative, else reset.
Z : Set on zero result, else reset.

Opcode:
(MSB) (LSB)
SWAP 0 1 0 0 1 1 0 1 (4DH)

Machine Cycles:
ROM execution time 3 cycles
RAM execution time 3 cycles
Cache RAM execution time 1 cycles

Example:
Under the following conditions:
S_reg: R3, D_reg: R13, R3=48D0H
the register R13 becomes D048H when
SWAP
is executed.

---

TO Rn

REGISTER PREFIX INSTRUCTION

Operation:
If B Flag = 0 then set D_reg to R_n
else S_reg → R_n
(n=0~15)

Description:
This instruction specifies register R_n as the destination register.
The destination register can be any of registers R_0 ~ R_15.

If the B flag has been set (i.e., if a WITH instruction was executed immediately prior to this instruction) the contents of the source register are loaded to R_n (refer to MOVE R_n, R_n').

Flags affected:
| B | ALT1 | ALT2 | O/V | S | CY | Z |
|---|------|------|-----|---|---|---|
| - | -    | -    | -   | - | - | - |
No flags affected

Opcode:
(MSB) (LSB)
TO R_n
[0] [0] [0] [1] n (0H~FH) (1nH)

Machine Cycles:
ROM execution time 3 cycles
RAM execution time 3 cycles
Cache RAM execution time 1 cycles

Example:
Under the following conditions:
R_6=7106H, R_3=0028H

the register R_4 becomes 712EH when the following program is executed.

```
FROM R_6
TO R_4
ADD R_3
```

---

UMULT Rn
Operation: S_reg (low byte) * R_n (low byte) → D_reg
Description: This instruction performs 8 x 8-bit unsigned multiplication using the low byte of the source register and the low byte of register R_n, specified in the operand. The result is stored in the destination register.

The source and destination registers are specified in advance using a FROM, WITH, or TO instruction. When not specified, the source and destination registers default to R0.

Flags affected:
| B | ALT1 | ALT2 | O/V | S | CY | Z |
|---|------|------|-----|---|----|---|
| 0 | 0    | 0    | -   | * | -  | * |

B : Reset
ALT1 : Reset
ALT2 : Reset
S : Set when the result is negative, else reset.
Z : Set on zero result, else reset.

Opcode:
(MSB) (LSB)
UMULT Rn
| 0 | 0 | 1 | 1 | 1 | 0 | 1 | (3DH)
| 1 | 0 | 0 | 0 | n (0H~FH) | (8nH)

Machine Cycles:
ROM execution time 6 or 8 cycles
RAM execution time 6 or 8 cycles
Cache RAM execution time 2 or 3 cycles

Note: The number of cycles depends on the CONFIG register setting.

Example: Under the following conditions,
S_reg: R3, D_reg: R0, R3= 364FH, R8= B2CFH
the register R0 becomes 3FE1H when
UMULT R8
is executed.

---

