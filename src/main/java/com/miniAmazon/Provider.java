//package com.miniAmazon;
//
//import org.hibernate.annotations.GenericGenerator;
//
//import javax.persistence.*;
//import java.util.HashSet;
//import java.util.Set;
//
//@Entity
//public class Provider {
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO,generator = "native")
//    @GenericGenerator(name="native",strategy="native")
//    private Long id;
//
//    @OneToMany(mappedBy = "providers",fetch= FetchType.EAGER)
//    Set<Product> providerProductsSet= new HashSet<>();
//
//    private String providerRole;
//    private String providerName;
//    private String providerEmail;
//    private String providerPassword;
//
//    public Provider(){}
//    public Provider(String providerRole,String providerName,String providerEmail,String providerPassword){
//        this.providerRole=providerRole;
//        this.providerName=providerName;
//        this.providerEmail=providerEmail;
//        this.providerPassword=providerPassword;
//
//    }
//    public void addProductProv(Product product){providerProductsSet.add(product);}
//    public Set<Product>getProvidersProductsSet() { return providerProductsSet;}
//    //////////////////////////////////setters////////////////////////////////////////
//    public void setId(Long id) { this.id = id;}
//    public void setProviderProductsSet(Set<Product> providerProductsSet) {this.providerProductsSet = providerProductsSet;}
//    public void setProviderRole(String providerRole) {this.providerRole = providerRole;}
//    public void setProviderName(String providerName) {this.providerName = providerName;}
//    public void setProviderEmail(String providerEmail) {this.providerEmail = providerEmail;}
//    public void setProviderPassword(String providerPassword) { this.providerPassword = providerPassword;}
//    //////////////////////////////////getters/////////////////////////////////////////////
//    public Long getId() {return id;}
//    public Set<Product> getProviderProductsSet() {return providerProductsSet;}
//    public String getProviderRole() {return providerRole;}
//    public String getProviderName() {return providerName;}
//    public String getProviderEmail() {return providerEmail;}
//    public String getProviderPassword() {return providerPassword;}
//}
