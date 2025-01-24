/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import exceptions.ReadException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import model.Event;

/**
 *
 * @author 2dam
 */
public interface EventManager {
    
    public <T> T findByDate_XML(Class<T> responseType, String fecha) throws WebApplicationException;

    public <T> T findByDate_JSON(Class<T> responseType, String fecha) throws WebApplicationException;

    public <T> T findByArtist_XML(Class<T> responseType, String idArtist) throws WebApplicationException, ReadException;

    public <T> T findByArtist_JSON(Class<T> responseType, String idArtist) throws WebApplicationException;

    public String countREST() throws WebApplicationException;

    public void edit_XML(Object requestEntity, String id) throws WebApplicationException;

    public void edit_JSON(Object requestEntity, String id) throws WebApplicationException ;

    public <T> T find_XML(Class<T> responseType, String id) throws WebApplicationException;

    public <T> T find_JSON(Class<T> responseType, String id) throws WebApplicationException;

    public <T> T findRange_XML(Class<T> responseType, String from, String to) throws WebApplicationException;

    public <T> T findRange_JSON(Class<T> responseType, String from, String to) throws WebApplicationException;

    public <T> T findByDates_XML(Class<T> responseType, String de_fecha, String hasta_fecha) throws WebApplicationException;

    public <T> T findByDates_JSON(Class<T> responseType, String de_fecha, String hasta_fecha) throws WebApplicationException;

    public void create_XML(Object requestEntity) throws WebApplicationException;

    public void create_JSON(Object requestEntity) throws WebApplicationException;

    public <T> T findAll_XML(Class <T> responseType) throws WebApplicationException;

    public <T> T findAll_JSON(Class<T> responseType) throws WebApplicationException;

    public void remove(String id) throws WebApplicationException;

    public void close();
        
}
