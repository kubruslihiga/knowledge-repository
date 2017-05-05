package br.base.conhecimento.services;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.jena.ontology.DatatypeProperty;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.vocabulary.XSD;
import org.springframework.stereotype.Service;

import br.base.conhecimento.Attribute;
import br.base.conhecimento.AttributeProperty;
import br.base.conhecimento.Knowledge;

@Service
public class KnowledgeRepoServices {

    private static final String HTTP_ONTOLOGY_EXAMPLE = "http://ontology.example/ont#";
    private static final String HTTP_ONTOLOGY_EXAMPLE2 = "<" + HTTP_ONTOLOGY_EXAMPLE + ">";
    final String BASE = "http://base.example/";

    public Knowledge insertKnowledge(Knowledge knowledge) throws Exception {
        Dataset dataset = null;
        try {
            dataset = TDBFactory.createDataset("/home/mauricio/tdb");
            OntModel ontModel = getOntModel(dataset, ReadWrite.WRITE);
            String knowledgeURIBase = BASE + knowledge.getName();
            String knowledgeURI = knowledgeURIBase + "#";
            OntClass knowledgeOntClass = ontModel.createClass(knowledgeURIBase);
            knowledgeOntClass.addComment(ontModel.createLiteral(knowledge.getComment()));
            knowledgeOntClass.addLabel(ontModel.createLiteral(
                    StringUtils.isNotBlank(knowledge.getLabel()) ? knowledge.getLabel() : knowledge.getName()));

            List<Attribute> attributeList = knowledge.getAttributeList();
            for (Attribute attribute : attributeList) {
                OntProperty property;
                Resource resource = attribute.getAttributeProperty().getResource();
                if (attribute.getAttributeProperty() == AttributeProperty.INTERNAL_OBJECT_PROPERTY) {
                    ObjectProperty objectProperty = ontModel.createObjectProperty(knowledgeURI + attribute.getName());
                    resource = ontModel.getResource(attribute.getInternalDatatypeURI());
                    property = objectProperty;
                } else {
                    property = ontModel.createDatatypeProperty(knowledgeURI + attribute.getName());
                }
                property.addDomain(knowledgeOntClass);
                property.addRange(resource);
            }
            ontModel.write(System.out);
            dataset.commit();
        } catch (Exception e) {
            if (dataset != null) {
                dataset.abort();
            }
            throw e;
        } finally {
            if (dataset != null) {
                dataset.end();
            }
        }
        return knowledge;
    }

    public void selectKnowledge() throws Exception {
        Dataset dataset = null;
        try {
            dataset = TDBFactory.createDataset("/home/mauricio/tdb");
            OntModel ontModel = getOntModel(dataset, ReadWrite.READ);
            try (QueryExecution qExec = QueryExecutionFactory.create("SELECT (count(*) AS ?count) { ?s ?p ?o} LIMIT 10",
                    dataset)) {
                ResultSet rs = qExec.execSelect();
                ResultSetFormatter.out(rs);
            }
        } catch (Exception e) {
            if (dataset != null) {
                dataset.abort();
            }
            throw e;
        } finally {
            if (dataset != null) {
                dataset.end();
            }
        }
    }

    private OntModel getOntModel(Dataset dataset, ReadWrite readWrite) {
        OntModel ontModel = null;
        dataset.begin(readWrite);
        if (dataset.containsNamedModel(HTTP_ONTOLOGY_EXAMPLE2)) {
            Model namedModel = dataset.getNamedModel(HTTP_ONTOLOGY_EXAMPLE2);
            OntModelSpec spec = new OntModelSpec(OntModelSpec.OWL_MEM);
            ontModel = ModelFactory.createOntologyModel(spec, namedModel);
        } else {
            ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
            ontModel.setNsPrefixes(Collections.singletonMap("ont", HTTP_ONTOLOGY_EXAMPLE));
            dataset.addNamedModel(HTTP_ONTOLOGY_EXAMPLE2, ontModel);
        }
        return ontModel;

    }

    public static void main(String[] args) throws Exception {
        KnowledgeRepoServices services = new KnowledgeRepoServices();
        Attribute attribute = new Attribute();
        attribute.setName("nome");
        attribute.setAttributeProperty(AttributeProperty.STRING);
        Knowledge formaCabelo = new Knowledge();
        formaCabelo.setName("Forma cabelo");
        formaCabelo.setComment("Forma do cabelo");
        formaCabelo.setLabel("Forma cabelo");
        formaCabelo.setAttributeList(Arrays.asList(attribute));
        services.insertKnowledge(formaCabelo);

        Knowledge cabelo = new Knowledge();
        cabelo.setName("Cabelo");
        cabelo.setComment("Definicao dos tipos, cores e tamanho de cabelo.");
        cabelo.setLabel("Cabelo");
        Attribute attr1 = new Attribute();
        Attribute attr2 = new Attribute();
        Attribute attr3 = new Attribute();
        attr1.setName("cor");
        attr1.setAttributeProperty(AttributeProperty.STRING);
        attr2.setName("tamanho");
        attr2.setAttributeProperty(AttributeProperty.LONG);
        attr3.setName("forma");
        attr3.setAttributeProperty(AttributeProperty.INTERNAL_OBJECT_PROPERTY);
        attr3.setInternalDatatypeURI("http://base.example/Forma cabelo");
        cabelo.setAttributeList(Arrays.asList(attr1, attr2, attr3));
        services.insertKnowledge(cabelo);
        services.selectKnowledge();
    }

    public void exemplo() {
        String base = "http://base.example/#";
        OntModel ontModel = ModelFactory.createOntologyModel();
        OntClass cameraDigitalDef = ontModel.createClass(base + "DigitalCamera");
        cameraDigitalDef.addComment(ontModel.createLiteral("Entidade camera digital para ser mapeada bonitinha."));
        cameraDigitalDef.addLabel(ontModel.createLiteral("Camera digital"));

        OntClass lente = ontModel.createClass(base + "LenteCamera");
        lente.addComment(ontModel.createLiteral("Lente de uma camera digital."));

        ontModel.setNsPrefix("cam", base);
        DatatypeProperty lenteProperty = ontModel.createDatatypeProperty(base + "lente");
        lenteProperty.addRange(lente);
        lenteProperty.addDomain(cameraDigitalDef);

        DatatypeProperty nomeProperty = ontModel.createDatatypeProperty(base + "nome");
        nomeProperty.addDomain(lente);
        nomeProperty.addDomain(cameraDigitalDef);
        nomeProperty.setRange(XSD.xstring);

        Individual lenteCarl = ontModel.createIndividual(base + "Carl", lente);
        lenteCarl.addProperty(nomeProperty, ontModel.createLiteral("Lente Carl"));

        Individual cameraSony = ontModel.createIndividual(base + "SonyC1", cameraDigitalDef);
        cameraSony.addProperty(nomeProperty, ontModel.createLiteral("Sony C1"));
        cameraSony.addProperty(lenteProperty, lenteCarl);

        Individual cameraCanonEp2 = ontModel.createIndividual(base + "Canon", cameraDigitalDef);
        cameraCanonEp2.addProperty(nomeProperty, ontModel.createLiteral("Canon Ep2"));
        cameraCanonEp2.addProperty(lenteProperty, lenteCarl);

        ontModel.write(System.out, "RDF/XML");
    }
}
