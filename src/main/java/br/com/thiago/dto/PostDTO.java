package br.com.thiago.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

//@JsonIgnoreProperties(ignoreUnknown = true)
public class PostDTO implements Serializable {
	
	private static final long serialVersionUID = -7632222629851439434L;

	private String nome;
	
	private Long chave;
	
	@JsonBackReference
	private List<SenhaDTO> resultado;
	
	

	public PostDTO(String nome, Long chave, List<SenhaDTO> resultado) {
		super();
		this.nome = nome;
		this.chave = chave;
		this.resultado = resultado;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Long getChave() {
		return chave;
	}

	public void setChave(Long chave) {
		this.chave = chave;
	}

	public List<SenhaDTO> getResultado() {
		return resultado;
	}

	public void setResultado(List<SenhaDTO> resultado) {
		this.resultado = resultado;
	}

}
