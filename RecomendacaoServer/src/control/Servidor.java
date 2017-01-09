package control;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@Path("/")
public class Servidor {

	private Mongo mongodb = new Mongo("pibic", "interacao");

	/*
	 * URL http://localhost:8080/RecomendacaoServer?user=2
	 * 
	 * Sem o caminho de contexto nem o do método (direto).
	 */
	@GET
	@Path("/")
	public String teste(@QueryParam("user") Integer user_id) {
		return "YEAH";
	}

}
