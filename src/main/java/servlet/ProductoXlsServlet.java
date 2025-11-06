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

@WebServlet("/producto.xls")

public class ProductoXlsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProductoServices service = new ProductosServicesImplement();
        List<Producto> productos = service.listar();
        resp.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = resp.getWriter()) {
            out.println("<!DOCTYPE html>");

            out.println("<html>");

            out.println("<head>");
            out.println("<title> Listado Productos </title>");
            out.println("</head>");

            out.println("<body>");
            out.println("<h1> Listado Productos </h1>");
            out.println("<table>");
            out.println("<thead>");
            out.println("<tr>");
            out.println("<th>ID</th>");
            out.println("<th>NOMBRE</th>");
            out.println("<th>TIPO</th>");
            out.println("<th>PRECIO</th>");
            out.println("</tr>");
            productos.forEach(p ->{
            out.println("<tr>");
            out.print("<td>" + p.getIdProducto() + "/<td>");
                out.print("<td>" + p.getNombre() + "/<td>");
                out.print("<td>" + p.getTipo() + "/<td>");
                out.print("<td>" + p.getPrecio() + "/<td>");
                out.println("/<tr>");
            });
            out.println("</thead>");
            out.println("<tbody>");

            out.println("</body>");

            out.println("</html>");
        }
    }
}
