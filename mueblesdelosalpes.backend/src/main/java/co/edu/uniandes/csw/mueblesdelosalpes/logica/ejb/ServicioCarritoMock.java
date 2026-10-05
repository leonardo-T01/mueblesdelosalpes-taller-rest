package co.edu.uniandes.csw.mueblesdelosalpes.logica.ejb;

import co.edu.uniandes.csw.mueblesdelosalpes.persistencia.mock.ServicioPersistenciaMock;
import co.edu.uniandes.csw.mueblesdelosalpes.dto.Mueble;
import co.edu.uniandes.csw.mueblesdelosalpes.dto.RegistroVenta;
import co.edu.uniandes.csw.mueblesdelosalpes.dto.Usuario;
import co.edu.uniandes.csw.mueblesdelosalpes.excepciones.OperacionInvalidaException;
import co.edu.uniandes.csw.mueblesdelosalpes.logica.interfaces.IServicioCarritoMockRemote;
import co.edu.uniandes.csw.mueblesdelosalpes.logica.interfaces.IServicioPersistenciaMockLocal;
import co.edu.uniandes.csw.mueblesdelosalpes.logica.interfaces.IServicioCarritoMockLocal;

import java.util.ArrayList;
import java.util.Date;
import javax.ejb.Singleton;

/**
 * Implementación de los servicios del carrito de compras.
 * Se usa @Singleton para que el inventario se conserve entre peticiones HTTP.
 */
@Singleton
public class ServicioCarritoMock implements IServicioCarritoMockRemote, IServicioCarritoMockLocal
{
    private IServicioPersistenciaMockLocal persistencia;
    private ArrayList<Mueble> inventario;
    private double precioTotalInventario = 0.0;
    private int totalUnidades = 0;

    public ServicioCarritoMock()
    {
        inventario = new ArrayList<Mueble>();
        persistencia = new ServicioPersistenciaMock();
    }

    @Override
    public ArrayList<Mueble> getInventario()
    {
        return inventario;
    }

    @Override
    public void setInventario(ArrayList<Mueble> inventario)
    {
        this.inventario = inventario;
    }

    @Override
    public double getPrecioTotalInventario()
    {
        return precioTotalInventario;
    }

    @Override
    public int getTotalUnidades()
    {
        return totalUnidades;
    }

    @Override
    public void comprar(Usuario usuario)
    {
        Mueble mueble;
        for (int i = 0; i < inventario.size(); i++)
        {
            mueble = inventario.get(i);
            Mueble editar = (Mueble) persistencia.findById(Mueble.class, mueble.getReferencia());
            editar.setCantidad(editar.getCantidad() - mueble.getCantidad());
            RegistroVenta compra = new RegistroVenta(new Date(System.currentTimeMillis()), mueble, mueble.getCantidad(), null, usuario);
            usuario.agregarRegistro(compra);
            try
            {
                persistencia.create(compra);
            }
            catch (OperacionInvalidaException ex)
            {
                throw new IllegalStateException(ex.getMessage());
            }
            persistencia.update(usuario);
            persistencia.update(editar);
        }
        limpiarLista();
    }

    @Override
    public void agregarItem(Mueble mueble)
    {
        boolean found = false;
        Mueble item;
        for (int i = 0, max = inventario.size(); i < max; i++)
        {
            item = (Mueble) inventario.get(i);
            if (item.getReferencia() == mueble.getReferencia())
            {
                item.incrementarCantidad();
                found = true;
                break;
            }
        }
        if (!found)
        {
            inventario.add(mueble);
            mueble.incrementarCantidad();
        }
        recalcularInventarioTotal();
    }

    @Override
    public void removerItem(Mueble mueble, boolean removerCero)
    {
        Mueble foundItem = null;
        Mueble item;
        for (int i = 0, max = inventario.size(); i < max; i++)
        {
            item = (Mueble) inventario.get(i);
            if (item.getReferencia() == mueble.getReferencia())
            {
                item.reducirCantidad();
                foundItem = item;
                break;
            }
        }
        if (removerCero && foundItem != null && foundItem.getCantidad() <= 0)
        {
            inventario.remove(foundItem);
        }
        recalcularInventarioTotal();
    }

    @Override
    public void recalcularInventarioTotal()
    {
        precioTotalInventario = 0;
        totalUnidades = 0;
        Mueble item;
        for (int i = 0, max = inventario.size(); i < max; i++)
        {
            item = (Mueble) inventario.get(i);
            precioTotalInventario += item.getPrecio() * item.getCantidad();
            totalUnidades += item.getCantidad();
        }
    }

    @Override
    public void limpiarLista()
    {
        inventario.clear();
        recalcularInventarioTotal();
    }
}