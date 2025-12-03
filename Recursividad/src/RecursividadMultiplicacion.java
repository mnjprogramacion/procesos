public class RecursividadMultiplicacion {
 
    public static int multiplicar(int n1, int n2) {

        if ((n1 == 0) || (n2 == 0)) {
            return 0;
        }

        if (n2 == 1) {
            return n1;
        } else {
            n2--;
            return n1 + multiplicar(n1, n2);
        }
    }
 
    public static void main(String[] args) {

        int n1 = Integer.parseInt(args[0]);
        int n2 = Integer.parseInt(args[1]);

        System.out.printf("Resultado: %d", multiplicar(n1, n2));
    }
}