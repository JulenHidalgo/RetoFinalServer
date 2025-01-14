/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import entities.Ticket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javassist.NotFoundException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
@Path("entities.ticket")
public class TicketFacadeREST extends AbstractFacade<Ticket> {

    @PersistenceContext(unitName = "NocturnaServerPU")
    private EntityManager em;

    public TicketFacadeREST() {
        super(Ticket.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Ticket entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Long id, Ticket entity) {
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
    public Ticket find(@PathParam("id") Long id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Ticket> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Ticket> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

     @GET
    @Path("findTicketByUser/{dni}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Ticket> findTicketByUser(@PathParam("dni") String dni) throws NotFoundException {
        List<Ticket> tickets = null;
        if (dni == null) { 
            throw new BadRequestException("El parámetro 'idEvent' es obligatorio.");
        }
        try {
           
            tickets = em.createNamedQuery("findTicketByUser").
                    setParameter("dni", dni).getResultList();
            if (tickets == null || tickets.isEmpty()) {
                throw new NotFoundException("El usuario con dni : " + dni+" no tiene ninguna entrada");
            }
        } catch(NotFoundException e){
            throw new NotFoundException("El usuario con dni : " + dni+" no tiene ninguna entrada");
        }catch (Exception ex) {
            

            throw new InternalServerErrorException(ex);
        }
        return tickets;
    }
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}
