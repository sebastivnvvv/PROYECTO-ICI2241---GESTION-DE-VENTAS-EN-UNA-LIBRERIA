
public class PromocionPorcentaje extends Promocion {

    private double porcentaje;

    public PromocionPorcentaje(double porcentaje) 
    {
        this.porcentaje = porcentaje;
    }

    @Override
    public double calcularDescuento(double montoBruto) 
    {
        return montoBruto * porcentaje;
    }
}