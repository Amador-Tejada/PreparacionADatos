package org.example;


import org.example.Entidades.CompradorClass;
import org.example.Entidades.PedidosClass;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        int option;

        do {
            System.out.println("--- MENÚ DE OPCIONES ---");
            System.out.println("1. LISTAR TABLA - Lista todos los elementos de la tabla indicada (clientes/pedidos)");
            System.out.println("2. INSERTAR COMPRADOR - Añadir un nuevo comprador");
            System.out.println("3. MODIFICAR COMPRADOR - Modificar los datos de un comprador");
            System.out.println("4. INSERTAR PEDIDO - Añadir un nuevo pedido");
            System.out.println("5. BORRAR PEDIDO -  Borrar todos los pedidos asociados a un comprador");
            System.out.println("6. LISTAR PEDIDOS DEL COMPRADOR - Lista todos los pedidos");
            System.out.println("7. IMPORTE TOTAL - Mostrar el importe total de los pedidos de un comprador");
            System.out.println("8. NUMERO PEDIDOS - Mostrar la cantidad de pedidos de un comprador ");
            System.out.println("0. - Salir del programa");
            System.out.print("Introduce una opción: ");

            option = sc.nextInt();

            switch (option) {
                case 1:
                    listarTabla();
                    break;
                case 2:
                    añadirComprador();
                    break;
                case 3:
                    modificarComprador();
                    break;
                case 4:
                    añadirPedido();
                    break;
                case 5:
                    borrarPedidos();
                    break;
                case 6:
                    listarPedidosComprador();
                    break;
                case 7:
                    mostrarImporteTotal();
                    break;
                case 8:
                    mostrarNumeroPedidos();
                    break;
                default:
                    System.out.println("Opción no reconocida. Intenta de nuevo.");
            }

            System.out.println();
        } while (option != 0);

        sc.close();
    }

    // 1. Método para listar los elementos de una tabla por id
    private static void listarTabla() {
        System.out.print("¿Qué tabla quieres listar? (comprador/pedidos): ");
        String tabla = sc.next();

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            if (tabla.equalsIgnoreCase("comprador")) {
                List<CompradorClass> compradores = session.createQuery("FROM CompradorClass", CompradorClass.class).list();

                // Con un forEach y un método de referencia para imprimir cada comprador
                for (CompradorClass comprador : compradores) {
                    System.out.println(comprador.getNOMBRE());
                }
            } else if (tabla.equalsIgnoreCase("pedidos")) {
                List<PedidosClass> pedidos = session.createQuery("FROM PedidosClass", PedidosClass.class).list();
                for (PedidosClass pedido : pedidos) {
                    System.out.println(pedido.getNOMBRE()+" cuyo comprador es: " + pedido.getComprador().getNOMBRE());
                }
            } else {
                System.out.println("Tabla no reconocida. Intenta de nuevo.");
            }
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    // 2. Método para añadir un nuevo comprador
    private static void añadirComprador() {
        // nombre, dirección, teléfono, email y ciudad
        System.out.print("Introduce el nombre del comprador: ");
        String nombre = sc.next();
        System.out.print("Introduce la dirección del comprador: ");
        String direccion = sc.next();
        System.out.print("Introduce el teléfono del comprador: ");
        int telefono = sc.nextInt();
        System.out.print("Introduce el email del comprador: ");
        String email = sc.next();
        System.out.print("Introduce la ciudad del comprador: ");
        String ciudad = sc.next();
        CompradorClass comprador = new CompradorClass(nombre, direccion, telefono, email, ciudad);

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.persist(comprador);
            transaction.commit();
            System.out.println("Comprador añadido correctamente.");
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // 3. Método para modificar los datos de un comprador
    private static void modificarComprador() {
        System.out.print("Introduce el ID del comprador a modificar: ");
        int id = sc.nextInt();

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            CompradorClass comprador = session.find(CompradorClass.class, id);
            if (comprador != null) {
                System.out.print("Introduce el nuevo nombre del comprador: ");
                comprador.setNOMBRE(sc.next());
                System.out.print("Introduce la nueva dirección del comprador: ");
                comprador.setDIRECCION(sc.next());
                System.out.print("Introduce el nuevo teléfono del comprador: ");
                comprador.setTELEFONO(Integer.parseInt(String.valueOf(sc.nextInt())));
                System.out.print("Introduce el nuevo email del comprador: ");
                comprador.setCORREO(sc.next());
                System.out.print("Introduce la nueva ciudad del comprador: ");
                comprador.setCIUDAD(sc.next());

                session.merge(comprador);
                transaction.commit();
                System.out.println("Comprador modificado correctamente.");
            } else {
                System.out.println("Comprador no encontrado.");
            }
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // 4. Método para añadir un nuevo pedido
    private static void añadirPedido() {
        // NOMBRE, FECHA, PRECIO, CANTIDAD, ESTADO
        System.out.print("Introduce el nombre del pedido: ");
        String nombre = sc.next();
        System.out.print("Introduce la fecha del pedido (dd/MM/yyyy): ");
        String fechaStr = sc.next();
        Date fecha = null;
        System.out.println("Introduce el precio del pedido: ");
        double precio = sc.nextDouble();
        System.out.println("Introduce la cantidad del pedido: ");
        int cantidad = sc.nextInt();
        System.out.println("Introduce el estado del pedido: ");
        String estado = sc.next();

        // Convertir la cadena de fecha a un objeto Date
        try {
            fecha = new SimpleDateFormat("dd/MM/yyyy").parse(fechaStr);
        } catch (ParseException e) {
            System.out.println("Formato de fecha no válido. Intenta de nuevo.");
            return;
        }

        // Crear un nuevo objeto PedidosClass con los datos introducidos
        PedidosClass pedido = new PedidosClass(nombre, fecha, precio, cantidad, estado);

        // Guardar el nuevo pedido en la base de datos utilizando Hibernate
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.persist(pedido);
            transaction.commit();
            System.out.println("Pedido añadido correctamente.");
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // 5. Método para borrar todos los pedidos asociados a un comprador
    private static void borrarPedidos() {
        System.out.print("Introduce el ID del comprador cuyos pedidos quieres borrar: ");
        int id = sc.nextInt();

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Query query = session.createQuery("DELETE FROM PedidosClass WHERE comprador = :compradorId");
            query.setParameter("compradorId", id);
            int result = query.executeUpdate();
            transaction.commit();
            System.out.println(result + " pedidos borrados correctamente.");
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // 6. Método para listar todos los pedidos de un comprador
    private static void listarPedidosComprador() {
        System.out.print("Introduce el ID del comprador cuyos pedidos quieres listar: ");
        int id = sc.nextInt();

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Query<PedidosClass> query = session.createQuery("FROM PedidosClass WHERE comprador = :compradorId", PedidosClass.class);
            query.setParameter("compradorId", id);
            List<PedidosClass> pedidos = query.list();
            pedidos.forEach(System.out::println);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // 7. Método para mostrar el importe total de los pedidos de un comprador
    private static void mostrarImporteTotal() {
        System.out.print("Introduce el ID del comprador para calcular el importe total de sus pedidos: ");
        int id = sc.nextInt();

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Query<Double> query = session.createQuery("SELECT SUM(p.PRECIO * p.CANTIDAD) FROM PedidosClass p WHERE p.comprador = :compradorId", Double.class);
            query.setParameter("compradorId", id);
            Double importeTotal = query.uniqueResult();
            System.out.println("El importe total de los pedidos del comprador con ID " + id + " es: " + (importeTotal != null ? importeTotal : 0.0));
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // 8. Método para mostrar la cantidad de pedidos de un comprador
    private static void mostrarNumeroPedidos() {
        System.out.print("Introduce el ID del comprador para calcular el número de sus pedidos: ");
        int id = sc.nextInt();

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Query<Long> query = session.createQuery("SELECT COUNT(p) FROM PedidosClass p WHERE p.comprador = :compradorId", Long.class);
            query.setParameter("compradorId", id);
            Long numeroPedidos = query.uniqueResult();
            System.out.println("El número de pedidos del comprador con ID " + id + " es: " + (numeroPedidos != null ? numeroPedidos : 0));
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }
}
