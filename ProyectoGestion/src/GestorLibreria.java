import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GestorLibreria 
{
	private Map<String, Autor> autores;
	private List<Venta> ventas;
	
	public GestorLibreria() 
	{
		this.autores = new LinkedHashMap<>();
		this.ventas = new ArrayList<>();
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
