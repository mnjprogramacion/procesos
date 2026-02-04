public class RecursividadDivide3 {
 
    public static int dividir(int n1, int n2, int n3) {
        if (n1 == 0 || n2 == 0 || n3 == 0) {
            return 0;
        }

        return dividirAux(n1, n2, n3, 0);
    }

    private static int dividirAux(int n1, int n2, int n3, int cuenta) {
        if (n1 < n2) {
            return dividirCuenta(cuenta, n3);
        }

        return dividirAux(n1 - n2, n2, n3, cuenta + 1);
    }

    private static int dividirCuenta(int cuenta, int n3) {
        if (cuenta < n3) {
            return 0;
        }
        return 1 + dividirCuenta(cuenta - n3, n3);
    }
 
    public static void main(String[] args) {

        int n1 = Integer.parseInt(args[0]);
        int n2 = Integer.parseInt(args[1]);
        int n3 = Integer.parseInt(args[2]);

        System.out.printf("Resultado: %d", dividir(n1, n2, n3));
    }
}