public class RecursividadCalculadora {

    private String expresion;
    private int pos;

    public RecursividadCalculadora(String expresion) {
        this.expresion = expresion.replaceAll("\\s+", "");
        this.pos = 0;
    }

    public static int multiplicar(int n1, int n2) {

        if ((n1 == 0) || (n2 == 0)) {
            return 0;
        }

        boolean negativo = (n1 < 0) ^ (n2 < 0);
        n1 = n1 < 0 ? -n1 : n1;
        n2 = n2 < 0 ? -n2 : n2;

        int resultado = multiplicarPositivo(n1, n2);
        return negativo ? -resultado : resultado;
    }

    private static int multiplicarPositivo(int n1, int n2) {

        if ((n1 == 0) || (n2 == 0)) {
            return 0;
        }

        if (n2 == 1) {
            return n1;
        } else {
            n2--;
            return n1 + multiplicarPositivo(n1, n2);
        }
    }

    public static int dividir(int n1, int n2) {

        if ((n1 == 0) || (n2 == 0)) {
            return 0;
        }

        boolean negativo = (n1 < 0) ^ (n2 < 0);
        n1 = n1 < 0 ? -n1 : n1;
        n2 = n2 < 0 ? -n2 : n2;

        int resultado = dividirPositivo(n1, n2);
        return negativo ? -resultado : resultado;
    }

    private static int dividirPositivo(int n1, int n2) {

        if ((n1 == 0) || (n2 == 0)) {
            return 0;
        }

        if (n1 < n2) {
            return 0;
        } else {
            n1 = n1 - n2;
            return 1 + dividirPositivo(n1, n2);
        }
    }

    public static int sumar(int n1, int n2) {
        if (n2 == 0) {
            return n1;
        }
        if (n2 > 0) {
            return sumar(n1 + 1, n2 - 1);
        } else {
            return sumar(n1 - 1, n2 + 1);
        }
    }

    public static int restar(int n1, int n2) {
        return sumar(n1, -n2);
    }

    public int parsearExpresion() {
        int resultado = parsearTermino();

        while (pos < expresion.length()) {
            char operador = expresion.charAt(pos);
            
            if (operador == '+') {
                pos++;
                resultado = sumar(resultado, parsearTermino());
            } else if (operador == '-') {
                pos++;
                resultado = restar(resultado, parsearTermino());
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
                resultado = multiplicar(resultado, parsearFactor());
            } else if (operador == '/') {
                pos++;
                resultado = dividir(resultado, parsearFactor());
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