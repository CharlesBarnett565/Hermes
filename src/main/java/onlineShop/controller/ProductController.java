package onlineShop.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import onlineShop.model.Product;
import onlineShop.service.ProductService;

@Controller
public class ProductController {

	@Autowired
	private ProductService productService;

	@RequestMapping(value = "/getAllProducts", method = RequestMethod.GET) //从数据库拿到所有商品(getallproduct)
	public ModelAndView getAllProducts() {
		List<Product> products = productService.getAllProducts();
		return new ModelAndView("productList", "products", products);
	}

	@RequestMapping(value = "/getProductById/{productId}", method = RequestMethod.GET) //拿到某个商品
	public ModelAndView getProductById(@PathVariable(value = "productId") int productId) {
		Product product = productService.getProductById(productId);
		return new ModelAndView("productPage", "product", product);
	}

	@RequestMapping(value = "/admin/product/addProduct", method = RequestMethod.GET) //通过get //返回一个商品页面
	public ModelAndView getProductForm() {
		return new ModelAndView("addProduct", "productForm", new Product());
	}

	@RequestMapping(value = "/admin/product/addProduct", method = RequestMethod.POST) //添加商品到数据库
	public String addProduct(@ModelAttribute(value = "productForm") Product product, BindingResult result) {

		if (result.hasErrors()) {
			return "addProduct";
		}
		productService.addProduct(product);
		MultipartFile image = product.getProductImage(); //从product //里面拿到image
		if (image != null && !image.isEmpty()) {
			// Mac
			// Path path = Paths.get("/Users/xxx/products/" + product.getId() + ".jpg");
			//Path path = Paths.get("/Users/xxx/products/" + product.getId() + ".jpg");

			// windows
			Path path = Paths.get("C:\\products\\" + product.getId() + ".jpg");
			try {
				image.transferTo(new File(path.toString()));
			} catch (IllegalStateException | IOException e) {
				e.printStackTrace();
			}
		}
		return "redirect:/getAllProducts";
	}
	
	@RequestMapping(value = "/admin/delete/{productId}")  //删除一个商品
	public String deleteProduct(@PathVariable(value = "productId") int productId) {
		// for MAC : Path path = Paths.get("/Users/xxx/products/" + productId + ".jpg");
		// For windows
		Path path = Paths.get("C:\\products\\" + productId + ".jpg");

		if (Files.exists(path)) {
			try {
				Files.delete(path);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		productService.deleteProduct(productId);
		return "redirect:/getAllProducts";
	}

	@RequestMapping(value = "/admin/product/editProduct/{productId}", method = RequestMethod.GET) //编辑商品 //拿到商品信息的form
	public ModelAndView getEditForm(@PathVariable(value = "productId") int productId) {
		Product product = productService.getProductById(productId);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("editProduct");
		modelAndView.addObject("editProductObj", product);
		modelAndView.addObject("productId", productId);

		return modelAndView;
	}

	@RequestMapping(value = "/admin/product/editProduct/{productId}", method = RequestMethod.POST) //更新商品
	public String editProduct(@ModelAttribute(value = "editProductObj") Product product,
			@PathVariable(value = "productId") int productId) {
		product.setId(productId);
		productService.updateProduct(product);
		return "redirect:/getAllProducts";
	}
}
