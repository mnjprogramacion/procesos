import java.util.Scanner;

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

        Scanner entrada = new Scanner(System.in);
        int n1, n2;

        System.out.printf("\nIntroduce el primer número: ");
        n1 = entrada.nextInt();
        System.out.printf("Introduce el segundo número: ");
        n2 = entrada.nextInt();

        System.out.printf("Resultado: %d", multiplicarFactoriales(n1, n2));

        entrada.close();
    }
}