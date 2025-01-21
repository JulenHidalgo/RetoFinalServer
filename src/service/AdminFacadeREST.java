/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import entities.Admin;
import entities.User;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.BadRequestException;
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
@Path("entities.admin")
public class AdminFacadeREST extends AbstractFacade<Admin> {

    @PersistenceContext(unitName = "NocturnaServerPU")
    private EntityManager em;
    private static final Logger log = Logger.getLogger(AdminFacadeREST.class.getName());

    public AdminFacadeREST() {
        super(Admin.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Admin entity) {
        Query query;
        User user;
        try{
            query=em.createNamedQuery("getUserByEmail");
            query.setParameter("mail", entity.getMail());
            user=(User) query.getSingleResult();
            throw new NotAcceptableException();
        } catch (NoResultException e) {
            try{
                entity.setPasswd(Security.hashText(entity.getPasswd()));
                super.create(entity);
            } catch (Exception ex) {
                log.log(Level.SEVERE, "UserRESTful service: Exception logging up .", ex.getMessage());
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
    public void edit(@PathParam("id") Long id, Admin entity) {
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
    public Admin find(@PathParam("id") Long id) {
        Admin admin;
        try{
            admin = super.find(id);
            admin.setPasswd("");
            return admin;
        } catch (Exception ex) {
            log.log(Level.SEVERE, "UserRESTful service: Exception logging up .", ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Admin> findAll() {
        List<Admin> admins;
        try{
            admins = super.findAll();
            for (Admin admin : admins) {
                admin.setPasswd("");
            }
            return admins;
        } catch (Exception ex) {
            log.log(Level.SEVERE, "UserRESTful service: Exception logging up .", ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Admin> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        List<Admin> admins;
        try{
            admins = super.findRange(new int[]{from, to});
            for (Admin admin : admins) {
                admin.setPasswd("");
            }
            return admins;
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
