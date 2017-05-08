package br.base.conhecimento;

import java.io.Serializable;
import java.util.List;

public class KnowledgeInstance implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    private String knowledgeName;

    private List<Attribute> attributeList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Attribute> getAttributeList() {
        return attributeList;
    }

    public void setAttributeList(List<Attribute> attributeList) {
        this.attributeList = attributeList;
    }

    public String getKnowledgeName() {
        return knowledgeName;
    }

    public void setKnowledgeName(String knowledgeName) {
        this.knowledgeName = knowledgeName;
    }

}
