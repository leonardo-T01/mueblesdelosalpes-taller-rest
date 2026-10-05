package co.edu.uniandes.csw.mueblesdelosalpes.logica.interfaces;

import co.edu.uniandes.csw.mueblesdelosalpes.dto.Vendedor;
import co.edu.uniandes.csw.mueblesdelosalpes.excepciones.OperacionInvalidaException;
import java.util.List;
import javax.ejb.Local;

/**
 * Contrato funcional de los servicios de administración de vendedores.
 */
@Local
public interface IServicioVendedoresMockLocal
{
    public void agregarVendedor(Vendedor vendedor) throws OperacionInvalidaException;

    public void actualizarVendedor(long id, Vendedor vendedor) throws OperacionInvalidaException;

    public void eliminarVendedor(long id) throws OperacionInvalidaException;

    public List<Vendedor> getVendedores();
}