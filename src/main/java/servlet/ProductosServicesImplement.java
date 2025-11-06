package servlet;

import modelos.Producto;
import services.ProductoServices;

import java.util.List;

//Debemos implementar la plantilla de ProductoServices
public class ProductosServicesImplement implements ProductoServices {
    @Override
    public List<Producto> listar() {
        return List.of();
    }
}
