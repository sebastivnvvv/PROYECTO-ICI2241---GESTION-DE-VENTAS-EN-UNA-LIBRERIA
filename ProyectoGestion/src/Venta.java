import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Venta
{
	private String id;
	private LocalDate fecha;
	private List<ItemVenta> productos;
	private double descuentoAplicado;
	
	// Constructor
	public Venta(String id) 
	{
		this.id = id;
		this.fecha = LocalDate.now();
		this.productos = new ArrayList<>();
		this.descuentoAplicado = 0;
	}
	
	
	// Metodos
	
	public double calcularTotalBruto() 
	{
		double total = 0;
		for(int i = 0; i < productos.size(); i++) 
		{
			total += productos.get(i).subTotal(); 
		}
		
		return total;
	}
	
	public double calcularTotal() 
	{
		return calcularTotalBruto() - this.descuentoAplicado;
	}
	
	// Sobrecarga de calcularTotal pero esta vez con el calculo total pero con descuento
	public double calcularTotal(Promocion promo) 
	{
		this.descuentoAplicado = promo.calcularDescuento(this);
		return calcularTotal();

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
	// Descuentos
	public double getDescuentoAplicado() {
        return descuentoAplicado;
    }

    public void setDescuentoAplicado(double descuentoAplicado) {
        this.descuentoAplicado = descuentoAplicado;
    }
}
