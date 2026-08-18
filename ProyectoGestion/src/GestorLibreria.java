import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GestorLibreria 
{
	private Map<String, Autor> autores;
	private List<Venta> ventas;
	
	// Constructor
	public GestorLibreria() 
	{
		this.autores = new LinkedHashMap<>();
		this.ventas = new ArrayList<>();
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
	
	
	
	
	
	
	
	// Getters y setters
	
	// Autores
	public Map<String, Autor> getAutores() {
		return autores;
	}
	
	public void setAutores(Map<String, Autor> autores) {
		this.autores = autores;
	}	
	
	// Venta
	public List<Venta> getVentas() {
		return ventas;
	}
	
	public void setVentas(List<Venta> ventas) {
		this.ventas = ventas;
	}
}
