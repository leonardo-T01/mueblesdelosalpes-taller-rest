package co.edu.uniandes.csw.mueblesdelosalpes.logica.interfaces;

import co.edu.uniandes.csw.mueblesdelosalpes.dto.Usuario;
import co.edu.uniandes.csw.mueblesdelosalpes.excepciones.AutenticacionException;
import javax.ejb.Local;

/**
 * Contrato funcional de los servicios de autenticación.
 */
@Local
public interface IServicioSeguridadMockLocal
{
    public Usuario ingresar(String nombre, String contraseña) throws AutenticacionException;
}