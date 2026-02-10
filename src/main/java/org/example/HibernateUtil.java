package org.example;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    // Crear la SessionFactory de Hibernate de forma estática
    private static final SessionFactory sessionFactory;

    // Bloque estático para inicializar la SessionFactory
    static {
        try {
            // Carga la configuración desde hibernate.cfg.xml
            sessionFactory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Fallo en la creación de SessionFactory." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    // Método para obtener la SessionFactory
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    // Método para cerrar la SessionFactory
    public static void shutdown() {
        // Cierra cachés y pools de conexión
        getSessionFactory().close();
    }
}
