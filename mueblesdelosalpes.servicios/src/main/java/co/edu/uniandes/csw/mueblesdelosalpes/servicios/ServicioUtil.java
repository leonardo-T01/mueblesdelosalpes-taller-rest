package co.edu.uniandes.csw.mueblesdelosalpes.servicios;

import co.edu.uniandes.csw.mueblesdelosalpes.dto.RegistroVenta;
import co.edu.uniandes.csw.mueblesdelosalpes.dto.Usuario;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Utilidades compartidas por los servicios REST.
 */
public final class ServicioUtil
{
    private ServicioUtil()
    {
    }

    /**
     * Crea una excepción REST con el código HTTP y el mensaje dados.
     */
    public static WebApplicationException error(Response.Status status, String mensaje)
    {
        return new WebApplicationException(
                Response.status(status)
                        .entity(mensaje)
                        .type(MediaType.TEXT_PLAIN)
                        .build());
    }

    /**
     * Copia del usuario sin contraseña y sin referencias circulares.
     */
    public static Usuario sinContrasena(Usuario u)
    {
        if (u == null)
        {
            return null;
        }
        Usuario copia = new Usuario(u.getLogin(), null, u.getTipoUsuario());
        copia.setNombreCompleto(u.getNombreCompleto());
        copia.setDocumento(u.getDocumento());
        copia.setTipoDocumento(u.getTipoDocumento());
        copia.setTelefonoLocal(u.getTelefonoLocal());
        copia.setTelefonoCelular(u.getTelefonoCelular());
        copia.setCiudad(u.getCiudad());
        copia.setDireccion(u.getDireccion());
        copia.setProfesion(u.getProfesion());
        copia.setCorreo(u.getCorreo());
        copia.setSeleccion(u.isSeleccion());

        ArrayList<RegistroVenta> compras = new ArrayList<RegistroVenta>();
        if (u.getCompras() != null)
        {
            for (RegistroVenta rv : u.getCompras())
            {
                compras.add(new RegistroVenta(rv.getFechaVenta(), rv.getProducto(),
                        rv.getCantidad(), rv.getCiudad(), null));
            }
        }
        copia.setCompras(compras);
        return copia;
    }

    public static List<Usuario> sinContrasena(List<Usuario> usuarios)
    {
        List<Usuario> resultado = new ArrayList<Usuario>();
        if (usuarios != null)
        {
            for (Usuario u : usuarios)
            {
                resultado.add(sinContrasena(u));
            }
        }
        return resultado;
    }
}