/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import entities.Client;
import entities.User;
import java.net.URLDecoder;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotAcceptableException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import security.Security;

/**
 *
 * @author 2dam
 */
@Stateless
@Path("entities.client")
public class ClientFacadeREST extends AbstractFacade<Client> {

    @PersistenceContext(unitName = "NocturnaServerPU")
    private EntityManager em;
    private static final Logger log = Logger.getLogger(ClientFacadeREST.class.getName());

    public ClientFacadeREST() {
        super(Client.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Client entity) {
        Query query;
        User user;
        Client client;
        Boolean emailExists;
        try{
            try{
                query=em.createNamedQuery("getUserByEmail");
                query.setParameter("mail", Security.desencriptartexto(URLDecoder.decode(entity.getMail(), "UTF-8")));
                user=(User) query.getSingleResult();
                emailExists = true;
            } catch (NoResultException ex) {
                emailExists = false;
            }
            if (emailExists == true) {
                throw new NotAcceptableException();
            }
            query=em.createNamedQuery("getUserByDni");
            query.setParameter("dni", entity.getDni());
            client=(Client) query.getSingleResult();
            throw new NotAcceptableException();
        } catch (NoResultException e) {
            try{
                entity.setMail(Security.desencriptartexto(URLDecoder.decode(entity.getMail(), "UTF-8")));
                entity.setPasswd(Security.hashText(Security.desencriptartexto(URLDecoder.decode(entity.getPasswd(), "UTF-8"))));
                super.create(entity);
            } catch (Exception ex) {
                log.log(Level.INFO, "UserRESTful service: Exception logging up .", ex.getMessage());
                throw new InternalServerErrorException(ex);
            }
        } catch (NotAcceptableException e) {
            throw new NotAcceptableException("El correo introducido ya existe");
        } catch (Exception ex) {
            log.log(Level.SEVERE, "UserRESTful service: Exception logging up .", ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Long id, Client entity) {
        try{
            entity.setMail(Security.desencriptartexto(URLDecoder.decode(entity.getMail(), "UTF-8")));
            entity.setPasswd(Security.hashText(Security.desencriptartexto(URLDecoder.decode(entity.getPasswd(), "UTF-8"))));
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
    public Client find(@PathParam("id") Long id) {
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
    public List<Client> findAll() {
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
    public List<Client> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}
