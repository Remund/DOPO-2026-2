PROYECTO: SlotMachine
ESCUELA COLOMBIANA DE INGENIERIA - Desarrollo Orientado por Objetos
Proyecto Inicial 2026-2 - Ciclo No. 3/4 (Refactoring y Extension)

-------------------------------------------------------------------
PROPOSITO
-------------------------------------------------------------------
Simulador de una maquina tragamonedas inspirado en el Problem I de la
maraton de programacion internacional 2025 (Slot Machine). En este
simulador los simbolos son colores diferentes.

Esta entrega SI resuelve el problema de la maraton (SlotMachineContest).

-------------------------------------------------------------------
CLASES
-------------------------------------------------------------------
SlotMachine ......... La maquina: catalogo de simbolos + ruedas.
SlotMachineContest .. Solucion (solve) y simulacion (simulate) de la maraton.
Symbol .............. Un simbolo (color CSS unico dentro del catalogo).
Wheel ............... Una rueda (reel) individual.
Canvas .............. Lienzo de dibujo (extendido a colores CSS).
Circle, Rectangle ... Figuras usadas para dibujar las ruedas.
Triangle ............ Figura del paquete shapes original (sin uso).

Pruebas de unidad:
SlotMachineTest ..... Pruebas del ciclo 1.
SlotMachineC2Test ... Pruebas del ciclo 2 (requisitos 9 a 12).
SlotMachineCC2Test .. Pruebas compartidas del curso (ciclo 2).
SlotMachineContestTest .. Pruebas del ciclo 3 (requisitos 13 y 14).
SlotMachineContestCTest . Pruebas compartidas del curso (ciclo 3).

Pruebas de aceptacion (visibles, con pausas; ejecutar sus metodos estaticos):
SlotMachineC2AcceptanceTest ....... swapAndLockWheels, spinWheelsBySteps
SlotMachineContestAcceptanceTest .. useMachineAsTestingTool, solveAndSimulate

-------------------------------------------------------------------
REQUISITOS DEL CICLO 3
-------------------------------------------------------------------
13. Maquina de n ruedas y n simbolos ..... SlotMachine(n)
14. Solucionar el problema de la maraton . SlotMachineContest.solve(n)
15. Simular la solucion .................. SlotMachineContest.simulate(n)

-------------------------------------------------------------------
REQUISITOS DEL CICLO 2
-------------------------------------------------------------------
 9. Intercambiar dos ruedas .............. swap(wheel1, wheel2)
10. Fijar y soltar una rueda ............. lock(wheel) / unlock(wheel)
11. Rotar una rueda un numero de pasos ... spin(wheel, steps)
12. Dejar la maquina en una configuracion . spin(String[] setSymbols)

Requisito de usabilidad: en spin(wheel, steps), si el simulador esta
visible, el movimiento se visualiza paso a paso.

-------------------------------------------------------------------
CONVENCIONES
-------------------------------------------------------------------
- Las posiciones se enumeran desde 1. Una posicion menor que 1 se
  ajusta a 1; una mayor al maximo se ajusta al maximo.
- Toda operacion de comando deja el resultado en ok(): true si se
  ejecuto, false si no fue posible.
- Las pruebas de unidad se ejecutan con la maquina INVISIBLE, para que
  no aparezcan dialogos modales (JOptionPane).

Basado en el proyecto "shapes" de David J. Barnes y Michael Kolling
(Objects First with Java, Pearson Education).
