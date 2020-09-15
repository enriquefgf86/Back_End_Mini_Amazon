package com.miniAmazon;

//AQUI import javafx.beans.binding.BooleanExpression;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.crypto.password.PasswordEncoder;


import javax.persistence.*;

@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "native")
    @GenericGenerator(name="native",strategy="native")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="product_id")
    private Product products;
//------------------------------------JUNIT para borrar elementos relacionados en la base de datos------------------------
//    private static CrudRepository<Product,Long> productRepository;
//    private static CrudRepository<Category,Long> categoryRepository;
//
//    @Test
//    public static void whenDeletingProducts_thenCategoriesShouldAlsoBeDeleted () {
//       productRepository.deleteAll();
//        assert(categoryRepository.count()).isEqualTo(2);
//        assert(productRepository.count()).isEqualTo(0);
//    }
//
//    @Test
//    public static void whenDeletingCategories_thenProductsShouldAlsoBeDeleted() {
//        categoryRepository.deleteAll();
//        assert(categoryRepository.count()).isEqualTo(0);
//        assert(productRepository.count()).isEqualTo(0);
//    }
//----------------------------------------------------------------------------------------------------
    private String productCategory;

    public Category(){}
    public Category(Product product,String productCategory){
        this.products=product;
        this.productCategory=productCategory;
        product.addCategory(this);//vease que al ser category parte del producto es necesario que esta clase forme
                                  //parte del mismo mediante la adicion de la primera a la segunda con un metodo
                                  // "addCategory(this)"
    }

//    public static void setProductRepository(CrudRepository<Product,Long> productRepository) {
//        Category.productRepository = productRepository;
//    }
//
//    public static void setCategoryRepository(CrudRepository<Category,Long> categoryRepository) {
//        Category.categoryRepository = categoryRepository;
//    }

//////////////////////////////////////////////getters/////////////////////////////////////
    public Long getId() {
        return id;
    }
    public String getProductCategory() { return productCategory;}
    public Product getProduct() {
        return products;
    }

//////////////////////////////////////setters//////////////////////////////////////////////////
    public void setId(Long id) {
        this.id = id;
    }
    public void setProduct(Product product) {this.products = product; }
    public void setProductCategory(String productCategory) {  this.productCategory = productCategory; }
}
