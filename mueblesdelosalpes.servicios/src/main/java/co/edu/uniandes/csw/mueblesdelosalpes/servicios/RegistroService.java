package co.edu.uniandes.csw.mueblesdelosalpes.servicios;

import co.edu.uniandes.csw.mueblesdelosalpes.dto.RegistroVenta;
import co.edu.uniandes.csw.mueblesdelosalpes.dto.TipoUsuario;
import co.edu.uniandes.csw.mueblesdelosalpes.dto.Usuario;
import co.edu.uniandes.csw.mueblesdelosalpes.excepciones.OperacionInvalidaException;
import co.edu.uniandes.csw.mueblesdelosalpes.logica.interfaces.IServicioRegistroMockLocal;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/Registro")
@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class RegistroService
{
    @EJB
    private IServicioRegistroMockLocal registroEjb;

    /** GET /Registro/clientes */
    @GET
    @Path("clientes/")
    public List<Usuario> getClientes()
    {
        return ServicioUtil.sinContrasena(registroEjb.darClientes());
    }

    /** GET /Registro/clientes/{login} */
    @GET
    @Path("clientes/{login}")
    public Usuario getCliente(@PathParam("login") String login)
    {
        return ServicioUtil.sinContrasena(buscar(login));
    }

    /** POST /Registro/clientes (siempre se crea con perfil Cliente) */
    @POST
    @Path("clientes/")
    public Usuario registrarCliente(Usuario usuario)
    {
        if (usuario == null || usuario.getLogin() == null || usuario.getLogin().trim().length() == 0)
        {
            throw ServicioUtil.error(Response.Status.BAD_REQUEST, "El login es obligatorio");
        }
        if (usuario.getContraseña() == null || usuario.getContraseña().length() == 0)
        {
            throw ServicioUtil.error(Response.Status.BAD_REQUEST, "La contraseña es obligatoria");
        }
        usuario.setTipoUsuario(TipoUsuario.Cliente);
        if (usuario.getCompras() == null)
        {
            usuario.setCompras(new ArrayList<RegistroVenta>());
        }
        try
        {
            registroEjb.registrar(usuario);
        }
        catch (OperacionInvalidaException ex)
        {
            throw ServicioUtil.error(Response.Status.BAD_REQUEST, ex.getMessage());
        }
        return ServicioUtil.sinContrasena(usuario);
    }

    /** PUT /Registro/clientes/{login} */
    @PUT
    @Path("clientes/{login}")
    public Usuario actualizarCliente(@PathParam("login") String login, Usuario usuario)
    {
        if (usuario == null)
        {
            throw ServicioUtil.error(Response.Status.BAD_REQUEST, "El cuerpo de la petición no puede estar vacío");
        }
        buscar(login);
        try
        {
            registroEjb.actualizarCliente(login, usuario);
        }
        catch (OperacionInvalidaException ex)
        {
            throw ServicioUtil.error(Response.Status.BAD_REQUEST, ex.getMessage());
        }
        return ServicioUtil.sinContrasena(buscar(login));
    }

    /** DELETE /Registro/clientes/{login} */
    @DELETE
    @Path("clientes/{login}")
    public void eliminarCliente(@PathParam("login") String login)
    {
        buscar(login);
        try
        {
            registroEjb.eliminarCliente(login);
        }
        catch (OperacionInvalidaException ex)
        {
            throw ServicioUtil.error(Response.Status.BAD_REQUEST, ex.getMessage());
        }
    }

    private Usuario buscar(String login)
    {
        for (Usuario u : registroEjb.darClientes())
        {
            if (u.getLogin().equals(login))
            {
                return u;
            }
        }
        throw ServicioUtil.error(Response.Status.NOT_FOUND, "No existe el usuario '" + login + "'");
    }
}