package net.pdp7.commons.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IoUtils {
	
	protected IoUtils() {}
	
	public static void copy(InputStream in, OutputStream out) throws IOException {
		byte[] buf = new byte[1024];
		int read;
		while( (read = in.read(buf)) != -1) {
			out.write(buf, 0, read);
		}
	}

}
