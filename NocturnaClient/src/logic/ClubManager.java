/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import exceptions.InternalServerErrorException;
import exceptions.ReadException;
import javax.ws.rs.WebApplicationException;

/**
 *
 * @author 2dam
 */
public interface ClubManager {

    public <T> T getClubsByEventDate_XML(Class<T> responseType, String fecha) throws InternalServerErrorException, ReadException;

    public <T> T getClubsByEventDate_JSON(Class<T> responseType, String fecha) throws WebApplicationException;

    public String countREST() throws WebApplicationException;

    public void edit_XML(Object requestEntity, String id) throws WebApplicationException;

    public void edit_JSON(Object requestEntity, String id) throws WebApplicationException;

    public <T> T getClubNameByEventId_XML(Class<T> responseType, String idEvent) throws InternalServerErrorException, ReadException;

    public <T> T getClubNameByEventId_JSON(Class<T> responseType, String idEvent) throws WebApplicationException;

    public <T> T find_XML(Class<T> responseType, String id) throws WebApplicationException;

    public <T> T find_JSON(Class<T> responseType, String id) throws WebApplicationException;

    public <T> T findRange_XML(Class<T> responseType, String from, String to) throws WebApplicationException;

    public <T> T findRange_JSON(Class<T> responseType, String from, String to) throws WebApplicationException;

    public void create_XML(Object requestEntity) throws WebApplicationException;

    public void create_JSON(Object requestEntity) throws WebApplicationException;

    public <T> T findAll_XML(Class<T> responseType) throws WebApplicationException;

    public <T> T findAll_JSON(Class<T> responseType) throws WebApplicationException;

    public void remove(String id) throws WebApplicationException;

    public <T> T getClubsByEventDates_XML(Class<T> responseType, String fechaIni, String fechafin) throws InternalServerErrorException, ReadException;

    public <T> T getClubsByEventDates_JSON(Class<T> responseType, String fechaIni, String fechafin) throws WebApplicationException;

    public void close();
}
