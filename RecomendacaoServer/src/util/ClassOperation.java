/**
 * 
 */
package util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

/**
 * @author trdp
 *
 */
public class ClassOperation {

	public static byte[] serialize(Object object) {

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = null;

		try {
			out = new ObjectOutputStream(bos);
			out.writeObject(object);
			out.flush();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				bos.close();
			} catch (IOException ex) {
				// ignore close exception
			}
		}

		return bos.toByteArray();
	}

	public static Object deserialize(byte[] data) {
		try {
			ByteArrayInputStream in = new ByteArrayInputStream(data);

			ObjectInputStream is = new ObjectInputStream(in);

			return is.readObject();
		} catch (Exception e) {
			e.printStackTrace();

			return null;
		}

	}

}
