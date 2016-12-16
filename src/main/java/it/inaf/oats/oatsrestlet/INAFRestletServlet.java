

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.inaf.oats.oatsrestlet;

import org.apache.log4j.Logger;
import org.restlet.Component;
import org.restlet.ext.servlet.ServerServlet;

/**
 *
 * @author bertocco
 */
public class INAFRestletServlet  extends ServerServlet {

    private static Logger log = Logger.getLogger(INAFRestletServlet.class);
    
    protected Component createComponent()
    {
        log.debug("Entering in INAFRestletServlet.createComponent");
        Component result = super.createComponent();
        log.debug("Component created");
        result.getLogService().setEnabled(true);
        return result;
    }
    
}

