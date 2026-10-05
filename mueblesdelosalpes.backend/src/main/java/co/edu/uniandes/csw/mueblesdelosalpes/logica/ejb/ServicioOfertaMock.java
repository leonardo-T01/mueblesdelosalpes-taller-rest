package co.edu.uniandes.csw.mueblesdelosalpes.logica.ejb;

import co.edu.uniandes.csw.mueblesdelosalpes.dto.Oferta;
import co.edu.uniandes.csw.mueblesdelosalpes.excepciones.OperacionInvalidaException;
import co.edu.uniandes.csw.mueblesdelosalpes.logica.interfaces.IServicioOfertaMockLocal;
import co.edu.uniandes.csw.mueblesdelosalpes.logica.interfaces.IServicioPersistenciaMockLocal;
import co.edu.uniandes.csw.mueblesdelosalpes.persistencia.mock.ServicioPersistenciaMock;
import java.util.List;
import javax.ejb.Stateless;

/**
 * Implementación de los servicios de ofertas sobre muebles.
 */
@Stateless
public class ServicioOfertaMock implements IServicioOfertaMockLocal
{
    private IServicioPersistenciaMockLocal persistencia;

    public ServicioOfertaMock()
    {
        persistencia = new ServicioPersistenciaMock();
    }

    @Override
    public void agregarOferta(Oferta oferta) throws OperacionInvalidaException
    {
        if (oferta == null)
        {
            throw new OperacionInvalidaException("La oferta no puede ser nula");
        }
        if (oferta.getOferta() <= 0)
        {
            throw new OperacionInvalidaException("El valor de la oferta debe ser mayor que cero");
        }
        persistencia.create(oferta);
    }

    @Override
    public List<Oferta> getOfertas()
    {
        return persistencia.findAll(Oferta.class);
    }
}