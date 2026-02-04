public class RecursividadCalculadora {

    private String expresion;
    private int pos;

    public RecursividadCalculadora(String expresion) {
        this.expresion = expresion.replaceAll("\\s+", "");
        this.pos = 0;
    }

    public int parsearExpresion() {
        int resultado = parsearTermino();

        while (pos < expresion.length()) {
            char operador = expresion.charAt(pos);
            
            if (operador == '+') {
                pos++;
                resultado = resultado + parsearTermino();
            } else if (operador == '-') {
                pos++;
                resultado = resultado - parsearTermino();
            } else {
                break;
            }
        }

        return resultado;
    }

    private int parsearTermino() {
        int resultado = parsearFactor();

        while (pos < expresion.length()) {
            char operador = expresion.charAt(pos);
            
            if (operador == '*') {
                pos++;
                resultado = resultado * parsearFactor();
            } else if (operador == '/') {
                pos++;
                resultado = resultado / parsearFactor();
            } else {
                break;
            }
        }

        return resultado;
    }

    private int parsearFactor() {
        boolean negativo = false;
        if (pos < expresion.length() && expresion.charAt(pos) == '-') {
            negativo = true;
            pos++;
        }

        int resultado;

        if (pos < expresion.length() && expresion.charAt(pos) == '(') {
            pos++;
            resultado = parsearExpresion();
            if (pos < expresion.length() && expresion.charAt(pos) == ')') {
                pos++;
            }
        } else {
            resultado = parsearNumero();
        }

        return negativo ? -resultado : resultado;
    }

    private int parsearNumero() {
        int inicio = pos;
        
        while (pos < expresion.length() && Character.isDigit(expresion.charAt(pos))) {
            pos++;
        }
        
        if (inicio == pos) {
            return 0;
        }
        
        return Integer.parseInt(expresion.substring(inicio, pos));
    }

    public static int calcular(String expresion) {
        RecursividadCalculadora calc = new RecursividadCalculadora(expresion);
        return calc.parsearExpresion();
    }

    public static void main(String[] args) {

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            sb.append(args[i]);
            if (i < args.length - 1) {
                sb.append(" ");
            }
        }
        String expresion = sb.toString();

        System.out.printf("Expresión: %s%n", expresion);
        System.out.printf("Resultado: %d%n", calcular(expresion));
    }
}