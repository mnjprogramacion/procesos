public class IterativoDivide3 {
 
    public static int dividir(int n1, int n2) {

        if ((n1 == 0) || (n2 == 0)) {
            return 0;
        }

        int resultado = 0;

        while (n1 >= n2) {
            n1 = n1 - n2;
            resultado++;
        }

        return resultado;
    }
 
    public static void main(String[] args) {

        int n1 = Integer.parseInt(args[0]);
        int n2 = Integer.parseInt(args[1]);
        int n3 = Integer.parseInt(args[2]);

        System.out.printf("Resultado: %d", dividir(dividir(n1, n2), n3));
    }
}
