package br.unicamp.siarq.cadastro.pesquisador;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import base.conhecimento.dao.PersistenceDAO;
import br.base.conhecimento.boot.Application;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT, classes = { Application.class }, properties = {
		"server.port=9999", "value=123" })
public class CadastroTest {
	private static final int PORT = 9999;
	private static final String URL = "http://localhost:" + PORT;
	private static WebDriver driver;
	@Autowired
	private WebApplicationContext wac;
	@Autowired
	private PersistenceDAO dao;
	private MockMvc mockMvc;

	@BeforeClass
	public static void init() {
		// Application.main(new String[0]);
		System.setProperty("spring.profiles.active", "dev");
		System.setProperty("webdriver.gecko.driver", "geckodriver");
		driver = new FirefoxDriver();
	}

	@Before
	public void setUp() throws Exception {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
		Map<String, Object> estados = new LinkedHashMap<>();
		estados.put("SP", "SP");
		estados.put("RJ", "RJ");
		estados.put("ES", "ES");
		estados.put("MG", "MG");
		Mockito.when(dao.buscarTodosEstados()).thenReturn(estados);

		Map<String, Object> servicos = new LinkedHashMap<>();
		servicos.put("Visita", "Visita");
		servicos.put("Palestra", "Palestra");
		servicos.put("Treinamento", "Treinamento");
		servicos.put("Assessoria", "Assessoria");
		Mockito.when(dao.buscarTodosServicos()).thenReturn(servicos);
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testIndex() throws Exception {
		driver.get(URL);
		WebElement element = driver.findElement(By.tagName("h1"));
		Assert.assertTrue(element.getText() != null);
	}

	@Test
	public void testPreencherTodosValores() throws Exception {
		driver.get(URL + "/manter_cadastro.jsf");

		WebElement instituicao = driver.findElement(By.id("instituicao"));
		instituicao.sendKeys("Institucao");

		WebElement textoNome = driver.findElement(By.id("nome"));
		textoNome.sendKeys("Texto");

		WebElement textoEmail = driver.findElement(By.id("email"));
		textoEmail.sendKeys("email@email.com");

		WebElement textoArea = driver.findElement(By.id("area"));
		textoArea.sendKeys("Area");

		WebElement textoTelefone = driver.findElement(By.id("telefone"));
		textoTelefone.sendKeys("19-23441234");

		WebElement textoCelular = driver.findElement(By.id("celular"));
		textoCelular.sendKeys("18-998113456");

		WebElement textoDescricao = driver.findElement(By.id("descricao"));
		textoDescricao.sendKeys("Descricao teste com muitos mais testes \nE quebra de linha.");

		WebElement servico = driver.findElement(By.id("servico"));
		servico.click();

		WebDriverWait waitSelect = new WebDriverWait(driver, 20);
		waitSelect.until(ExpectedConditions.presenceOfElementLocated(By.id("servico_items")));
		WebElement estado2 = driver.findElement(By.xpath("//ul[@id='servico_items']/li[contains(.,'Palestra')]"));
		estado2.click();

		WebElement confirmarBtn = driver.findElement(By.id("confirmar-btn"));
		confirmarBtn.click();
		WebDriverWait wait = new WebDriverWait(driver, 20);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("detalhe_cadastro")));
	}

	@AfterClass
	public static void finalizeAll() throws Exception {
		if (driver != null) {
			driver.quit();
		}
	}
}

@Profile("test")
@Configuration
class MockPersistenceDAO {
	@Bean
	@Primary
	public PersistenceDAO persistenceDAOMock() {
		PersistenceDAO mock = Mockito.mock(PersistenceDAO.class);
		return mock;
	}
}
