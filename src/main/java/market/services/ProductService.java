package market.services;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import market.model.dao.ProductDAO;
import market.model.persistence.Product;

public class ProductService {
	
	private final Logger LOG = LogManager.getLogger(ProductService.class);
	
	private EntityManager entityManager;
	
	private ProductDAO productDAO;
	
	public ProductService(EntityManager entityManager) {
		this.entityManager = entityManager;
		this.productDAO = new ProductDAO(entityManager);
	}
	
	public void create(Product product) {
		this.LOG.info("Preparando para a cria��o de um produto");
		if (product == null) {
			this.LOG.error("O produto informado est� nulo!");
			throw new RuntimeException("O produto est� nulo!");
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

	private void commitAndCloseTransaction() {
		this.LOG.info("Commitando e fechando transa��o.");
		entityManager.getTransaction().commit();
		entityManager.close();
	}

	private void getBeginTransaction() {
		this.LOG.info("Come�ando transa��o.");
		entityManager.getTransaction().begin();
	}
}
