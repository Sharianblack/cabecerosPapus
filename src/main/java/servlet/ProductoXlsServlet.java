package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import services.ProductoServices;
import services.ProductosServicesImplement;

import modelos.Producto;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet({"/productos.xls", "/productos.html", "/productos.json"})
public class ProductoXlsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProductoServices service = new ProductosServicesImplement();
        List<Producto> productos = service.listar();

        String servletPath = req.getServletPath();
        boolean esXls = servletPath.endsWith(".xls");
        boolean esJson = servletPath.endsWith(".json");

        // Configurar el tipo de contenido según el formato
        if (esXls) {
            resp.setContentType("application/vnd.ms-excel");
            resp.setHeader("Content-Disposition", "attachment; filename=productos.xls");
        } else if (esJson) {
            resp.setContentType("application/json;charset=UTF-8");
            //quitar  comentario si quieres descargar el Json en vez de verlo desde el navegador
            //resp.setHeader("Content-Disposition", "attachment; filename=productos.json");
        } else {
            resp.setContentType("text/html;charset=UTF-8");
        }

        try (PrintWriter out = resp.getWriter()) {

            // Si es JSON, generar el formato JSON y terminar
            if (esJson) {
                generarJson(out, productos);
                return;
            }

            // Si es HTML, agregar el encabezado
            if (!esXls) {
                out.print("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<meta charset=\"utf-8\">");
                out.println("<title>Listado de Productos</title>");
                out.println("</head>");
                out.println("<body>");
                out.println("<h1>Listado de productos</h1>");
                out.println("<p><a href=\"" + req.getContextPath() + "/productos.xls\">exportar a excel</a></p>");
                out.println("<p><a href=\"" + req.getContextPath() + "/productos.json\">mostrar json</a></p>");
            }

            // Generar la tabla (para HTML y XLS)
            out.println("<table>");
            out.println("<tr>");
            out.println("<th>id</th>");
            out.println("<th>nombre</th>");
            out.println("<th>tipo</th>");
            out.println("<th>precio</th>");
            out.println("</tr>");
            productos.forEach(p -> {
                out.println("<tr>");
                out.println("<td>" + p.getIdProducto() + "</td>");
                out.println("<td>" + p.getNombre() + "</td>");
                out.println("<td>" + p.getTipo() + "</td>");
                out.println("<td>" + p.getPrecio() + "</td>");
                out.println("</tr>");
            });
            out.println("</table>");

            // Si es HTML, cerrar las etiquetas
            if (!esXls) {
                out.println("</body>");
                out.println("</html>");
            }
        }
    }

    /**
     * Método para generar la salida JSON
     */
    private void generarJson(PrintWriter out, List<Producto> productos) {
        out.println("[");
        for (int i = 0; i < productos.size(); i++) {
            Producto p = productos.get(i);
            out.println("  {");
            out.println("    \"idProducto\": " + p.getIdProducto() + ",");
            out.println("    \"nombre\": \"" + escaparJson(p.getNombre()) + "\",");
            out.println("    \"tipo\": \"" + escaparJson(p.getTipo()) + "\",");
            out.println("    \"precio\": " + p.getPrecio());
            out.print("  }");

            if (i < productos.size() - 1) {
                out.println(",");
            } else {
                out.println();
            }
        }
        out.println("]");
    }

    /**
     * Método auxiliar para escapar caracteres especiales en JSON
     */
    private String escaparJson(String texto) {
        if (texto == null) {
            return "";
        }
        return texto.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}