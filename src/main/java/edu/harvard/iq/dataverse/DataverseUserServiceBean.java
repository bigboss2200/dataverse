/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.harvard.iq.dataverse;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author xyang
 */
@Stateless
@Named
public class DataverseUserServiceBean {

    @PersistenceContext(unitName = "VDCNet-ejbPU")
    private EntityManager em;
    
    public String encryptPassword(String plainText) {
        return PasswordEncryption.getInstance().encrypt(plainText);
    }
       
    public DataverseUser save(DataverseUser dataverseUser) {
         return em.merge(dataverseUser);
    }
    
    /*
    public List<DataverseUser> findByUserName(String userName) {
        Query query = em.createQuery("select object(o) from DataverseUser as o where o.userName =:userName");
        query.setParameter("userName", userName);
        return query.getResultList();
    }
    */
    
    public DataverseUser find(Long pk) {
        return (DataverseUser) em.find(DataverseUser.class, pk);
    }    
    
    public DataverseUser findByUserName(String userName) {
        String query = "SELECT u from DataverseUser u where u.userName = :userName ";
        DataverseUser user = null;
        try {
            user = (DataverseUser) em.createQuery(query).setParameter("userName", userName).getSingleResult();
        } catch (javax.persistence.NoResultException e) {
        }
        return user;
    }

    public DataverseUser findByEmail(String email) {
        String query = "SELECT u from DataverseUser u where u.email = :email ";
        DataverseUser user = null;
        try {
            user = (DataverseUser) em.createQuery(query).setParameter("email", email).getSingleResult();
        } catch (javax.persistence.NoResultException e) {
        }
        return user;
    }
    
    public DataverseUser findDataverseUser() {
        return (DataverseUser) em.createQuery("select object(o) from DataverseUser as o where o.id = 1").getSingleResult();
    }
	
	public List<DataverseUser> findAll() {
		return em.createNamedQuery("DataverseUser.findAll", DataverseUser.class).getResultList();
	}
}