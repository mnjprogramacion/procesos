#!/bin/bash

HP_JUGADOR=100
HP_BOSS=100
HEROE=${1:-"Aventurero"}
JEFE="El Rey del Bug"

# selección de clase
clear
echo "=== BASH LEGENDS II: EL REY DEL BUG ==="
echo ""
echo "Bienvenido, $HEROE. ¿Cuál es tu clase?"
echo "1) Mago"
echo "2) Paladín"
echo "3) Arquero"
echo "4) Bardo"
read -p "Elige un número (1-4): " CLASE_NUM

case $CLASE_NUM in
    1) CLASE="Mago";;
    2) CLASE="Paladín";;
    3) CLASE="Arquero";;
    4|*) CLASE="Bardo";;
esac

echo "¡Has elegido ser un $CLASE!"
