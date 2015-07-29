/*******************************************************************************
 * This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/cpl-v10.html
 *
 * Contributors:
 *     Peter Smith
 *******************************************************************************/
package org.boris.pecoff4j.asm;

import org.jetbrains.annotations.NotNull;

public abstract class AbstractInstruction implements Instruction {
  protected byte[] code;
  protected int offset;
  protected String label;

  public int size() {
    return code.length;
  }

  public byte[] toCode() {
    return code;
  }

  public int getOpCode() {
    return code[0] & 0xff;
  }

  public void setOffset(int offset) {
    this.offset = offset;
  }

  public int getOffset() {
    return offset;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public String getLabel() {
    return label;
  }

  @NotNull
  protected byte[] toCode(int opcode) {
    return new byte[]{(byte) opcode};
  }

  @NotNull
  protected byte[] toCode(int opcode, @NotNull ModRM modrm) {
    return new byte[]{(byte) opcode, (byte) modrm.encode()};
  }

  @NotNull
  protected byte[] toCode(int opcode, byte imm8) {
    return new byte[]{(byte) opcode, imm8};
  }

  @NotNull
  protected byte[] toCode(int opcode, int imm32) {
    return new byte[]{(byte) opcode, b1(imm32), b2(imm32), b3(imm32), b4(imm32)};
  }

  @NotNull
  protected byte[] toCode(int opcode, @NotNull ModRM modrm, byte imm8) {
    return new byte[]{(byte) opcode, (byte) modrm.encode(), imm8};
  }

  @NotNull
  protected byte[] toCode(int opcode, @NotNull ModRM modrm, int imm32) {
    return new byte[]{(byte) opcode, (byte) modrm.encode(), b1(imm32), b2(imm32), b3(imm32), b4(imm32)};
  }

  @NotNull
  protected byte[] toCode(int opcode, @NotNull ModRM modrm, @NotNull SIB sib, byte imm8) {
    return new byte[]{(byte) opcode, (byte) modrm.encode(), (byte) sib.encode(), imm8};
  }

  @NotNull
  protected byte[] toCode(int opcode, @NotNull ModRM modrm, @NotNull SIB sib, int imm32) {
    return new byte[]{(byte) opcode, (byte) modrm.encode(), (byte) sib.encode(), b1(imm32), b2(imm32), b3(imm32),
            b4(imm32)};
  }

  @NotNull
  protected byte[] toCode(int opcode, @NotNull ModRM modrm, int disp32, int imm32) {
    return new byte[]{(byte) opcode, (byte) modrm.encode(), b1(disp32), b2(disp32), b3(disp32), b4(disp32),
            b1(imm32), b2(imm32), b3(imm32), b4(imm32)};
  }

  @NotNull
  protected byte[] toCode(int opcode, @NotNull ModRM modrm, byte disp8, int imm32) {
    return new byte[]{(byte) opcode, (byte) modrm.encode(), disp8, b1(imm32), b2(imm32), b3(imm32), b4(imm32)};
  }

  protected byte b1(int value) {
    return (byte) (value & 0xff);
  }

  protected byte b2(int value) {
    return (byte) ((value >> 8) & 0xff);
  }

  protected byte b3(int value) {
    return (byte) ((value >> 16) & 0xff);
  }

  protected byte b4(int value) {
    return (byte) ((value >> 24) & 0xff);
  }

  public static String toHexString(int value, boolean showSign) {
    StringBuilder sb = new StringBuilder();
    if (showSign) {
      if (value < 0) {
        value *= -1;
        sb.append('-');
      } else
        sb.append('+');
    }
    String s = Integer.toHexString(value);
    int pad = 8 - s.length();
    for (int i = 0; i < pad; i++)
      sb.append('0');
    sb.append(s);
    // sb.append('h');
    return sb.toString();
  }

  public static String toHexString(byte value, boolean showSign) {
    StringBuilder sb = new StringBuilder();
    if (showSign) {
      if (value < 0) {
        value *= -1;
        sb.append('-');
      } else
        sb.append('+');
    }
    String s = Integer.toHexString(value & 0xff);
    int pad = 2 - s.length();
    for (int i = 0; i < pad; i++)
      sb.append('0');
    sb.append(s);
    // sb.append('h');
    return sb.toString();
  }

}
