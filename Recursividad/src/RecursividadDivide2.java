public class RecursividadDivide2 {
 
    public static int dividir(int n1, int n2) {

        if ((n1 == 0) || (n2 == 0)) {
            return 0;
        }

        if (n1 < n2) {
            return 0;
        } else {
            n1 = n1 - n2;
            return 1 + dividir(n1, n2);
        }
    }
 
    public static void main(String[] args) {

        int n1 = Integer.parseInt(args[0]);
        int n2 = Integer.parseInt(args[1]);

        System.out.printf("Resultado: %d", dividir(n1, n2));
    }
}