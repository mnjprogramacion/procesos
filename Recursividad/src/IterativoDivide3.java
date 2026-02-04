public class IterativoDivide3 {
 
    public static int dividir(int n1, int n2, int n3) {
        if (n1 == 0 || n2 == 0 || n3 == 0) {
            return 0;
        }

        int resultado = 0;
        while (n1 >= n2) {
            n1 = n1 - n2;
            resultado++;
        }

        int resultadoFinal = 0;
        while (resultado >= n3) {
            resultado = resultado - n3;
            resultadoFinal++;
        }

        return resultadoFinal;
    }
 
    public static void main(String[] args) {

        int n1 = Integer.parseInt(args[0]);
        int n2 = Integer.parseInt(args[1]);
        int n3 = Integer.parseInt(args[2]);

        System.out.printf("Resultado: %d", dividir(n1, n2, n3));
    }
}