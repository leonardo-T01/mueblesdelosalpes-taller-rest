package co.edu.uniandes.csw.mueblesdelosalpes.servicios;

import co.edu.uniandes.csw.mueblesdelosalpes.dto.Vendedor;
import co.edu.uniandes.csw.mueblesdelosalpes.excepciones.OperacionInvalidaException;
import co.edu.uniandes.csw.mueblesdelosalpes.logica.interfaces.IServicioVendedoresMockLocal;
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

@Path("/Vendedores")
@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class VendedoresService
{
    @EJB
    private IServicioVendedoresMockLocal vendedoresEjb;

    /** GET /Vendedores/vendedores */
    @GET
    @Path("vendedores/")
    public List<Vendedor> getVendedores()
    {
        return vendedoresEjb.getVendedores();
    }

    /** GET /Vendedores/vendedores/{id} */
    @GET
    @Path("vendedores/{id}")
    public Vendedor getVendedor(@PathParam("id") long id)
    {
        return buscar(id);
    }

    /** POST /Vendedores/vendedores (el id lo asigna el sistema) */
    @POST
    @Path("vendedores/")
    public Vendedor agregarVendedor(Vendedor vendedor)
    {
        if (vendedor == null || vendedor.getNombres() == null || vendedor.getNombres().trim().length() == 0)
        {
            throw ServicioUtil.error(Response.Status.BAD_REQUEST, "El vendedor debe tener nombres");
        }
        try
        {
            vendedoresEjb.agregarVendedor(vendedor);
        }
        catch (OperacionInvalidaException ex)
        {
            throw ServicioUtil.error(Response.Status.BAD_REQUEST, ex.getMessage());
        }
        return vendedor;
    }

    /** PUT /Vendedores/vendedores/{id} */
    @PUT
    @Path("vendedores/{id}")
    public Vendedor actualizarVendedor(@PathParam("id") long id, Vendedor vendedor)
    {
        if (vendedor == null)
        {
            throw ServicioUtil.error(Response.Status.BAD_REQUEST, "El cuerpo de la petición no puede estar vacío");
        }
        buscar(id); // lanza 404 si no existe
        try
        {
            vendedoresEjb.actualizarVendedor(id, vendedor);
        }
        catch (OperacionInvalidaException ex)
        {
            throw ServicioUtil.error(Response.Status.BAD_REQUEST, ex.getMessage());
        }
        return buscar(id);
    }

    /** DELETE /Vendedores/vendedores/{id} */
    @DELETE
    @Path("vendedores/{id}")
    public void eliminarVendedor(@PathParam("id") long id)
    {
        buscar(id);
        try
        {
            vendedoresEjb.eliminarVendedor(id);
        }
        catch (OperacionInvalidaException ex)
        {
            throw ServicioUtil.error(Response.Status.BAD_REQUEST, ex.getMessage());
        }
    }

    private Vendedor buscar(long id)
    {
        for (Vendedor v : vendedoresEjb.getVendedores())
        {
            if (v.getIdentificacion() == id)
            {
                return v;
            }
        }
        throw ServicioUtil.error(Response.Status.NOT_FOUND, "No existe un vendedor con id " + id);
    }
}