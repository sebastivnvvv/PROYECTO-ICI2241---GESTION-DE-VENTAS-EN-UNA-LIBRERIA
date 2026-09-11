import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class GestorArchivosCSV 
{
    private static final String ARCHIVO_AUTORES = "autores.csv";
    private static final String ARCHIVO_LIBROS = "libros.csv";
    private static final String ARCHIVO_VENTAS = "ventas.csv";
    private static final String SEPARADOR = ";";
    private static final String SEPARADOR_ITEMS = ",";
    private static final String SEPARADOR_ITEM = ":";

    // Carga los datos desde los archivos CSV hacia el GestorLibreria
    // si los archivos no existen, crear datos iniciales
    
    public static void cargarDatos(GestorLibreria gestor) 
    {
        File fileAutores = new File(ARCHIVO_AUTORES);
        File fileLibros = new File(ARCHIVO_LIBROS);

        if (!fileAutores.exists() || !fileLibros.exists()) 
        {
            System.out.println("No se encontraron archivos CSV previos. Se usaran datos iniciales.");
            return;
        }

        // Cargar Autores
        
        try (BufferedReader br = new BufferedReader(new FileReader(fileAutores))) 
        {
            String linea;
            while ((linea = br.readLine()) != null) 
            {
                linea = linea.trim();
                if (linea.isEmpty()) continue;

                String[] partes = linea.split(SEPARADOR);
                if (partes.length >= 2) 
                {
                    String id = partes[0].trim();
                    String nombre = partes[1].trim();
                    gestor.agregarAutor(new Autor(id, nombre));
                }
            }
            System.out.println("Autores cargados exitosamente desde CSV.");
        } 
        catch (IOException e) 
        {
            System.out.println("Error al leer el archivo de autores: " + e.getMessage());
        }

        // Cargar Libros asociados a cada autor
        
        try (BufferedReader br = new BufferedReader(new FileReader(fileLibros))) 
        {
            String linea;
            while ((linea = br.readLine()) != null) 
            {
                linea = linea.trim();
                if (linea.isEmpty()) continue;

                String[] partes = linea.split(SEPARADOR);
                if (partes.length >= 6) 
                {
                    String autorId = partes[0].trim();
                    String isbn = partes[1].trim();
                    String titulo = partes[2].trim();
                    String genero = partes[3].trim();
                    int precio = Integer.parseInt(partes[4].trim());
                    int stock = Integer.parseInt(partes[5].trim());

                    Libro libro = new Libro(titulo, genero, isbn, precio, stock);
                    try 
                    {
                        gestor.agregarLibro(autorId, libro);
                    } 
                    catch (LibroNoEncontradoException e) 
                    {
                        System.out.println("No se pudo asociar el libro " + titulo + " al autor ID " + autorId);
                    }
                }
            }
            System.out.println("Libros cargados exitosamente desde CSV.");
        } 
        catch (IOException e) 
        {
            System.out.println("Error al leer el archivo de libros: " + e.getMessage());
        } 
        catch (NumberFormatException e) 
        {
            System.out.println("Error en formato numerico al cargar libros: " + e.getMessage());
        }

        // Cargar Ventas
        
        File fileVentas = new File(ARCHIVO_VENTAS);

        if (!fileVentas.exists())
        {
            System.out.println("No se encontro un archivo de ventas previo. Se parte sin ventas.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(fileVentas)))
        {
            String linea;
            while ((linea = br.readLine()) != null)
            {
                linea = linea.trim();
                if (linea.isEmpty()) continue;

                String[] partes = linea.split(SEPARADOR, 4);
                if (partes.length < 4) continue;

                String id = partes[0].trim();
                String fechaTexto = partes[1].trim();
                String itemsTexto = partes[2].trim();
                double descuento = Double.parseDouble(partes[3].trim());

                Venta venta = new Venta(id);
                venta.setFecha(LocalDate.parse(fechaTexto));
                venta.setDescuentoAplicado(descuento);

                String[] itemsSeparados = itemsTexto.split(SEPARADOR_ITEMS);
                for (int i = 0; i < itemsSeparados.length; i++)
                {
                    String itemTexto = itemsSeparados[i].trim();
                    if (itemTexto.isEmpty()) continue;

                    String[] datosItem = itemTexto.split(SEPARADOR_ITEM);
                    if (datosItem.length < 4) continue;

                    String isbn = datosItem[0].trim();
                    String titulo = datosItem[1].trim();
                    int cantidad = Integer.parseInt(datosItem[2].trim());
                    int precioHist = Integer.parseInt(datosItem[3].trim());

                    try
                    {
                        Libro libro = gestor.buscarLibro(isbn);
                        ItemVenta nuevoItem = new ItemVenta(libro, cantidad);
                        nuevoItem.setPrecioUnitario(precioHist);
                        venta.agregarItem(nuevoItem);
                    }
                    catch (LibroNoEncontradoException e)
                    {
                        // Si el libro ya fue eliminado creamos el item historico
                        ItemVenta itemHistorico = new ItemVenta(isbn, titulo, cantidad, precioHist);
                        venta.agregarItem(itemHistorico);
                    }
                }

                gestor.agregarVentaCargada(venta);
            }
            System.out.println("Ventas cargadas exitosamente desde CSV.");
        }
        catch (IOException e)
        {
            System.out.println("Error al leer el archivo de ventas: " + e.getMessage());
        }
        catch (NumberFormatException e)
        {
            System.out.println("Error en formato numerico al cargar ventas: " + e.getMessage());
        }
    }

    
    
    
    
    // Guarda los autores, los libros y las ventas en sus respectivos archivos CSV
    // sobrescribe los archivos con el estado actual de los datos
    
    public static void guardarDatos(GestorLibreria gestor) 
    {
        List<Autor> listaAutores = gestor.listarAutores();

        // Guardar Autores
        
        try (BufferedWriter bwAutores = new BufferedWriter(new FileWriter(ARCHIVO_AUTORES))) 
        {
            for (int i = 0; i < listaAutores.size(); i++) 
            {
                Autor autor = listaAutores.get(i);
                bwAutores.write(autor.getId() + SEPARADOR + autor.getNombre());
                bwAutores.newLine();
            }
            System.out.println("Autores guardados exitosamente en " + ARCHIVO_AUTORES);
        } 
        catch (IOException e) 
        {
            System.out.println("Error al guardar autores en CSV: " + e.getMessage());
        }

        // Guardar Libros
        
        try (BufferedWriter bwLibros = new BufferedWriter(new FileWriter(ARCHIVO_LIBROS))) 
        {
            for (int i = 0; i < listaAutores.size(); i++) 
            {
                Autor autor = listaAutores.get(i);
                List<Libro> libros = autor.getLibrosPublicados();

                for (int j = 0; j < libros.size(); j++) 
                {
                    Libro l = libros.get(j);
                    // Formato autorId;isbn;titulo;genero;precio;stock
                    
                    String fila = autor.getId() + SEPARADOR + l.getIsbn() + SEPARADOR + l.getTitulo() + SEPARADOR + l.getGenero() + SEPARADOR + l.getPrecio() + SEPARADOR + l.getStock();
                    bwLibros.write(fila);
                    bwLibros.newLine();
                }
            }
            System.out.println("Libros guardados exitosamente en " + ARCHIVO_LIBROS);
        } 
        catch (IOException e) 
        {
            System.out.println("Error al guardar libros en CSV: " + e.getMessage());
        }

        // Guardar Ventas
        
        try (BufferedWriter bwVentas = new BufferedWriter(new FileWriter(ARCHIVO_VENTAS)))
        {
            List<Venta> listaVentas = gestor.listarVentas();

            for (int i = 0; i < listaVentas.size(); i++)
            {
                Venta venta = listaVentas.get(i);
                List<ItemVenta> items = venta.getProductos();

                StringBuilder itemsTexto = new StringBuilder();
                for (int j = 0; j < items.size(); j++)
                {
                    ItemVenta item = items.get(j);
                    // Usamos los métodos seguros para no depender del objeto Libro
                    itemsTexto.append(item.getIsbnSeguro());
                    itemsTexto.append(SEPARADOR_ITEM);
                    itemsTexto.append(item.getTituloSeguro());
                    itemsTexto.append(SEPARADOR_ITEM);
                    itemsTexto.append(item.getCantidad());
                    itemsTexto.append(SEPARADOR_ITEM);
                    itemsTexto.append(item.getPrecioUnitario());

                    if (j < items.size() - 1)
                    {
                        itemsTexto.append(SEPARADOR_ITEMS);
                    }
                }
                // Formato id;fecha;isbn1:cantidad1,isbn2:cantidad2
                
                String fila = venta.getId() + SEPARADOR + venta.getFecha().toString() + SEPARADOR + itemsTexto.toString() + SEPARADOR + venta.getDescuentoAplicado();
                bwVentas.write(fila);
                bwVentas.newLine();
            }
            System.out.println("Ventas guardadas exitosamente en " + ARCHIVO_VENTAS);
        }
        catch (IOException e)
        {
            System.out.println("Error al guardar ventas en CSV: " + e.getMessage());
        }
    }

    // Verifica si existen archivos de datos previamente guardados
    
    public static boolean existenDatosGuardados() 
    {
        File f1 = new File(ARCHIVO_AUTORES);
        File f2 = new File(ARCHIVO_LIBROS);
        return f1.exists() && f2.exists();
    }
}