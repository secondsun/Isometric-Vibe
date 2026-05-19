; SuperFX GSU-2 Vertical Span Filler Snippet
; Optimized for 4bpp, 192x172 resolution
; Target: ca65 assembler

.segment "GSU_CODE"

; --- Register Usage Strategy ---
; R0      : Temp / MATH
; R1      : Current Y
; R2      : End Y
; R3      : Color
; R11     : Screen X (Column)
; R12     : Screen Y (Current Plot Y)
; R13     : Color (Hardware COLOR register linked)
; R14     : Pointer to ROM/RAM (if needed)
; R15     : Program Counter

.align 512 ; Ensure the inner loop doesn't cross a cache page boundary (512 bytes)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; Fast Vertical Span Filler
; Inputs:
;   R11 = X coordinate (0-191)
;   R1  = Start Y coordinate
;   R2  = End Y coordinate
;   R13 = Color Index (0-15 for 4bpp)
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

DrawVerticalSpan:
    ; Calculate height (R0 = R2 - R1)
    FROM R2
    SUB R1
    BCC @Exit           ; If EndY < StartY, nothing to draw
    MOVE R0, R12        ; R12 will be our loop counter / iterator
    
    ; Initialize Y for PLOT
    FROM R1
    MOVE R0, R12        ; Set initial Y in R12
    
    ; Set the color once (affects all subsequent PLOTs)
    COLOR               ; Moves R13 to internal COLOR register

@Loop:
    PLOT                ; Plot pixel at (R11, R12). 
                        ; This also increments R12 (Y) automatically if the 
                        ; POR (Plot Option Register) is configured for it.
                        ; On GSU-2, PLOT is extremely fast when hitting 
                        ; the internal 8-pixel buffer.

    ; Decrement our count (R0) and loop
    ; Note: If we use the 'LOOP' instruction, it uses R13 as the counter,
    ; but we are using R13 for COLOR. We'll use manual DEC/BNE for safety
    ; or re-map registers if absolute speed is needed.
    
    SUB #1
    BNE @Loop

@Exit:
    ; Return or continue
    ; ... (typically RTS if called as subroutine)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; 1D Span Buffer Optimization (Stencil Check)
; Use this before calling DrawVerticalSpan to reduce overdraw.
; Memory: Allocation of 192 bytes for 'UpperY' and 192 for 'LowerY' in GSU RAM.
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

CheckStencil:
    ; R11 = Current X
    ; R1  = Candidate Start Y
    ; R2  = Candidate End Y
    
    ; 1. Load existing min/max for this X from GSU RAM
    ; 2. Compare R1, R2 against stored bounds
    ; 3. Only PLOT the segments that are outside the current bounds
    ; 4. Update bounds in RAM
    ; This enables FRONT-TO-BACK rendering to be 100% efficient.
