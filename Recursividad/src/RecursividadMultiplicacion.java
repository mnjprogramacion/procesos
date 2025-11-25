import java.util.Scanner;

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

        Scanner entrada = new Scanner(System.in);
        int n1, n2;

        System.out.printf("\nIntroduce el primer número: ");
        n1 = entrada.nextInt();
        System.out.printf("Introduce el segundo número: ");
        n2 = entrada.nextInt();

        System.out.printf("Resultado: %d", multiplicar(n1, n2));

    }
}