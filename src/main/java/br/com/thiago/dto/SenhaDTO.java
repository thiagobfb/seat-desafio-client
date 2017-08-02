package br.com.thiago.dto;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SenhaDTO implements Serializable, Comparable<SenhaDTO> {

	private static final long serialVersionUID = 2263832538117224590L;

	private String senha;
	
	private Long emissao;
	
	private String prioridade;
	
	private Long chamada;
	
	private String atendente;
	
	private Integer naFrente;
	
	private Long espera;

	public SenhaDTO() {
		
	}
	
	public SenhaDTO(String senha, Long emissao, String prioridade, Long chamada, String atendente, Integer naFrente,
			Long espera) {
		super();
		this.senha = senha;
		this.emissao = emissao;
		this.prioridade = prioridade;
		this.chamada = chamada;
		this.atendente = atendente;
		this.naFrente = naFrente;
		this.espera = espera;
	}

	public SenhaDTO(String senha, Long emissao, String prioridade, Long chamada, String atendente) {
		this.senha = senha;
		this.emissao = emissao;
		this.prioridade = prioridade;
		this.chamada = chamada;
		this.atendente = atendente;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public Long getEmissao() {
		return emissao;
	}

	public void setEmissao(Long emissao) {
		this.emissao = emissao;
	}

	public String getPrioridade() {
		return prioridade;
	}

	public void setPrioridade(String prioridade) {
		this.prioridade = prioridade;
	}

	public Long getChamada() {
		return chamada;
	}

	public void setChamada(Long chamada) {
		this.chamada = chamada;
	}

	public String getAtendente() {
		return atendente;
	}

	public void setAtendente(String atendente) {
		this.atendente = atendente;
	}

	public Integer getNaFrente() {
		return naFrente;
	}

	public Long getEspera() {
		return espera;
	}

	public void setEspera(Long espera) {
		this.espera = espera;
	}

	public void setNaFrente(Integer naFrente) {
		this.naFrente = naFrente;
	}

	@Override
	public String toString() {
		return "SenhaDTO [senha=" + senha + ", emissao=" + emissao + ", prioridade=" + prioridade + ", chamada="
				+ chamada + ", atendente=" + atendente + ", naFrente=" + naFrente + ", espera=" + espera + "]";
	}

	@Override
	public int compareTo(SenhaDTO o) {
		LocalDateTime dataEmissao1 = Instant.ofEpochMilli(this.emissao).atZone(ZoneId.systemDefault()).toLocalDateTime(); 
		LocalDateTime dataEmissao2 = Instant.ofEpochMilli(o.emissao).atZone(ZoneId.systemDefault()).toLocalDateTime();
		
		if (dataEmissao1.isBefore(dataEmissao2)) {
			return -1;
		} 
		
		if (dataEmissao1.isAfter(dataEmissao2)) {
			return 1;
		}
		
		return 0;
	}

	
	
}
