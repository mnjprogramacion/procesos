public class RecursividadMultiplicacion {
 
    public static int multiplicar(int n1, int n2) {

        if (n2 == 0) {
            return n2;
        }

        if (n2 == 1) {
            return n1;
        } else {
            n2--;
            return n1 + multiplicar(n1, n2);
        }
    }
 
    public static void main(String[] args) {

        System.out.printf("\nResultado: %d", multiplicar(2, 2));

    }
}