package co.edu.uniandes.csw.mueblesdelosalpes.logica.interfaces;

import co.edu.uniandes.csw.mueblesdelosalpes.dto.Oferta;
import co.edu.uniandes.csw.mueblesdelosalpes.excepciones.OperacionInvalidaException;
import java.util.List;
import javax.ejb.Local;

/**
 * Contrato funcional de los servicios de ofertas sobre muebles.
 */
@Local
public interface IServicioOfertaMockLocal
{
    /**
     * Agrega una oferta al sistema.
     */
    public void agregarOferta(Oferta oferta) throws OperacionInvalidaException;

    /**
     * Devuelve todas las ofertas registradas.
     */
    public List<Oferta> getOfertas();
}