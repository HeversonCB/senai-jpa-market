package market.application;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;

import market.connection.JpaConnectionFactory;
import market.model.persistence.Category;
import market.model.persistence.Product;
import market.services.ProductService;

public class Program {
	
	public static void main(String[] args) {
		EntityManager entityManager = new JpaConnectionFactory().getEntityManager();
		ProductService productService = new ProductService(entityManager);
		
		Product product = new Product("Cheetos", "Requeijão 180g", new BigDecimal(12.99), new Category("Alimento"));
		
//		productService.create(product);
//		productService.delete(2L);
//		productService.update(product, 5L);
		
		List<Product> products = productService.listAll();
		products.stream().forEach(p -> System.out.println(p));
	}

}
