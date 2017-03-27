/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metier.service;

/**
 *
 * @author Gheorghe
 */
public class ServiceException extends Exception {

    public ServiceException(String message) {
        super(message);
    }

    ServiceException(String message, Exception ex) {
        super(message,ex);
    }
    
}
