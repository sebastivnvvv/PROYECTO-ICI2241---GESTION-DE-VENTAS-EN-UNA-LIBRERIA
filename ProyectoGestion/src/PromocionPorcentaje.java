
public class PromocionPorcentaje extends Promocion {

	private double porcentaje;

    public PromocionPorcentaje(double porcentaje) 
    {
    	if (porcentaje>1)
    	{
    		this.porcentaje = porcentaje / 100.0;
    	}
    	else 
    	{
    		this.porcentaje = porcentaje;
    	}
    }

    @Override
    public double calcularDescuento(Venta venta) 
    {
    	return venta.calcularTotalBruto() * porcentaje;
    }
}