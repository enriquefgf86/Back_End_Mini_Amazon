package com.miniAmazon;

//import com.fasterxml.jackson.annotation.JsonFormat;
//import org.hibernate.mapping.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Date;


@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
	@Bean
	public PasswordEncoder passwordEncoder(){
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
//	@Bean
//	public CommandLineRunner initData(ProductRepository productRepository, UserRepository userRepository,
//									  CategoryRepository categoryRepository, PurchaseRepository purchaseRepository
//									  ){
//		return(args)->{
//
//
//			User user1=new User("lolo@gmail.com","lolo gomex",passwordEncoder().encode("24"),"buyer");
//			User user2=new User("jhony@gmail.com","Jack Ripper",passwordEncoder().encode("mole"),"buyer");
//			User user3=new User("gothic@gmail.com","demo gago",passwordEncoder().encode("adu"),"buyer");
//			User user4=new User("grau@gmail.com","grau gomex",passwordEncoder().encode("24"),"buyer");
//			User user5=new User("goiy@gmail.com","divan Ripper",passwordEncoder().encode("mole"),"buyer");
//			User user6=new User("gatti@gmail.com","guti gago",passwordEncoder().encode("adu"),"buyer");
//			User user7=new User("pp@gmail.com","pepeito",passwordEncoder().encode("rol"),"seller");
//			User user8=new User("nappy@gmail.com","nappy",passwordEncoder().encode("nap"),"seller");
//			User user9=new User("dovisioso@gmail.com","dupree",passwordEncoder().encode("pre"),"seller");
//			User user10=new User("jic@gmail.com","tupac shakur",passwordEncoder().encode("23"),"seller");
//			User user11=new User("duval@gmail.com","dr dree",passwordEncoder().encode("mola"),"seller");
//			User user12=new User("ram@gmail.com","wu tang clan",passwordEncoder().encode("ion"),"seller");
//
//			userRepository.save(user1);
//			userRepository.save(user2);
//			userRepository.save(user3);
//			userRepository.save(user4);
//			userRepository.save(user5);
//			userRepository.save(user6);
//			userRepository.save(user7);
//			userRepository.save(user8);
//			userRepository.save(user9);
//			userRepository.save(user10);
//			userRepository.save(user11);
//			userRepository.save(user12);
//
//			Product product1=new Product("Chain Saw",153.40, Arrays.asList
//					("https://mobileimages.lowes.com/product/converted/024761/024761039086xl.jpg",
//					"https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcRlwsePxcq1xEGXrXBFBw_61vxPQSWz875q-4uevw6qig-eDTLi",
//					"https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcRz4kRU3uKFow4GL4L4Ckf_zk6ATZZa3dbIzIb-cBZSF3-R2-eg"),
//					"Wood cutter good for masacre humans too",13,3.0,26,user2,user10);
//			Product product2=new Product("Chuky",60.00, Arrays.asList
//					("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcSBUAZm88FkZR9eyKQQTkobTm0p3AjYz0g5pHoh3uWF5CnE3wsU",
//					"https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcSoHpxzJuU-GEtb0uyY0AG6gr4QYy_LOJHCxUdwGB4pTODqV4VH",
//					"https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQKnw9FM7zNhIwCsxfGL1x99kJRDFTo4BzM2WVYU-kBqVXIjINv"),
//					"Bloody male doll ",130,5.0,100,user1,user10);
//			Product product3=new Product("Glasses",3.25, Arrays.asList
//					("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQTkZv-Y9TBv3fJ5GFAt9Rr1_ZN3njRuVo0ovKOhxWgUN9gImFj",
//					"https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcR6wemKFiyZ7BhdaA6p3u3mU0HH8j5vilGdTEetx2fSSd_bnTif",
//					"https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcSZMkHBypxRv7bY2JRe8rKxYbIf9navXlDuYkTuXE8ehXLSuWmi"),
//					"made of Finest Cristal",8,5.0,2000,user2,user10);
//			Product product4=new Product("Tile",153.40, Arrays.asList
//					("https://www.textures.com/system/categories/35904/frontend-large.jpg",
//					"https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcRbvayBwMvxaZaZdkuPUBR1Y_4WD0PgR9bKHyQQ5HTlXF373OQI",
//					"https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcSYByeR1hhNH2JpQrNOmCyqDI3tIZDwf3BJXStFiLxdgr1LJvCe"),
//					"Wood cutter good for masacre humans too",200,5.0,26,user2,user12);
//			Product product5=new Product("Mouse",60.00, Arrays.asList
//					("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcTaU_Cbo1XVHmoqyiwcTS1zi_TU2oPAOM8V4Cnpg4AR4ZIsjFS2",
//					"https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcTWZyayx4M9lQYdcdMMkLbEBqIHyoftzr5sFHS1ELoYVK7iG0a_",
//					"https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQmh0wYEJCNJuAiQsXzs3Ze0WgvN8Zr0X35PkM5tWVfICkHHB9o"),
//					"Bloody male doll ",21,3.0,100,user2,user11);
//			Product product6=new Product("rino",3.25, Arrays.asList
//					("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcRSg8gS2vq8utS1HvTL3W3fcfIUbiu0ND7r4VrmF5TJMJLe5oJX",
//					"https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQ4Tc0qKI-T5oLD42INXjdhtJrI1Ps1iVc7YhQj8RXtjCO6YYQx",
//					"https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcRWmbcCy3kvHp0iNAZTsQU6HIu6TJOo76HKzhFrl5SUpdE83Weq"),
//					"made of Finest Cristal",234,4.0,2000,user2,user12);
//			Product product7=new Product("Chain Saw",153.40, Arrays.asList
//					("https://mobileimages.lowes.com/product/converted/024761/024761039086xl.jpg",
//					"https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcRlwsePxcq1xEGXrXBFBw_61vxPQSWz875q-4uevw6qig-eDTLi",
//					"https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcRz4kRU3uKFow4GL4L4Ckf_zk6ATZZa3dbIzIb-cBZSF3-R2-eg"),
//					"Wood cutter good for masacre humans too",12,1.0,25,user2,user12);
//			Product product8=new Product("Chain Saw",153.40, Arrays.asList
//					("https://mobileimages.lowes.com/product/converted/024761/024761039086xl.jpg",
//					"https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcRlwsePxcq1xEGXrXBFBw_61vxPQSWz875q-4uevw6qig-eDTLi",
//					"https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcRz4kRU3uKFow4GL4L4Ckf_zk6ATZZa3dbIzIb-cBZSF3-R2-eg"),
//					"Wood cutter good for masacre humans too",120,1.5,24,user3,user12);
//			Product product9=new Product("Chain Saw",153.40, Arrays.asList
//					("https://mobileimages.lowes.com/product/converted/024761/024761039086xl.jpg",
//					"https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcRlwsePxcq1xEGXrXBFBw_61vxPQSWz875q-4uevw6qig-eDTLi",
//					"https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcRz4kRU3uKFow4GL4L4Ckf_zk6ATZZa3dbIzIb-cBZSF3-R2-eg"),
//					"Wood cutter good for masacre humans too",49,1.0,23,user4,user12);
//			Product product10=new Product("rino",3.25,	Arrays.asList
//					("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcRSg8gS2vq8utS1HvTL3W3fcfIUbiu0ND7r4VrmF5TJMJLe5oJX",
//					"https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQ4Tc0qKI-T5oLD42INXjdhtJrI1Ps1iVc7YhQj8RXtjCO6YYQx",
//					"https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcRWmbcCy3kvHp0iNAZTsQU6HIu6TJOo76HKzhFrl5SUpdE83Weq"),
//					"made of Finest Cristal",3,3.0,2000,user2,user10);
//
//			productRepository.save(product1);
//			productRepository.save(product2);
//			productRepository.save(product3);
//			productRepository.save(product4);
//			productRepository.save(product5);
//			productRepository.save(product6);
//			productRepository.save(product7);
//			productRepository.save(product8);
//			productRepository.save(product9);
//			productRepository.save(product10);
//
//			Category category1=new Category(product1,"Danger");
//			Category category2=new Category(product1,"Good");
//			Category category3=new Category(product1,"Homer");
//			Category category4=new Category(product2,"Danger");
//			Category category5=new Category(product3,"Good");
//			Category category6=new Category(product6,"Homer");
//
//			categoryRepository.save(category1);
//			categoryRepository.save(category2);
//			categoryRepository.save(category3);
//			categoryRepository.save(category4);
//			categoryRepository.save(category5);
//			categoryRepository.save(category6);
//
//			Purchase purchase1=new Purchase(new Date(),user1,6,770.25,0.00,Arrays.asList(product1,product3,product1,product1,product1,product1));
//			Purchase purchase2=new Purchase(new Date(),user1,6,546.80,0.00,Arrays.asList(product2,product2,product2,product2,product1,product1));
//			Purchase purchase3=new Purchase(new Date(),user5,6,583.45,0.00,Arrays.asList(product7,product8,product9,product1,product1,product6));
//			Purchase purchase4=new Purchase(new Date(),user2,1,3.25,0.00,Arrays.asList(product3));
//
//			purchaseRepository.save(purchase1);
//			purchaseRepository.save(purchase2);
//			purchaseRepository.save(purchase3);
//			purchaseRepository.save(purchase4);
//
//		};
//	}
}
@Configuration
@EnableWebSecurity
class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {
	@Autowired
	UserRepository userRepository;

	@Override
	public void init(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(inputName-> {
			User user =userRepository.findByuserName(inputName);
			if (user != null) {
				return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getUserPassword(),
						AuthorityUtils.createAuthorityList("USER"));
			}
			else {
				throw new UsernameNotFoundException("Unknown user: " + inputName);
			}
		});
	}
}
@Configuration
@EnableWebSecurity
class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors();
		http.authorizeRequests()
				.antMatchers("/mini/all_products/user").permitAll()
				.antMatchers("/mini/all_products/provider").permitAll()
				.antMatchers("mini/all_products/user_dashboard/purchase/{id}").permitAll()
				.antMatchers("/mini/all_products/allpurchase_view").permitAll()
				.antMatchers("/mini/all_products/provider/product_added").permitAll()
				.antMatchers("/mini/all_products/delete/{id}").permitAll()
				.antMatchers("/mini/all_products/provider/product_edited/{id}").permitAll()
				.antMatchers("/mini/all_products/user/product_rated/{id}").permitAll()
				.antMatchers("/mini/all_products/one_selected/purchase_view/{id}").permitAll()
				.antMatchers("/mini/all_products/user_dashboard/detail_each_purchase/final_view/{idPurchase}").permitAll()
				.antMatchers("/mini/all_products/user_dashboard/final_view").permitAll()
				.antMatchers("/mini/all_products/one_selected/purchase_view/{id}").permitAll()
				.antMatchers("/mini/all_product/registering").permitAll()
//				.antMatchers("/mini/all_product/registering/provider").permitAll()
				.antMatchers("/h2-console/**").permitAll()
				.antMatchers("/rest/**").hasAuthority("ADMIN")
				.antMatchers("/**").hasAuthority("USER")
//				.antMatchers("/**").hasAuthority("PROVIDER")
				.anyRequest().fullyAuthenticated();
		/////Autorizaciones y permisos para los distintos niveles de seguridad que tendria el usuario segun su casificacion
		http.formLogin()
				.usernameParameter("name")
				.passwordParameter("password")
				.loginPage("/api/login");

		http.logout().logoutUrl("/api/logout");

		http.csrf().disable();

		http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

		http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

		http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

		http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
        http.headers().frameOptions().disable();//Aqui
		http.headers().frameOptions().sameOrigin();
	}

	private void clearAuthenticationAttributes(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
		}
	}
    @Bean////importando Heroku a la base de datos
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        // The value of the 'Access-Control-Allow-Origin' header in the response must not be the wildcard '*' when the request's credentials mode is 'include'.
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("HEAD",
                "GET", "POST", "PUT", "DELETE", "PATCH"));
        // setAllowCredentials(true) is important, otherwise:
        // will fail with 403 Invalid CORS request
        configuration.setAllowCredentials(true);
        // setAllowedHeaders is important! Without it, OPTIONS preflight request
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}