package market.model.dao;

import javax.persistence.EntityManager;

import market.model.persistence.Category;

public class CategoryDAO {
	
private EntityManager entityManager;
	
	public CategoryDAO(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	public Category findByName(String name) {
		String sql = "SELECT * FROM category WHERE name =:name";
		return (Category) this.entityManager.createNativeQuery(sql, Category.class).setParameter("name", name).getSingleResult();
	}
}
