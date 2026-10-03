import java.util.ArrayList;
import java.util.List;

public class Autor 
{
	// atributos
	private String id;
	private String nombre;
	private List<Libro> librosPublicados;
	
	// constructor
	public Autor(String id, String nombre) 
	{
		this.id = id;
		this.nombre = nombre;
		this.librosPublicados = new ArrayList<>();
	}
	
	// Metodos
	
	public void agregarLibro(Libro libro) 
	{
		for(int i = 0; i < librosPublicados.size(); i++) 
		{
			String idLibro = libro.getIsbn();
			Libro libroActual = librosPublicados.get(i);
			String idActual = libroActual.getIsbn();  
			
			if(idLibro.equals(idActual)) 
			{
				libroActual.setStock(libroActual.getStock() + libro.getStock());
				return;
			}
		}
		librosPublicados.add(libro);
	}
	
	// Elimina un libro de este autor segun su ISBN. Devuelve true si lo encontro y elimino
	public boolean eliminarLibro(String isbn) 
	{
		for(int i = 0; i < librosPublicados.size(); i++) 
		{
			if(librosPublicados.get(i).getIsbn().equalsIgnoreCase(isbn)) 
			{
				librosPublicados.remove(i);
				return true;
			}
		}
		return false;
	}
	
	// Getters y Setters
	
	// ID
	public String getId() {
		return id;
	}
	/*
	public void setId(String id) {
		this.id = id;
	}*/
	
	// nombre
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	/*public void setLibrosPublicados(List<Libro> librosPublicados) {
		this.librosPublicados = librosPublicados;
	}*/
	
	public List<Libro> getLibrosPublicados() {
	    return new ArrayList<>(librosPublicados);
	}
	
}
