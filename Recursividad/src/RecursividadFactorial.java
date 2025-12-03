public class RecursividadFactorial {

    public static int factorial(int n) {
        if (n <= 1) {
            return 1;
        }
        return n * factorial(n - 1);
    }

    public static int multiplicarFactoriales(int n1, int n2) {
        return factorial(n1) * factorial(n2);
    }
 
    public static void main(String[] args) {

        int n1 = Integer.parseInt(args[0]);
        int n2 = Integer.parseInt(args[1]);

        System.out.printf("Resultado: %d", multiplicarFactoriales(n1, n2));
    }
}