#!/bin/bash

HP_JUGADOR=100
HP_BOSS=100
DANO_EXTRA=0
HEROE=${1:-"Aventurero"}
JEFE="El Rey del Bug"

# Selección de clase
clear
echo "╔═════════════════════════════════╗"
sleep 0.3
echo "║ BASH LEGENDS II: EL REY DEL BUG ║"
sleep 0.3
echo "╚═════════════════════════════════╝"
sleep 0.5
echo ""
echo "Bienvenido, $HEROE. ¿Cuál es tu clase?"
echo ""
sleep 1
echo "1) Mago (más daño)"
echo "2) Paladín (más vida)"
echo "3) Arquero (menos vida pero mucho más daño)"
echo "4) Vagabundo (peregrino sin rumbo)"
read -p "Elige un número (1-4): " CLASE_NUM

case $CLASE_NUM in
    1) CLASE="Mago"
       DANO_EXTRA=2
       ;;
    2) CLASE="Paladín"
       HP_JUGADOR=120
       ;;
    3) CLASE="Arquero"
       HP_JUGADOR=80
       DANO_EXTRA=3
       ;;
    4|*) CLASE="Vagabundo"
       HP_JUGADOR=50
       DANO_EXTRA=-2
       ;;
esac

echo ""
echo "¡Has elegido ser un $CLASE!"
sleep 1

# Introducción
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
echo "¡$JEFE está al otro lado!"
sleep 1.5
echo "$JEFE se da la vuelta..."
sleep 1.5
echo "\"Así que tú eres $HEROE... prepárate para morir.\""
sleep 1.5
for i in {1..40}; do
    echo -n "-"
    sleep 0.03
done
echo ""
echo ""
echo "            EMPIEZA LA PELEA"
echo ""
for i in {1..40}; do
    echo -n "-"
    sleep 0.03
done
echo ""

