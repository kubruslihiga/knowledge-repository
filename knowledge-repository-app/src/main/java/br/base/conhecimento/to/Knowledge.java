package br.base.conhecimento.to;

import java.io.Serializable;

public class Knowledge implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;

	private String JSONSchema;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getJSONSchema() {
		return JSONSchema;
	}

	public void setJSONSchema(String JSONSchema) {
		this.JSONSchema = JSONSchema;
	}
}
