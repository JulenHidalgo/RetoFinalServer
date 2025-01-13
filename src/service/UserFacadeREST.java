/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import entities.Event;
import entities.User;
import java.sql.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javassist.NotFoundException;
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
import security.PasswordGenerator;
import security.Security;
import smtp.Smtp;


/**
 *
 * @author 2dam
 */
@Stateless
@Path("entities.user")
public class UserFacadeREST extends AbstractFacade<User> {

    @PersistenceContext(unitName = "NocturnaServerPU")
    private EntityManager em;
    private static final Logger log = Logger.getLogger(ArtistFacadeREST.class.getName());
    
    public UserFacadeREST() {
        super(User.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(User entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Long id, User entity) {
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
    public User find(@PathParam("id") Long id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<User> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<User> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }
    
    @GET
    @Path("login/{mail}/{passwd}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public User login(@PathParam("mail") String mail, @PathParam("passwd") String passwd) throws NotFoundException {
       User user=null;
        Query query;
        if(mail == null || passwd == null){
            log.log(Level.INFO,"UserRESTful service: find users by event {0}.", mail);
            throw new BadRequestException("Los parametros no pueden estar vacios");  
        }
        try {
            log.log(Level.INFO,"UserRESTful service: find users by event {0}.", mail);
            query=em.createNamedQuery("login");
            query.setParameter("mail", mail);
            query.setParameter("passwd", passwd);
            user=(User) query.getSingleResult();
            if(user==null)
              throw new NotFoundException("El email o la contraseña no coinciden");
            
        }catch(NotFoundException e){
            
            throw new NotFoundException("El email o la contraseña no coinciden");
            
        } catch (Exception ex) {
            log.log(Level.SEVERE,
                    "ArtistRESTful service: Exception reading users by profile, {0}",
                    ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
        return user;
    }
    
    
    
    
    @PUT
    @Path("updatePasswd/{newPasswd}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void updatePasswd(@PathParam("newPasswd") String newPasswd, User user) {
         Query query;
         
        if (newPasswd == null || newPasswd.isEmpty()) {
            log.log(Level.INFO, "UserRESTful service: invalid new password {0}.", newPasswd);
            throw new BadRequestException("Los parámetros no pueden estar vacíos");
        }
        try {
            log.log(Level.INFO, "UserRESTful service: updating password for {0}.", newPasswd);
            user.setPasswd(Security.hashText(newPasswd));
            super.edit(user);
        } catch (Exception ex) {
            log.log(Level.SEVERE, "UserRESTful service: Exception updating password for {0}.", ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
    }
 
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    
    @GET
    @Path("resetPassword/{userEmail}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void resetPassword(@PathParam("userEmail") String userEmail) throws NotFoundException {
         Query query;
         User user;
         String pass;
         
        if (userEmail == null || userEmail.isEmpty()) {
            log.log(Level.INFO, "UserRESTful service: invalid new password {0}.", userEmail);
            throw new BadRequestException("Los parámetros no pueden estar vacíos");
        }
        try {
            query=em.createNamedQuery("getUserByEmail");
            query.setParameter("mail", userEmail);
            user=(User) query.getSingleResult();
            if(user==null){
              throw new NotFoundException("El email o la contraseña no coinciden");
            }
            log.log(Level.INFO, "UserRESTful service: reseting password for {0}.", userEmail);
            pass = PasswordGenerator.getPassword();
            user.setPasswd(Security.hashText(pass));
            super.edit(user);
            Smtp.sendEmail(user.getMail(), pass);
        } catch (NotFoundException ex) {
            log.log(Level.SEVERE, "UserRESTful service: Exception updating password for {0}.", ex.getMessage());
            throw new NotFoundException("El correo no coincide con ningun usuario");
        } catch (Exception ex) {
            log.log(Level.SEVERE, "UserRESTful service: Exception updating password for {0}.", ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
    }
    
}
