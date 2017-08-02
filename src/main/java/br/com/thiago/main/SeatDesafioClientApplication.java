package br.com.thiago.main;

import static java.util.Comparator.comparing;
import static java.util.Comparator.naturalOrder;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;

import br.com.thiago.dto.AcessoGetDTO;
import br.com.thiago.dto.PostDTO;
import br.com.thiago.dto.SenhaDTO;
import br.com.thiago.service.SeatDesafioService;

@SpringBootApplication
@Configuration
@ComponentScan(basePackages = "br.com.thiago.*")
public class SeatDesafioClientApplication implements CommandLineRunner {
	
	private static final Logger log = LoggerFactory.getLogger(SeatDesafioClientApplication.class);
	
	@Autowired
	private SeatDesafioService service;

	private Scanner scanner;
	
	public static void main(String[] args) {
		log.info("Executando Client...");
		SpringApplication.run(SeatDesafioClientApplication.class, args);
	}
	
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		RestTemplate restTemplate = builder.build();
		
		List<HttpMessageConverter<?>> converters = restTemplate.getMessageConverters();
	    for (HttpMessageConverter<?> converter : converters) {
	        if (converter instanceof MappingJackson2HttpMessageConverter) {
	            MappingJackson2HttpMessageConverter jsonConverter = (MappingJackson2HttpMessageConverter) converter;
	            jsonConverter.setObjectMapper(new ObjectMapper());
	            jsonConverter.setSupportedMediaTypes(ImmutableList.of(new MediaType("application", "json", MappingJackson2HttpMessageConverter.DEFAULT_CHARSET), new MediaType("text", "html", MappingJackson2HttpMessageConverter.DEFAULT_CHARSET)));
	        }
	    }
	    return restTemplate;
	}

	@Override
	public void run(String... arg0) throws Exception {
		scanner = new Scanner(System.in);
		System.out.println("Digite o nome completo: ");
		String nome = scanner.nextLine();
		AcessoGetDTO acessoGetDTO = service.getJsonInicial(nome).getBody();
		log.info(acessoGetDTO.getMensagem() + "\n");
		log.info("Sua chave é: " + acessoGetDTO.getChave() + "\n");
		
		PostDTO postDTO = new PostDTO(acessoGetDTO.getNome(), acessoGetDTO.getChave(), acessoGetDTO.getInput());
		postDTO.setResultado(service.getSenhasNaoChamadas(postDTO.getResultado()));	
		log.info("Lista Não Ordenada: " + new Gson().toJson(postDTO.getResultado()) + "\n");
		
		Comparator<SenhaDTO> comparatorByEmissao = comparing(SenhaDTO::getEmissao, naturalOrder());
		Comparator<SenhaDTO> comparatorByPrioridade = comparing(SenhaDTO::getPrioridade, String.CASE_INSENSITIVE_ORDER.reversed());
		postDTO.getResultado().sort(comparatorByPrioridade.thenComparing(comparatorByEmissao));
		
		service.obterQuantidadeSenhasNaFrente(postDTO.getResultado());
		
		log.info("Lista Ordenada: " + new Gson().toJson(postDTO.getResultado()) + "\n");
		
		gerarHistograma(postDTO.getResultado());
		
		String resultado = service.enviarPost(postDTO, acessoGetDTO.getPostTo().getUrl());
		log.info(resultado);
		
	}

	private void gerarHistograma(List<SenhaDTO> senhas) {
		int qtdSenhaEspera5Min = 0;
		int qtdSenhaEspera10Min = 0;
		int qtdSenhaEspera15Min = 0;
		int qtdSenhaEsperaMais15Min = 0;
		
		for (SenhaDTO senhaDTO : senhas) {
			if (senhaDTO.getEspera() < 300000) {
				qtdSenhaEspera5Min++;
			} else if (senhaDTO.getEspera() < 600000) {
				qtdSenhaEspera10Min++;
			} else if (senhaDTO.getEspera() < 900000) {
				qtdSenhaEspera15Min++;
			} else {
				qtdSenhaEsperaMais15Min++;
			}
		}
		
		String graficoEspera5Min = gerarGrafico(qtdSenhaEspera5Min);
		String graficoEspera10Min = gerarGrafico(qtdSenhaEspera10Min);
		String graficoEspera15Min = gerarGrafico(qtdSenhaEspera15Min);
		String graficoEsperaMais15Min = gerarGrafico(qtdSenhaEsperaMais15Min);
		
		log.info("< 05 Min: " + graficoEspera5Min + " " + qtdSenhaEspera5Min + "\n");
		log.info("< 10 Min: " + graficoEspera10Min + " " + qtdSenhaEspera10Min + "\n");
		log.info("< 15 Min: " + graficoEspera15Min + " " + qtdSenhaEspera15Min + "\n");
		log.info("> 05 Min: " + graficoEsperaMais15Min + " " + qtdSenhaEsperaMais15Min + "\n");
		
		
	}

	private String gerarGrafico(int qtdSenhaEspera) {
		String valor = "";
		int cont = 0;
		
		while(cont < qtdSenhaEspera) {
			valor += "#";
			cont++;
		}
		
		return valor;
	}
	
}
