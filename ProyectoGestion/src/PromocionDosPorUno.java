import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PromocionDosPorUno extends Promocion
{
	@Override
	public double calcularDescuento(Venta venta) 
	{
		List<Double> precios = new ArrayList<>();
		
		// Extrae la lista de productos de la venta
		List<ItemVenta> productos = venta.getProductos();
	        
		for (int i = 0; i < productos.size(); i++) 
		{
			ItemVenta item = productos.get(i);
	            
			// Se agrega el precio tantas veces como la cantidad comprada
			for (int j = 0; j < item.getCantidad(); j++) 
			{
				precios.add((double) item.getLibro().getPrecio());
			}
		}
	        
		Collections.sort(precios, Collections.reverseOrder());
	        
		double descuento = 0;
	        
		// Sumar al descuento el valor de cada segundo libro (los más baratos del par)
		for (int i = 1; i < precios.size(); i += 2) 
		{
			descuento += precios.get(i);
		}
	        
		return descuento;
	}
}
