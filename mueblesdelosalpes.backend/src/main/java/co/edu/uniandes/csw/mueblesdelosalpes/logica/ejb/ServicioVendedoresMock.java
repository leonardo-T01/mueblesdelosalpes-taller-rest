package co.edu.uniandes.csw.mueblesdelosalpes.logica.ejb;

import co.edu.uniandes.csw.mueblesdelosalpes.dto.Vendedor;
import co.edu.uniandes.csw.mueblesdelosalpes.excepciones.OperacionInvalidaException;
import co.edu.uniandes.csw.mueblesdelosalpes.logica.interfaces.IServicioPersistenciaMockLocal;
import co.edu.uniandes.csw.mueblesdelosalpes.logica.interfaces.IServicioVendedoresMockLocal;
import co.edu.uniandes.csw.mueblesdelosalpes.logica.interfaces.IServicioVendedoresMockRemote;
import co.edu.uniandes.csw.mueblesdelosalpes.persistencia.mock.ServicioPersistenciaMock;
import java.util.List;
import javax.ejb.Stateless;

/**
 * Implementación de los servicios de administración de vendedores.
 */
@Stateless
public class ServicioVendedoresMock implements IServicioVendedoresMockRemote, IServicioVendedoresMockLocal
{
    private IServicioPersistenciaMockLocal persistencia;

    public ServicioVendedoresMock()
    {
        persistencia = new ServicioPersistenciaMock();
    }

    @Override
    public void agregarVendedor(Vendedor vendedor) throws OperacionInvalidaException
    {
        try
        {
            persistencia.create(vendedor);
        }
        catch (OperacionInvalidaException ex)
        {
            throw new OperacionInvalidaException(ex.getMessage());
        }
    }

    @Override
    public void actualizarVendedor(long id, Vendedor vendedor) throws OperacionInvalidaException
    {
        Vendedor existente = (Vendedor) persistencia.findById(Vendedor.class, id);
        if (existente == null)
        {
            throw new OperacionInvalidaException("No existe el vendedor con id " + id);
        }
        
        vendedor.setIdentificacion(id);
        
        if (vendedor.getExperiencia() == null || vendedor.getExperiencia().isEmpty())
        {
            vendedor.setExperiencia(existente.getExperiencia());
        }
        persistencia.update(vendedor);
    }

    @Override
    public void eliminarVendedor(long id) throws OperacionInvalidaException
    {
        Vendedor v = (Vendedor) persistencia.findById(Vendedor.class, id);
        try
        {
            persistencia.delete(v);
        }
        catch (OperacionInvalidaException ex)
        {
            throw new OperacionInvalidaException(ex.getMessage());
        }
    }

    @Override
    public List<Vendedor> getVendedores()
    {
        return persistencia.findAll(Vendedor.class);
    }
}