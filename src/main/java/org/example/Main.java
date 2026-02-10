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

    // Scanner para leer la entrada del usuario por consola
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        int option;

        // Bucle principal del programa: muestra un menú y ejecuta la opción seleccionada
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

            // Leer la opción numérica introducida por el usuario
            option = sc.nextInt();

            // Ejecutar la opción correspondiente
            switch (option) {
                case 1:
                    listarTabla(); // listar compradores o pedidos
                    break;
                case 2:
                    añadirComprador(); // solicitar datos y persistir comprador
                    break;
                case 3:
                    modificarComprador(); // cambiar datos existentes
                    break;
                case 4:
                    añadirPedido(); // solicitar datos y persistir pedido
                    break;
                case 5:
                    borrarPedidos(); // borrar todos los pedidos de un comprador
                    break;
                case 6:
                    listarPedidosComprador(); // listar pedidos de un comprador concreto
                    break;
                case 7:
                    mostrarImporteTotal(); // sumar precios*cantidad de sus pedidos
                    break;
                case 8:
                    mostrarNumeroPedidos(); // contar pedidos del comprador
                    break;
                case 0:
                    System.out.println("Saliendo del programa.");
                    break;
                default:
                    System.out.println("Opción no reconocida. Intenta de nuevo.");
            }

            System.out.println();
        } while (option != 0);

        // Cerrar el Scanner al terminar para liberar recursos
        sc.close();
    }

    // 1. Método para listar los elementos de una tabla por id
    private static void listarTabla() {
        System.out.print("¿Qué tabla quieres listar? (comprador/pedidos): ");
        String tabla = sc.next(); // leer qué tabla quiere listar el usuario

        // Abrir una sesión de Hibernate (conexión a la BD) y manejar la transacción
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            if (tabla.equalsIgnoreCase("comprador")) {
                // Consulta HQL para obtener todos los compradores
                List<CompradorClass> compradores = session.createQuery("FROM CompradorClass", CompradorClass.class).list();

                // Imprimir el nombre de cada comprador encontrado con un For-each
                for (CompradorClass comprador : compradores) {
                    System.out.println(comprador.getNOMBRE());
                }
            } else if (tabla.equalsIgnoreCase("pedidos")) {
                // Consulta HQL para obtener todos los pedidos
                List<PedidosClass> pedidos = session.createQuery("FROM PedidosClass", PedidosClass.class).list();
                // Para cada pedido, imprimimos su nombre y el nombre de su comprador asociado con un For-each
                for (PedidosClass pedido : pedidos) {
                    System.out.println(pedido.getNOMBRE()+" cuyo comprador es: " + pedido.getComprador().getNOMBRE());
                }
            } else {
                // Opción no reconocida
                System.out.println("Tabla no reconocida. Intenta de nuevo.");
            }
        } catch (Exception e) {
            // En caso de error, revertir la transacción y mostrar la excepción
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            // Cerrar la sesión (importante para liberar la conexión)
            session.close();
        }
    }

    // 2. Método para añadir un nuevo comprador
    private static void añadirComprador() {
        // Pedimos los datos por consola: nombre, dirección, teléfono, email y ciudad
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

        // Crear un objeto CompradorClass con los datos introducidos
        CompradorClass comprador = new CompradorClass(nombre, direccion, telefono, email, ciudad);

        // Abrir sesión, iniciar transacción y persistir el objeto en la BD
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.persist(comprador); // guardar el comprador
            transaction.commit(); // confirmar los cambios
            System.out.println("Comprador añadido correctamente.");
        } catch (Exception e) {
            // Si hay error, deshacer la transacción y mostrar la excepción
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // 3. Método para modificar los datos de un comprador
    private static void modificarComprador() {
        System.out.print("Introduce el ID del comprador a modificar: ");
        int id = sc.nextInt(); // identificador del comprador a modificar

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            // Buscar el comprador por su id (devuelve null si no existe)
            CompradorClass comprador = session.find(CompradorClass.class, id);
            if (comprador != null) {
                // Pedimos los nuevos datos y los asignamos al objeto recuperado
                System.out.print("Introduce el nuevo nombre del comprador: ");
                comprador.setNOMBRE(sc.next());
                System.out.print("Introduce la nueva dirección del comprador: ");
                comprador.setDIRECCION(sc.next());
                System.out.print("Introduce el nuevo teléfono del comprador: ");
                // sc.nextInt() ya devuelve un int, convertirlo no es necesario pero se mantiene la lógica
                comprador.setTELEFONO(sc.nextInt());
                System.out.print("Introduce el nuevo email del comprador: ");
                comprador.setCORREO(sc.next());
                System.out.print("Introduce la nueva ciudad del comprador: ");
                comprador.setCIUDAD(sc.next());

                // merge actualizará la entidad en la BD
                session.merge(comprador);
                transaction.commit();
                System.out.println("Comprador modificado correctamente.");
            } else {
                System.out.println("Comprador no encontrado.");
            }
        } catch (Exception e) {
            // Revertir si falla
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // 4. Método para añadir un nuevo pedido
    private static void añadirPedido() {
        // Pedimos campos del pedido: nombre, fecha, precio, cantidad, estado
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

        // Convertir la cadena de fecha a Date usando el formato dd/MM/yyyy
        try {
            fecha = new SimpleDateFormat("dd/MM/yyyy").parse(fechaStr);
        } catch (ParseException e) {
            // Si el formato es incorrecto, avisar y salir del método (no se guarda nada)
            System.out.println("Formato de fecha no válido. Intenta de nuevo.");
            return;
        }

        // Crear el pedido con los datos recogidos
        PedidosClass pedido = new PedidosClass(nombre, fecha, precio, cantidad, estado);

        // Persistir el pedido en la base de datos
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
        int id = sc.nextInt(); // id del comprador

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            // HQL para eliminar pedidos donde el campo comprador coincide con el id dado
            Query query = session.createQuery("DELETE FROM PedidosClass WHERE comprador = :compradorId");
            query.setParameter("compradorId", id);
            int result = query.executeUpdate(); // número de filas afectadas
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

        // Abrir sesión y manejar transacción
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            // HQL para recuperar pedidos con comprador = id
            Query<PedidosClass> query = session.createQuery("FROM PedidosClass WHERE comprador = :compradorId", PedidosClass.class);
            query.setParameter("compradorId", id);
            List<PedidosClass> pedidos = query.list();
            // Imprimir cada pedido encontrado con un For-each para mostrar su información
            for (PedidosClass pedido : pedidos) {
                System.out.println(pedido);
            }
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

        // Abrir sesión y manejar transacción
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            // HQL que suma (precio * cantidad) para todos los pedidos del comprador
            Query<Double> query = session.createQuery("SELECT SUM(p.PRECIO * p.CANTIDAD) FROM PedidosClass p WHERE p.comprador = :compradorId", Double.class);
            query.setParameter("compradorId", id);
            Double importeTotal = query.uniqueResult(); // puede ser null si no hay pedidos
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

        // Abrir sesión y manejar transacción
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            // HQL que cuenta los pedidos del comprador
            Query<Long> query = session.createQuery("SELECT COUNT(p) FROM PedidosClass p WHERE p.comprador = :compradorId", Long.class); // COUNT devuelve un Long
            query.setParameter("compradorId", id); // establecer el parámetro del id del comprador
            Long numeroPedidos = query.uniqueResult(); // puede ser null si no hay pedidos, pero COUNT siempre devuelve un número (0 si no hay resultados)
            System.out.println("El número de pedidos del comprador con ID " + id + " es: " + (numeroPedidos != null ? numeroPedidos : 0));
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }
}

