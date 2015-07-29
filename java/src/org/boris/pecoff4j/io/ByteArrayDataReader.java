/*******************************************************************************
 * This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/cpl-v10.html
 *
 * Contributors:
 *     Peter Smith
 *******************************************************************************/
package org.boris.pecoff4j.io;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public class ByteArrayDataReader implements IDataReader {
  private byte[] data;
  private int position;
  private int offset;
  private int length;

  public ByteArrayDataReader(byte[] data) {
    this.data = data;
  }

  public ByteArrayDataReader(byte[] data, int offset, int length) {
    this.data = data;
    this.offset = offset;
    this.length = length;
  }

  public void close() throws IOException {
  }

  public int getPosition() {
    return position;
  }

  public void jumpTo(int location) throws IOException {
    position = location;
  }

  public void read(@NotNull byte[] b) throws IOException {
    for (int i = 0; i < b.length; i++) {
      b[i] = data[offset + position + i];
    }
    position += b.length;
  }

  public int readByte() throws IOException {
    if (offset + position >= data.length)
      return -1;
    return (char) (data[offset + position++] & 0xff);
  }

  public long readLong() throws IOException {
    return readDoubleWord() | ((long) readDoubleWord()) << 32;
  }

  public int readDoubleWord() throws IOException {
    return readWord() | readWord() << 16;
  }

  @NotNull
  public String readUtf(int size) throws IOException {
    byte[] b = new byte[size];
    read(b);
    return new String(b);
  }

  public String readUtf() throws IOException {
    StringBuilder sb = new StringBuilder();
    while (true) {
      char c = (char) readByte();
      if (c == 0) {
        break;
      }
      sb.append(c);
    }
    return sb.toString();
  }

  public int readWord() throws IOException {
    return readByte() | readByte() << 8;
  }

  public void skipBytes(int numBytes) throws IOException {
    position += numBytes;
  }

  @Nullable
  public String readUnicode() throws IOException {
    StringBuilder sb = new StringBuilder();
    char c = 0;
    while ((c = (char) readWord()) != 0) {
      sb.append(c);
    }
    if (sb.length() == 0) {
      return null;
    }
    return sb.toString();
  }

  public String readUnicode(int size) throws IOException {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < size; i++) {
      sb.append((char) readWord());
    }
    return sb.toString();
  }
}
