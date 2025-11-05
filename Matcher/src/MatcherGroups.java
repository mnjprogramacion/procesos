import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MatcherGroups {
    public static void main(String[] args) {

        String text = "\nEl hombre dijo 'El perro no sabe leer' y el perro le contestó 'Si me hubieras preguntado sabrías la verdad'. 'Cuál es la verdad' preguntó el hombre. 'La verdad es ...' y el perro sonó como 'aaaaagh'.";

        String regex = "'([^']*)'";
        Pattern p1 = Pattern.compile(regex);
        Matcher m1 = p1.matcher(text);
       
        while (m1.find()) {
            System.out.println(m1.group(1));
        }
    }
}