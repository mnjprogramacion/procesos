import java.util.Scanner;

public class RecursividadFibonacci {
 
    public static int fibonacci(int n) {

        if ((n == 0)) {
            return 0;
        }

        if (n == 1) {
            return n;
        } else {
            n--;
            return n1 + fibonacci(n1, n2);
        }
    }
 
    public static void main(String[] args) {

        Scanner entrada = new Scanner(System.in);
        int n;

        System.out.printf("\nIntroduce un número: ");
        n = entrada.nextInt();

        System.out.printf("Resultado: %d", fibonacci(n));

        entrada.close();
    }
}