package br.com.thiago.dto;

import java.io.Serializable;
import java.util.List;

public class PostToDTO implements Serializable {

	private static final long serialVersionUID = -3175084820191870311L;

	private String url;
	
	private List<String> parameters;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<String> getParameters() {
		return parameters;
	}

	public void setParameters(List<String> parameters) {
		this.parameters = parameters;
	}
	
}
