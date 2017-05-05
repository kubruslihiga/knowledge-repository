package br.base.conhecimento;

import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.XSD;

public enum AttributeProperty {

    FLOAT(XSD.xfloat),
    DOUBLE(XSD.xdouble),
    INT(XSD.xint),
    LONG(XSD.xlong),
    SHORT(XSD.xshort),
    BYTE(XSD.xbyte),
    UNSIGNEDBYTE(XSD.unsignedByte),
    UNSIGNEDSHORT(XSD.unsignedShort),
    UNSIGNEDINT(XSD.unsignedInt),
    UNSIGNEDLONG(XSD.unsignedLong),
    DECIMAL(XSD.decimal),
    INTEGER(XSD.integer),
    NONPOSITIVEINTEGER(XSD.nonPositiveInteger),
    NONNEGATIVEINTEGER(XSD.nonNegativeInteger),
    POSITIVEINTEGER(XSD.positiveInteger),
    NEGATIVEINTEGER(XSD.negativeInteger),
    BOOLEAN(XSD.xboolean),
    STRING(XSD.xstring),
    NORMALIZEDSTRING(XSD.normalizedString),
    ANYURI(XSD.anyURI),
    TOKEN(XSD.token),
    NAME(XSD.Name),
    QNAME(XSD.QName),
    LANGUAGE(XSD.language),
    NMTOKEN(XSD.NMTOKEN),
    ENTITY(XSD.ENTITY),
    ID(XSD.ID),
    NCNAME(XSD.NCName),
    IDREF(XSD.IDREF),
    NOTATION(XSD.NOTATION),
    HEXBINARY(XSD.hexBinary),
    BASE64BINARY(XSD.base64Binary),
    DATE(XSD.date),
    TIME(XSD.time),
    DATETIME(XSD.dateTime),
    DATETIMESTAMP(XSD.dateTimeStamp),
    DURATION(XSD.duration),
    YEARMONTHDURATION(XSD.yearMonthDuration),
    DAYTIMEDURATION(XSD.dayTimeDuration),
    GDAY(XSD.gDay),
    GMONTH(XSD.gMonth),
    GYEAR(XSD.gYear),
    GYEARMONTH(XSD.gYearMonth),
    GMONTHDAY(XSD.gMonthDay),
    INTERNAL_OBJECT_PROPERTY(null); // datatype que ira referenciar uma outra ontologia
                            // do sistema.

    final Resource resource;

    AttributeProperty(Resource resource) {
        this.resource = resource;
    }

    public Resource getResource() {
        return resource;
    }
}
