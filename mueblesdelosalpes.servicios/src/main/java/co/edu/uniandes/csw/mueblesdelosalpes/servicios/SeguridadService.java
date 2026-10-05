package co.edu.uniandes.csw.mueblesdelosalpes.servicios;

import co.edu.uniandes.csw.mueblesdelosalpes.dto.Usuario;
import co.edu.uniandes.csw.mueblesdelosalpes.excepciones.AutenticacionException;
import co.edu.uniandes.csw.mueblesdelosalpes.logica.interfaces.IServicioSeguridadMockLocal;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/Seguridad")
@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SeguridadService
{
    @EJB
    private IServicioSeguridadMockLocal seguridadEjb;

    /** POST /Seguridad/ingresar  Body: {"login":"admin","password":"adminadmin"} */
    @POST
    @Path("ingresar/")
    public Usuario ingresar(Map<String, String> credenciales)
    {
        if (credenciales == null || credenciales.get("login") == null || credenciales.get("password") == null)
        {
            throw ServicioUtil.error(Response.Status.BAD_REQUEST,
                    "Se requieren los campos 'login' y 'password'");
        }
        try
        {
            Usuario u = seguridadEjb.ingresar(credenciales.get("login"), credenciales.get("password"));
            return ServicioUtil.sinContrasena(u);
        }
        catch (AutenticacionException ex)
        {
            throw ServicioUtil.error(Response.Status.UNAUTHORIZED, ex.getMessage());
        }
    }
}