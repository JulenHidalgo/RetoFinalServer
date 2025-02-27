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
import javax.ws.rs.NotFoundException;
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
        try {
            query = em.createNamedQuery("getUserByEmail");
            query.setParameter("mail", entity.getMail());
            user = (User) query.getSingleResult();
            throw new NotAcceptableException();
        } catch (NoResultException e) {
            try {
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
        try {
            return super.find(id);
        } catch (Exception ex) {
            log.log(Level.SEVERE, "UserRESTful service: Exception logging up .", ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<User> findAll() {
        try {
            return super.findAll();
        } catch (Exception ex) {
            log.log(Level.SEVERE, "UserRESTful service: Exception logging up .", ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<User> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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
    @Path("login/{mail}/{passwd}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public User login(@PathParam("mail") String mail, @PathParam("passwd") String passwd) throws NotFoundException {
        User user = new User();
        User userRecibir;
        Query query;
        try {
            log.log(Level.INFO, mail);
            query = em.createNamedQuery("login");
            query.setParameter("mail", Security.desencriptartexto(mail));
            query.setParameter("passwd", Security.hashText(Security.desencriptartexto(passwd)));
            userRecibir = (User) query.getSingleResult();
            user.setId(userRecibir.getId());
            user.setIsAdmin(userRecibir.getIsAdmin());
            user.setMail(userRecibir.getMail());
        } catch (NoResultException ex) {
            log.log(Level.INFO, "UserRESTful service: Log in failed.", ex.getMessage());
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
    public void updatePasswd(@PathParam("newPasswd") String newPasswd, User user) throws NotFoundException {
        User userLogin;
        Query query;
        try {
            query = em.createNamedQuery("login");
            query.setParameter("mail", user.getMail());
            query.setParameter("passwd", Security.hashText(Security.desencriptartexto(URLDecoder.decode(user.getPasswd(), "UTF-8"))));
            userLogin = (User) query.getSingleResult();
            //userLogin = login(URLDecoder.decode(user.getMail(), "UTF-8"), URLDecoder.decode(user.getPasswd(), "UTF-8"));

            log.log(Level.INFO, "Actualizando contraseña para: {0}", user.getMail());

            userLogin.setPasswd(Security.hashText(Security.desencriptartexto(newPasswd)));
            log.log(Level.SEVERE, user.getPasswd());
            log.log(Level.SEVERE, Security.desencriptartexto(newPasswd));
            log.log(Level.SEVERE, userLogin.getMail());
            log.log(Level.SEVERE, userLogin.getId().toString());
            log.log(Level.SEVERE, userLogin.getIsAdmin().toString());
            // Actualiza el usuario en la base de datos
            super.edit(user);

            // Envía correo de confirmación
            String subject = "Contraseña actualizada";
            String text = "¡Tu contraseña se ha actualizado con éxito!";
            Smtp.sendEmail(userLogin.getMail(), subject, text);

        } catch (NoResultException ex) {
            log.log(Level.SEVERE, "Usuario no encontrado: {0}", ex.getMessage());
            throw new NotFoundException("Usuario no registrado");
        } catch (Exception ex) {
            log.log(Level.SEVERE, "Error interno: {0}", ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
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
        try {
            query = em.createNamedQuery("getUserByEmail");
            query.setParameter("mail", userEmail);
            user = (User) query.getSingleResult();
            log.log(Level.INFO, "UserRESTful service: reseting password for {0}.", userEmail);
            pass = PasswordGenerator.getPassword();
            user.setPasswd(Security.hashText(pass));
            super.edit(user);
            subject = "Solicitud de restablecimiento de contraseña";
            text = "Su nueva contraseña es: " + pass;
            Smtp.sendEmail(user.getMail(), subject, text);
        } catch (NoResultException ex) {
            log.log(Level.INFO, "UserRESTful service: No user found {0}.", ex.getMessage());
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

}
