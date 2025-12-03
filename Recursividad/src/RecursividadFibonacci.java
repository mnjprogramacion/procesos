public class RecursividadFibonacci {
 
    public static int fibonacci(int n) {

        if (n == 0){
            return 0;
        }

        if (n == 1) {
            return n;
        } else {
            n--;
            return n + fibonacci(n);
        }
    }
 
    public static void main(String[] args) {

        int n = Integer.parseInt(args[0]);

        System.out.printf("Resultado: %d", fibonacci(n));
    }
}