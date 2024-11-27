package pe.edu.upeu.parisfx.servicio;

import org.springframework.stereotype.Service;
import pe.edu.upeu.parisfx.dto.MenuMenuItenTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service
public class MenuMenuItemDao implements MenuMenuItenDaoI{

    @Override
    public List<MenuMenuItenTO> listaAccesos(String perfil, Properties idioma) {
        List<MenuMenuItenTO> lista = new ArrayList<>();
        lista.add(new MenuMenuItenTO(idioma.getProperty("menu.nombre.archivo"), "", "mifile"));
        lista.add(new MenuMenuItenTO(idioma.getProperty("menu.nombre.archivo"), "Salir", "misalir"));

        lista.add(new MenuMenuItenTO("Edit", "Cortar", "micut"));
        lista.add(new MenuMenuItenTO("Edit", "copiar", "micopy"));
        lista.add(new MenuMenuItenTO("Edit", "pegar", "mipaste"));
        lista.add(new MenuMenuItenTO("Edit", idioma.getProperty("menuitem.nombre.postulante"), "miselectall"));

        lista.add(new MenuMenuItenTO("Producto", "Reg. Producto", "miregproduct"));
        lista.add(new MenuMenuItenTO("Producto", "crear cuenta", "crearcuenta"));
        lista.add(new MenuMenuItenTO("Producto", "Auto Complete", "miautcomp"));

        lista.add(new MenuMenuItenTO("Principal", "Cliente", "cliente"));
        lista.add(new MenuMenuItenTO("Principal", "Reg. Venta", "miventa"));
        lista.add(new MenuMenuItenTO("Principal", "Almacen", "miAlmacen"));
        lista.add(new MenuMenuItenTO("Principal", "Proveedores", "miproveedores"));

        List<MenuMenuItenTO> accesoReal = new ArrayList<>();
        switch (perfil) {
            case "Administrador":
                accesoReal.add(lista.get(0));
                accesoReal.add(lista.get(1));
                accesoReal.add(lista.get(2));
                accesoReal.add(lista.get(3));
                accesoReal.add(lista.get(4));
                accesoReal.add(lista.get(5));
                accesoReal.add(lista.get(6));
                accesoReal.add(lista.get(7));
                accesoReal.add(lista.get(8));
                break;
            case "Root":
                accesoReal = lista;
                break;
            case "Reporter":
                accesoReal.add(lista.get(0));
                accesoReal.add(lista.get(5));
                accesoReal.add(lista.get(6));
                break;
            default:
                throw new AssertionError();
        }
        return accesoReal;
    }

}
