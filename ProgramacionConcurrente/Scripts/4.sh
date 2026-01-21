#!/bin/bash

HP_JUGADOR=100
HP_BOSS=100
DANO_EXTRA=0
HEROE=${1:-"Aventurero"}
JEFE=${2:-"El Rey del Bug"}

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

# Bucle pelea
while [ $HP_JUGADOR -gt 0 ] && [ $HP_BOSS -gt 0 ]; do
    
    echo "$HEROE: $HP_JUGADOR HP  vs  $JEFE: $HP_BOSS HP"
    
    # Turno jugador
    OBJETIVO_ALEATORIO=$((RANDOM % 11))
    
    echo "¡Tu turno! Elige un ataque (número del 0 al 10):"
    read -p "> " NUM_USER

    if [[ ! $NUM_USER =~ ^[0-9]+$ ]]; then
        echo "No has introducido un número válido."
        HP_JUGADOR=$((HP_JUGADOR - 1))
        echo "Pierdes 1 de vida por listo."
    else
        DANO=$((OBJETIVO_ALEATORIO - NUM_USER))
        if [ $DANO -lt 0 ]; then DANO=$((DANO * -1)); fi
        
        DANO=$((DANO + DANO_EXTRA))
        echo "Tu golpe tiene una fuerza de $DANO."
        
        if [ $DANO -lt 0 ]; then 
            echo "¡$HEROE está tan perdido que le ha sumado vida a $JEFE!"
        fi
        
        HP_BOSS=$((HP_BOSS - DANO))
    fi

    # Comprobación vida jefe
    if [ $HP_BOSS -le 0 ]; then break; fi
    
    sleep 1

    # Turno jefe
    ATAQUE_JEFE=$((RANDOM % 11))
    DANO_JEFE=$((ATAQUE_JEFE - OBJETIVO_ALEATORIO))
    if [ $DANO_JEFE -lt 0 ]; then DANO_JEFE=$((DANO_JEFE * -1)); fi

    echo "$JEFE contrataca..."
    echo "Te hace $DANO_JEFE de daño."
    
    HP_JUGADOR=$((HP_JUGADOR - DANO_JEFE))
    
    echo "----------------------------------------"
    sleep 1
done

# Final
echo ""
echo "========================================"
if [ $HP_JUGADOR -gt 0 ]; then
    echo "¡VICTORIA!"
    sleep 1.5
    echo "$HEROE el $CLASE ha derrotado a $JEFE."
    sleep 1.5
    echo "Tu leyenda perdurará por los siglos venideros."
else
    echo "HAS MUERTO..."
    sleep 1.5
    echo "$JEFE se ríe sobre tu cadáver."
    sleep 1.5
    echo "A ver si vamos espabilando."
fi
echo "========================================"
echo ""