package co.edu.uniandes.csw.mueblesdelosalpes.servicios;

import co.edu.uniandes.csw.mueblesdelosalpes.dto.Oferta;
import co.edu.uniandes.csw.mueblesdelosalpes.excepciones.OperacionInvalidaException;
import co.edu.uniandes.csw.mueblesdelosalpes.logica.interfaces.IServicioOfertaMockLocal;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/oferta")
@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class OfertaService
{
    @EJB
    private IServicioOfertaMockLocal ofertaEjb;

    /** GET /oferta/ofertas */
    @GET
    @Path("ofertas/")
    public List<Oferta> getOfertas()
    {
        return ofertaEjb.getOfertas();
    }

    /** POST /oferta/agregar  Body: lista JSON de ofertas */
    @POST
    @Path("agregar/")
    public List<Oferta> agregarOfertas(List<Oferta> ofertas)
    {
        try
        {
            for (Oferta oferta : ofertas)
            {
                ofertaEjb.agregarOferta(oferta);
            }
        }
        catch (OperacionInvalidaException ex)
        {
            throw ServicioUtil.error(Response.Status.BAD_REQUEST, ex.getMessage());
        }
        return ofertas;
    }
}