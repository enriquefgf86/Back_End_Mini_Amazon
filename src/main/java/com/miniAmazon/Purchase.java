package com.miniAmazon;


import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;

@Entity
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "native")
    @GenericGenerator(name="native",strategy="native")
    private Long id;

//    @OneToMany(mappedBy = "purchases",fetch= FetchType.EAGER)
    @ManyToMany
    @JoinTable(name="conectprodpurch",joinColumns = @JoinColumn(name= "purchase_id"),
            inverseJoinColumns = @JoinColumn(name="product_id"))
    private List<Product> productsSet= new ArrayList<>();

    @JsonFormat(pattern="yyyy-MM-dd")
    private Date date;

//    private Date date;
    private Integer numberPurchase;
    private Double pricePurchase;
    private Double discountPurchase;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="users_id")
    private User users;

    public Purchase(){}
    public Purchase(Date date,User user,Integer numberPurchase,Double pricePurchase,Double discountPurchase,List<Product>product){
        user.addPurchase(this);//adicionando a la clase usuario la compra y todos sus productos
        this.discountPurchase=discountPurchase;
        this.numberPurchase=numberPurchase;
        this.pricePurchase=pricePurchase;
        this.date=date;
        this.users=user;
        this.productsSet=product;
        //OJO//
        product.forEach(product1 -> product1.addPUrchase(this));////para determinar que producto ha estado envuelto en una
    }                                                            ///compra especifica

    public void addProduct(Product product){
        productsSet.add(product);
    }

    /////////////////////////////setters///////////////////////////////
    public void setDate(Date date) { this.date = date; }
    public void setId(Long id) {  this.id = id;}
    public void setProductsSet(List<Product> productsSet) {this.productsSet = productsSet; }
    public void setNumberPurchase(Integer numberPurchase) {this.numberPurchase = numberPurchase;}
    public void setPricePurchase(Double pricePurchase) {this.pricePurchase = pricePurchase;}
    public void setDiscountPurchase(Double discountPurchase) {this.discountPurchase = discountPurchase; }
    public void setUsers(User users) {this.users = users;}

    ///////////////////////////////getters/////////////////////////////////////
    public Long getId() { return id;}
    public Integer getNumberPurchase() {return numberPurchase;}
    public List<Product> getProductsSet() { return productsSet;}
    public Double getPricePurchase() {return pricePurchase;}
    public Double getDiscountPurchase() {return discountPurchase;}
    public User getUsers() {return users;}
    public Date getDate() { return date;}

    @Override
    public String toString() {
        return "Purchase{" +
                "id=" + id +
                ", products=" + productsSet +
                ", numberPurchase=" + numberPurchase +
                ", pricePurchase=" + pricePurchase +
                ", discountPurchase=" + discountPurchase +
                ", user=" + users +
                '}';
    }
}

