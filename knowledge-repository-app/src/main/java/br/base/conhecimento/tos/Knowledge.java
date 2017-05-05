package br.base.conhecimento.tos;

import java.io.Serializable;
import java.util.List;

public class Knowledge implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;

	private String comment;

	private String label;

	private List<Attribute> attributeList;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public List<Attribute> getAttributeList() {
		return attributeList;
	}

	public void setAttributeList(List<Attribute> attributeList) {
		this.attributeList = attributeList;
	}
}
