package co.edu.uniandes.csw.mueblesdelosalpes.servicios;

import co.edu.uniandes.csw.mueblesdelosalpes.dto.Mueble;
import co.edu.uniandes.csw.mueblesdelosalpes.logica.interfaces.IServicioCatalogoMockLocal;
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

@Path("/Catalogo")
@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CatalogoService
{
    @EJB
    private IServicioCatalogoMockLocal catalogoEjb;

    /** GET /Catalogo/muebles */
    @GET
    @Path("muebles/")
    public List<Mueble> getTodosLosMuebles()
    {
        return catalogoEjb.darMuebles();
    }

    /** GET /Catalogo/muebles/{id} */
    @GET
    @Path("muebles/{id}")
    public Mueble getMueblePorId(@PathParam("id") long id)
    {
        return buscar(id);
    }

    /** POST /Catalogo/muebles (la referencia la asigna el sistema) */
    @POST
    @Path("muebles/")
    public Mueble agregarMueble(Mueble mueble)
    {
        if (mueble == null || mueble.getNombre() == null || mueble.getNombre().trim().length() == 0)
        {
            throw ServicioUtil.error(Response.Status.BAD_REQUEST, "El mueble debe tener nombre");
        }
        catalogoEjb.agregarMueble(mueble);
        return mueble;
    }

    /** PUT /Catalogo/muebles/{id}/removerEjemplar (descuenta una unidad) */
    @PUT
    @Path("muebles/{id}/removerEjemplar")
    public Mueble removerEjemplar(@PathParam("id") long id)
    {
        Mueble m = buscar(id);
        if (m.getCantidad() <= 0)
        {
            throw ServicioUtil.error(Response.Status.BAD_REQUEST, "No hay ejemplares disponibles de este mueble");
        }
        catalogoEjb.removerEjemplarMueble(id);
        return buscar(id);
    }

    /** DELETE /Catalogo/muebles/{id} */
    @DELETE
    @Path("muebles/{id}")
    public void eliminarMueble(@PathParam("id") long id)
    {
        buscar(id);
        catalogoEjb.eliminarMueble(id);
    }

    private Mueble buscar(long id)
    {
        for (Mueble m : catalogoEjb.darMuebles())
        {
            if (m.getReferencia() == id)
            {
                return m;
            }
        }
        throw ServicioUtil.error(Response.Status.NOT_FOUND, "No existe un mueble con referencia " + id);
    }
}