import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class GestorLibreria 
{
	private Map<String, Autor> autores;
	private Map<String, Venta> ventas;
	private int siguienteNumeroVenta;
	
	// Constructor
	public GestorLibreria() 
	{
		this.autores = new LinkedHashMap<>();
		this.ventas = new LinkedHashMap<>();
		this.siguienteNumeroVenta = 1;
	}
	
	// Metodos
	
	// Agregar un autor al mapa de autores
	public void agregarAutor(Autor autor) 
	{
		autores.put(autor.getId(), autor);
	}
	
	// ArrayList que contiene todos los autores de forma independiente al mapa
	public List<Autor> listarAutores()
	{
		return new ArrayList<>(autores.values());
	}
	
	// Busqueda de autor en el mapa de autores
	public Autor buscarAutor(String id) throws LibroNoEncontradoException
	{
		Autor autor = autores.get(id);
		if(autor == null) 
		{
			throw new LibroNoEncontradoException("No existe un autor con id " + id);
		}
		return autor;
	}
	
	// Cambiar nombre del autor
	public void editarAutor(String id, String nuevoNombre) throws LibroNoEncontradoException
	{
		Autor autor = buscarAutor(id);
		autor.setNombre(nuevoNombre);
	}
	
	// Eliminar autor del mapa de autores
	public void eliminarAutor(String id) throws LibroNoEncontradoException
	{
		if(!autores.containsKey(id)) 
		{
			throw new LibroNoEncontradoException("No existe el autor con id " + id);
		}
		autores.remove(id);
	}
	
	// Ventas
	
	private String generarIdVenta(LocalDate fecha)
	{
		DateTimeFormatter formato = DateTimeFormatter.ofPattern("ddMMyyyy");
		String correlativo = String.format("%05d", siguienteNumeroVenta);
		String fechaTexto = fecha.format(formato);
		return correlativo + fechaTexto;
	}
	
	public void registrarVenta(Venta venta) throws StockInsuficienteException
	{
		List<ItemVenta> items = venta.getProductos();
		// se valida la venta si es posible
		for (int i = 0; i < items.size(); i++)
		{
			ItemVenta item = items.get(i);
			Libro libro = item.getLibro();
			
			if (item.getCantidad() > libro.getStock())
			{
				throw new StockInsuficienteException("Stock insuficiente para \"" + libro.getTitulo() + "\". "+ "Disponible: " + libro.getStock() + ", solicitado: " + item.getCantidad());
			}
		}
		
		// si paso la validacion, se descuenta el stock de cada libro
		for (int i = 0; i < items.size(); i++)
		{
			ItemVenta item = items.get(i);
			Libro libro = item.getLibro();
			libro.setStock(libro.getStock() - item.getCantidad());
		}
		
		// se genera el id, se le asigna a la venta, y se guarda en el mapa
		String id = generarIdVenta(venta.getFecha());
		venta.setId(id);
		ventas.put(id, venta);
		
		siguienteNumeroVenta++;
	}

	public List<Venta> listarVentas()
	{
		return new ArrayList<>(ventas.values());
	}
	
	public Venta buscarVenta(String id) throws LibroNoEncontradoException
	{
		Venta venta = ventas.get(id);
		if (venta == null)
		{
			throw new LibroNoEncontradoException("No existe una venta con id " + id);
		}
		return venta;
	}

	public void editarVenta(String id, LocalDate nuevaFecha) throws LibroNoEncontradoException
	{
		Venta venta = buscarVenta(id);
		venta.setFecha(nuevaFecha);
	}

	public void eliminarVenta(String id) throws LibroNoEncontradoException
	{
		if (!ventas.containsKey(id))
		{
			throw new LibroNoEncontradoException("No existe una venta con id " + id);
		}
		ventas.remove(id);
	}
	
	
	
	
	
	
	
	// Getters y setters
	
	// Autores
	public Map<String, Autor> getAutores() {
		return autores;
	}
	
	public void setAutores(Map<String, Autor> autores) {
		this.autores = autores;
	}	
	
	// Ventas
	public Map<String, Venta> getVentas() {
		return ventas;
	}
	
	public void setVentas(Map<String, Venta> ventas) {
		this.ventas = ventas;
	}
}
