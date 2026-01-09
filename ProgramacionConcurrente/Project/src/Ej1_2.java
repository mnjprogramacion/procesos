import java.io.File;

public class Ej1_2 {
    public static void main(String[] args) {
        String directorioWSL = "/mnt/c/Users/matti/Documents/notepad/";
        String directorioWindows = "C:\\Users\\matti\\Documents\\notepad\\";
        String notepadPath = "/mnt/c/Windows/System32/notepad.exe";
        
        String[] archivos = {"notas1.txt", "notas2.txt", "notas3.txt", "notas4.txt", "notas5.txt"};
        System.out.println("Abriendo 5 instancias de Notepad...\n");

        for (String archivo : archivos) {
            try {
                ProcessBuilder pBuilder = new ProcessBuilder(notepadPath, directorioWindows + archivo);
                pBuilder.directory(new File(directorioWSL));
                pBuilder.start();
                System.out.println("Abierto: " + directorioWindows + archivo);
                Thread.sleep(500);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        System.out.println("\nSe han abierto 5 instancias de Notepad.");
    }
}