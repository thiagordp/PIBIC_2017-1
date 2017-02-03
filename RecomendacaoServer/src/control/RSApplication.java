/**
 * 
 */
package control;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

/**
 * @author trdp
 *
 */
public class RSApplication extends Application {
	public Set<Class<?>> getClasses() {

		Set<Class<?>> s = new HashSet<Class<?>>();
		s.add(Servidor.class);

		return s;
	}
}
