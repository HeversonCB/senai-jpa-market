package market.services;

import java.util.ArrayList;
import java.util.List;

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
	
	private void getBeginTransaction() {
		this.LOG.info("Abrindo Transacao com o banco de dados...");
		entityManager.getTransaction().begin();
	}

	private void commitAndCloseTransaction() {
		this.LOG.info("Commitando e Fechando transacao com o banco de dados");
		entityManager.getTransaction().commit();
		entityManager.close();
	}
	
	public void create(Product product) {
		this.LOG.info("Preparando para a Criacao de um Produto");
		
		if (product == null) {
			this.LOG.error("O Produto informado esta nulo!");
			throw new RuntimeException("Product Null!");
		}
		String categoryName = product.getCategory().getName();
		
		this.LOG.info("Buscando se ja existe a Categoria: " + categoryName);
		Category category = this.categoryService.findByName(categoryName);
		
		if (category != null) {
			this.LOG.info("Categoria '" + categoryName + "' encontrada no banco!");
			product.setCategory(category);
		}
		
		try {
			getBeginTransaction();
			this.productDAO.create(product);
			commitAndCloseTransaction();
		} catch (Exception e) {
			this.LOG.error("Erro ao criar um Produto, causado por: " + e.getMessage());
			throw new RuntimeException(e);
		}
		this.LOG.info("Produto foi criado com sucesso!");
	}
	
	public void delete(Long id) {
		this.LOG.info("Preparando para encontrar o Produto");
		if (id == null) {
			this.LOG.error("O ID do Produto informado esta nulo!");
			throw new RuntimeException("The ID is Null");
		}
		
		Product product = this.productDAO.getById(id);
		validateProductIsNull(product);
		
		this.LOG.info("Produto encontrado com sucesso!");
		
		getBeginTransaction();
		this.productDAO.delete(product);
		commitAndCloseTransaction();
		this.LOG.info("Produto deletado com sucesso!");
	}
	
	public void update(Product newProduct, Long productId) {
		this.LOG.info("Preparando para Atualizar o Produto");
		if (newProduct == null || productId == null) {
			this.LOG.error("Um dos parametros esta nulo!");
			throw new RuntimeException("The parameter is null");
		}
		
		Product product = this.productDAO.getById(productId);
		validateProductIsNull(product);
		
		this.LOG.info("Produto encontrado no banco!");
		
		getBeginTransaction();
		product.setName(newProduct.getName());
		product.setDescription(newProduct.getDescription());
		product.setPrice(newProduct.getPrice());
		product.setCategory(this.categoryService.findByName(newProduct.getCategory().getName()));
		
		commitAndCloseTransaction();
		this.LOG.info("Produto atualizado com sucesso!");
	}
	
	public List<Product> listAll() {
		this.LOG.info("Preparando para listar os produtos");
		List<Product> products = this.productDAO.listAll();
		
		if (products == null) {
			this.LOG.info("Não foram encontrados Produtos");
			return new ArrayList<Product>();
		}
		this.LOG.info("Foram encontrados " + products.size() + " produtos.");
		return products;
	}

	private void validateProductIsNull(Product product) {
		if (product == null) {
			this.LOG.error("O Produto nao Existe!");
			throw new EntityNotFoundException("Product not Found!");
		}
	}

}
