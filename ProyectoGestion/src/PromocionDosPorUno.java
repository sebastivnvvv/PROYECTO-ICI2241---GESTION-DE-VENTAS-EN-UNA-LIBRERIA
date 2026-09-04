
public class PromocionDosPorUno extends Promocion
{
	 @Override
	 public double calcularDescuento(double montoBruto) 
	 {
		 return montoBruto * 0.5;
	 }
}
