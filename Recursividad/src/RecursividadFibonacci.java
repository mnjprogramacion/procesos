public class RecursividadFibonacci {
 
    public static int fibonacci(int n) {
        if (n <= 0) {
            return 0;
        }
        if (n == 1) {
            return 1;
        }
        return fibonacci(n - 1) + fibonacci(n - 2);
    }

    public static void imprimirFibonacci(int max, int index) {
        int fib = fibonacci(index);
        
        if (fib >= max) {
            return;
        }
        
        System.out.print(fib + " ");
        imprimirFibonacci(max, index + 1);
    }
 
    public static void main(String[] args) {

        int n = Integer.parseInt(args[0]);

        System.out.printf("Números Fibonacci menores que %d: ", n);
        imprimirFibonacci(n, 0);
    }
}