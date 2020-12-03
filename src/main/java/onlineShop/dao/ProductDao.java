
package onlineShop.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import onlineShop.model.Product;

@Repository //��Դ��
public class ProductDao {

	@Autowired
	private SessionFactory sessionFactory;

	public void addProduct(Product product) {//��Ӳ�Ʒ
		Session session = null; //ͨ��session����ʵ�ֹ���
		try {
			session = sessionFactory.openSession();
			session.beginTransaction();  //�� //Ҫô��ӳɹ�Ҫôȫ��ʧ�ܣ�
			session.save(product); //�������product;
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback(); //��Ӳ��ɹ��ͳ������//��ǰ��֪�����ʧ��
		} finally {
			if (session != null) {
				session.close(); //�����ɰ�session�ر�
			}
		}
	}

	public void deleteProduct(int productId) { //��ǰ��ɾ����Ʒ//�����ݿ��ҵ�������ɾ��//���̺�������
		Session session = null;
		try {
			session = sessionFactory.openSession();
			session.beginTransaction();
			Product product = (Product) session.get(Product.class, productId);
			session.delete(product);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		} finally {
			if (session != null) {
				session.close();
			}
		}

	}

	public void updateProduct(Product product) {//���²�Ʒ��ȥ���ݿ������
		Session session = null;
		try {
			session = sessionFactory.openSession(); //ԭ���ԣ� //Ҫô��ӳɹ�Ҫôȫ��ʧ�ܣ�
			session.beginTransaction();
			session.saveOrUpdate(product);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		} finally {
			if (session != null) {
				session.close();
			}
		}

	}

	public Product getProductById(int productId) {//��ǰ�˿�ĳ���������Ʒ��Ϣ��
		try (Session session = sessionFactory.openSession()) {
			session.beginTransaction();
			Product product = (Product) session.get(Product.class, productId);
			session.getTransaction().commit();
			return product;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<Product> getAllProducts() {//�����ݿ��ȡ������Ʒ��Ϣ��
		List<Product> products = new ArrayList<Product>();
		try (Session session = sessionFactory.openSession()) {
			session.beginTransaction();
			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder(); //ͨ��builderclass //����query //��instance
			CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
			Root<Product> root = criteriaQuery.from(Product.class);
			criteriaQuery.select(root);
			products = session.createQuery(criteriaQuery).getResultList(); //����product table //���ҷ���һ��list
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return products;
	}
}
