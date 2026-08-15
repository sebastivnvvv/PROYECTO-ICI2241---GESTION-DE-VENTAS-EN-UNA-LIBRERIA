import java.time.LocalDate;
import java.util.List;

public class Venta 
{
	private int idVenta;
	private LocalDate fecha; // podemos cambiarlo
	private List<DetalleVenta> productos;
	private double total;
}
