package co.edu.uniandes.csw.mueblesdelosalpes.logica.ejb;

import co.edu.uniandes.csw.mueblesdelosalpes.dto.Usuario;
import co.edu.uniandes.csw.mueblesdelosalpes.excepciones.OperacionInvalidaException;
import co.edu.uniandes.csw.mueblesdelosalpes.logica.interfaces.IServicioPersistenciaMockLocal;
import co.edu.uniandes.csw.mueblesdelosalpes.logica.interfaces.IServicioRegistroMockLocal;
import co.edu.uniandes.csw.mueblesdelosalpes.logica.interfaces.IServicioRegistroMockRemote;
import co.edu.uniandes.csw.mueblesdelosalpes.persistencia.mock.ServicioPersistenciaMock;
import java.util.List;
import javax.ejb.Stateless;

/**
 * Implementación de los servicios de registro de clientes.
 */
@Stateless
public class ServicioRegistroMock implements IServicioRegistroMockRemote, IServicioRegistroMockLocal
{
    private IServicioPersistenciaMockLocal persistencia;

    public ServicioRegistroMock()
    {
        persistencia = new ServicioPersistenciaMock();
    }

    @Override
    public void registrar(Usuario u) throws OperacionInvalidaException
    {
        if (u.getDocumento() == 0)
        {
            throw new OperacionInvalidaException("El número de documento no es válido");
        }
        persistencia.create(u);
    }

    @Override
    public void actualizarCliente(String login, Usuario u) throws OperacionInvalidaException
    {
        Usuario existente = (Usuario) persistencia.findById(Usuario.class, login);
        if (existente == null)
        {
            throw new OperacionInvalidaException("No existe el usuario '" + login + "'");
        }
        
        u.setLogin(login);
        
        if (u.getContraseña() == null || u.getContraseña().length() == 0)
        {
            u.setContraseña(existente.getContraseña());
        }
        
        if (u.getCompras() == null)
        {
            u.setCompras(existente.getCompras());
        }
        
        u.setTipoUsuario(existente.getTipoUsuario());
        persistencia.update(u);
    }

    @Override
    public void eliminarCliente(String login) throws OperacionInvalidaException
    {
        try
        {
            Usuario u = (Usuario) persistencia.findById(Usuario.class, login);
            persistencia.delete(u);
        }
        catch (OperacionInvalidaException e)
        {
            throw new OperacionInvalidaException("Ocurrió un error al momento de eliminar");
        }
    }

    @Override
    public List<Usuario> darClientes()
    {
        return persistencia.findAll(Usuario.class);
    }
}