/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import entities.User;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javassist.NotFoundException;
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
    private static final Logger log = Logger.getLogger(UserFacadeREST.class.getName());
    
    public UserFacadeREST() {
        super(User.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(User entity) {
        Query query;
        User user;
        
        if(entity == null){
            log.log(Level.INFO,"UserRESTful service: invalid params {0}.");
            throw new BadRequestException("Los parametros no pueden estar vacios");  
        }
        try{
            query=em.createNamedQuery("getUserByEmail");
            query.setParameter("mail", entity.getMail());
            user=(User) query.getSingleResult();
            throw new NotAcceptableException();
        } catch (NoResultException e) {
            try{
                log.log(Level.SEVERE, entity.getPasswd());
                log.log(Level.SEVERE, Security.hashText(entity.getPasswd()));
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
    public void edit(@PathParam("id") Long id, User entity) {
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
    public User find(@PathParam("id") Long id) {
        User user;
        try{
            user = super.find(id);
            user.setPasswd("");
            return user;
        } catch (Exception ex) {
            log.log(Level.SEVERE, "UserRESTful service: Exception logging up .", ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<User> findAll() {
        List<User> users;
        try{
            users = super.findAll();
            for (User user : users) {
                user.setPasswd("");
            }
            return users;
        } catch (Exception ex) {
            log.log(Level.SEVERE, "UserRESTful service: Exception logging up .", ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<User> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        List<User> users;
        try{
            users = super.findRange(new int[]{from, to});
            for (User user : users) {
                user.setPasswd("");
            }
            return users;
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
    @Path("login/{mail}/{passwd}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public User login(@PathParam("mail") String mail, @PathParam("passwd") String passwd) throws NotFoundException {
        User user=null;
        Query query;
        if(mail == null || passwd == null){
            log.log(Level.INFO,"UserRESTful service: invalid params {0}.", mail);
            throw new BadRequestException("Los parametros no pueden estar vacios");  
        }
        try {
            log.log(Level.INFO,"UserRESTful service: find users by event {0}.", mail);
            query=em.createNamedQuery("login");
            query.setParameter("mail", mail);
            query.setParameter("passwd", Security.hashText(passwd));
            user=(User) query.getSingleResult();
        }catch(NoResultException ex){
            log.log(Level.INFO, "UserRESTful service: Log in failed.", ex.getMessage()); 
            throw new NotFoundException("El email o la contraseña no coinciden");
        } catch (Exception ex) {
            log.log(Level.SEVERE,
                    "ArtistRESTful service: Exception reading users by profile, {0}",
                    ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
        user.setPasswd("");
        return user;
    }
    
    @PUT
    @Path("updatePasswd/{newPasswd}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void updatePasswd(@PathParam("newPasswd") String newPasswd, User user)  throws NotFoundException {
         Query query;
         String subject, text;
         
        if (newPasswd == null || newPasswd.isEmpty()) {
            log.log(Level.INFO, "UserRESTful service: invalid new password {0}.", newPasswd);
            throw new BadRequestException("Los parámetros no pueden estar vacíos");
        }
        try {
            log.log(Level.INFO, "UserRESTful service: updating password for {0}.", newPasswd);
            user.setPasswd(Security.hashText(newPasswd));
            super.edit(user);
            subject = "Solicitud de cambio de contraseña";
            text = "El cambio de contraseña solicitado ha sido un exito";
            Smtp.sendEmail(user.getMail(), newPasswd, subject, text);
        } catch (NoResultException ex) {
            log.log(Level.SEVERE, "UserRESTful service: No user found.", ex.getMessage());
            throw new NotFoundException("El correo no coincide con ningun usuario");
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
         String subject, text;
         
        if (userEmail == null || userEmail.isEmpty()) {
            log.log(Level.INFO, "UserRESTful service: invalid new password {0}.", userEmail);
            throw new BadRequestException("Los parámetros no pueden estar vacíos");
        }
        try {
            query=em.createNamedQuery("getUserByEmail");
            query.setParameter("mail", userEmail);
            user=(User) query.getSingleResult();
            log.log(Level.INFO, "UserRESTful service: reseting password for {0}.", userEmail);
            pass = PasswordGenerator.getPassword();
            user.setPasswd(Security.hashText(pass));
            super.edit(user);
            subject = "Solicitud de restablecimiento de contraseña";
            text = "Su nueva contraseña es: " + pass;
            Smtp.sendEmail(user.getMail(), pass, subject, text);
        } catch (NoResultException ex) {
            log.log(Level.INFO, "UserRESTful service: No user found {0}.", ex.getMessage());
            throw new NotFoundException("El correo no coincide con ningun usuario");
        } catch (Exception ex) {
            log.log(Level.SEVERE, "UserRESTful service: Exception updating password for {0}.", ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
    }
    
}
