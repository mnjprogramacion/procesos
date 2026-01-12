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
sleep 1

# introducción
echo ""
echo "----------------------------------------"
echo "Te adentras en el palacio prohibido..."
sleep 1.5
echo "No se oye nada... Pero te sientes observado..."
sleep 1.5
echo "Avanzas lentamente por un largo pasillo, hasta llegar a una puerta enorme."
sleep 1.5
echo "Decides abrir la puerta..."
sleep 1.5
echo ""
echo "¡$JEFE ESTÁ AL OTRO LADO!"
sleep 1.5
echo "$JEFE se da la vuelta..."
echo "\"Así que tú eres $HEROE... prepárate para morir.\""
echo "----------------------------------------"
echo ""
