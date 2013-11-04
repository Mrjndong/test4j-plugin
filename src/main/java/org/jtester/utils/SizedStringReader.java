package org.jtester.utils;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class SizedStringReader {
	private final DataInputStream in;

	public SizedStringReader(InputStream inputStream) {
		this.in = new DataInputStream(inputStream);
	}

	public String get() throws IOException {
		int length = this.in.readChar();
		char[] buffer = new char[length];
		for (int i = 0; i < buffer.length; ++i) {
			buffer[i] = this.in.readChar();
		}
		return new String(buffer);
	}

	public void close() throws IOException {
		this.in.close();
	}
}