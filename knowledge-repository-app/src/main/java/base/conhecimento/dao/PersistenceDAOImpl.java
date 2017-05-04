package base.conhecimento.dao;

import java.util.LinkedHashMap;
import java.util.Map;

public class PersistenceDAOImpl implements PersistenceDAO {

	@Override
	public Map<String, Object> buscarTodosEstados() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> buscarTodosServicos() {
		Map<String, Object> servicos = new LinkedHashMap<>();
		servicos.put("Visita 1", "Visita");
		servicos.put("Palestra 2", "Palestra");
		servicos.put("Treinamento 3", "Treinamento");
		servicos.put("Assessoria 4", "Assessoria");
		return servicos;
	}

}