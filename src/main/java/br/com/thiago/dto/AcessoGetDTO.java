package br.com.thiago.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

public class AcessoGetDTO implements Serializable {

	private static final long serialVersionUID = 5758352144514304877L;
	
	private String mensagem;
	
	private String nome;
	
	private Long chave;
	
	@JsonBackReference
	private List<SenhaDTO> input;
	
	private PostToDTO postTo;
	
	public String getMensagem() {
		return mensagem;
	}
	
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
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
	
	public List<SenhaDTO> getInput() {
		return input;
	}
	
	public void setInput(List<SenhaDTO> input) {
		this.input = input;
	}

	public PostToDTO getPostTo() {
		return postTo;
	}

	public void setPostTo(PostToDTO postTo) {
		this.postTo = postTo;
	}

	@Override
	public String toString() {
		return "AcessoGetDTO [mensagem=" + mensagem + ", nome=" + nome + ", chave=" + chave + ", input=" + input + "]";
	}
	
	
}
