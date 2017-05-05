package br.base.conhecimento.services;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.jena.ontology.DatatypeProperty;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.vocabulary.XSD;
import org.springframework.stereotype.Service;

import br.base.conhecimento.tos.Attribute;
import br.base.conhecimento.tos.Knowledge;

@Service
public class KnowledgeRepoServices {

	public Knowledge insertKnowledge(Knowledge knowledge) throws Exception {
		String base = "http://base.example/";
		OntModel ontModel = ModelFactory.createOntologyModel();

		String knowledgeURIBase = base + knowledge.getName();
		String knowledgeURI = knowledgeURIBase + "#";
		OntClass knowledgeOntClass = ontModel.createClass(knowledgeURIBase);
		knowledgeOntClass.addComment(ontModel.createLiteral(knowledge.getComment()));
		knowledgeOntClass.addLabel(ontModel.createLiteral(
				StringUtils.isNotBlank(knowledge.getLabel()) ? knowledge.getLabel() : knowledge.getName()));

		List<Attribute> attributeList = knowledge.getAttributeList();
		for (Attribute attribute : attributeList) {
			DatatypeProperty nomeProperty = ontModel.createDatatypeProperty(knowledgeURI + attribute.getName());
			nomeProperty.addDomain(knowledgeOntClass);
			nomeProperty.setRange(XSD.xstring);
		}
		ontModel.write(System.out);
		return knowledge;
	}

	public static void main(String[] args) throws Exception {
		Knowledge a = new Knowledge();
		a.setName("Cabelo");
		a.setComment("Definicao dos tipos, cores e tamanho de cabelo.");
		a.setLabel("Cabelo");
		Attribute attr1 = new Attribute();
		Attribute attr2 = new Attribute();
		Attribute attr3 = new Attribute();
		attr1.setName("cor");
		attr2.setName("tamanho");
		attr3.setName("forma tipo");
		a.setAttributeList(Arrays.asList(attr1, attr2, attr3));
		KnowledgeRepoServices services = new KnowledgeRepoServices();
		services.insertKnowledge(a);
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
