
public class DetalleVenta 
{
	// atributos
	private Libro libro;
	private int cantidad;
	private double precioUnitario;
	private double subtotal;
	
	// constructor
	public DetalleVenta(Libro libro1, int cantidad1, double precioU, double sub) 
	{
		libro = libro1;
		cantidad = cantidad1;
		precioUnitario = precioU;
		subtotal = sub;
	}
}
