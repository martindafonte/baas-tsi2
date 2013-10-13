package persistencia;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;

import modelo.Usuario;

/**
 * Session Bean implementation class UsuarioDAO
 */
@Stateless
@LocalBean
@TransactionManagement(TransactionManagementType.CONTAINER)
public class UsuarioDAO implements UsuarioDAOLocal {

    /**
     * Default constructor. 
     */
	
	@PersistenceUnit(unitName = "WebUserManager")
	private EntityManagerFactory emf;
	
    public UsuarioDAO() {
        // TODO Auto-generated constructor stub
    }

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void altaUsuario(Usuario u) {
		// TODO Auto-generated method stub
//		EntityManager em = Persistence.createEntityManagerFactory("WebUserManager").createEntityManager();
		EntityManager em = emf.createEntityManager();
		try{
			em.persist(u);
			em.close();
			
		}catch(Throwable ex){}
		
	}

	@Override
	public Usuario getUsuario(String nick, String pass) {
		// TODO Auto-generated method stub
		
		EntityManager em = emf.createEntityManager();
		try{
			Query q = em.createQuery("SELECT x FROM Usuario x WHERE x.nick = ?1 and pass = ?2");
			q.setParameter(1, nick).setParameter(2, pass);
			List<Usuario> us = q.getResultList();
			if (us.size() == 1 ){
				return us.remove(0);
			}
			
			
		}catch(Exception e){
			return null;
			
		}
		return null;
	}
		

}
