package br.thiago.main;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import br.com.thiago.dto.SenhaDTO;
import br.com.thiago.service.SeatDesafioService;

public class SeatDesafioClientApplicationTest {

	private RestTemplate restTemplate;
	private List<SenhaDTO> senhaDTOs;
	
	private SeatDesafioService service;
	
	@Before
	public void iniciar() {
		restTemplate = new RestTemplate();
		service = new SeatDesafioService();
		senhaDTOs = new ArrayList<SenhaDTO>();
		preencherListaSenhas(senhaDTOs);
	}
	
	private void preencherListaSenhas(List<SenhaDTO> senhas) {
		senhaDTOs.add(new SenhaDTO("T 106", 1501206018000L, "geral", null, null));
		senhaDTOs.add(new SenhaDTO("A 200", 1501205720000L, "preferencial", null, null));
		senhaDTOs.add(new SenhaDTO("O 216", 1501205934000L, "preferencial", null, null));
		senhaDTOs.add(new SenhaDTO("G 214", 1501206511000L, "geral", null, null));
		senhaDTOs.add(new SenhaDTO("T 184", 1501205414000L, "geral", null, null));
		senhaDTOs.add(new SenhaDTO("T 191", 1501206102000L, "geral", null, null));
		senhaDTOs.add(new SenhaDTO("H 205", 1501206102000L, "preferencial", null, null));
		senhaDTOs.add(new SenhaDTO("O 223", 1501206622000L, "preferencial", null, null));
		senhaDTOs.add(new SenhaDTO("I 214", 1501206311000L, "geral", null, null));
	}

	@Test
	public void testarURLGet() {
		String resourceUrl = "http://seat.ind.br/processo-seletivo/desafio-2017-03.php";
		ResponseEntity<String> response = restTemplate.getForEntity(resourceUrl + "?nome=test", String.class);
		assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
	}

	@Test
	public void testarURLPost() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		String resourceUrl = "http://seat.ind.br/processo-seletivo/desafio-post-2017-03.php";
		MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();
		parameters.add("nome", "Teste");
		parameters.add("chave", Integer.toString(150127177));
		parameters.add("resultado", "[{\\\"senha\\\":\\\"T 106\\\",\\\"emissao\\\":1501206018000,\\\"prioridade\\\":\\\"geral\\\",\\\"chamada\\\":1501207214000,\\\"guiche\\\":\\\"Guiche 01\\\",\\\"atendente\\\":\\\"Thiago\\\"},"
				+ "{\\\"senha\\\":\\\"A 200\\\",\\\"emissao\\\":1501205720000,\\\"prioridade\\\":\\\"preferencial\\\",\\\"chamada\\\":1501206314000,\\\"guiche\\\":\\\"Guiche 01\\\",\\\"atendente\\\":\\\"Thiago\\\",\\\"fim\\\":1501206614000},"
				+ "{\\\"senha\\\":\\\"O 216\\\",\\\"emissao\\\":1501205934000,\\\"prioridade\\\":\\\"preferencial\\\",\\\"chamada\\\":1501206914000,\\\"guiche\\\":\\\"Guiche 01\\\",\\\"atendente\\\":\\\"Thiago\\\",\\\"fim\\\":1501207214000},"
				+ "{\\\"senha\\\":\\\"G 214\\\",\\\"emissao\\\":1501206511000,\\\"prioridade\\\":\\\"geral\\\"},"
				+ "{\\\"senha\\\":\\\"T 184\\\",\\\"emissao\\\":1501205414000,\\\"prioridade\\\":\\\"geral\\\",\\\"chamada\\\":1501205414000,\\\"guiche\\\":\\\"Guiche 01\\\",\\\"atendente\\\":\\\"Thiago\\\",\\\"fim\\\":1501205714000},"
				+ "{\\\"senha\\\":\\\"T 191\\\",\\\"emissao\\\":1501206102000,\\\"prioridade\\\":\\\"geral\\\"},"
				+ "{\\\"senha\\\":\\\"H 205\\\",\\\"emissao\\\":1501205518000,\\\"prioridade\\\":\\\"preferencial\\\",\\\"chamada\\\":1501205714000,\\\"guiche\\\":\\\"Guiche 01\\\",\\\"atendente\\\":\\\"Thiago\\\",\\\"fim\\\":1501206014000},"
				+ "{\\\"senha\\\":\\\"O 223\\\",\\\"emissao\\\":1501206622000,\\\"prioridade\\\":\\\"preferencial\\\"},"
				+ "{\\\"senha\\\":\\\"I 214\\\",\\\"emissao\\\":1501206311000,\\\"prioridade\\\":\\\"geral\\\"},"
				+ "{\\\"senha\\\":\\\"G 207\\\",\\\"emissao\\\":1501205823000,\\\"prioridade\\\":\\\"geral\\\",\\\"chamada\\\":1501206614000,\\\"guiche\\\":\\\"Guiche 01\\\",\\\"atendente\\\":\\\"Thiago\\\",\\\"fim\\\":1501206914000},"
				+ "{\\\"senha\\\":\\\"H 212\\\",\\\"emissao\\\":1501206206000,\\\"prioridade\\\":\\\"preferencial\\\"},"
				+ "{\\\"senha\\\":\\\"A 207\\\",\\\"emissao\\\":1501206408000,\\\"prioridade\\\":\\\"preferencial\\\"},"
				+ "{\\\"senha\\\":\\\"I 207\\\",\\\"emissao\\\":1501205623000,\\\"prioridade\\\":\\\"geral\\\",\\\"chamada\\\":1501206014000,\\\"guiche\\\":\\\"Guiche 01\\\",\\\"atendente\\\":\\\"Thiago\\\",\\\"fim\\\":1501206314000}]");
		HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<MultiValueMap<String, Object>>(parameters, headers);
		
		ResponseEntity<String> response = restTemplate.postForEntity(resourceUrl, request, String.class);
		assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
		
	}
	
	@Test
	public void quantidadeSenhasNaFrente() {
		Collections.sort(senhaDTOs);
		service.obterQuantidadeSenhasNaFrente(senhaDTOs);
		assertEquals(0, senhaDTOs.get(0).getNaFrente(), 0.00001);
		assertEquals(1, senhaDTOs.get(1).getNaFrente(), 0.00001);
		assertEquals(2, senhaDTOs.get(2).getNaFrente(), 0.00001);
	}
	
	@Test
	public void tempoEspera() {
		Collections.sort(senhaDTOs);
		service.obterQuantidadeSenhasNaFrente(senhaDTOs);
		assertEquals(0, senhaDTOs.get(0).getEspera(), 0.00001);
		assertEquals(306000, senhaDTOs.get(1).getEspera(), 0.00001);
//		assertEquals(2, senhaDTOs.get(2).getNaFrente(), 0.00001);
	}
}
