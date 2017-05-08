package br.base.conhecimento.services;

import static org.apache.commons.lang3.StringUtils.replace;
import static org.apache.commons.lang3.StringUtils.trim;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.query.DatasetAccessor;
import org.apache.jena.query.DatasetAccessorFactory;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.springframework.stereotype.Service;

import com.google.common.base.CaseFormat;

import br.base.conhecimento.Attribute;
import br.base.conhecimento.AttributeProperty;
import br.base.conhecimento.Knowledge;
import br.base.conhecimento.KnowledgeInstance;

@Service
public class KnowledgeServices {

	private static final String DIRECTORY_SAVE_OWL = "/home/mauricio/Documentos/owl/";
	private static final String HTTP_ONTOLOGY_EXAMPLE = "http://ontology.example/ont#";
	private static final String HTTP_ONTOLOGY_EXAMPLE2 = "<" + HTTP_ONTOLOGY_EXAMPLE + ">";
	final String BASE = "http://base.example/";
	private static final String FUSEKI_SERVICE_DATASETS_URI = "http://localhost:3030/rdf/data";
	private static final String FUSEKI_SERVICE_QUERY_URI = "http://localhost:3030/rdf/query";

	private String treatString(String string) {
		return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, replace((trim(string)), " ", "_"));
	}

	public Knowledge insertKnowledge(Knowledge knowledge) throws Exception {
		OntModel ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
		ontModel.setNsPrefixes(Collections.singletonMap("ont", HTTP_ONTOLOGY_EXAMPLE));
		String name = treatString(knowledge.getName());// recupera os nomes camelcase
		String knowledgeURIBase = BASE + name; // concatena uri com o nome
		String knowledgeURI = knowledgeURIBase + "#";

		OntClass knowledgeOntClass = ontModel.createClass(knowledgeURIBase); // cria a definicao OWL

		knowledgeOntClass.addComment(ontModel.createLiteral(knowledge.getComment()));
		knowledgeOntClass.addLabel(ontModel.createLiteral(
				StringUtils.isNotBlank(knowledge.getLabel()) ? knowledge.getLabel() : knowledge.getName()));

		List<Attribute> attributeList = knowledge.getAttributeList();
		for (Attribute attribute : attributeList) {
			OntProperty property;
			Resource resource = attribute.getAttributeProperty().getResource();
			String attributeURI = knowledgeURI + attribute.getName();
            if (attribute.getAttributeProperty() == AttributeProperty.INTERNAL_OBJECT_PROPERTY) {
				ObjectProperty objectProperty = ontModel.createObjectProperty(attributeURI);
				resource = ontModel.getResource(attribute.getInternalDatatypeURI());
				property = objectProperty;
			} else {
				property = ontModel.createDatatypeProperty(attributeURI);
			}
			property.addDomain(knowledgeOntClass);
			property.addRange(resource);
			attribute.setAttributeURI(attributeURI);
		}

		// escreve o nome do arquivo
		String owlPath = DIRECTORY_SAVE_OWL + name + ".owl";
        ontModel.write(new FileOutputStream(owlPath));

        knowledge.setOwlPath(owlPath);

		return knowledge;
	}

	public void insertInstance(KnowledgeInstance instance) throws Exception {
		Model model = ModelFactory.createDefaultModel();
		String treatedString = treatString(instance.getKnowledgeName());
		String uri = BASE + treatedString;
		OntModel ontologyModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM, model.read(new FileInputStream(DIRECTORY_SAVE_OWL + treatedString + ".owl"), uri));
		OntClass ontClass = ontologyModel.getOntClass(uri);
		Individual individual = ontClass.createIndividual(uri + "/" + treatString(instance.getName()));
		List<Attribute> attributeList = instance.getAttributeList();
		for (Attribute attribute : attributeList) {
			OntProperty ontProperty = ontologyModel.getOntProperty(attribute.getAttributeURI());
			if (ontProperty.isLiteral() || ontProperty.isDatatypeProperty()) {
				individual.addLiteral(ontProperty, attribute.getAttributeValue());
			} else if (ontProperty.isObjectProperty()) {
				individual.addProperty(ontProperty, attribute.getAttributeValue());
			}
		}
		DatasetAccessor accessor = DatasetAccessorFactory.createHTTP(FUSEKI_SERVICE_DATASETS_URI);
		if (accessor != null) {
			accessor.add(individual.getModel());
		}
	}

	public void selectInstance() throws Exception {
		// String sparql = "SELECT ?class WHERE {" + " ?class
		// <http://www.w3.org/2000/01/rdf-schema#label> \"Cabelo\" }";
		String sparql = "SELECT ?class ?label WHERE {"
				+ " ?class <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.w3.org/2002/07/owl#Class> }";
		QueryExecution queryExecution = QueryExecutionFactory.sparqlService(FUSEKI_SERVICE_QUERY_URI, sparql);
		ResultSet resultSet = queryExecution.execSelect();
		while (resultSet.hasNext()) {
			QuerySolution next = resultSet.next();
			RDFNode rdfNode = next.get("?class");
			OntModel ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM, rdfNode.getModel());
			OntClass ontClass = ontModel.getOntClass(rdfNode.asNode().getURI());

			String propertiesByDomainSparql = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> ";
			propertiesByDomainSparql += "SELECT ?dClass WHERE { ?dClass rdfs:domain " + rdfNode.asNode().getURI()
					+ " }";
			QueryExecution propertiesQuery = QueryExecutionFactory.sparqlService(FUSEKI_SERVICE_QUERY_URI,
					propertiesByDomainSparql);
			ResultSet propertiesResultSet = propertiesQuery.execSelect();
			while (propertiesResultSet.hasNext()) {
				QuerySolution propResultSet = propertiesResultSet.next();
				RDFNode rdfNode2 = propResultSet.get("?pURI");
				RDFNode rdfNode3 = propResultSet.get("?object");
				System.out.println("Predicate: " + rdfNode2);
				System.out.println("Object: " + rdfNode3);
			}
		}
	}

	public static void main(String[] args) throws Exception {
		KnowledgeServices services = new KnowledgeServices();
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

        KnowledgeInstance instance = new KnowledgeInstance();
        instance.setName("Liso");
        instance.setKnowledgeName("Forma cabelo");
        attribute.setAttributeValue("Liso");
        instance.setAttributeList(Arrays.asList(attribute));
        services.insertInstance(instance);
    }
}
