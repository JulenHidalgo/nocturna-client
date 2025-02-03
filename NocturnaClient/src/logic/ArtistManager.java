/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import exceptions.CreateException;
import exceptions.InternalServerErrorException;
import exceptions.ReadException;
import javax.ws.rs.WebApplicationException;

/**
 *
 * @author 2dam
 */
public interface ArtistManager {

    public String countREST() throws WebApplicationException;

    public void edit_XML(Object requestEntity, String id) throws WebApplicationException;

    public <T> T find_XML(Class<T> responseType, String id) throws WebApplicationException;

    public <T> T findRange_XML(Class<T> responseType, String from, String to) throws WebApplicationException;

    public void create_XML(Object requestEntity) throws InternalServerErrorException;

    public <T> T findAll_XML(Class<T> responseType) throws WebApplicationException;

    public <T> T findNotByEvent_XML(Class<T> responseType, String idEvent) throws ReadException, InternalServerErrorException;

    public void remove(String id) throws InternalServerErrorException;

    public <T> T findByEvent_XML(Class<T> responseType, String idEvent) throws ReadException, InternalServerErrorException;

    public void close();

}
