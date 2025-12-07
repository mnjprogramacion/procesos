public class IterativoFibonacci {
 
    public static int fibonacci(int n) {
        if (n <= 0) {
            return 0;
        }
        if (n == 1) {
            return 1;
        }
        
        int anterior = 0;
        int actual = 1;
        
        for (int i = 2; i <= n; i++) {
            int siguiente = anterior + actual;
            anterior = actual;
            actual = siguiente;
        }
        
        return actual;
    }

    public static void imprimirFibonacci(int max) {
        int index = 0;
        int fib = fibonacci(index);
        
        while (fib < max) {
            System.out.print(fib + " ");
            index++;
            fib = fibonacci(index);
        }
    }
 
    public static void main(String[] args) {

        int n = Integer.parseInt(args[0]);

        System.out.printf("Números Fibonacci menores que %d: ", n);
        imprimirFibonacci(n);
    }
}
