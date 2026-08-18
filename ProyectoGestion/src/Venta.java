import java.time.LocalDate;
import java.util.List;

public class Venta 
{
	private LocalDate fecha;
	private List<ItemVenta> productos;
	
	// Constructor
	public Venta(LocalDate fecha, List<ItemVenta> productos) 
	{
		fecha = this.fecha;
		productos = this.productos;
	}
	
	
	// Metodos
	
	
	
	
	//Getters y setters
	
	// Fecha
	public LocalDate getFecha() {
		return fecha;
	}
	
	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}
	
	// productos
	public List<ItemVenta> getProductos() {
		return productos;
	}
	
	public void setProductos(List<ItemVenta> productos) {
		this.productos = productos;
	}
}
