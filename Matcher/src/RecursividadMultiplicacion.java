public class RecursividadMultiplicacion {
 
    public static void multiplicar(int n1, int n2) {
        if ((n1 == 0) || (n2 == 0)) {
            System.out.printf("\nResultado: %d", n1);
        } else {
            System.out.printf("");
        }
    }
 
    public static void main(String[] args) {
        multiplicar(0, 0); // Comienza la recursión
    }
}