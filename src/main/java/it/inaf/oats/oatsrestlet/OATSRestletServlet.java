/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.inaf.oats.oatsrestlet;

import java.util.Enumeration;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import org.apache.log4j.Logger;
import org.restlet.Application;
import org.restlet.Context;
import org.restlet.engine.Engine;
import org.restlet.routing.Router;

/**
 *
 * @author bertocco
 */

//public class INAFRestletServlet  extends ServerServlet {
public class OATSRestletServlet  extends HttpServlet {
    
    private static Logger log = Logger.getLogger(OATSRestletServlet.class);
    
    // create Component (as ever for Restlet)
    //protected Component comp = new Component();
    protected String applicationClassName = null;
        
    @Override
    public void init(ServletConfig config) throws ServletException
    {
    
        super.init(config);
        Context appContext = new Context();
        Context childAppContext = appContext.createChildContext();
        try {    
            applicationClassName = config.getInitParameter("org.restlet.application");
            
            if (applicationClassName == null)
               throw new ServletException("org.restlet.application parameter null");
            
            Class targetClass = this.loadClass(applicationClassName);
            Application application = null;
            try {
                application = (Application)targetClass.getConstructor(new Class[0]).newInstance(new Object[0]);
                application.setContext(childAppContext);
            } catch (NoSuchMethodException e) {
                this.log("[SB:] ServerServlet couldn't invoke the constructor of the target class. Please check this class has a constructor without parameter. The constructor with a parameter of type Context will be used instead.");
                application = (Application) targetClass.getConstructor(Context.class).newInstance(new Object[]{childAppContext});
            }

            // Copy all the servlet parameters into the context
            String initParam;

            // Copy all the Web component initialization parameters
            final javax.servlet.ServletConfig servletConfig = getServletConfig();
            for (final Enumeration<String> enum1 = servletConfig
                    .getInitParameterNames(); enum1.hasMoreElements();) {
                initParam = enum1.nextElement();
                appContext.getParameters().add(initParam,
                        servletConfig.getInitParameter(initParam));
            }

            // Copy all the Web Application initialization parameters
            for (final Enumeration<String> enum1 = getServletContext()
                    .getInitParameterNames(); enum1.hasMoreElements();) {
                initParam = enum1.nextElement();
                childAppContext.getParameters().add(initParam,
                        getServletContext().getInitParameter(initParam));
            }
            
            // attach Application
            application.setContext(childAppContext);
            
            //Server server = comp.getServers().add(Protocol.HTTP, 8080);
            //server = comp.getServers().add(Protocol.HTTPS, 443);
            
            Router router = new Router(childAppContext);
            // Defines a route for the resource "list of items"
            router.attach("/", targetClass);
            
            application.setInboundRoot(router);
            
            // Attach the application to the component and start it
            //comp.getDefaultHost().attach(application);
            //comp.getLogService().setEnabled(true);
     
            //comp.start();
            
        } catch (ClassNotFoundException ex) {
            
                log.fatal("Unable to get class " + applicationClassName, ex);

        } catch (Exception ex) {
            
            log.fatal("ERROR starting application " + applicationClassName, ex);
            
        } 
        
    }
/*   
    @Override
    public void destroy() {
        
        try {
            comp.stop();
        } catch (Exception ex) {
            log.fatal("ERROR stopping application " + applicationClassName, ex);
        }
        
    }
*/       
    protected Class<?> loadClass(String className) throws ClassNotFoundException {
        return Engine.loadClass((String)className);
    }
    
}