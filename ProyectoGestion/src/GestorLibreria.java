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
			//libro.setStock(libro.getStock() - item.getCantidad());
			libro.venderUnidad(item.getCantidad());
		}
		
		// se genera el id, se le asigna a la venta, y se guarda en el mapa
		String id = generarIdVenta(venta.getFecha());
		venta.setId(id);
		ventas.put(id, venta);
		
		siguienteNumeroVenta++;
	}
	
	// Sobrecarga registra la venta aplicando promocion y devuelve el total con descuento
	public double registrarVenta(Venta venta, Promocion promocion) throws StockInsuficienteException
	{
	    registrarVenta(venta); 
	    return venta.calcularTotal(promocion);
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
	
	// libro
	
	public void agregarLibro(String autorId, Libro libro) throws LibroNoEncontradoException
	{
	    Autor autor = buscarAutor(autorId);
	    autor.agregarLibro(libro);
	}
	
	
	// unimos todos los libros
	public List<Libro> listarLibros()
	{
	    List<Libro> todos = new ArrayList<>();
	    List<Autor> listaAutores = new ArrayList<>(autores.values());

	    for (int i = 0; i < listaAutores.size(); i++)
	    {
	        Autor autor = listaAutores.get(i);
	        List<Libro> librosDelAutor = autor.getLibrosPublicados();

	        for (int j = 0; j < librosDelAutor.size(); j++)
	        {
	            todos.add(librosDelAutor.get(j));
	        }
	    }
	    return todos;
	}
	
	
	
	// Buscar libro pero por el ISBN del libro
	public Libro buscarLibro(String isbn) throws LibroNoEncontradoException
	{
	    List<Libro> libros = listarLibros();

	    for (int i = 0; i < libros.size(); i++)
	    {
	        if (libros.get(i).getIsbn().equalsIgnoreCase(isbn))
	        {
	            return libros.get(i);
	        }
	    }
	    throw new LibroNoEncontradoException("No existe un libro con ISBN: " + isbn);
	}
 
	// Buscar libro pero esta vez con el titulo y el nombre del autor
	public List<Libro> buscarLibro(String titulo, String nombreAutor)
	{
	    List<Libro> resultado = new ArrayList<>();
	    List<Autor> listaAutores = new ArrayList<>(autores.values());

	    for (int i = 0; i < listaAutores.size(); i++)
	    {
	        Autor autor = listaAutores.get(i);
	        String nombreActual = autor.getNombre().toLowerCase();
	        String nombreBuscado = nombreAutor.toLowerCase();

	        if (!nombreActual.contains(nombreBuscado))
	        {
	            continue;
	        }

	        List<Libro> librosDelAutor = autor.getLibrosPublicados();

	        for (int j = 0; j < librosDelAutor.size(); j++)
	        {
	            Libro libroActual = librosDelAutor.get(j);
	            String tituloActual = libroActual.getTitulo().toLowerCase();
	            String tituloBuscado = titulo.toLowerCase();

	            if (tituloActual.contains(tituloBuscado))
	            {
	                resultado.add(libroActual);
	            }
	        }
	    }
	    return resultado;
	}
	
	
	public void editarLibro(String isbn, String nuevoTitulo, int nuevoPrecio, int nuevoStock) throws LibroNoEncontradoException
	{
	    Libro libro = buscarLibro(isbn);
	    libro.setTitulo(nuevoTitulo);
	    libro.setPrecio(nuevoPrecio);
	    libro.setStock(nuevoStock);
	}
	
	public void eliminarLibro(String isbn) throws LibroNoEncontradoException
	{
	    List<Autor> listaAutores = new ArrayList<>(autores.values());

	    for (int i = 0; i < listaAutores.size(); i++)
	    {
	        List<Libro> librosDelAutor = listaAutores.get(i).getLibrosPublicados();

	        for (int j = 0; j < librosDelAutor.size(); j++)
	        {
	            if (librosDelAutor.get(j).getIsbn().equalsIgnoreCase(isbn))
	            {
	                librosDelAutor.remove(j);
	                return;
	            }
	        }
	    }
	    throw new LibroNoEncontradoException("No existe un libro con ISBN: " + isbn);
	}
	
	public List<Libro> sugerirRelacionados(Libro libro)
	{
	    List<Libro> sugeridos = new ArrayList<>();
	    List<Libro> todos = listarLibros();

	    for (int i = 0; i < todos.size(); i++)
	    {
	        Libro candidato = todos.get(i);
	        boolean mismoGenero = candidato.getGenero().equalsIgnoreCase(libro.getGenero());
	        boolean esElMismo = candidato.getIsbn().equalsIgnoreCase(libro.getIsbn());

	        if (mismoGenero && !esElMismo)
	        {
	            sugeridos.add(candidato);
	        }
	    }
	    return sugeridos;
	}
	
	// Getters y setters
	
	// Autores
	public Map<String, Autor> getAutores() {
	    return new LinkedHashMap<>(autores);
	}
	
	public void setAutores(Map<String, Autor> autores) {
		this.autores = autores;
	}	
	
	// Ventas
	public Map<String, Venta> getVentas() {
	    return new LinkedHashMap<>(ventas);
	}
	
	public void setVentas(Map<String, Venta> ventas) {
		this.ventas = ventas;
	}
}
