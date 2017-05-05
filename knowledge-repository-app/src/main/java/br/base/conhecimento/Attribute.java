package br.base.conhecimento;

public class Attribute {

    private String name;

    private AttributeProperty attributeProperty;

    private String internalDatatypeURI;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AttributeProperty getAttributeProperty() {
        return attributeProperty;
    }

    public void setAttributeProperty(AttributeProperty attributeProperty) {
        this.attributeProperty = attributeProperty;
    }

    public String getInternalDatatypeURI() {
        return internalDatatypeURI;
    }

    public void setInternalDatatypeURI(String internalDatatypeURI) {
        this.internalDatatypeURI = internalDatatypeURI;
    }

}
