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
    private static final Logger log = Logger.getLogger(ArtistFacadeREST.class.getName());

    @PersistenceContext(unitName = "NocturnaServerPU")
    private EntityManager em;

    public ClubFacadeREST() {
        super(Club.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Club entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Long id, Club entity) {
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
    public Club find(@PathParam("id") Long id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Club> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Club> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }
    
    @GET
    @Path("club/date/{fechaIni}/{fechafin}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Club> getClubsByEventDates(@PathParam("fechaIni") Date fechaIni,
            @PathParam("fechafin") Date fechafin) {
        List<Club> clubs=null;
        try {
            log.log(Level.INFO,"ClubRESTful service: find clubs with events "
                    + "into dates{0}.");
            clubs=em.createNamedQuery("getClubsByEventDates").
                    setParameter("fechaIni", fechaIni).
                    setParameter("fechafin", fechafin).getResultList();
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
    public List<Club> getClubNameByEventId(@PathParam("idEvent") Long idEvent) {
        List<Club> clubs=null;
        try {
            log.log(Level.INFO,"ClubRESTful service: find clubs "
                    + "with events into dates{0}.");
            clubs=em.createNamedQuery("getClubNameByEventId").
                    setParameter("idEvent", idEvent).getResultList();
        } catch (Exception ex) {
            log.log(Level.SEVERE,
                    "ClubRESTful service: Exception finding users by "
                            + "profile, {0}",
                    ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
        return clubs;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}
