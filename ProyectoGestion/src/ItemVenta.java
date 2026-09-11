
public class ItemVenta 
{
	private Libro libro;
	private int cantidad;
	private int precioUnitario; // precio al momento de la venta
	private String isbnSnapshot;
	private String tituloSnapshot;
	
	// Constructor
	
	public ItemVenta(Libro libro, int cantidad) 
	{
		this.libro = libro;
		this.cantidad = cantidad;
		this.precioUnitario = libro.getPrecio();
		this.isbnSnapshot = libro.getIsbn();
		this.tituloSnapshot = libro.getTitulo();
	}
	
	// Constructor historico para cargar ventas de libros que ya no existen
	public ItemVenta(String isbn, String titulo, int cantidad, int precioUnitario) 
	{
		this.libro = null; 
		this.isbnSnapshot = isbn;
		this.tituloSnapshot = titulo;
		this.cantidad = cantidad;
		this.precioUnitario = precioUnitario;
	}
	// Metodos
	public double subTotal()
	{
		// usa el precio congelado, ignorando cambios futuros en el libro
		return precioUnitario * cantidad;
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
	
	// PrecioUnitario

	public int getPrecioUnitario() {
		return precioUnitario;
	}

	public void setPrecioUnitario(int precioUnitario) {
		this.precioUnitario = precioUnitario;
	}
	// Historico
	
	public String getTituloSeguro() {
		return tituloSnapshot;
	}

	public String getIsbnSeguro() {
		return isbnSnapshot;
	}
	
}
