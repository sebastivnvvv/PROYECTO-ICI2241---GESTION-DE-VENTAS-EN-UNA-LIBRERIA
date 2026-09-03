import java.time.LocalDate;
import java.util.List;

public class Venta 
{
	private String id;
	private LocalDate fecha;
	private List<ItemVenta> productos;
	
	// Constructor
	public Venta(LocalDate fecha, List<ItemVenta> productos) 
	{
		this.fecha = fecha;
		this.productos = productos;
	}
	
	
	// Metodos
	
	
	
	
	//Getters y setters
	
	// Id
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
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
