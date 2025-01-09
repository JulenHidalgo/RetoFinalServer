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
import javax.ws.rs.BadRequestException;
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
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Long id, Artist entity) {
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
    public Artist find(@PathParam("id") Long id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Artist> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Artist> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    @GET
    @Path("artistsByEvent/{idEvent}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Artist> findByEvent(@PathParam("idEvent") Long idEvent) {
        List<Artist> artists = null;
        if (idEvent == null) {
            log.log(Level.SEVERE, "ArtistRESTful service: BadRequestException reading artist/artistsByEvent/{0}", idEvent);
            throw new BadRequestException("El parámetro 'idEvent' es obligatorio.");
        }
        try {
            log.log(Level.INFO, "ArtistRESTful service: reading artists that are related to an id of an Event, {0}", idEvent);
            artists = em.createNamedQuery("findArtistsByEvent").
                    setParameter("idEvent", idEvent).getResultList();
            if (artists == null || artists.isEmpty()) {
                log.log(Level.INFO, "ArtistRESTful service: No se encontraron artistas para el evento con id {0}", idEvent);
                throw new NotFoundException("No se encontraron artistas para el evento con id " + idEvent);
            }
        } catch(NotFoundException e){
            throw new NotFoundException("No se encontraron artistas para el evento con id " + idEvent);
        }catch (Exception ex) {
            log.log(Level.SEVERE,
                    "ArtistRESTful service: Exception reading artists that are related to an id of an Event, {0}",
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
        if (idEvent == null) {
            log.log(Level.SEVERE, "ArtistRESTful service: BadRequestException reading artist/artistsNotByEvent/{0}", idEvent);
            throw new BadRequestException("El parámetro 'idEvent' es obligatorio.");
        }
        try {
            log.log(Level.INFO, "ArtistRESTful service: reading artists that are not related to an id of an Event, {0}", idEvent);
            artists = em.createNamedQuery("findArtistsNotByEvent").
                    setParameter("idEvent", idEvent).getResultList();
        } catch (Exception ex) {
            log.log(Level.SEVERE,
                    "ArtistRESTful service: Exception reading artists that are not related to an id of an Event, {0}",
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
