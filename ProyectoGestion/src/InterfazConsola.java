import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.time.format.DateTimeParseException;

public class InterfazConsola
{
	private GestorLibreria gestor;
	private Scanner sc;

	public InterfazConsola(GestorLibreria gestor, Scanner sc)
	{
		this.gestor = gestor;
		this.sc = sc;
	}

	// Menu principal

	public void iniciar()
	{
		boolean salir = false;

		while (!salir)
		{
			System.out.println("\n===== MENU PRINCIPAL =====");
			System.out.println("1. Gestionar autores");
			System.out.println("2. Gestionar libros");
			System.out.println("3. Gestionar ventas");
			System.out.println("0. Salir");
			System.out.print("Opcion: ");

			int opcion = leerEntero();

			switch (opcion)
			{
				case 1:
					menuAutores();
					break;
				case 2:
					menuLibros();
					break;
				case 3:
					menuVentas();
					break;
				case 0:
					salir = true;
					break;
				default:
					System.out.println("Opcion no reconocida.");
			}
		}
	}

	// Submenu Autores
	private void menuAutores()
	{
		boolean volver = false;

		while (!volver)
		{
			System.out.println("\n----- AUTORES -----");
			System.out.println("1. Agregar autor");
			System.out.println("2. Listar autores");
			System.out.println("3. Buscar autor");
			System.out.println("4. Editar autor");
			System.out.println("5. Eliminar autor");
			System.out.println("0. Volver");
			System.out.print("Opcion: ");

			int opcion = leerEntero();

			switch (opcion)
			{
				case 1:
					agregarAutor();
					break;
				case 2:
					listarAutores();
					break;
				case 3:
					buscarAutor();
					break;
				case 4:
					editarAutor();
					break;
				case 5:
					eliminarAutor();
					break;
				case 0:
					volver = true;
					break;
				default:
					System.out.println("Opcion no reconocida.");
			}
		}
	}

	private void agregarAutor()
	{
		System.out.print("ID del autor: ");
		String id = sc.nextLine().trim();
		System.out.print("Nombre del autor: ");
		String nombre = sc.nextLine().trim();

		boolean agregado = gestor.agregarAutor(new Autor(id, nombre));
		if (agregado)
		{
			System.out.println("Autor agregado correctamente.");
		}
		else
		{
			System.out.println("Ya existe un autor con el id " + id + ". No se modifico nada.");
		}
	}

	private void listarAutores()
	{
		List<Autor> autores = gestor.listarAutores();
		if (autores.isEmpty())
		{
			System.out.println("No hay autores registrados.");
			return;
		}
		for (int i = 0; i < autores.size(); i++)
		{
			Autor a = autores.get(i);
			System.out.println("- [" + a.getId() + "] " + a.getNombre()
					+ " (" + a.getLibrosPublicados().size() + " libro(s))");
		}
	}

	private void buscarAutor()
	{
		System.out.print("ID del autor a buscar: ");
		String id = sc.nextLine().trim();
		try
		{
			Autor autor = gestor.buscarAutor(id);
			System.out.println("Encontrado: [" + autor.getId() + "] " + autor.getNombre());
		}
		catch (LibroNoEncontradoException e)
		{
			System.out.println(e.getMessage());
		}
	}

	private void editarAutor()
	{
		System.out.print("ID del autor a editar: ");
		String id = sc.nextLine().trim();
		System.out.print("Nuevo nombre: ");
		String nuevoNombre = sc.nextLine().trim();
		try
		{
			gestor.editarAutor(id, nuevoNombre);
			System.out.println("Autor actualizado.");
		}
		catch (LibroNoEncontradoException e)
		{
			System.out.println(e.getMessage());
		}
	}

	private void eliminarAutor()
	{
		System.out.print("ID del autor a eliminar: ");
		String id = sc.nextLine().trim();
		try
		{
			gestor.eliminarAutor(id);
			System.out.println("Autor eliminado.");
		}
		catch (LibroNoEncontradoException e)
		{
			System.out.println(e.getMessage());
		}
	}

	// Submenu Libros
	
	private void menuLibros()
	{
		boolean volver = false;

		while (!volver)
		{
			System.out.println("\n----- LIBROS -----");
			System.out.println("1. Agregar libro a un autor");
			System.out.println("2. Listar libros");
			System.out.println("3. Buscar libro por ISBN");
			System.out.println("4. Buscar libro por titulo y autor");
			System.out.println("5. Editar libro");
			System.out.println("6. Eliminar libro");
			System.out.println("7. Sugerir libros relacionados");
			System.out.println("0. Volver");
			System.out.print("Opcion: ");

			int opcion = leerEntero();

			switch (opcion)
			{
				case 1:
					agregarLibro();
					break;
				case 2:
					listarLibros();
					break;
				case 3:
					buscarLibroPorIsbn();
					break;
				case 4:
					buscarLibroPorTituloYAutor();
					break;
				case 5:
					editarLibro();
					break;
				case 6:
					eliminarLibro();
					break;
				case 7:
					sugerirRelacionados();
					break;
				case 0:
					volver = true;
					break;
				default:
					System.out.println("Opcion no reconocida.");
			}
		}
	}

	private void agregarLibro()
	{
		System.out.print("ID del autor: ");
		String autorId = sc.nextLine().trim();
		System.out.print("Titulo del libro: ");
		String titulo = sc.nextLine().trim();
		System.out.print("Genero: ");
		String genero = sc.nextLine().trim();
		System.out.print("ISBN: ");
		String isbn = sc.nextLine().trim();
		System.out.print("Precio: ");
		int precio = leerEntero();
		System.out.print("Stock: ");
		int stock = leerEntero();

		if (precio <= 0 || stock < 0)
		{
			System.out.println("El precio debe ser mayor a 0 y el stock no puede ser negativo.");
			return;
		}

		Libro libro = new Libro(titulo, genero, isbn, precio, stock);

		try
		{
			gestor.agregarLibro(autorId, libro);
			System.out.println("Libro agregado correctamente.");
		}
		catch (LibroNoEncontradoException e)
		{
			System.out.println(e.getMessage());
		}
	}

	private void listarLibros()
	{
		List<Libro> libros = gestor.listarLibros();
		if (libros.isEmpty())
		{
			System.out.println("No hay libros registrados.");
			return;
		}
		for (int i = 0; i < libros.size(); i++)
		{
			Libro l = libros.get(i);
			System.out.println("- [" + l.getIsbn() + "] " + l.getTitulo()+ " (" + l.getGenero() + ") - $" + l.getPrecio() + " - stock: " + l.getStock());
		}
	}

	private void buscarLibroPorIsbn()
	{
		System.out.print("ISBN a buscar: ");
		String isbn = sc.nextLine().trim();
		try
		{
			Libro libro = gestor.buscarLibro(isbn);
			System.out.println("Encontrado: " + libro.getTitulo() + " (" + libro.getGenero() + ")");
		}
		catch (LibroNoEncontradoException e)
		{
			System.out.println(e.getMessage());
		}
	}

	private void buscarLibroPorTituloYAutor()
	{
		System.out.print("Titulo (o parte del titulo): ");
		String titulo = sc.nextLine().trim();
		System.out.print("Nombre del autor (o parte del nombre): ");
		String nombreAutor = sc.nextLine().trim();

		List<Libro> resultado = gestor.buscarLibro(titulo, nombreAutor);
		if (resultado.isEmpty())
		{
			System.out.println("No se encontraron libros que coincidan.");
			return;
		}
		for (int i = 0; i < resultado.size(); i++)
		{
			Libro l = resultado.get(i);
			System.out.println("- [" + l.getIsbn() + "] " + l.getTitulo());
		}
	}

	private void editarLibro()
	{
		System.out.print("ISBN del libro a editar: ");
		String isbn = sc.nextLine().trim();
		System.out.print("Nuevo titulo: ");
		String nuevoTitulo = sc.nextLine().trim();
		System.out.print("Nuevo precio: ");
		int nuevoPrecio = leerEntero();
		System.out.print("Nuevo stock: ");
		int nuevoStock = leerEntero();

		if (nuevoPrecio <= 0 || nuevoStock < 0)
		{
			System.out.println("El precio debe ser mayor a 0 y el stock no puede ser negativo.");
			return;
		}

		try
		{
			gestor.editarLibro(isbn, nuevoTitulo, nuevoPrecio, nuevoStock);
			System.out.println("Libro actualizado.");
		}
		catch (LibroNoEncontradoException e) 
		{
			System.out.println(e.getMessage());
		}
	}

	private void eliminarLibro()
	{
		System.out.print("ISBN del libro a eliminar: ");
		String isbn = sc.nextLine().trim();
		try
		{
			gestor.eliminarLibro(isbn);
			System.out.println("Libro eliminado.");
		}
		catch (LibroNoEncontradoException e)
		{
			System.out.println(e.getMessage());
		}
	}

	private void sugerirRelacionados()
	{
		System.out.print("ISBN del libro base: ");
		String isbn = sc.nextLine().trim();
		try
		{
			Libro libro = gestor.buscarLibro(isbn);
			List<Libro> sugeridos = gestor.sugerirRelacionados(libro);

			if (sugeridos.isEmpty())
			{
				System.out.println("No hay libros relacionados (mismo genero) todavia.");
				return;
			}
			System.out.println("Libros relacionados con \"" + libro.getTitulo() + "\":");
			for (int i = 0; i < sugeridos.size(); i++)
			{
				System.out.println("- " + sugeridos.get(i).getTitulo());
			}
		}
		catch (LibroNoEncontradoException e)
		{
			System.out.println(e.getMessage());
		}
	}

// Submenu Ventas

	private void menuVentas()
	{
		boolean volver = false;

		while (!volver)
		{
			System.out.println("\n----- VENTAS -----");
			System.out.println("1. Registrar venta");
			System.out.println("2. Listar ventas");
			System.out.println("3. Buscar venta");
			System.out.println("4. Editar fecha de una venta");
			System.out.println("5. Eliminar venta");
			System.out.println("0. Volver");
			System.out.print("Opcion: ");

			int opcion = leerEntero();

			switch (opcion)
			{
				case 1:
					registrarVenta();
					break;
				case 2:
					listarVentas();
					break;
				case 3:
					buscarVenta();
					break;
				case 4:
					editarVenta();
					break;
				case 5:
					eliminarVenta();
					break;
				case 0:
					volver = true;
					break;
				default:
					System.out.println("Opcion no reconocida.");
			}
		}
	}

	private void registrarVenta()
	{
		// el id real se lo asigna GestorLibreria al momento de registrar la venta
		Venta venta = new Venta(null);

		boolean agregarMas = true;
		while (agregarMas)
		{
			System.out.print("ISBN del libro a vender (Enter para cancelar): ");
			String isbn = sc.nextLine().trim();

			if (isbn.isEmpty())
			{
				agregarMas = false;
				continue;
			}

			Libro libro;
			
			try
			{
				libro = gestor.buscarLibro(isbn);
			}
			catch (LibroNoEncontradoException e)
			{
				System.out.println(e.getMessage());
				continue;
			}

			System.out.print("Cantidad: ");
			int cantidad = leerEntero();

			venta.agregarItem(new ItemVenta(libro, cantidad));

			System.out.print("¿Agregar otro libro a la venta? (s/n): ");
			agregarMas = sc.nextLine().trim().equalsIgnoreCase("s");
		}

		if (venta.getProductos().isEmpty())
		{
			System.out.println("Venta cancelada (sin items).");
			return;
		}

		System.out.println("¿Aplicar una promocion?");
		System.out.println("1. Ninguna");
		System.out.println("2. Descuento porcentual");
		System.out.println("3. 2x1 (50% de descuento)");
		System.out.print("Opcion: ");
		int tipoPromo = leerEntero();

		try
		{
			if (tipoPromo == 2)
			{
				System.out.print("Porcentaje de descuento (ej: 20 para 20%): ");
				int porcentajeIngresado = leerEntero();

				if (porcentajeIngresado <= 0 || porcentajeIngresado > 100)
				{
					System.out.println("El porcentaje debe estar entre 1 y 100. Se registrara sin descuento.");
					gestor.registrarVenta(venta);
					System.out.println("Venta registrada con id " + venta.getId()
							+ ". Total: $" + venta.calcularTotal());
				}
				else
				{
					double porcentaje = porcentajeIngresado / 100.0;
					Promocion promo = new PromocionPorcentaje(porcentaje);
					double totalConDescuento = gestor.registrarVenta(venta, promo);
					System.out.println("Venta registrada con id " + venta.getId()
							+ ". Total con descuento: $" + totalConDescuento);
				}
			}
			else if (tipoPromo == 3)
			{
				Promocion promo = new PromocionDosPorUno();
				double totalConDescuento = gestor.registrarVenta(venta, promo);
				System.out.println("Venta registrada con id " + venta.getId()
						+ ". Total con descuento: $" + totalConDescuento);
			}
			else
			{
				gestor.registrarVenta(venta);
				System.out.println("Venta registrada con id " + venta.getId()
						+ ". Total: $" + venta.calcularTotal());
			}
		}
		catch (StockInsuficienteException e)
		{
			System.out.println(e.getMessage());
		}
	}

	private void listarVentas()
	{
		List<Venta> ventas = gestor.listarVentas();
		if (ventas.isEmpty())
		{
			System.out.println("No hay ventas registradas.");
			return;
		}
		for (int i = 0; i < ventas.size(); i++)
		{
			Venta v = ventas.get(i);
			System.out.println("Venta [" + v.getId() + "] - " + v.getFecha()
					+ " - Total: $" + v.calcularTotal());
			List<ItemVenta> items = v.getProductos();
			for (int j = 0; j < items.size(); j++)
			{
				ItemVenta item = items.get(j);
				System.out.println("   - " + item.getLibro().getTitulo() + " x" + item.getCantidad());
			}
		}
	}

	private void buscarVenta()
	{
		System.out.print("ID de la venta a buscar: ");
		String id = sc.nextLine().trim();
		try
		{
			Venta venta = gestor.buscarVenta(id);
			System.out.println("Venta [" + venta.getId() + "] - " + venta.getFecha()
					+ " - Total: $" + venta.calcularTotal());
		}
		catch (LibroNoEncontradoException e)
		{
			System.out.println(e.getMessage());
		}
	}

	private void editarVenta()
	{
		System.out.print("ID de la venta a editar: ");
		String id = sc.nextLine().trim();
		System.out.print("Nueva fecha (yyyy-MM-dd): ");
		String textoFecha = sc.nextLine().trim();

		try
		{
			LocalDate nuevaFecha = LocalDate.parse(textoFecha);
			gestor.editarVenta(id, nuevaFecha);
			System.out.println("Venta actualizada.");
		}
		catch (DateTimeParseException e)
		{
			System.out.println("Formato de fecha invalido. Usa el formato yyyy-MM-dd (ej: 2026-05-01).");
		}
		catch (LibroNoEncontradoException e)
		{
			System.out.println(e.getMessage());
		}
	}

	private void eliminarVenta()
	{
		System.out.print("ID de la venta a eliminar: ");
		String id = sc.nextLine().trim();
		try
		{
			gestor.eliminarVenta(id);
			System.out.println("Venta eliminada.");
		}
		catch (LibroNoEncontradoException e)
		{
			System.out.println(e.getMessage());
		}
	}

	// Metodo de lectura opcion
	
	// Lee un numero entero desde la consola,si es invalido el switch cae en "default")
	private int leerEntero()
	{
		String entrada = sc.nextLine().trim();
		try
		{
			return Integer.parseInt(entrada);
		}
		catch (NumberFormatException e)
		{
			return -1;
		}
	}
}