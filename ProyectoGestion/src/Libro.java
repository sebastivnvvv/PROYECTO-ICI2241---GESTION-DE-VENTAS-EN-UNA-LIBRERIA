
public class Libro 
{
	private String titulo;
	private String genero;
	private String isbn;
	private int precio;
	private int stock;
	
	// constructor
	public Libro(String titulo, String genero, String isbn, int precio, int stock)
	{
		this.titulo = titulo;
		this.genero = genero;
		this.isbn = isbn;
		this.precio = precio;
		this.stock = stock;
	}
	
	
	// Metodos
	
	// vender unidad == a ver si el stock alcanza o si no
	public void venderUnidad(int cantidad) throws StockInsuficienteException {
	    if (cantidad > stock) 
	    {
	        throw new StockInsuficienteException(
	            "Stock insuficiente para \"" + titulo + "\". Disponible: " + stock + ", solicitado: " + cantidad);
	    }
	    stock -= cantidad;
	}
	
	// getters y setters
	
	// genero
	public String getGenero() {
		return genero;
	}
	
	public void setGenero(String genero) {
		this.genero = genero;
	}
	
	// Titulo
	
	public String getTitulo() {
		return titulo;
	}
	
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
	// ISBN
	
	public String getIsbn() {
		return isbn;
	}
	
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	
	// Precio
	
	public int getPrecio() {
		return precio;
	}
	
	public void setPrecio(int precio) {
		this.precio = precio;
	}
	
	// stock
	
	public int getStock() 
	{
		return stock;
	}
	
	public void setStock(int stock) 
	{
		this.stock = stock;
	}	
}
