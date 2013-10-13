package ejb;

import java.io.IOException;

import javax.ejb.Remote;

@Remote
public interface MongoRemote {
	public void Crear(String nombreDB, int clienteId) throws IOException;
}
