/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import exceptions.SignInException;
import javax.ws.rs.WebApplicationException;

/**
 *
 * @author 2dam
 */
public interface UserManager {

    public <T> T resetPassword_XML(Class<T> responseType, String userEmail) throws WebApplicationException;

    public String countREST() throws WebApplicationException;

    public void edit_XML(Object requestEntity, String id) throws WebApplicationException;

    public <T> T find_XML(Class<T> responseType, String id) throws WebApplicationException;

    public <T> T findRange_XML(Class<T> responseType, String from, String to) throws WebApplicationException;

    public void create_XML(Object requestEntity) throws WebApplicationException;

    public <T> T login_XML(Class<T> responseType, String mail, String passwd) throws WebApplicationException, SignInException;

    public void updatePasswd_XML(Object requestEntity, String newPasswd) throws WebApplicationException;

    public <T> T findAll_XML(Class<T> responseType) throws WebApplicationException;

    public void remove(String id) throws WebApplicationException;

    public void close();

}
