import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MatcherLookAround {
    public static void main(String[] args) {

        String text = "\n250 Euros es el dinero que me dio cuando yo le presté 200$ ¿o fueron 220 Libras?, ya no me acuerdo. Debo de estar perdiendo la memoria ya sé eran 120 Libras. Cuando dijiste 100 Libraste una batalla. Entonces sería que quería darte 100 Librasilios, qué es 1 Librasilio, 10 Librasilios son 10 veces 1 Librasillo. Entonces 1 Libra ¿no? Sí, sí, 1 Libra.";

        String regex = "\\d+(?= (?:Libra|Libras)\\b)";
        Pattern p1 = Pattern.compile(regex);
        Matcher m1 = p1.matcher(text);

        while (m1.find()) {
            System.out.println(m1.group());
        }
    }
}