/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import entities.Club;
import java.sql.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
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
@Path("entities.club")
public class ClubFacadeREST extends AbstractFacade<Club> {
    private static final Logger log = Logger.getLogger(ClubFacadeREST.class.getName());

    @PersistenceContext(unitName = "NocturnaServerPU")
    private EntityManager em;

    public ClubFacadeREST() {
        super(Club.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Club entity) {
        try{
            super.create(entity);
        } catch (Exception ex) {
            log.log(Level.SEVERE, "UserRESTful service: Exception logging up .", ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Long id, Club entity) {
        try{
            super.edit(entity);
        } catch (Exception ex) {
            log.log(Level.SEVERE, "UserRESTful service: Exception logging up .", ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Long id) {
        try{
            super.remove(super.find(id));
        } catch (Exception ex) {
            log.log(Level.SEVERE, "UserRESTful service: Exception logging up .", ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Club find(@PathParam("id") Long id) {
        try{
            return super.find(id);
        } catch (Exception ex) {
            log.log(Level.SEVERE, "UserRESTful service: Exception logging up .", ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Club> findAll() {
        try{
            return super.findAll();
        } catch (Exception ex) {
            log.log(Level.SEVERE, "UserRESTful service: Exception logging up .", ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Club> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        try{
            return super.findRange(new int[]{from, to});
        } catch (Exception ex) {
            log.log(Level.SEVERE, "UserRESTful service: Exception logging up .", ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        try{
            return String.valueOf(super.count());
        } catch (Exception ex) {
            log.log(Level.SEVERE, "UserRESTful service: Exception logging up .", ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
    }
    
    @GET
    @Path("club/date/{fechaIni}/{fechafin}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Club> getClubsByEventDates(@PathParam("fechaIni") Date fechaIni,
            @PathParam("fechafin") Date fechafin) {
        List<Club> clubs=null;
        Query query;
        if (fechaIni == null || fechafin == null){
            throw new BadRequestException("Los parametros de fecha no pueden"
                    + "estar vacios");
        }
        try {
            log.log(Level.INFO,"ClubRESTful service: find clubs with events "
                    + "into dates{0}.");
            query = em.createNamedQuery("getClubsByEventDates");
            query.setParameter("fechaIni", fechaIni);
            query.setParameter("fechafin", fechafin);
            clubs = query.getResultList();
        } catch (Exception ex) {
            log.log(Level.SEVERE,
                    "ClubRESTful service: Exception finding clubs with "
                            + "events into dates{0}.",
                    ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
        return clubs;
    }
    
    @GET
    @Path("club/date/{fecha}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Club> getClubsByEventDate(@PathParam("fecha") Date fecha) {
        List<Club> clubs=null;
        if (fecha == null){
            throw new BadRequestException("El parametro de fecha no puede"
                    + "estar vacio");
        }
        try {
            log.log(Level.INFO,"ClubRESTful service: find clubs with "
                    + "events in one date{0}.");
            clubs=em.createNamedQuery("getClubsByEventDate").
                    setParameter("fecha", fecha).getResultList();
        } catch (Exception ex) {
            log.log(Level.SEVERE,
                    "ClubRESTful service: Exception finding clubs with "
                            + "events in one date{0}.",
                    ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
        return clubs;
    }
    
    @GET
    @Path("club/{idEvent}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Club getClubNameByEventId(@PathParam("idEvent") Long idEvent) {
        Club club=null;
        if (idEvent == null){
            throw new BadRequestException("El evento no puede ser nulo");
        }
        try {
            log.log(Level.INFO,"ClubRESTful service: find clubs "
                    + "with events into dates{0}.");
            club=(Club) em.createNamedQuery("getClubNameByEventId").
                    setParameter("idEvent", idEvent).getSingleResult();
        } catch (Exception ex) {
            log.log(Level.SEVERE,
                    "ClubRESTful service: Exception finding users by "
                            + "profile, {0}",
                    ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
        return club;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}
