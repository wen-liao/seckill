package com.ds.seckill.util;

import javax.servlet.http.HttpSession;

public class HttpSessionUtil {

    public static boolean checkLoggedIn(HttpSession httpSession, String username, String role){
        String usernameAttr = (String) httpSession.getAttribute("username");
        String roleAttr = (String) httpSession.getAttribute("role");
        return usernameAttr != null && usernameAttr.equals(username) && roleAttr != null && roleAttr.equals(role);
    }

    public static boolean isConsumer(HttpSession httpSession){
        return "consumer".equals(httpSession.getAttribute("role"));
    }

    public static boolean isLoggedIn(HttpSession httpSession){
        return httpSession.getAttribute("name") != null;
    }

    public static void logIn(HttpSession httpSession, String name, String role, int id){
        httpSession.setAttribute("name", name);
        httpSession.setAttribute("role", role);
        httpSession.setAttribute("id", id);
    }

    public static void logOut(HttpSession httpSession){
        httpSession.removeAttribute("name");
        httpSession.removeAttribute("role");
        httpSession.removeAttribute("id");
    }
    
}
