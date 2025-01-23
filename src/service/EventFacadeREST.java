/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import entities.Artist;
import entities.Event;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javassist.NotFoundException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NamedQuery;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author 2dam
 */
@Stateless
@Path("entities.event")
public class EventFacadeREST extends AbstractFacade<Event> {

    @PersistenceContext(unitName = "NocturnaServerPU")
    private EntityManager em;
     private static final Logger log = Logger.getLogger(ArtistFacadeREST.class.getName());
     
    public EventFacadeREST() {
        super(Event.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Event entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Long id, Event entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Long id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Event find(@PathParam("id") Long id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Event> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Event> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }
    
    
    @GET
    @Path("findEventsByArtist/{idArtist}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Event> findByArtist(@PathParam("idArtist") Long idArtist) {
        List<Event> events=null;
        Query query;
        if(idArtist == null){
              throw new BadRequestException("Los parametros no pueden estar vacios");  
        }
        try {
            log.log(Level.INFO,"UserRESTful service: find users by event {0}.", idArtist);         
            query=em.createNamedQuery("findEventsByArtist");
            query.setParameter("idArtist", idArtist);
            query.setParameter("fecha", Date.valueOf(LocalDate.now()));
          
            events=query.getResultList();
        }catch (javax.ws.rs.NotFoundException e) {
            log.log(Level.INFO, "EventRESTful service: NotFoundException reading artist/artistsNotByEvent/{0}", idArtist);
            throw new javax.ws.rs.NotFoundException(e);
        } catch (Exception ex) {
            log.log(Level.SEVERE,
                    "ArtistRESTful service: Exception reading users by profile, {0}",
                    ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
        return events;
    }
     
    @GET
    @Path("findEventsByDate/{fecha}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Event> findByDate(@PathParam("fecha") Date  fechalocal) {
        List<Event> events=null;
        if(fechalocal == null){
              throw new BadRequestException("Los parametros no pueden estar vacios");  
        }
        try {
            log.log(Level.INFO,"UserRESTful service: find users by event {0}.", fechalocal);
            events=em.createNamedQuery("findEventsByDate").
                    setParameter("fecha", fechalocal).getResultList();
        }catch (javax.ws.rs.NotFoundException e) {
            log.log(Level.INFO, "EventRESTful service: NotFoundException reading artist/artistsNotByEvent/{0}", fechalocal);
            throw new javax.ws.rs.NotFoundException(e);
        }  catch (Exception ex) {
            log.log(Level.SEVERE,
                    "ArtistRESTful service: Exception reading users by profile, {0}",
                    ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
        return events;
    }
    
    @GET
    @Path("findEventsByDates/{fechaIni}/{fechaFin}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Event> findByDates(@PathParam("fechaIni") Date fechaIni, @PathParam("fechaFin") Date fechaFin) {
        List<Event> events=null;
        Query query;
        if(fechaIni == null || fechaFin == null){
            log.log(Level.INFO,"UserRESTful service: find users by event {0}.", fechaIni);
              throw new BadRequestException("Los parametros no pueden estar vacios");  
        }
        try {
            log.log(Level.INFO,"UserRESTful service: find users by event {0}.", fechaIni);
            query=em.createNamedQuery("findEventsByDates");
            query.setParameter("fechaIni", fechaIni);
            query.setParameter("fechaFin", fechaFin);
            events=query.getResultList();
        }catch (javax.ws.rs.NotFoundException e) {
            log.log(Level.INFO, "EventRESTful service: NotFoundException reading artist/artistsNotByEvent/{0}", fechaIni);
            throw new javax.ws.rs.NotFoundException(e);
        }  catch (Exception ex) {
            log.log(Level.SEVERE,
                    "ArtistRESTful service: Exception reading users by profile, {0}",
                    ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
        return events;
    }
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}
