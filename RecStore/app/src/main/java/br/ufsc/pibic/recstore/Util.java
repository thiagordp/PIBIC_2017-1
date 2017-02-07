package br.ufsc.pibic.recstore;

/**
 * Created by trdp on 2/3/17.
 */

public class Util {
    public static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
