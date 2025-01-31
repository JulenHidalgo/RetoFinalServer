/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import entities.Artist;
import java.util.List;
import java.util.logging.Level;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.logging.Logger;
import javax.persistence.NoResultException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;

/**
 *
 * @author 2dam
 */
@Stateless
@Path("entities.artist")
public class ArtistFacadeREST extends AbstractFacade<Artist> {

    @PersistenceContext(unitName = "NocturnaServerPU")
    private EntityManager em;
    private static final Logger log = Logger.getLogger(ArtistFacadeREST.class.getName());

    public ArtistFacadeREST() {
        super(Artist.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Artist entity) {
        try {
            super.create(entity);
        } catch (Exception ex) {
            log.log(Level.SEVERE, "UserRESTful service: Exception logging up .", ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Long id, Artist entity) {
        try {
            super.edit(entity);
        } catch (Exception ex) {
            log.log(Level.SEVERE, "UserRESTful service: Exception logging up .", ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Long id) {
        try {
            em.createNativeQuery("DELETE FROM nocturna.artist_event WHERE artist_idArtist = :artistId").setParameter("artistId", id).executeUpdate();
            super.remove(super.find(id));
        } catch (NoResultException ex) {
            log.log(Level.INFO, "The Artist tried to delete is not on the DB, Artist {0}", id);
            throw new NotFoundException(ex.getMessage());
        } catch (Exception ex) {
            log.log(Level.SEVERE, "UserRESTful service: Exception logging up .", ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Artist find(@PathParam("id") Long id) {
        try {
            return super.find(id);
        } catch (NoResultException e) {
            log.log(Level.INFO, "There is not an Artist related no the id {0}", id);
            throw new NotFoundException(e.getMessage());
        } catch (Exception ex) {
            log.log(Level.SEVERE,
                    "An exception ocurred while getting the Artists related to an id",
                    ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Artist> findAll() {
        try {
            return super.findAll();
        } catch (NoResultException e) {
            log.log(Level.INFO, "There is not any Artist on the DB");
            throw new NotFoundException(e.getMessage());
        } catch (Exception ex) {
            log.log(Level.SEVERE,
                    "An exception ocurred while getting all the Artists",
                    ex.getMessage());
            throw new InternalServerErrorException(ex);
        }

    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Artist> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        try {
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
        try {
            return String.valueOf(super.count());
        } catch (Exception ex) {
            log.log(Level.SEVERE, "UserRESTful service: Exception logging up .", ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
    }

    @GET
    @Path("findArtistsByEvent/{idEvent}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Artist> findByEvent(@PathParam("idEvent") Long idEvent) {
        List<Artist> artists = null;
        try {
            log.log(Level.INFO, "ArtistRESTful service: reading artists that are related to an id of an Event, {0}", idEvent);
            artists = em.createNamedQuery("findArtistsByEvent").
                    setParameter("idEvent", idEvent).getResultList();

        } catch (NoResultException e) {
            log.log(Level.INFO, "ArtistRESTful service: NotFoundException, there are not artists related to Event, {0}", idEvent);
            throw new NotFoundException(e.getMessage());
        } catch (Exception ex) {
            log.log(Level.SEVERE,
                    "ArtistRESTful service: Exception reading artists that are related to the Event, {0} ",
                    ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
        return artists;
    }

    @GET
    @Path("artistsNotByEvent/{idEvent}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Artist> findNotByEvent(@PathParam("idEvent") Long idEvent) {
        List<Artist> artists = null;
        try {
            log.log(Level.INFO, "ArtistRESTful service: reading artists that are NOT related to an id of an Event, {0}", idEvent);
            artists = em.createNamedQuery("findArtistsNotByEvent").
                    setParameter("idEvent", idEvent).getResultList();

        } catch (NoResultException e) {
            log.log(Level.INFO, "ArtistRESTful service: NotFoundException, there are NOT artists not related to Event, {0}", idEvent);
            throw new NotFoundException(e.getMessage());
        } catch (Exception ex) {
            log.log(Level.SEVERE,
                    "ArtistRESTful service: Exception reading artists that are NOT related to the Event, {0} ",
                    ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
        return artists;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}
