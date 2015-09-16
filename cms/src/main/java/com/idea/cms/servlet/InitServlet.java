package com.idea.cms.servlet;


import com.idea.common.cache.core.Configuration;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;


/**
 * Servlet implementation class InitServlet
 */
public class InitServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see Servlet#init(ServletConfig)
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
//        String webRoot = getServletContext().getRealPath("");
//        System.setProperty("webroot", webRoot);
        Configuration.init(this.getClass().getClassLoader().getResource("/").getPath());
    }

}
