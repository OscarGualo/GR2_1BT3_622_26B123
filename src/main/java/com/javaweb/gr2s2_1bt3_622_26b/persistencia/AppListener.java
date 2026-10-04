package com.javaweb.gr2s2_1bt3_622_26b.persistencia;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class AppListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        JPAUtil.getEmf();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        JPAUtil.cerrar();
    }
}
