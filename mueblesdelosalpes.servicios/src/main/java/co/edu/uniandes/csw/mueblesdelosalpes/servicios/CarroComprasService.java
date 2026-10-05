package co.edu.uniandes.csw.mueblesdelosalpes.servicios;

import co.edu.uniandes.csw.mueblesdelosalpes.dto.Mueble;
import co.edu.uniandes.csw.mueblesdelosalpes.dto.Usuario;
import co.edu.uniandes.csw.mueblesdelosalpes.logica.interfaces.IServicioCarritoMockLocal;
import co.edu.uniandes.csw.mueblesdelosalpes.logica.interfaces.IServicioCatalogoMockLocal;
import co.edu.uniandes.csw.mueblesdelosalpes.logica.interfaces.IServicioRegistroMockLocal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

@Path("/CarroCompras")
@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CarroComprasService
{
    @EJB
    private IServicioCarritoMockLocal carroEjb;

    @EJB
    private IServicioCatalogoMockLocal catalogoEjb;

    @EJB
    private IServicioRegistroMockLocal registroEjb;

    /** GET /CarroCompras/muebles */
    @GET
    @Path("muebles/")
    public List<Mueble> getTodosLosMuebles()
    {
        return carroEjb.getInventario();
    }

    /** GET /CarroCompras/total */
    @GET
    @Path("total/")
    public Map<String, Object> getTotales()
    {
        carroEjb.recalcularInventarioTotal();
        Map<String, Object> totales = new HashMap<String, Object>();
        totales.put("precioTotal", carroEjb.getPrecioTotalInventario());
        totales.put("totalUnidades", carroEjb.getTotalUnidades());
        return totales;
    }

    /** POST /CarroCompras/agregar  Body: lista JSON de muebles */
    @POST
    @Path("agregar/")
    public List<Mueble> agregarMuebles(List<Mueble> mb)
    {
        if (mb == null || mb.isEmpty())
        {
            throw ServicioUtil.error(Response.Status.BAD_REQUEST, "Debe enviar al menos un mueble");
        }
        for (Mueble mueble : mb)
        {
            carroEjb.agregarItem(mueble);
        }
        return carroEjb.getInventario();
    }

    /** PUT /CarroCompras/recalcular */
    @PUT
    @Path("recalcular/")
    public void recalcular()
    {
        carroEjb.recalcularInventarioTotal();
    }

    /** POST /CarroCompras/comprar/{login} */
    @POST
    @Path("comprar/{login}")
    public List<Mueble> comprar(@PathParam("login") String login)
    {
        List<Mueble> inventario = carroEjb.getInventario();
        if (inventario == null || inventario.isEmpty())
        {
            throw ServicioUtil.error(Response.Status.BAD_REQUEST, "El carrito está vacío");
        }

        Usuario comprador = null;
        for (Usuario u : registroEjb.darClientes())
        {
            if (u.getLogin().equals(login))
            {
                comprador = u;
                break;
            }
        }
        if (comprador == null)
        {
            throw ServicioUtil.error(Response.Status.NOT_FOUND, "No existe el usuario '" + login + "'");
        }

        List<Mueble> catalogo = catalogoEjb.darMuebles();
        for (Mueble item : inventario)
        {
            boolean existe = false;
            for (Mueble m : catalogo)
            {
                if (m.getReferencia() == item.getReferencia())
                {
                    existe = true;
                    break;
                }
            }
            if (!existe)
            {
                throw ServicioUtil.error(Response.Status.BAD_REQUEST,
                        "El mueble con referencia " + item.getReferencia() + " no existe en el catálogo");
            }
        }

        List<Mueble> comprados = new ArrayList<Mueble>(inventario);
        carroEjb.comprar(comprador);
        return comprados;
    }

    /**
     * POST /CarroCompras/borrar  Body: lista JSON de muebles a quitar.
     * Se usa POST en lugar de DELETE porque la mayoría de clientes REST
     * (navegadores, fetch, algunos Postman) ignoran el body en DELETE.
     */
    @POST
    @Path("borrar/")
    public List<Mueble> eliminarMuebles(List<Mueble> mb)
    {
        if (mb != null)
        {
            for (Mueble mueble : mb)
            {
                carroEjb.removerItem(mueble, true);
            }
        }
        return carroEjb.getInventario();
    }

    /** DELETE /CarroCompras/limpiar */
    @DELETE
    @Path("limpiar/")
    public void limpiar()
    {
        carroEjb.limpiarLista();
    }
}