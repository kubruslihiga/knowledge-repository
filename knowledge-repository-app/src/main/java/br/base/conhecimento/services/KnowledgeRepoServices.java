package br.base.conhecimento.services;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.base.conhecimento.to.Knowledge;

@Service
public class KnowledgeRepoServices {

	public Knowledge insertKnowledge(Knowledge knowledge) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(knowledge.getJSONSchema());
		Iterator<String> fieldNames = root.fieldNames();
		return knowledge;
	}

	public static void main(String[] args) throws Exception {
		Knowledge a = new Knowledge();
		a.setName("Nome do conhecimento");
		byte[] encoded = Files.readAllBytes(Paths.get("exemplo.json"));
		a.setJSONSchema(new String(encoded));
		KnowledgeRepoServices services = new KnowledgeRepoServices();
		services.insertKnowledge(a);
	}
}
