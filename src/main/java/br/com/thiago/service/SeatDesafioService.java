package br.com.thiago.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;

import br.com.thiago.dto.AcessoGetDTO;
import br.com.thiago.dto.PostDTO;
import br.com.thiago.dto.SenhaDTO;

@Service
public class SeatDesafioService {

	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${spring.resource}")
	private String resource;
	
	public ResponseEntity<AcessoGetDTO> getJsonInicial(String... params) {
		String paramGet = "?";
		for (int i = 0; i < params.length; i++) {
			if (i == 1) {
				paramGet += "&chave=" + params[i];
			} else {
				paramGet += "nome=" + params[i];
			}		
		}
		
		ResponseEntity<AcessoGetDTO> response = restTemplate.getForEntity(resource + paramGet, AcessoGetDTO.class);
		
		return response;
	}
	
	public String enviarPost(PostDTO postDTO, String url) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();
		parameters.add("nome", postDTO.getNome());
		parameters.add("chave", Long.toString(postDTO.getChave()));
		parameters.add("resultado", getResultadoToString(postDTO.getResultado()));
		HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<MultiValueMap<String, Object>>(parameters, headers);
		
		ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
		
		return response.getBody();
	}

	private String getResultadoToString(List<SenhaDTO> resultado) {
		return new Gson().toJson(resultado);
	}

	public List<SenhaDTO> getSenhasNaoChamadas(List<SenhaDTO> resultado) {
		List<SenhaDTO> senhas = new ArrayList<SenhaDTO>();
		
		for (SenhaDTO senhaDTO : resultado) {
			if (senhaDTO.getChamada() == null) {
				senhas.add(senhaDTO);
			}
		}
		return senhas;
	}

}
