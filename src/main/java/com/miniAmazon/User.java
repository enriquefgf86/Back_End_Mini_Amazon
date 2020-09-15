package com.miniAmazon;


import org.hibernate.annotations.GenericGenerator;
//import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)//yoque aqui
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "native")
    @GenericGenerator(name="native",strategy="native")
    private Long id;

//    @OneToMany(mappedBy = "providers",fetch= FetchType.EAGER)
//    Set<Product> productsProvidersSet= new HashSet<>();

    @OneToMany(mappedBy = "users",fetch= FetchType.EAGER)
    List<Product> productsSet= new ArrayList<>();

    @OneToMany(mappedBy = "providers",fetch= FetchType.EAGER)
    Set<Product> providerProductsSet= new HashSet<>();

    @OneToMany(mappedBy="users", fetch=FetchType.EAGER)
    Set<Purchase> purchaseSet=new HashSet<>();

    private String userRole;
    private String userName;
    private String userEmail;
    private String userPassword;

    public User(){}
    public User( String userEmail,String userName,String userPassword,String userRole){
       this.userName=userName;
       this.userEmail=userEmail;
       this.userPassword=userPassword;
       this.userRole=userRole;
    }

    public void addPurchase(Purchase purchase){ purchaseSet.add(purchase);}
    public Set<Purchase>getUserPurchaseSet(){
        return purchaseSet;
    }

    public void addProduct(Product product){productsSet.add(product);}
    public List<Product>getUserProductsSet() { return productsSet;}

    public void addProductProv(Product product){providerProductsSet.add(product);}
    public Set<Product>getProvidersProductsSet() { return providerProductsSet;}

    ////////////////////////////////////////////setter/////////////////////////////////////////////
    public void setId(Long id) { this.id = id;}
    public void setProductsSet(List<Product> productsSet) {this.productsSet = productsSet; }
    public void setPurchaseSet(Set<Purchase> purchaseSet) { this.purchaseSet = purchaseSet; }
    public void setUserRole(String userRole) {this.userRole = userRole;}
    public void setUserName(String userName) {this.userName = userName;}
    public void setUserEmail(String userEmail) {this.userEmail = userEmail;}
    public void setUserPassword(String userPassword) {this.userPassword = userPassword; }
    public void setProviderProductsSet(Set<Product> providerProductsSet) {  this.providerProductsSet = providerProductsSet;}
    ///////////////////////////////////////////getters////////////////////////////////////////////////
    public Long getId() {return id;}
    public List<Product> getProductsSet() { return productsSet;}
    public Set<Purchase> getPurchaseSet() { return purchaseSet;}
    public String getUserRole() { return userRole;}
    public String getUserName() { return userName;}
    public String getUserEmail() {return userEmail;}
    public String getUserPassword() { return userPassword;}
    public Set<Product> getProviderProductsSet() {  return providerProductsSet; }
    ///////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", productsSet=" + productsSet +
                ", purchaseSet=" + purchaseSet +
                ", userRole='" + userRole + '\'' +
                ", userName='" + userName + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userPassword='" + userPassword + '\'' +
                '}';
    }


}
