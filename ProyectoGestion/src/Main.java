
import java.util.Scanner;

public class Main
{
	public static void main(String[] args)
	{
		Scanner sc = new Scanner(System.in);
		GestorLibreria gestor = new GestorLibreria();

		cargarDatosIniciales(gestor);

		System.out.println("¿Como deseas usar el sistema?");
		System.out.println("1. Consola");
		System.out.println("2. Ventana");
		System.out.print("Opcion: ");
		String opcion = sc.nextLine().trim();

		if (opcion.equals("2"))
		{
			// Todavia no esta implementada la interfaz de ventana y se cierra al elegirla
			System.out.println("El modo ventana todavia no esta implementado. Cerrando el programa...");
			sc.close();
			return;
		}

		// Cualquier otra opcion entra al modo consola
		InterfazConsola interfaz = new InterfazConsola(gestor, sc);
		interfaz.iniciar();

		sc.close();
		System.out.println("Programa finalizado.");
	}

	// Datos iniciales SIA-3 para poder probar el sistema sin tener que cargarlos a mano
	private static void cargarDatosIniciales(GestorLibreria gestor)
	{
		Autor autor1 = new Autor("A001", "ElRubiusOMG");
		Autor autor2 = new Autor("A002", "German Garmendia");

		gestor.agregarAutor(autor1);
		gestor.agregarAutor(autor2);

		Libro libro1 = new Libro("EL LIBRO TROLL", "Comedia", "9780307474728", 15000, 20);
		Libro libro2 = new Libro("Virtual Hero", "Aventura", "9780307389732", 13000, 10);
		Libro libro3 = new Libro("Chupa el Perro", "Comedia", "9780525433455", 12000, 15);

		try
		{
			gestor.agregarLibro("A001", libro1);
			gestor.agregarLibro("A001", libro2);
			gestor.agregarLibro("A002", libro3);
		}
		catch (LibroNoEncontradoException e)
		{
			// no deberia pasar nunca
			System.out.println("Error cargando datos iniciales: " + e.getMessage());
		}
	}
}