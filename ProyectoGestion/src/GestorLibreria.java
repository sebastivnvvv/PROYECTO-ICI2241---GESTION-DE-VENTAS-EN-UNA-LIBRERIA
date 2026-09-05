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
	
	// METODOS
	
	// Agregar un autor al mapa de autores
	public boolean agregarAutor(Autor autor) 
	{
		if (autores.containsKey(autor.getId()))
		{
			return false;
		}
		autores.put(autor.getId(), autor);
		return true;
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
	
	// METODOS VENTA
	
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

		// Primero se valida toda la venta sin modificar nada todavia:
		// 1) que la cantidad pedida sea mayor a 0
		// 2) que sumando todos los items del mismo libro por si esta repetido
		//    en la misma venta no se pase del stock disponible
		
		for (int i = 0; i < items.size(); i++)
		{
			ItemVenta itemActual = items.get(i);
			Libro libroActual = itemActual.getLibro();

			if (itemActual.getCantidad() <= 0)
			{
				throw new StockInsuficienteException("La cantidad debe ser mayor a 0 para \"" + libroActual.getTitulo() + "\"");
			}

			int totalPedido = 0;
			for (int j = 0; j < items.size(); j++)
			{
				Libro otroLibro = items.get(j).getLibro();
				if (otroLibro.getIsbn().equals(libroActual.getIsbn()))
				{
					totalPedido += items.get(j).getCantidad();
				}
			}

			if (totalPedido > libroActual.getStock())
			{
				throw new StockInsuficienteException("Stock insuficiente para \"" + libroActual.getTitulo() + "\". "+ "Disponible: " + libroActual.getStock() + ", solicitado en total: " + totalPedido);
			}
		}

		// si paso toda la validacion, recien ahi se descuenta el stock de cada libro
		for (int i = 0; i < items.size(); i++)
		{
			ItemVenta item = items.get(i);
			item.getLibro().venderUnidad(item.getCantidad());
		}

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
	
	// METODOS APLICADO A LIBRO
	
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
 
	// Le quita las tildes a un texto para que buscar garcia tambien encuentre a García
	private String quitarTildes(String texto)
	{
		return texto
				.replace("á", "a").replace("é", "e").replace("í", "i")
				.replace("ó", "o").replace("ú", "u").replace("ñ", "n");
	}

	// Buscar libro pero esta vez con el titulo y el nombre del autor
	public List<Libro> buscarLibro(String titulo, String nombreAutor)
	{
	    List<Libro> resultado = new ArrayList<>();
	    List<Autor> listaAutores = new ArrayList<>(autores.values());

	    for (int i = 0; i < listaAutores.size(); i++)
	    {
	        Autor autor = listaAutores.get(i);
	        String nombreActual = quitarTildes(autor.getNombre().toLowerCase());
	        String nombreBuscado = quitarTildes(nombreAutor.toLowerCase());

	        if (!nombreActual.contains(nombreBuscado))
	        {
	            continue;
	        }

	        List<Libro> librosDelAutor = autor.getLibrosPublicados();

	        for (int j = 0; j < librosDelAutor.size(); j++)
	        {
	            Libro libroActual = librosDelAutor.get(j);
	            String tituloActual = quitarTildes(libroActual.getTitulo().toLowerCase());
	            String tituloBuscado = quitarTildes(titulo.toLowerCase());

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
