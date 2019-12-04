package resources;

import java.awt.*;

/**
 * Clase que se encarga de cargar los recursos de imagen, para ello las pongo en el mismo direcorio que la clase
 */

public class Imagenes {
    public Image cargar(String sRuta){
        //return Toolkit.getDefaultToolkit().createImage((getClass().getResource(sRuta)));
        return Toolkit.getDefaultToolkit().createImage(sRuta);
    }
}
