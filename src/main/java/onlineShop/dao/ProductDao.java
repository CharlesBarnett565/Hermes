
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

@Repository //资源库
public class ProductDao {

	@Autowired
	private SessionFactory sessionFactory;

	public void addProduct(Product product) {//添加产品
		Session session = null; //通过session对象实现功能
		try {
			session = sessionFactory.openSession();
			session.beginTransaction();  //， //要么添加成功要么全部失败；
			session.save(product); //保存这个product;
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback(); //添加不成功就撤回添加//让前端知道添加失败
		} finally {
			if (session != null) {
				session.close(); //添加完成把session关闭
			}
		}
	}

	public void deleteProduct(int productId) { //从前端删除商品//再数据库找到主键并删除//过程和上面差不多
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

	public void updateProduct(Product product) {//更新产品，去数据库里更新
		Session session = null;
		try {
			session = sessionFactory.openSession(); //原子性， //要么添加成功要么全部失败；
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

	public Product getProductById(int productId) {//从前端看某个具体的商品信息；
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

	public List<Product> getAllProducts() {//从数据库获取所有商品信息；
		List<Product> products = new ArrayList<Product>();
		try (Session session = sessionFactory.openSession()) {
			session.beginTransaction();
			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder(); //通过builderclass //创造query //的instance
			CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
			Root<Product> root = criteriaQuery.from(Product.class);
			criteriaQuery.select(root);
			products = session.createQuery(criteriaQuery).getResultList(); //搜索product table //并且返回一个list
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return products;
	}
}
