import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

import javax.swing.JOptionPane;

public class InterfazVentana
{
	private GestorLibreria gestor;
	public InterfazVentana(GestorLibreria gestor)
	{
		this.gestor = gestor;
	}

	// Menu principal
	public void iniciar()
	{
		boolean salir = false;
		while (!salir)
		{
			String[] opciones = { "Gestionar autores", "Gestionar libros", "Gestionar ventas", "Salir" };
			int opcion = JOptionPane.showOptionDialog(null, "Menu principal", "Gestion de ventas - Libreria",
					JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, opciones, opciones[0]);
			switch (opcion)
			{
				case 0:
					menuAutores();
					break;
				case 1:
					menuLibros();
					break;
				case 2:
					menuVentas();
					break;
				default:
					salir = true;
			}
		}

		salirGuardando();
	}
  
	private void salirGuardando()
	{
		int opcion = JOptionPane.showConfirmDialog(null, "Â¿Guardar los datos antes de salir?",
				"Salir", JOptionPane.YES_NO_OPTION);
		if (opcion == JOptionPane.YES_OPTION)
		{
			GestorArchivosCSV.guardarDatos(gestor);
		}
	}

	private void menuAutores()
	{
		boolean volver = false;

		while (!volver)
		{
			String[] opciones = { "Agregar autor", "Listar autores", "Buscar autor", "Editar autor",
					"Eliminar autor", "Volver" };
			int opcion = JOptionPane.showOptionDialog(null, "Autores", "Autores",
					JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, opciones, opciones[0]);
			switch (opcion)
			{
				case 0:
					agregarAutor();
					break;
				case 1:
					listarAutores();
					break;
				case 2:
					buscarAutor();
					break;
				case 3:
					editarAutor();
					break;
				case 4:
					eliminarAutor();
					break;
				default:
					volver = true;
			}
		}
	}

	private void agregarAutor()
	{
		String id = pedirTexto("ID del autor:");
		if (id == null)
		{
			return;
		}
		String nombre = pedirTexto("Nombre del autor:");
		if (nombre == null)
		{
			return;
		}

		boolean agregado = gestor.agregarAutor(new Autor(id, nombre));
		if (agregado)
		{
			JOptionPane.showMessageDialog(null, "Autor agregado correctamente.");
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Ya existe un autor con el id " + id + ". No se modifico nada.");
		}
	}

	private void listarAutores()
	{
		List<Autor> autores = gestor.listarAutores();
		if (autores.isEmpty())
		{
			JOptionPane.showMessageDialog(null, "No hay autores registrados.");
			return;
		}
		StringBuilder texto = new StringBuilder();
		for (int i = 0; i < autores.size(); i++)
		{
			Autor a = autores.get(i);
			texto.append("- [").append(a.getId()).append("] ").append(a.getNombre())
					.append(" (").append(a.getLibrosPublicados().size()).append(" libro(s))\n");
		}
		JOptionPane.showMessageDialog(null, texto.toString());
	}

	private void buscarAutor()
	{
		String id = pedirTexto("ID del autor a buscar:");
		if (id == null)
		{
			return;
		}
		try
		{
			Autor autor = gestor.buscarAutor(id);
			JOptionPane.showMessageDialog(null, "Encontrado: [" + autor.getId() + "] " + autor.getNombre());
		}
		catch (LibroNoEncontradoException e)
		{
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	private void editarAutor()
	{
		String id = pedirTexto("ID del autor a editar:");
		if (id == null)
		{
			return;
		}
		String nuevoNombre = pedirTexto("Nuevo nombre:");
		if (nuevoNombre == null)
		{
			return;
		}
		try
		{
			gestor.editarAutor(id, nuevoNombre);
			JOptionPane.showMessageDialog(null, "Autor actualizado.");
		}
		catch (LibroNoEncontradoException e)
		{
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	private void eliminarAutor()
	{
		String id = pedirTexto("ID del autor a eliminar:");
		if (id == null)
		{
			return;
		}
		try
		{
			gestor.eliminarAutor(id);
			JOptionPane.showMessageDialog(null, "Autor eliminado.");
		}
		catch (LibroNoEncontradoException e)
		{
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	private void menuLibros()
	{
		boolean volver = false;
		while (!volver)
		{
			String[] opciones = { "Agregar libro", "Listar libros", "Buscar por ISBN", "Buscar por titulo y autor",
					"Editar libro", "Eliminar libro", "Sugerir relacionados", "Volver" };
			int opcion = JOptionPane.showOptionDialog(null, "Libros", "Libros",
					JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, opciones, opciones[0]);
			switch (opcion)
			{
				case 0:
					agregarLibro();
					break;
				case 1:
					listarLibros();
					break;
				case 2:
					buscarLibroPorIsbn();
					break;
				case 3:
					buscarLibroPorTituloYAutor();
					break;
				case 4:
					editarLibro();
					break;
				case 5:
					eliminarLibro();
					break;
				case 6:
					sugerirRelacionados();
					break;
				default:
					volver = true;
			}
		}
	}

	private void agregarLibro()
	{
		String autorId = pedirTexto("ID del autor:");
		if (autorId == null)
		{
			return;
		}
		String titulo = pedirTexto("Titulo del libro:");
		if (titulo == null)
		{
			return;
		}
		String genero = pedirTexto("Genero:");
		if (genero == null)
		{
			return;
		}
		String isbn = pedirTexto("ISBN:");
		if (isbn == null)
		{
			return;
		}
		Integer precio = pedirEntero("Precio:");
		if (precio == null)
		{
			return;
		}
		Integer stock = pedirEntero("Stock:");
		if (stock == null)
		{
			return;
		}
		if (precio <= 0 || stock < 0)
		{
			JOptionPane.showMessageDialog(null, "El precio debe ser mayor a 0 y el stock no puede ser negativo.");
			return;
		}
		Libro libro = new Libro(titulo, genero, isbn, precio, stock);
		try
		{
			gestor.agregarLibro(autorId, libro);
			JOptionPane.showMessageDialog(null, "Libro agregado correctamente.");
		}
		catch (LibroNoEncontradoException e)
		{
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	private void listarLibros()
	{
		List<Libro> libros = gestor.listarLibros();
		if (libros.isEmpty())
		{
			JOptionPane.showMessageDialog(null, "No hay libros registrados.");
			return;
		}
		StringBuilder texto = new StringBuilder();
		for (int i = 0; i < libros.size(); i++)
		{
			Libro l = libros.get(i);
			texto.append("- [").append(l.getIsbn()).append("] ").append(l.getTitulo())
					.append(" (").append(l.getGenero()).append(") - $").append(l.getPrecio())
					.append(" - stock: ").append(l.getStock()).append("\n");
		}
		JOptionPane.showMessageDialog(null, texto.toString());
	}

	private void buscarLibroPorIsbn()
	{
		String isbn = pedirTexto("ISBN a buscar:");
		if (isbn == null)
		{
			return;
		}
		try
		{
			Libro libro = gestor.buscarLibro(isbn);
			JOptionPane.showMessageDialog(null, "Encontrado: " + libro.getTitulo() + " (" + libro.getGenero() + ")");
		}
		catch (LibroNoEncontradoException e)
		{
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	private void buscarLibroPorTituloYAutor()
	{
		String titulo = pedirTexto("Titulo (o parte del titulo):");
		if (titulo == null)
		{
			return;
		}
		String nombreAutor = pedirTexto("Nombre del autor (o parte del nombre):");
		if (nombreAutor == null)
		{
			return;
		}
		List<Libro> resultado = gestor.buscarLibro(titulo, nombreAutor);
		if (resultado.isEmpty())
		{
			JOptionPane.showMessageDialog(null, "No se encontraron libros que coincidan.");
			return;
		}
		StringBuilder texto = new StringBuilder();
		for (int i = 0; i < resultado.size(); i++)
		{
			Libro l = resultado.get(i);
			texto.append("- [").append(l.getIsbn()).append("] ").append(l.getTitulo()).append("\n");
		}
		JOptionPane.showMessageDialog(null, texto.toString());
	}

	private void editarLibro()
	{
		String isbn = pedirTexto("ISBN del libro a editar:");
		if (isbn == null)
		{
			return;
		}
		String nuevoTitulo = pedirTexto("Nuevo titulo:");
		if (nuevoTitulo == null)
		{
			return;
		}
		Integer nuevoPrecio = pedirEntero("Nuevo precio:");
		if (nuevoPrecio == null)
		{
			return;
		}
		Integer nuevoStock = pedirEntero("Nuevo stock:");
		if (nuevoStock == null)
		{
			return;
		}
		if (nuevoPrecio <= 0 || nuevoStock < 0)
		{
			JOptionPane.showMessageDialog(null, "El precio debe ser mayor a 0 y el stock no puede ser negativo.");
			return;
		}
		try
		{
			gestor.editarLibro(isbn, nuevoTitulo, nuevoPrecio, nuevoStock);
			JOptionPane.showMessageDialog(null, "Libro actualizado.");
		}
		catch (LibroNoEncontradoException e)
		{
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	private void eliminarLibro()
	{
		String isbn = pedirTexto("ISBN del libro a eliminar:");
		if (isbn == null)
		{
			return;
		}
		try
		{
			gestor.eliminarLibro(isbn);
			JOptionPane.showMessageDialog(null, "Libro eliminado.");
		}
		catch (LibroNoEncontradoException e)
		{
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	private void sugerirRelacionados()
	{
		String isbn = pedirTexto("ISBN del libro base:");
		if (isbn == null)
		{
			return;
		}
		try
		{
			Libro libro = gestor.buscarLibro(isbn);
			List<Libro> sugeridos = gestor.sugerirRelacionados(libro);
			if (sugeridos.isEmpty())
			{
				JOptionPane.showMessageDialog(null, "No hay libros relacionados (mismo genero) todavia.");
				return;
			}
			StringBuilder texto = new StringBuilder("Libros relacionados con \"" + libro.getTitulo() + "\":\n");
			for (int i = 0; i < sugeridos.size(); i++)
			{
				texto.append("- ").append(sugeridos.get(i).getTitulo()).append("\n");
			}
			JOptionPane.showMessageDialog(null, texto.toString());
		}
		catch (LibroNoEncontradoException e)
		{
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	private void menuVentas()
	{
		boolean volver = false;

		while (!volver)
		{
			String[] opciones = { "Registrar venta", "Listar ventas", "Buscar venta", "Editar fecha de una venta",
					"Eliminar venta", "Volver" };
			int opcion = JOptionPane.showOptionDialog(null, "Ventas", "Ventas",
					JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, opciones, opciones[0]);
			switch (opcion)
			{
				case 0:
					registrarVenta();
					break;
				case 1:
					listarVentas();
					break;
				case 2:
					buscarVenta();
					break;
				case 3:
					editarVenta();
					break;
				case 4:
					eliminarVenta();
					break;
				default:
					volver = true;
			}
		}
	}

	private void registrarVenta()
	{
		Venta venta = new Venta(null);
		boolean agregarMas = true;
		while (agregarMas)
		{
			String isbn = JOptionPane.showInputDialog(null, "ISBN del libro a vender (vacio para cancelar):");
			if (isbn == null || isbn.trim().isEmpty())
			{
				agregarMas = false;
				continue;
			}
			Libro libro;
			try
			{
				libro = gestor.buscarLibro(isbn.trim());
			}
			catch (LibroNoEncontradoException e)
			{
				JOptionPane.showMessageDialog(null, e.getMessage());
				continue;
			}
			Integer cantidad = pedirEntero("Cantidad:");
			if (cantidad == null)
			{
				continue;
			}
			venta.agregarItem(new ItemVenta(libro, cantidad));
			int resp = JOptionPane.showConfirmDialog(null, "Â¿Agregar otro libro a la venta?",
					"Venta en curso", JOptionPane.YES_NO_OPTION);
			agregarMas = (resp == JOptionPane.YES_OPTION);
		}
		if (venta.getProductos().isEmpty())
		{
			JOptionPane.showMessageDialog(null, "Venta cancelada (sin items).");
			return;
		}

		String[] opcionesPromo = { "Ninguna", "Descuento porcentual", "2x1 (50% de descuento)" };
		int tipoPromo = JOptionPane.showOptionDialog(null, "Â¿Aplicar una promocion?", "Promocion",
				JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, opcionesPromo, opcionesPromo[0]);
		try
		{
			if (tipoPromo == 1)
			{
				Integer porcentajeIngresado = pedirEntero("Porcentaje de descuento (ej: 20 para 20%):");
				if (porcentajeIngresado == null)
				{
					porcentajeIngresado = 0;
				}

				if (porcentajeIngresado <= 0 || porcentajeIngresado > 100)
				{
					JOptionPane.showMessageDialog(null,
							"El porcentaje debe estar entre 1 y 100. Se registrara sin descuento.");
					gestor.registrarVenta(venta);
					JOptionPane.showMessageDialog(null,
							"Venta registrada con id " + venta.getId() + ". Total: $" + venta.calcularTotal());
				}
				else
				{
					double porcentaje = porcentajeIngresado / 100.0;
					Promocion promo = new PromocionPorcentaje(porcentaje);
					double totalConDescuento = gestor.registrarVenta(venta, promo);
					JOptionPane.showMessageDialog(null, "Venta registrada con id " + venta.getId()
							+ ". Total con descuento: $" + totalConDescuento);
				}
			}
			else if (tipoPromo == 2)
			{
				Promocion promo = new PromocionDosPorUno();
				double totalConDescuento = gestor.registrarVenta(venta, promo);
				JOptionPane.showMessageDialog(null, "Venta registrada con id " + venta.getId()
						+ ". Total con descuento: $" + totalConDescuento);
			}
			else
			{
				gestor.registrarVenta(venta);
				JOptionPane.showMessageDialog(null,
						"Venta registrada con id " + venta.getId() + ". Total: $" + venta.calcularTotal());
			}
		}
		catch (StockInsuficienteException e)
		{
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}
	private void listarVentas()
	{
		List<Venta> ventas = gestor.listarVentas();
		if (ventas.isEmpty())
		{
			JOptionPane.showMessageDialog(null, "No hay ventas registradas.");
			return;
		}
		StringBuilder texto = new StringBuilder();
		for (int i = 0; i < ventas.size(); i++)
		{
			Venta v = ventas.get(i);
			texto.append("Venta [").append(v.getId()).append("] - ").append(v.getFecha())
					.append(" - Total: $").append(v.calcularTotal()).append("\n");
			List<ItemVenta> items = v.getProductos();
			for (int j = 0; j < items.size(); j++)
			{
				ItemVenta item = items.get(j);
				texto.append("   - ").append(item.getLibro().getTitulo())
						.append(" x").append(item.getCantidad()).append("\n");
			}
		}
		JOptionPane.showMessageDialog(null, texto.toString());
	}
	private void buscarVenta()
	{
		String id = pedirTexto("ID de la venta a buscar:");
		if (id == null)
		{
			return;
		}
		try
		{
			Venta venta = gestor.buscarVenta(id);
			JOptionPane.showMessageDialog(null, "Venta [" + venta.getId() + "] - " + venta.getFecha()
					+ " - Total: $" + venta.calcularTotal());
		}
		catch (LibroNoEncontradoException e)
		{
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	private void editarVenta()
	{
		String id = pedirTexto("ID de la venta a editar:");
		if (id == null)
		{
			return;
		}
		String textoFecha = pedirTexto("Nueva fecha (yyyy-MM-dd):");
		if (textoFecha == null)
		{
			return;
		}
		try
		{
			LocalDate nuevaFecha = LocalDate.parse(textoFecha);
			gestor.editarVenta(id, nuevaFecha);
			JOptionPane.showMessageDialog(null, "Venta actualizada.");
		}
		catch (DateTimeParseException e)
		{
			JOptionPane.showMessageDialog(null,
					"Formato de fecha invalido. Usa el formato yyyy-MM-dd (ej: 2026-05-01).");
		}
		catch (LibroNoEncontradoException e)
		{
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	private void eliminarVenta()
	{
		String id = pedirTexto("ID de la venta a eliminar:");
		if (id == null)
		{
			return;
		}
		try
		{
			gestor.eliminarVenta(id);
			JOptionPane.showMessageDialog(null, "Venta eliminada.");
		}
		catch (LibroNoEncontradoException e)
		{
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	private String pedirTexto(String mensaje)
	{
		String texto = JOptionPane.showInputDialog(null, mensaje);
		if (texto == null)
		{
			return null;
		}
		return texto.trim();
	}

	private Integer pedirEntero(String mensaje)
	{
		String texto = JOptionPane.showInputDialog(null, mensaje);
		if (texto == null)
		{
			return null;
		}
		try
		{
			return Integer.parseInt(texto.trim());
		}
		catch (NumberFormatException e)
		{
			JOptionPane.showMessageDialog(null, "Debes ingresar un numero valido.");
			return null;
		}
	}
}
