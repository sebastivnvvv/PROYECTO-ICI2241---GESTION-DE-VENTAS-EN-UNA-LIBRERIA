
public class ItemVenta 
{
	private Libro libro;
	private int cantidad;

	// Constructor
	
	public ItemVenta(Libro libro, int cantidad) 
	{
		this.libro = libro;
		this.cantidad = cantidad;
	}
	
	
	// Metodos
	public double subTotal()
	{
		double precio = libro.getPrecio();
		double total = precio * cantidad;
		return total;
	}
	
	
	// getters y setters
	
	// cantidad
	public int getCantidad() {
		return cantidad;
	}
	
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	
	//Libro
	
	public Libro getLibro() {
		return libro;
	}
	
	public void setLibro(Libro libro) {
		this.libro = libro;
	}
	
	
}
