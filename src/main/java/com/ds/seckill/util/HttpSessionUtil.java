package com.ds.seckill.util;

import org.slf4j.Logger;

import javax.servlet.http.HttpSession;

public class HttpSessionUtil {

    public static boolean checkSignedIn(HttpSession httpSession, String name, String role, Logger logger){
        logger.info("HttpSessionUtil.checkSignedIn()");
        logger.info("name: {}", name);
        logger.info("role: {}", role);

        String nameAttr = (String) httpSession.getAttribute("name");
        String roleAttr = (String) httpSession.getAttribute("role");
        logger.info("nameAttr: {}", nameAttr);
        logger.info("roleAttr: {}", roleAttr);

        return nameAttr != null && nameAttr.equals(name) && roleAttr != null && roleAttr.equals(role);
    }

    public static boolean isConsumer(HttpSession httpSession, Logger logger){
        logger.info("HttpSessionUtil.isConsumer()");

        String roleAttr = (String) httpSession.getAttribute("role");
        logger.info("roleAttr: {}", roleAttr);

        return "consumer".equals(httpSession.getAttribute("role"));
    }

    public static boolean isSignedIn(HttpSession httpSession, Logger logger){
        logger.info("HttpSessionUtil.isSignedIn()");

        return httpSession.getAttribute("name") != null;
    }

    public static void signIn(HttpSession httpSession, String name, String role, int id, Logger logger){
        logger.info("HttpSessionUtil.signIn()");

        httpSession.setAttribute("name", name);
        httpSession.setAttribute("role", role);
        httpSession.setAttribute("id", id);
    }

    public static void signOut(HttpSession httpSession, Logger logger){
        logger.info("HttpSessionUtil.signOut()");

        httpSession.removeAttribute("name");
        httpSession.removeAttribute("role");
        httpSession.removeAttribute("id");
    }
    
}
