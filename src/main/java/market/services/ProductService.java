package market.services;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import market.model.dao.ProductDAO;
import market.model.persistence.Category;
import market.model.persistence.Product;

public class ProductService {

	private final Logger LOG = LogManager.getLogger(ProductService.class);

	private EntityManager entityManager;

	private ProductDAO productDAO;
	
	private CategoryService categoryService;

	public ProductService(EntityManager entityManager) {
		this.entityManager = entityManager;
		this.productDAO = new ProductDAO(entityManager);
		this.categoryService = new CategoryService(entityManager);
	}

	private void commitAndCloseTransaction() {
		this.LOG.info("Commitando e fechando transação.");
		entityManager.getTransaction().commit();
		entityManager.close();
	}

	private void getBeginTransaction() {
		this.LOG.info("Começando transação.");
		entityManager.getTransaction().begin();
	}

	public void create(Product product) {
		this.LOG.info("Preparando para a criação de um produto");
		if (product == null) {
			this.LOG.error("O produto informado está nulo!");
			throw new RuntimeException("Product Null");
		}

		String categoryName = product.getCategory().getName();
		this.LOG.info("Buscando se ja existe a categoria " + categoryName);
		Category category = this.categoryService.findByName(categoryName);
		
		if (category != null) {
			product.setCategory(category);
		}
		
		try {
			getBeginTransaction();
			this.productDAO.create(product);
			commitAndCloseTransaction();
		} catch (Exception e) {
			this.LOG.error("Erro ao criar um produto, causado por: " + e.getMessage());
			throw new RuntimeException();
		}
		this.LOG.info("Produto foi criado com sucesso!");
	}

	public void delete(Long id) {
		if (id == null) {
			this.LOG.error("O ID do produto informado está nulo!");
			throw new RuntimeException("The ID is null");
		}

		Product product = this.productDAO.getById(id);

		if (product == null) {
			this.LOG.error("O produto não existe");
			throw new EntityNotFoundException("Product not found");
		}
		this.LOG.error("Produto encontrado com sucesso");

		getBeginTransaction();
		this.productDAO.delete(product);
		commitAndCloseTransaction();
		this.LOG.error("Produto deletado com sucesso");
	}
}
