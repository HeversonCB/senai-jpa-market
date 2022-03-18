package market.services;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import market.model.dao.CategoryDAO;
import market.model.persistence.Category;

public class CategoryService {

	private final Logger LOG = LogManager.getLogger(CategoryService.class);
	
	private EntityManager entityManager;
	
	private CategoryDAO categoryDAO;
	
	public CategoryService(EntityManager entityManager) {
		this.entityManager = entityManager;
		this.categoryDAO = new CategoryDAO(entityManager);
	}
	
	public Category findByName(String name) {
		if (name == null || name.isEmpty()) {
			this.LOG.error("O Name nao pode ser Nulo!");
			throw new RuntimeException("The name is null!");
		}
		try {
			return this.categoryDAO.findByName(name.toLowerCase());
		} catch (NoResultException r) {
			this.LOG.info("Nao foi encontrado Categoria, sera criada!");
			return null;
		}
		
	}
}
