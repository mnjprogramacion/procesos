#!/bin/bash

ANCHO=40
DINO_POS=35
SALTO=0
PUNTOS=0

# Colores
C_MARRON="\e[38;5;130m"
C_VERDE="\e[32m"
C_ROJO="\e[31m"
C_RESET="\e[0m"

# Sprites
DINO="🦖"
CACTUS="🌵"
METEORITO="☄️"

# Configuración de obstáculos
ESPACIO_MIN=12
MAX_OBS=3

# Arrays para múltiples obstáculos
declare -a OBS_X=()
declare -a OBS_TIPO=()

trap "tput cnorm; stty sane; clear; exit" EXIT INT
tput civis
clear

echo "╔══════════╗"
echo "║ DINO RUN ║"
echo "╚══════════╝"
echo ""
echo -e "${C_VERDE}S = saltar${C_RESET} | ${C_ROJO}Q = salir${C_RESET}"
echo ""
read -p "Presiona ENTER para empezar..."
clear

while true; do
    # Mover cursor al inicio (sin borrar)
    tput cup 0 0
    
    # Leer tecla sin bloquear
    tecla=""
    read -rsn1 -t 0.2 tecla 2>/dev/null || true
    [[ "$tecla" == "q" || "$tecla" == "Q" ]] && break
    [[ "$tecla" == "s" || "$tecla" == "S" ]] && [[ $SALTO -eq 0 ]] && SALTO=3
    
    # Salto (baja cada frame)
    [[ $SALTO -gt 0 ]] && ((SALTO--))
    
    # Mover todos los obstáculos
    nuevos_x=()
    nuevos_tipo=()
    for ((j=0; j<${#OBS_X[@]}; j++)); do
        nueva_pos=$((OBS_X[j] + 2))
        if [[ $nueva_pos -le $ANCHO ]]; then
            nuevos_x+=($nueva_pos)
            nuevos_tipo+=(${OBS_TIPO[j]})
        else
            ((PUNTOS++))
        fi
    done
    OBS_X=("${nuevos_x[@]}")
    OBS_TIPO=("${nuevos_tipo[@]}")
    
    # Crear nuevo obstáculo si hay espacio
    if [[ ${#OBS_X[@]} -lt $MAX_OBS ]]; then
        # Buscar el obstáculo más cercano al inicio (posición más baja)
        menor=999
        for pos in "${OBS_X[@]}"; do
            [[ $pos -lt $menor ]] && menor=$pos
        done
        # Solo crear si el obstáculo más cercano ya ha pasado el espacio mínimo
        if [[ $menor -ge $ESPACIO_MIN ]] || [[ ${#OBS_X[@]} -eq 0 ]]; then
            if [[ $((RANDOM % 5)) -eq 0 ]]; then  # 20% probabilidad
                OBS_X+=(0)
                OBS_TIPO+=($((RANDOM % 2)))
            fi
        fi
    fi
    
    # Construir líneas antes de imprimir
    aire=""
    suelo=""
    skip_aire=0
    skip_suelo=0
    for ((i=0; i<ANCHO; i++)); do
        # Saltar posición si el emoji anterior ya la ocupó
        if [[ $skip_aire -eq 1 ]]; then
            skip_aire=0
        else
            char_aire=" "
            # Dino en aire
            if [[ $i -eq $DINO_POS && $SALTO -gt 0 ]]; then
                char_aire="$DINO"
                skip_aire=1
            fi
            # Meteoritos
            for ((j=0; j<${#OBS_X[@]}; j++)); do
                if [[ $i -eq ${OBS_X[j]} && ${OBS_TIPO[j]} -eq 1 ]]; then
                    char_aire="$METEORITO"
                    skip_aire=1
                fi
            done
            aire+="$char_aire"
        fi
        
        if [[ $skip_suelo -eq 1 ]]; then
            skip_suelo=0
        else
            char_suelo="_"
            # Dino en suelo
            if [[ $i -eq $DINO_POS && $SALTO -eq 0 ]]; then
                char_suelo="$DINO"
                skip_suelo=1
            fi
            # Cactus
            for ((j=0; j<${#OBS_X[@]}; j++)); do
                if [[ $i -eq ${OBS_X[j]} && ${OBS_TIPO[j]} -eq 0 ]]; then
                    char_suelo="$CACTUS"
                    skip_suelo=1
                fi
            done
            suelo+="$char_suelo"
        fi
    done
    
    # Dibujar todo de una vez
    printf "Puntos: %-10s\n\n%s\n${C_MARRON}%s${C_RESET}\n" "$PUNTOS" "$aire" "$suelo"
    
    # Colisión con cualquier obstáculo
    COLISION=0
    for ((j=0; j<${#OBS_X[@]}; j++)); do
        if [[ ${OBS_X[j]} -ge $((DINO_POS-1)) && ${OBS_X[j]} -le $((DINO_POS+1)) ]]; then
            # Cactus: muere si NO salta
            [[ ${OBS_TIPO[j]} -eq 0 && $SALTO -eq 0 ]] && COLISION=1
            # Meteorito: muere si SALTA
            [[ ${OBS_TIPO[j]} -eq 1 && $SALTO -gt 0 ]] && COLISION=1
        fi
    done
    
    if [[ $COLISION -eq 1 ]]; then
        echo ""
        echo -e "${C_ROJO}GAME OVER${C_RESET}"
        echo ""
        echo -e "${C_VERDE}Puntos: $PUNTOS${C_RESET}"
        echo ""
        read -p "ENTER para reiniciar..."
        OBS_X=()
        OBS_TIPO=()
        PUNTOS=0
        clear
    fi
done

echo "¡Hasta luego!"