package base.conhecimento.dao;

import java.util.Map;

public interface PersistenceDAO {

	Map<String, Object> buscarTodosEstados();
	
	Map<String, Object> buscarTodosServicos();
}
