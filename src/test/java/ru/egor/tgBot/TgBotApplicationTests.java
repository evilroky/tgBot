package ru.egor.tgBot;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import ru.egor.tgBot.entity.Category;
import ru.egor.tgBot.entity.Client;
import ru.egor.tgBot.entity.Product;
import ru.egor.tgBot.repository.CategoryRepository;
import ru.egor.tgBot.repository.ClientRepository;
import ru.egor.tgBot.repository.ProductRepository;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class TgBotApplicationTests {

	@Autowired
	private ClientRepository clientRepository;
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private ProductRepository productRepository;




	static class TestData {
		public List<ClientYaml> clients;
		public List<CategoryYaml> categories;
		public List<ProductYaml> products;
	}

	static class ClientYaml {
		public Long externalId;
		public String fullName;
		public String phoneNumber;
		public String address;
	}

	static class CategoryYaml {
		public String name;
		public List<CategoryYaml> children;
	}

	static class ProductYaml {
		public String category;
		public String name;
		public String description;
		public Double price;
	}

	@Test
	void loadTestData() throws IOException {

		productRepository.deleteAll();
		categoryRepository.deleteAll();
		clientRepository.deleteAll();

		LoaderOptions options = new LoaderOptions();
		Constructor constructor = new Constructor(TestData.class, options);
		Yaml yaml = new Yaml(constructor);
		InputStream inputStream = new FileInputStream("src/test/resources/test-data.yaml");
		TestData data = yaml.load(inputStream);

		for (ClientYaml c : data.clients) {
			Client client = new Client();
			client.setExternalId(c.externalId);
			client.setFullName(c.fullName);
			client.setPhoneNumber(c.phoneNumber);
			client.setAddress(c.address);
			clientRepository.save(client);
		}

		Map<String, Category> savedCategories = new HashMap<>();
		for (CategoryYaml rootYaml : data.categories) {
			Category parent = new Category();
			parent.setName(rootYaml.name);
			parent.setParent(null);
			categoryRepository.save(parent);
			savedCategories.put(parent.getName(), parent);

			if (rootYaml.children != null) {
				for (CategoryYaml childYaml : rootYaml.children) {
					saveCategoryRecursive(childYaml, parent, savedCategories);
				}
			}
		}

		for (ProductYaml p : data.products) {
			Product product = new Product();
			product.setName(p.name);
			product.setDescription(p.description);
			product.setPrice(p.price);
			product.setCategory(savedCategories.get(p.category));
			productRepository.save(product);
		}
	}

	private Category saveCategoryRecursive(CategoryYaml yamlCategory, Category parent, Map<String, Category> saved) {
		Category category = new Category();
		category.setName(yamlCategory.name);
		category.setParent(parent);

		categoryRepository.save(category); // обязательно до дальнейших действий
		saved.put(category.getName(), category);

		if (yamlCategory.children != null) {
			for (CategoryYaml childYaml : yamlCategory.children) {
				saveCategoryRecursive(childYaml, category, saved);
			}
		}

		return category;
	}
}
