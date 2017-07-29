package br.com.thiago.main;

import java.util.Collections;
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
		
		Collections.sort(postDTO.getResultado());
		
		log.info("Lista Ordenada: " + new Gson().toJson(postDTO.getResultado()) + "\n");
		
		String resultado = service.enviarPost(postDTO, acessoGetDTO.getPostTo().getUrl());
		log.info(resultado);
	}
	
}
