import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Venta
{
	private String id;
	private LocalDate fecha;
	private List<ItemVenta> productos;
	
	// Constructor
	public Venta(String id) 
	{
		this.id = id;
		this.fecha = LocalDate.now();
		this.productos = new ArrayList<>();
	}
	
	
	// Metodos
	public double calcularTotal() 
	{
		double total = 0;
		for(int i = 0; i < productos.size(); i++) 
		{
			double actual = productos.get(i).subTotal(); 
			total += actual;
		}
		
		return total;
	}
	
	// Sobrecarga de calcularTotal pero esta vez con el calculo total pero con descuento
	public double calcularTotal(Promocion promo) 
	{
		double totalBruto = calcularTotal();
		return totalBruto - promo.calcularDescuento(totalBruto);

	}
	
	public void agregarItem(ItemVenta item)
	{
	    productos.add(item);
	}
	
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
	    return new ArrayList<>(productos);
	}
	
	public void setProductos(List<ItemVenta> productos) {
		this.productos = productos;
	}
}
