import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MatcherMadera {
    public static void main(String[] args) {

        String text = "\nEl lobo soplo y soplo pero la casa de madera no se calló." +
                "\nEntonces pensó, ¿y si en vez de usar mis pulmones uso un palo de madera?." +
                "\nFue entonces cuando buscó por todo el bosque pero sólo encontraba maderas gordas que no podía transportar." +
                "\nA lo mejor haciendo una carretilla de madera podría transportar algo más grande y usarlo para aporrear con fuerza la casita de madera…";

        Pattern pattern = Pattern.compile("\\bmadera\\b", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);

        StringBuffer stringBuffer = new StringBuffer();
        int contador = 0;

        while (matcher.find()) {
            contador++;
            if (contador == 2 || contador == 4) {
                matcher.appendReplacement(stringBuffer, "metal");
            } else {
                matcher.appendReplacement(stringBuffer, matcher.group());
            }
        }
        matcher.appendTail(stringBuffer);

        System.out.println(stringBuffer.toString());
    }
}