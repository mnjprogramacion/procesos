#!/bin/bash

NUM_1=0
NUM_2=0
RES=0
OPERADOR="+"
ERROR=0
GREEN="\e[32m"
BLUE="\e[36m"
RESET="\e[0m"

# Funciones

suma (){
    RES=$((NUM_1 + NUM_2))
    echo ""
    echo -e "Resultado: ${GREEN}$RES${RESET}"
    echo ""
}

resta (){
    RES=$((NUM_1 - NUM_2))
    echo ""
    echo -e "Resultado: ${GREEN}$RES${RESET}"
    echo ""
}

division (){
    RES=$((NUM_1 / NUM_2))
    echo ""
    echo -e "Resultado: ${GREEN}$RES${RESET}"
    echo ""
}

multiplicacion (){
    RES=$((NUM_1 * NUM_2))
    echo ""
    echo -e "Resultado: ${GREEN}$RES${RESET}"
    echo ""
}

modulo (){
    RES=$((NUM_1 % NUM_2))
    echo ""
    echo -e "Resultado: ${GREEN}$RES${RESET}"
    echo ""
}

exponencial (){
    RES=$((NUM_1 ** NUM_2))
    echo ""
    echo -e "Resultado: ${GREEN}$RES${RESET}"
    echo ""
}

menu (){
    while [ $ERROR -eq 0 ]; do

        echo "Introduce un número:"
        read -p "> $(echo -e $BLUE)" NUM_1
        echo -ne "${RESET}"

        echo "Introduce otro número:"
        read -p "> $(echo -e $BLUE)" NUM_2
        echo -ne "${RESET}"

        echo ""
        echo "Elige un operador de la lista:"
        echo ""
        echo "1) + (suma)"
        echo "2) - (resta)"
        echo "3) / (división)"
        echo "4) * (multipliación)"
        echo "5) % (módulo)"
        echo "6) ** (exponencial)"
        echo "7) salir"
        read -p "> $(echo -e $BLUE)" OPERADOR
        echo -ne "${RESET}"

        case $OPERADOR in
            "1"|"+"|"suma") suma
            ;;
            "2"|"-"|"resta") resta
            ;;
            "3"|"/"|"división"|"division") division
            ;;
            "4"|"*"|"multipliación"|"multipliacion") multiplicacion
            ;;
            "5"|"%"|"módulo"|"modulo") modulo
            ;;
            "6"|"**"|"exponencial") exponencial
            ;;
            "7"|"salir") break
            ;;
            *) echo "Operador no válido."
            ;;
        esac
    done
}

# Inicio del programa

clear
echo "╔═════════════╗"
echo "║ CALCULADORA ║"
echo "╚═════════════╝"
echo ""
menu