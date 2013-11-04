package org.test4j.plugin.utils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class SizedStringWriter {
	private final DataOutputStream out;

	public SizedStringWriter(OutputStream outputStream) {
		this.out = new DataOutputStream(outputStream);
	}

	public void put(String string) throws IOException {
		char[] buffer = string.toCharArray();
		if (buffer.length > 65536) {
			throw new IOException("Attempt to write too a large string");
		}
		this.out.writeChar(buffer.length);
		for (int i = 0; i < buffer.length; ++i) {
			this.out.writeChar(buffer[i]);
		}
	}

	public void close() throws IOException {
		this.out.close();
	}

	public void flush() throws IOException {
		this.out.flush();
	}
}