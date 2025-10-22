public class MatcherAnimales {
    public static void main(String[] args) {

        String text = "\nEn un mundo dominado por los gatos el perro es el mejor aliado ya que los gatos son felinos mientras que los perros caninos." +
                "\n\tLos gatos y los perros son dos de las mascotas más populares del mundo. Ambos tienen sus ventajas y desventajas, y la elección entre ellos depende de las preferencias y el estilo de vida de cada persona. Algunos aspectos a considerar son:" +
                "\n\n\t- **Personalidad**: Los gatos suelen ser más independientes y tranquilos que los perros, que son más sociables y activos. Los gatos pueden pasar más tiempo solos y se adaptan mejor a los espacios pequeños, mientras que los perros necesitan más atención y ejercicio, y se llevan mejor con otros animales y personas. Los gatos también son más curiosos y juguetones, mientras que los perros son más leales y obedientes." +
                "\n\t- **Cuidados**: Los gatos y los perros requieren diferentes tipos de cuidados. Los gatos se limpian solos y solo necesitan un arenero, un cepillado ocasional y una visita al veterinario cada año. Los perros, en cambio, necesitan baños regulares, cortes de pelo, vacunas, desparasitación y revisiones frecuentes. Además, los perros tienen que salir a pasear varias veces al día, lo que implica un mayor compromiso y responsabilidad por parte de sus dueños." +
                "\n\t- **Costes**: Tener una mascota implica un gasto económico que hay que tener en cuenta. Los gatos y los perros tienen costes similares en cuanto a alimentación, accesorios y juguetes, pero los perros suelen ser más caros en cuanto a servicios veterinarios, higiene y adiestramiento. Según un estudio, el coste medio de tener un gato durante su vida es de unos 15.000 euros, mientras que el de tener un perro es de unos 25.000 euros." +
                "\n\nEn conclusión, los gatos y los perros son animales muy diferentes que ofrecen distintas experiencias a sus dueños. No hay una respuesta única a la pregunta de cuál es mejor, sino que depende de las características y necesidades de cada persona y de cada animal. Lo importante es elegir con responsabilidad y ofrecerles el mejor cuidado y cariño posible.";

        // gato -> placeholder
        text = text.replaceAll("Gatos", "@@GATOS_CAP@@");
        text = text.replaceAll("gatos", "@@GATOS@@");
        text = text.replaceAll("Gato", "@@GATO_CAP@@");
        text = text.replaceAll("gato", "@@GATO@@");

        // perro -> gato
        text = text.replaceAll("Perros", "Gatos");
        text = text.replaceAll("perros", "gatos");
        text = text.replaceAll("Perro", "Gato");
        text = text.replaceAll("perro", "gato");

        // gato (placeholder) -> perro
        text = text.replaceAll("@@GATOS_CAP@@", "Perros");
        text = text.replaceAll("@@GATOS@@", "perros");
        text = text.replaceAll("@@GATO_CAP@@", "Perro");
        text = text.replaceAll("@@GATO@@", "perro");

        System.out.println(text);
    }
}
