package com.miniAmazon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
//import java.util.function.BiConsumer;
//import java.util.function.Function;
import java.util.stream.Collectors;
//import java.util.stream.DoubleStream;
//import java.util.stream.IntStream;

@RestController
public class DemoController {

    @Autowired
     private ProductRepository productRepository;
    @Autowired
    private PurchaseRepository purchaseRepository;
    @Autowired
    private UserRepository userRepository;
//    @Autowired
//    private ProviderRepository providerRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CategoryRepository categoryRepository;

/////////////////////////////////acceso a todos los productos////////////////////////////////////////////////////
    @RequestMapping(value="/mini/all_products/user",method = RequestMethod.GET)
    public Map<String,Object> getProductsDetails(Authentication authentication){
        Map<String,Object> dto=new HashMap<>();
        if(productRepository.findAll().size()==0){
            dto.put("products",null);
        }
        else{
            dto.put("products",productRepository.findAll().stream().filter(stock->stock.getProductStock()!=0).map(product -> makeProductDto(product)).collect(Collectors.toList()));

        }

        if(authentication==null){
          dto.put("user",null);
        }
        else{
            dto.put("user",makeUserDto(userDetailslogged(authentication)));
        }
        return dto;
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @RequestMapping(value="/mini/all_products/provider",method = RequestMethod.GET)
    public Map<String,Object> getProductsDetailsProvider(Authentication authentication){
        Map<String,Object> dto=new HashMap<>();
        dto.put("products",productRepository.findAll().stream().map(product -> makeProductDto(product)).collect(Collectors.toList()));
        if(authentication==null){
            dto.put("provider",null);

        }
        return dto;
    }

///////////////////////////////creandose la compra por parte del usuario con los productos y precios de la misma/////////////////////////
    @RequestMapping(value="/mini/all_products/allpurchase_view",method = RequestMethod.POST)
    public ResponseEntity <Map<String,Object>>addNewPurchase( Authentication authentication, @RequestBody List<Long> products){
        Map<String,Object> dto=new HashMap<>();
        User user=userDetailslogged(authentication);

        if(authentication==null){
            return new ResponseEntity<>(makeMapResponse("Error","User No Authenticated"), HttpStatus.UNAUTHORIZED);
        }
        if(user==null){
            return new ResponseEntity<>(makeMapResponse("Error","Register This User Please "), HttpStatus.UNAUTHORIZED);
        }

        Double pricePurchase=0.0;//declaring variable which abridge purchase cost

        List<Product>purchaseProducts=new ArrayList<>();//declaring array which will include all products  adeed to that puechase

        for (Long id : products){// se realiza el loop sobre el set de ids de productos recibidos desde el front end como compras
                                 //vease que la variable se declara como long pues dichos products recibidos desde el front end ,
                                  //son mandados como ids.
             Product product=productRepository.getOne(id); //se delcara una variable dentro de este loop referente al repositorio
                                                           //de productos pues sobre dicha base es donde se obtendran los productos

             product.setProductStock(product.getProductStock()-1);//vease que al ser una compra , y al haberse delarado la variable product
                                                                   //como acceso a la base de datos para el id de ese product especifico para
                                                                  //el momento en que el loop coincide con dicho i simplemente e estable(set...) una nueva
                                                                  //stock de productos en inventario restando 1 al monto existente que se obtiene
                                                                   //a traves de la variable en si(product.getProductStock()-1)

             productRepository.save(product);//una vez terminada la operacion se salva el producto de nuevo en la base de datos con su stock actualizado

             pricePurchase=pricePurchase+product.getProductPrice();//a su vez , vease que por cada vuelta  la variable previamente creada pricePurchase
                                                                   //se le suma el precio del nuevo producto obtenido mediante id del repositorio de productos
                                                                   //actualizandose tambien el precio de la compra

            purchaseProducts.add(product);//lo mismo sicede con la variable previamente creada como arrayList purchase products , pues a la misma , por cada
                                          //vuelta se le adiciona el producto obtenido desde el repositorio conformando y actualizando dicho arrayList
                                          //por cada vuelta.
        }
//DESCUENTO PARA MAS DE 5 ELEMENTOS EN LA CESTA DE COMPRA
        Double DiscountMore5=0.0;
        if(purchaseProducts.size()>5){
            DiscountMore5=pricePurchase*0.10;
        }
        else DiscountMore5=0.0;//previamente conformado el array de productos de compra mediante el previo loop , se procede a determinar entonces
                               //una espece de descuento por mas de 5 elementos en la compra , para eso se declara una variable de tipo double
                               // ( DiscountMore5) y se inicializa una condicion booleana en donde se determina si el previo array de productos
                               // y su longitud(length o size) es mayor a 5, donde de ser asi entonces se estableceria dichpo descuento sobre
                               // un 10% del total de compras previamente determinado y almacenado en la variable pricePurchase

//PROCESO DE DETERMINACION DESCUENTO SOBRE MAS DE 4 PRODUCTSO REPETIDOS DEJANDO UNO LIBRE
    //PASO1--Construyendo objeto parsa determinar que productos se repiten
        Map<Long,Integer> counter=new HashMap<Long,Integer>();//vease que lo primero que secrea es una variable llamada counter , la cual albergaria
                                                   //un objeto de formato mapeable con una estructura de Long(por el list de ids recibidos del
                                                    //frontend, y Integer, (para determinar el nuemo de veces que dicho id se repite )

        Double discountEqual4=0.0;//vease que se determina la variable de tipo Double la cual determinaria el descuento

        for(Long productId:products){//otra vex se inicaliza un lopp sobre el listado de ids de productos recibidos desde el front end ,
                                     //determinandose como variable iterante Long productId

             if(counter.containsKey(productId)){
                 counter.put(productId,counter.get(productId)+1);
             }//vease que se establece una condicion para ir formando dicho objeto.Primero se determina , si el objeto vacio creado mediante variable
               //counter contiene en sus keys alguna similar a la misma que para ese momento la variable prodcut id esta iternaod.Entonces de ser asi
               //para esa vuelta en el loop se conformaria dicho objeto en su key con el mismo de variable product id, y el integer , entonces seria
               //la cantidad de productId para ese momento en la vuelta mediante metodo get(), sumandosele 1

             else{
                 counter.put(productId,1);
             }//de no lo contrario de no existir id similar para esa vuelata enonces se conformaria mediante el productId para esa vuelata y
              //su conteo inicial en 1
        }
//PASO2--determinando condiciones para decuento
        for(Map.Entry<Long,Integer> repeated :counter.entrySet()){//vease que en este caso se inicializa un loop sobre el objeto previamente creado
                                                                  //counter, y mediante al acceso al metod single key/value pair Map.entry se inicializa
                                                                  //una variable llamada repeated conformadsa por un single key/value(Long,Integer), la
                                                                  //cula iteraria sobre el objeto creado previamente llamado counter, en este caso, haciendose
                                                                  //iterable mediante el metodo entry set

            Product prods=productRepository.getOne(repeated.getKey());//vese que primero nuevamente se hace un llamado al repositorio de productos, aunque en este caso
                                                                      //el id al cual se hace mencion dentro del mismo seria el mismo que nos refiere el key que
                                                                      //el map entry de la variable repeated iterando sobre counter nos facilita en esa vuelta del loop

            if(repeated.getKey()==prods.getId()&&repeated.getValue()>=4){//acto seguido se establece una condicion en donde se compara el map.entry de la variable repeated
                                                                         //sobre el objeto counter para esa vuelta con el product del repositorio obtenido mediante
                                                                         //mediante variable prods, obteniendose su id ademas de determinar si para ese mismo map.entry para su
                                                                         //apartado value el valor del integer es mayor a 4(4 productso iguales , 1 libre)

                discountEqual4= discountEqual4+ prods.getProductPrice()*Math.floor(repeated.getValue()/4);
                //de cumplirse la condicion entonces se procede a lamacenar en la variable discountEqual4 previamente inicializada, la suma del valor existente en la misma
                //y el precio obtenido del producto(prods.getProductPrice()) para esa vuelta por la cantidia de productos que quedarian libres en ese sentido, a traves del uso
                //del metodo Math.floor sobre el map.entry de la variable repeated en su apartado value(repeated.getValue()) entre 4 por ser 4 el valor marcado para determinar
                //descuentos sobre productos repetidos
            }
        }

        Double totalDiscount=discountEqual4+DiscountMore5;//se suman ambos descuentos calculados mediante previos procesos

        Purchase purchase1=new Purchase(new Date(),user,products.size(),pricePurchase,totalDiscount,purchaseProducts);//se crea una nueva compra, acutualizandose varios
                                                                                                                      //elementos en su contructor, como son products.size(),
                                                                                                                      // pricePurchase,totalDiscount,purchaseProducts
                                                                                                                       //todos calculados en este proceso
        purchaseRepository.save(purchase1);//se slava dicha compra como nueva adicion en el repositorio de compras

        return new ResponseEntity<>(makeMapResponse("Success","Product Added"), HttpStatus.CREATED);
    }

////////////////////////////////adicionandose producto al sotock de productos en venta por parte del proveedor/////////////////////////////////////////////
    @RequestMapping(value="/mini/all_products/provider/product_added",method = RequestMethod.POST)
    public ResponseEntity <Map<String,Object>>addNewProductSale( Authentication authentication, @RequestBody Product productAdded){
        Map<String,Object> dto=new HashMap<>();
        User user=userDetailslogged(authentication);

        if(authentication==null){
            return new ResponseEntity<>(makeMapResponse("Error","User No Authenticated"), HttpStatus.UNAUTHORIZED);
        }
        if(user==null){
            return new ResponseEntity<>(makeMapResponse("Error","Register This User Please "), HttpStatus.UNAUTHORIZED);
        }
        if (user.getUserRole().equals("buyer")){
            return new ResponseEntity<>(makeMapResponse("Error","You don have permission for sell products"), HttpStatus.CONFLICT);
        }

       Product product=new Product(productAdded.getProductName(),productAdded.getProductPrice(),productAdded.getImgURLList(),
                                   productAdded.getProductDescription(),productAdded.getPeopleRating(),productAdded.getProductRate(),productAdded.getProductStock()
                                    ,null,user);
        //Al ser mediante request method post , pues es un nuevo producto que se crea y que se emite mediante un requestbody al backend  simplemente se
        //crea una variable de tipo Product que inicializaria un constructor para dicho product en su repositorio , y que iria conformando el constructor mediante
        //el acceso a la variable previamente recibida del request body llamada productAdded, en donde a traves de la misma se accedria a los diferentes items que conforman
        //el producto recibidop en si(getProductName(),getProductPrice(),getImgURLList(), getProductDescription(),getPeopleRating(),getProductRate()and getProductStock().
        //Vease que a pesar de que el producto tiene categorias la misma no se crea en este contructor pues fue creada en una clase aparte de ahi que la misma
        //lleve un proceso aparte.

       productRepository.save(product);//acto seguido se salva el producto cread en el repositorio de Product.

       for(Category category : productAdded.getProductCategorySet()){
             categoryRepository.save(new Category(product,category.getProductCategory()));
           //Al ser category una clase distainta no incluida en el constructor del product se hace necesario
           // crear un avariable de tipo Category, la cual iterando sobre el request body mandado desde el
           //front-end a traves de la variable productAdded se accede al set de categories , entonces iterando
           //sobre este , creandose y guardando para el repositorio de categorias en cada vuelta
           //una categoria  para ese producuto en si
       }
       productRepository.save(product);//vease que se slava el repositorio de producto despues de todas las operaciones hechas

       return new ResponseEntity<>(makeMapResponse("Success","Product Added"), HttpStatus.CREATED);
    }

    /////////////////////////////////////Borranndo productos del sock existente del proveedor/////////////////////////////////////////
    @RequestMapping(value="/mini/all_products/delete/{id}",method=RequestMethod.POST)
    public ResponseEntity <Map<String,Object>>getProductDeleted(@PathVariable ("id") Long id, Authentication authentication,
                                                                @RequestBody Product productDeleted){
        Map<String,Object> dto=new HashMap<>();
        User user=userDetailslogged(authentication);

        if(authentication==null){
            return new ResponseEntity<>(makeMapResponse("Error","User No Authenticated"), HttpStatus.UNAUTHORIZED);
        }
        if(user==null){
            return new ResponseEntity<>(makeMapResponse("Error","Register This User Please "), HttpStatus.UNAUTHORIZED);
        }
//        if(user.getUserRole()!="seller"||user.getUserRole()=="seller"&&!productDeleted.getProviders().getUserName().equals(user.getUserName())){
//            return new ResponseEntity<>(makeMapResponse("Error","This isn't one of your products you can't delete it"), HttpStatus.CONFLICT);
//        }

        Product product=productRepository.getOne(id);
        Integer del=productDeleted.getProductStock()*0;
        product.setProductStock(del);
        productRepository.save(product);//vease que el proceso en general en este caso se inicializa por la creacion de una variable de tipo Product, a la cual se
                                        //se le asignaria.Entonces se accederia a dicho producto en cuestion mediante su id en el repositorio de products.
                                        //acto seguido vease que se declara una variable de tipo Integer llmada del , en donde a la misma se le asigana el valor
                                        //que surga de la multiplalcion del producto traido desde el frontend a traves del request body productDeleted y su exitencia en
                                        //en general (getProductStock()) por cero , llevando de hecho dicho stock a cero.
                                        //una vez hecho este paso entonces se procederia a establecer para la variable product la cual previamente se le asigno el producto
                                         //id desde su repositorio el Stock de productos existentes , el cual se estableceria mediante setter (setProductStock) y como aragumento
                                         //entonces tendria la variable del previamente inicialiazada y calculada a valor 0, siendo este el nuevo valor en existencias para el
                                         //producto en si, siendo lo mismo que si no existiera.
                                         //Posteriormente se procede a slavar dicho producto en e repositorio.

        return new ResponseEntity<>(makeMapResponse("Success","Product Deleted"), HttpStatus.OK);
    }

    ////////////////////////////////////////////edit product/////////////////////////////////////////////////////////
    @RequestMapping(value="/mini/all_products/provider/product_edited/{id}",method = RequestMethod.POST)
    public ResponseEntity <Map<String,Object>>editproductOnsale(@PathVariable("id") Long id,  Authentication authentication,
                                                                  @RequestBody Product productEdited){
        Map<String,Object> dto=new HashMap<>();
        User user=userDetailslogged(authentication);

        if(authentication==null){
            return new ResponseEntity<>(makeMapResponse("Error","User No Authenticated"), HttpStatus.UNAUTHORIZED);
        }
        if(user==null){
            return new ResponseEntity<>(makeMapResponse("Error","Register This User Please "), HttpStatus.UNAUTHORIZED);
        }

        Product product=productRepository.getOne(id);

//        Product productNotTouched=productRepository.getOne(id);//aqui
//        if(productEdited.getProductStock()==null||productEdited.getProductStock()==0){
//            product.setProductStock(productNotTouched.getProductStock());
//        }
//        else{
//            product.setProductStock(productEdited.getProductStock());
//        }
//        if(productEdited.getProductDescription()==null||productEdited.getProductDescription().equals(0)){
//            product.setProductDescription(productNotTouched.getProductDescription());
//        }
//        else{
//            product.setProductDescription(productEdited.getProductDescription());
//        }
//        if(productEdited.getImgURLList()==null||productEdited.getImgURLList().equals(0)){
//            product.setImgURLList(productNotTouched.getImgURLList());
//        }
//        else{
//            product.setImgURLList(productEdited.getImgURLList());
//        }
        product.setProductStock(productEdited.getProductStock());
        product.setProductDescription(productEdited.getProductDescription());
        product.setImgURLList(productEdited.getImgURLList());
        //este caso seria parecido al caso anterior  pues a traves de una variable llamada productedited obtenida desde el front-end mediante request body
        // se reciben datos que serianmodificadores de la variable que accede al repositorio de productos mediante su id, dandole una nueva gama de datos
        // utilizando los metodos setters que tendrian entonces como parametros el request body con cada uno de sus getters

        productRepository.save(product);//se salva el producto editado en el repositorio

        return new ResponseEntity<>(makeMapResponse("Success","Product Edited"), HttpStatus.CREATED);
    }

    ////////////////////////////////////Dando rating al producto///////////////////////////////////////////////
    @RequestMapping(value="/mini/all_products/user/product_rated/{id}/{voter}",method = RequestMethod.POST)
    public ResponseEntity <Map<String,Object>>rateProductOnBuy(@PathVariable("id") Long id, @PathVariable("voter") String voter, Authentication authentication
                                                                ,@RequestBody Product productRated
    ){
        Map<String,Object> dto=new HashMap<>();
        User user=userDetailslogged(authentication);

        if(authentication==null){
            return new ResponseEntity<>(makeMapResponse("Error","User No Authenticated"), HttpStatus.UNAUTHORIZED);
        }
        if(user==null){
            return new ResponseEntity<>(makeMapResponse("Error","Register This User Please "), HttpStatus.UNAUTHORIZED);
        }
        if(user.getUserRole().equals("seller")&&user.getUserName().equals(voter)){
            return new ResponseEntity<>(makeMapResponse("Error","Sorry you cant rate on your own product "), HttpStatus.CONFLICT);
        }

        Product product=productRepository.getOne(id);//Se determina mediante el id el producto al cual se sometera la votacion desde el repositorio.

        Double deframingRate=product.getProductRate()*product.getPeopleRating()+productRated.getProductRate();
        Integer deframingPeopleVoting=product.getPeopleRating()+1;
        Double finalAverage=deframingRate/deframingPeopleVoting;
        //basicamente en este proceso simplemente se procede a deconstruir el rate promedio obtenido hasta el momento albergado en la base de dato, y adicionarle
        //el nuevo rate venido desde el front-end mediante request body obtenido de la variable productRated.
        //Vease que en el primer caso se establece una relacion bruta de la multipalcacion del rate promedio y los votantes hasta ese momento a lo cual se le suma
        //el rate vendio desde el front-end.Despues se hace una sumatorioa de los votantes obtenidos hasta ese momento , mas los votantes venidos desde el front
        //end; y posteriormente , teniendo calculado ambos procesos se procederia a recalcular el rate promedio sobre la nueva cantidad de votantes y los nuevos votos
        //sumados , albergandose en la ultima variable de tipo Double finalAverage
        product.setProductRate(finalAverage);
        product.setPeopleRating(deframingPeopleVoting);
        //Vease entonces que se procede a determinar entonces el nuevo rate promedio y los numeros de votantes mediante el establecimiento en la variable del repositorio
        //product de setters tanto para obtrener el rate del producto como el numero de votantes , los cuales tendrian com parametros de resultado las variables previamente
        //claculadas para cada uno de los casos
        productRepository.save(product);//Se slava entonces el producto modificado

        return new ResponseEntity<>(makeMapResponse("Success","Product Rated"), HttpStatus.CREATED);
    }
////////////////////////////////////Determinando  la vista previa de una compra hecha en particular por el usuario///////////////////////////////////////////////
    @RequestMapping(value="/mini/all_products/one_selected/purchase_view/{id}",method = RequestMethod.GET)
    public ResponseEntity <Map<String,Object>> getUserPrePurchaseBoard(@PathVariable("id") Long id, Authentication authentication
                                                                 ){
        Map<String,Object> dto=new HashMap<>();
        User user=userDetailslogged(authentication);
        Purchase purchase=purchaseRepository.getOne(id);

        if(authentication==null){
            return new ResponseEntity<>(makeMapResponse("Error","User No Authenticated"), HttpStatus.UNAUTHORIZED);
        }
        if(user==null){
            return new ResponseEntity<>(makeMapResponse("Error","Register This User Please "), HttpStatus.UNAUTHORIZED);
        }

        if(user.getUserPurchaseSet()==null||user.getUserPurchaseSet().equals(0)||user.purchaseSet.size()==0){
            dto.put("purchase_id","no purchases");
            dto.put("user_name",user.getUserName());
            dto.put("purchase_discount","no purchases");
            dto.put("details_of_purchase","no purchases");
            dto.put("price_purchase","no purchases");
        }
        else {
            dto.put("purchase_id",purchase.getId());
            dto.put("user_name",purchase.getUsers().getUserName());
            dto.put("purchase_discount",purchase.getDiscountPurchase());
            dto.put("details_of_purchase",purchase.getProductsSet().stream().sorted((purch1,purch2)->purch1.getId().compareTo(purch2.getId())).map(product1 -> makeProductDtoReducedMini(product1)).collect(Collectors.toList()));
            dto.put("price_purchase",purchase.getPricePurchase());
        }
        //En este metodo simplemente al ser un get se establecen las pautas que darian informacion acerca de una compra especifica realizada por el usuario
        //accediendose a la isma mediante su Path Varaible determinado en su id, conformandose los Dto correspondientes para su ulterior uso.

        return new ResponseEntity<>(dto,HttpStatus.OK);
    }

    //////////////////////////////////////Final Viev de cada compra hecah por el usario/////////////////////////////////////////////////
    @RequestMapping(value="/mini/all_products/user_dashboard/detail_each_purchase/final_view/{idPurchase}",method = RequestMethod.GET)
    public ResponseEntity <Map<String,Object>> getUserEachPurchasesBoardDeatil(@PathVariable("idPurchase") Long id, Authentication authentication
    ){
        Map<String,Object> dto=new HashMap<>();
        User user=userDetailslogged(authentication);
        Purchase purchase=purchaseRepository.getOne(id);

        if(authentication==null){
            return new ResponseEntity<>(makeMapResponse("Error","User No Authenticated"), HttpStatus.UNAUTHORIZED);
        }
        if(user==null){
            return new ResponseEntity<>(makeMapResponse("Error","Register This User Please "), HttpStatus.UNAUTHORIZED);
        }

        dto.put("user_name",user.getUserName());
        dto.put("purchase_id",purchase.getId());
        dto.put("purchase_price",purchase.getPricePurchase());
        dto.put("purchase_discount",purchase.getDiscountPurchase());
        dto.put("purchase_details",purchase.getProductsSet().stream().sorted((purch1,purch2)->purch1.getId().compareTo(purch2.getId())).map(product -> makeProductDto(product)));
         //Este metodo es copia fiel del anterior

        return new ResponseEntity<>(dto,HttpStatus.OK);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @RequestMapping(value="/mini/all_products/user_dashboard/final_view",method = RequestMethod.GET)
    public ResponseEntity <Map<String,Object>> getUserAllPurchasesBoard( Authentication authentication
    ){
        Map<String,Object> dto=new HashMap<>();
        User user=userDetailslogged(authentication);
        List<Purchase> purchase=purchaseRepository.findAll();

        if(authentication==null){
            return new ResponseEntity<>(makeMapResponse("Error","User No Authenticated"), HttpStatus.UNAUTHORIZED);
        }
        if(user==null){
            return new ResponseEntity<>(makeMapResponse("Error","Register This User Please "), HttpStatus.UNAUTHORIZED);
        }

        dto.put("user_name",user.getUserName());
        dto.put("all_user_purchases",user.getUserPurchaseSet().stream().sorted((purch1,purch2)->purch1.getId().compareTo(purch2.getId())).map(purchase1 -> makePurchaseDto(purchase1)));
        //en este proceso se realizaria lo mismo que el proceso anterior , pues simplemente al ser un requestmethod e tipo GET , su objetivo es disenar dtos que
        //brinden informacion que el usuario pueda accceder porsteriormente, en este caso no se muestra Path variable en general pues la informacion no requiere
        // un producto o id especifico, so se trae todo el collage de compras en si

        return new ResponseEntity<>(dto,HttpStatus.OK);
    }


////////////////////////////////////////////user registering //////////////////////////////////////////////////////////
    @RequestMapping(value="/mini/all_product/registering",method=RequestMethod.POST)
    public ResponseEntity <Map<String,Object>> registerUser(@RequestBody User user) {

        if (user.getUserName().isEmpty() || user.getUserPassword().isEmpty()|| user.getUserEmail().isEmpty()) {
            return new ResponseEntity<>( makeMapResponse("Error"," please fill all fields"), HttpStatus.FORBIDDEN);
        }

        if (userRepository.findByuserName(user.getUserName()) != null) {
            return new ResponseEntity<>(
                    makeMapResponse("Error"," please try with other one"), HttpStatus.FORBIDDEN);
        }
        user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));
        User newUser = userRepository.save(user);
        //Metodo clasico para establecer las pautas de seguridad en cuanto a registro del usuario

        return new ResponseEntity<>(makeMapResponse("id of user created", newUser.getId()), HttpStatus.CREATED);
    }

/////////////////////////////////RESPONSE ENTITY CONFORMER METHOD////////////////////////////////////////////
    private Map<String, Object> makeMapResponse(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
        //Este metodo es unua funcin complementaria que se utiliza despues como parte de los metodos anteriores en la conformacion de sus Responses Entities
    }

///////////////////////////////////////////////////DTO's//////////////////////////////////////////////////////
              ///////////////////////////DTO general para Product for User////////////////////////
    private Map<String,Object> makeProductDto(Product product){
        Map<String,Object> dto=new HashMap<>();
        dto.put("product_provider",product.getProviders().getUserName());
        dto.put("product_id",product.getId());
        dto.put("product_name",product.getProductName());
        dto.put("product_image",product.getImgURLList());
        dto.put("product_price",product.getProductPrice());
        dto.put("product_stock",product.getProductStock());
        dto.put("product_category",product.getProductCategorySet().stream().map(category -> makeCategoryDto(category)));
        dto.put("product_description",product.getProductDescription());
        dto.put("rate",product.getProductRate());
        dto.put("people_voting_in_rate",product.getPeopleRating());
//        dto.put("product_purchases",product.getUsers().getUserPurchaseSet().stream().map(purchase -> makePurchaseDto(purchase)).count());
//        dto.put("people_buying_this_product", product.getUsers().getUserName());

        return dto;
    }
    ///////////////////////////DTO general para Product for Provider////////////////////////

    private Map<String,Object> makeProductProviderDto(Product product){
        Map<String,Object> dto=new HashMap<>();
        dto.put("product_provider",product.getProviders().getUserName());
        dto.put("product_id",product.getId());
        dto.put("product_name",product.getProductName());
        dto.put("product_image",product.getImgURLList());
        dto.put("product_price",product.getProductPrice());
        dto.put("product_stock",product.getProductStock());
        dto.put("product_category",product.getProductCategorySet().stream().map(category -> makeCategoryDto(category)));
        dto.put("product_description",product.getProductDescription());
        dto.put("people_buying_this_product", product.getUsers().getUserName());

        return dto;
    }

    ///////////////////////////DTO general para Category////////////////////////
    private Map<String,Object> makeCategoryDto( Category category){
        Map<String,Object> dto=new HashMap<>();
        dto.put("categories_of_product",category.getProductCategory());
        return dto;
    }
    ///////////////////////////DTO general para User////////////////////////
    private Map<String,Object> makeUserDto(User user){
        Map<String,Object> dto=new HashMap<>();
        dto.put("user_id",user.getId());
        dto.put("user_name",user.getUserName());
        dto.put("user_role",user.getUserRole());
        dto.put("user_purchases",user.getUserPurchaseSet().stream().sorted((purch1,purch2)->purch1.getId().compareTo(purch2.getId())).map(purchase -> makePurchaseDtoMini(purchase)));
//        dto.put("user_products",user.getProductsSet().stream().map(product -> makeProductDto(product)).collect(Collectors.toList()));
        return dto;
    }
    ///////////////////////////DTO general para Provider////////////////////////
//    private Map<String,Object> makeProviderDto(Provider provider){
//        Map<String,Object> dto=new HashMap<>();
//        dto.put("provider_id",provider.getId());
//        dto.put("provider_name",provider.getProviderName());
//        dto.put("provider_role",provider.getProviderRole());
//        dto.put("provider_email", provider.getProviderEmail());
//        dto.put("provider_products",provider.getProviderProductsSet().stream().sorted((purch1,purch2)->purch1.getId().compareTo(purch2.getId())).map(product -> makeProductDto(product)));
//        return dto;
//    }
    ///////////////////////////DTO general para Purchase////////////////////////
    private Map<String,Object> makePurchaseDto(Purchase purchase){
        Map<String,Object> dto=new HashMap<>();

        dto.put("purchase_id",purchase.getId());
        dto.put("purchase_number",purchase.getNumberPurchase());
        dto.put("purchase_date",purchase.getDate());
        dto.put("purchase_price",purchase.getPricePurchase());
        dto.put("purchase_disccount",purchase.getDiscountPurchase());
        dto.put("products_in_purchase",purchase.getProductsSet().stream().map(product -> makeProductDtoReducedMini(product)));
        return dto;
    }
               ///////////////Sub DTO para Product////////////////////////
    private Map<String,Object> makeProductDtoReducedMini(Product product){
        Map<String,Object> dto=new HashMap<>();
        dto.put("product_name",product.getProductName());
        dto.put("product_price",product.getProductPrice());
        dto.put("product_imgs",product.getImgURLList());
        dto.put("product_id",product.getId());
        dto.put("rate",product.getProductRate());
        dto.put("people_voting_in_rate",product.getPeopleRating());
        return dto;
    }
    private Map<String,Object> makeProductDtoReducedProvider(Product product){
        Map<String,Object> dto=new HashMap<>();
        dto.put("product_provider",product.getProviders().getUserName());
        return dto;
    }
    private Map<String,Object> makeProductDtoReducedListImg(Product product){
        Map<String,Object> dto=new HashMap<>();
        dto.put("product_img",product.getImgURLList());
        return dto;
    }
    ///////////////Sub DTO para Purchase////////////////////////
    private Map<String,Object> makePurchaseDtoMini(Purchase purchase){
        Map<String,Object> dto=new HashMap<>();
        dto.put("purchase_id",purchase.getId());
        dto.put("date",purchase.getDate());
        dto.put("products_in_purchase",purchase.getProductsSet().stream().map(product -> makeProductDtoReducedMini(product)));

        return dto;
    }
/////////////////////////////////////////Logic//////////////////////////////////////////////////////////////////////////
//    private Map<String,Object> makeUserPurchaseStatus(Purchase purchase,Authentication authentication,Product product){
//        Map<String,Object> dto=new HashMap<>();
//        double discount=0.10;
//        User user=userDetailslogged(authentication);
//        if(user==null){
//            dto.put("state","User not logged in");
//        }
//        else{
//            if(user.getProductsSet().size()>5){
//               dto.put("discount",purchase.getPricePurchase()-purchase.getPricePurchase()*discount);
//            }
//
//        }
//        return dto;
//    }
//////////////////////////////////////////Private Methods//////////////////////////////////////////////////////////////////////
    private User userDetailslogged(Authentication authentication){
       return userRepository.findByuserName(authentication.getName());
    }//Metodo complementario utilizado para establecer las pautas de seguridad en cuanto a saber si el usuario es autentificado o no


}
