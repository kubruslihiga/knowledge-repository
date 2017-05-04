package br.base.conhecimento;

import javax.faces.webapp.FacesServlet;
import javax.servlet.MultipartConfigElement;

import org.primefaces.webapp.filter.FileUploadFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import base.conhecimento.configuration.DefaultPage;
import base.conhecimento.configuration.FacesInitializer;
import base.conhecimento.dao.PersistenceDAO;
import base.conhecimento.dao.PersistenceDAOImpl;

@SpringBootApplication
@ComponentScan(basePackages = { "br.base.conhecimento" })
public class Application extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(new Class[] { Application.class, FacesInitializer.class });
	}
	
	@Bean
	public FilterRegistrationBean fileUploadFilterRegistrationBean() {
		FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		registrationBean.addUrlPatterns("*.jsf");
		registrationBean.addUrlPatterns("*.xhtml");
		registrationBean.setFilter(new FileUploadFilter());
		return registrationBean;
	}

	@Bean
	public ServletRegistrationBean servletRegistrationBean(MultipartConfigElement multipartConfigElement) {
		FacesServlet servlet = new FacesServlet();
		ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(servlet, "*.jsf", "*.xhtml");
		servletRegistrationBean.setMultipartConfig(multipartConfigElement);
		return servletRegistrationBean;
	}

	@Bean
	public DefaultPage defaultPage() {
		return new DefaultPage();
	}

	@Bean
	public PersistenceDAO persistenceDAO() {
		return new PersistenceDAOImpl();
	}
}
