package com.miniAmazon;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;


@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "native")
    @GenericGenerator(name="native",strategy="native")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="users_id")
    private User users;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="providers_id")
    private User providers;

//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name="purchases_id")

    @ManyToMany(mappedBy = "productsSet")
    private Set<Purchase> purchases=new HashSet<>();

    @OneToMany(mappedBy = "products",fetch= FetchType.EAGER,cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Category> categorySet= new HashSet<>();


    @ElementCollection
    @Column(name="imagesURL")
    private List<String>imgURLList=new ArrayList<>();

    private String productName;
    private Double productPrice;
    private Integer productStock;
    private String productDescription;
    private Double productRate=0.0;
    private Integer peopleRating=0;

    public Product(){}
    public Product(
             String productName, Double productPrice
           , List<String>imgURLList, String productDescription,Integer peopleRating
           , Double productRate, Integer productStock,User user,User provider){
//       user.addProduct(this);
//       provider.addProductProv(this);
       this.productName=productName;
       this.productDescription=productDescription;
       this.productPrice=productPrice;
       this.productRate=productRate;
       this.productStock=productStock;
       this.imgURLList=imgURLList;
       this.users=user;
       this.peopleRating=peopleRating;
       this.providers=provider;
    }

    public void addCategory(Category category){categorySet.add(category);};
    public Set<Category>getProductCategorySet(){return categorySet;}

    public void addPUrchase(Purchase purchase){
        this.purchases.add(purchase);
    }

    ///////////////////////////////////// setters/////////////////////////////////////
    public void setId(Long id) { this.id = id;}
    public void setUsers(User users) {this.users = users; }
    public void setProviders(User providers) {this.providers = providers;}
    public void setPurchases(Set<Purchase> purchases) {this.purchases =purchases;}
    public void setCategorySet(Set<Category> categorySet) {this.categorySet = categorySet;}
    public void setImgURLList(List<String> imgURLList) {this.imgURLList = imgURLList;}
    public void setProductName(String productName) {this.productName = productName;}
    public void setProductPrice(Double productPrice) { this.productPrice = productPrice;}
    public void setProductStock(Integer productStock) {this.productStock = productStock;}
    public void setProductDescription(String productDescription) {this.productDescription = productDescription;}
    public void setProductRate(Double productRate) {this.productRate = productRate;}
    public void setPeopleRating(Integer peopleRating) { this.peopleRating = peopleRating; }

    /////////////////////////////////////////getters///////////////////////////////////////////
    public Long getId() {return id;}
    public User getUsers() {return users; }
    public User getProviders() {return providers;}
    public Set<Purchase> getPurchases() {return purchases;}
    public Set<Category> getCategorySet() { return categorySet;}
    public List<String> getImgURLList() {return imgURLList;}
    public String getProductName() {return productName; }
    public Double getProductPrice() {return productPrice; }
    public Integer getProductStock() {return productStock; }
    public String getProductDescription() {return productDescription;  }
    public Double getProductRate() { return productRate; }
    public Integer getPeopleRating() { return peopleRating; }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", users=" + users +
                ", providers=" + providers +
                ", purchases=" + purchases +
                ", categorySet=" + categorySet +
                ", imgURLList=" + imgURLList +
                ", productName='" + productName + '\'' +
                ", productPrice=" + productPrice +
                ", productStock=" + productStock +
                ", productDescription='" + productDescription + '\'' +
                ", productRate=" + productRate +
                ", peopleRating=" + peopleRating +
                '}';
    }
}
