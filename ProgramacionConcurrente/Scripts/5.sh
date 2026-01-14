#!/bin/bash

NUM_1=0
NUM_2=0
RES=0
OPERADOR="+"
GREEN="\e[32m"
BLUE="\e[36m"
RED="\e[31m"
BOLD="\e[1m"
RESET="\e[0m"

# Funciones

error_msg(){
    echo -e "${RED}Error: $1${RESET}"
}

validar_numeros(){
    if ! [[ "$NUM_1" =~ ^-?[0-9]+$ ]] || ! [[ "$NUM_2" =~ ^-?[0-9]+$ ]]; then
        error_msg "Debes introducir números enteros válidos."
        return 1
    fi
    return 0
}

suma(){
    RES=$((NUM_1 + NUM_2))
    echo ""
    echo -e "Resultado: ${GREEN}$RES${RESET}"
    echo ""
}

resta(){
    RES=$((NUM_1 - NUM_2))
    echo ""
    echo -e "Resultado: ${GREEN}$RES${RESET}"
    echo ""
}

division(){
    if [ "$NUM_2" -eq 0 ]; then
        echo ""
        error_msg "No se puede dividir por cero."
        echo ""
        return
    fi
    RES=$((NUM_1 / NUM_2))
    echo ""
    echo -e "Resultado: ${GREEN}$RES${RESET}"
    echo ""
}

multiplicacion(){
    RES=$((NUM_1 * NUM_2))
    echo ""
    echo -e "Resultado: ${GREEN}$RES${RESET}"
    echo ""
}

modulo(){
    if [ "$NUM_2" -eq 0 ]; then
        echo ""
        error_msg "No se puede calcular módulo con cero."
        echo ""
        return
    fi
    RES=$((NUM_1 % NUM_2))
    echo ""
    echo -e "Resultado: ${GREEN}$RES${RESET}"
    echo ""
}

exponencial(){
    RES=$((NUM_1 ** NUM_2))
    echo ""
    echo -e "Resultado: ${GREEN}$RES${RESET}"
    echo ""
}

menu(){
    while :
        do
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
            echo -e "${BOLD}7) salir${BOLD}"
            read -p "> $(echo -e $BLUE)" OPERADOR
            echo -ne "${RESET}"

            case $OPERADOR in
                "1"|"+"|"suma") validar_numeros && suma
                ;;
                "2"|"-"|"resta") validar_numeros && resta
                ;;
                "3"|"/"|"división"|"division") validar_numeros && division
                ;;
                "4"|"*"|"multipliación"|"multiplicacion") validar_numeros && multiplicacion
                ;;
                "5"|"%"|"módulo"|"modulo") validar_numeros && modulo
                ;;
                "6"|"**"|"exponencial") validar_numeros && exponencial
                ;;
                "7"|"salir del programa") break
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