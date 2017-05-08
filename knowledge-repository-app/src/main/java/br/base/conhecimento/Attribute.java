package br.base.conhecimento;

public class Attribute {

    private String name;

    private AttributeProperty attributeProperty;

    private String internalDatatypeURI;

    private String attributeURI;

    private String attributeValue;

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

    public String getAttributeURI() {
        return attributeURI;
    }

    public void setAttributeURI(String attributeURI) {
        this.attributeURI = attributeURI;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }
}
