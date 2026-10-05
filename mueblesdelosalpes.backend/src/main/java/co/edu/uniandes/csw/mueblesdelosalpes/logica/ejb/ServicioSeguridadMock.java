package co.edu.uniandes.csw.mueblesdelosalpes.logica.ejb;

import co.edu.uniandes.csw.mueblesdelosalpes.dto.Usuario;
import co.edu.uniandes.csw.mueblesdelosalpes.excepciones.AutenticacionException;
import co.edu.uniandes.csw.mueblesdelosalpes.logica.interfaces.IServicioPersistenciaMockLocal;
import co.edu.uniandes.csw.mueblesdelosalpes.logica.interfaces.IServicioSeguridadMockLocal;
import co.edu.uniandes.csw.mueblesdelosalpes.persistencia.mock.ServicioPersistenciaMock;
import javax.ejb.Stateless;

/**
 * Autenticación de usuarios en el sistema.
 */
@Stateless
public class ServicioSeguridadMock implements IServicioSeguridadMockLocal
{
    private IServicioPersistenciaMockLocal persistencia;

    public ServicioSeguridadMock()
    {
        persistencia = new ServicioPersistenciaMock();
    }

    @Override
    public Usuario ingresar(String nombre, String contraseña) throws AutenticacionException
    {
        Usuario u = (Usuario) persistencia.findById(Usuario.class, nombre);

        if (u != null)
        {
            if (u.getLogin().equals(nombre) && u.getContraseña().equals(contraseña))
            {
                return u;
            }
            else
            {
                throw new AutenticacionException("La contraseña no es válida. Por favor, asegúrate de que el bloqueo de mayúsculas no está activado e inténtalo de nuevo.");
            }
        }
        else
        {
            throw new AutenticacionException("El nombre de usuario proporcionado no pertenece a ninguna cuenta.");
        }
    }
}