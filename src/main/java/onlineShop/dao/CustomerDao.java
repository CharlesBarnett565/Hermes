package onlineShop.dao;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import onlineShop.model.Authorities;
import onlineShop.model.Cart;
import onlineShop.model.Customer;
import onlineShop.model.User;

@Repository
public class CustomerDao {

	@Autowired
	private SessionFactory sessionFactory;

	public void addCustomer(Customer customer) {
		customer.getUser().setEnabled(true);

		Authorities authorities = new Authorities();
		authorities.setAuthorities("ROLE_USER");
		authorities.setEmailId(customer.getUser().getEmailId());

		Cart cart = new Cart();
		cart.setCustomer(customer);// set customer, one customer, one cart;
		customer.setCart(cart);
		
		Session session = null;

		try {
			session = sessionFactory.openSession();
			session.beginTransaction();
			session.save(authorities);
			session.save(customer);// 通过customer级联cart;
			session.getTransaction().commit();//保存用户信息到数据库
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		} finally {
			if (session != null) {
				session.close();//用完要close;
			}
		}
	}

	public Customer getCustomerByUserName(String userName) {//从表格里面找到一个指定的用户
		User user = null;
		try (Session session = sessionFactory.openSession()) {
                    session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<User> criteriaQuery = builder.createQuery(User.class);
			Root<User> root = criteriaQuery.from(User.class);
			criteriaQuery.select(root).where(builder.equal(root.get("emailId"), userName));
			user = session.createQuery(criteriaQuery).getSingleResult();
                    session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (user != null)
			return user.getCustomer();
		return null;
	}
}
