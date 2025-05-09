package com.dinkygames.graaleditor;

import java.awt.Point;

public class TileFunctions {
   public static final String BASE64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

   public static final Point tileToPoint(int t) {
      int px = t % 16 * 16 + t / 512 * 256;
      int py = t / 16 * 16 % 512;
      return new Point(px, py);
   }

   public static final short base64toTile(String s) {
      return s.length() > 2 ? 0 : (short)("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".indexOf(s.charAt(0)) * 64 + "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".indexOf(s.charAt(1)));
   }

   public static final String tiletoBase64(Short t) {
      if (t < 0) {
         return "  ";
      } else if (t >= 4096) {
         return "AA";
      } else {
         int one = Math.max(0, Math.min(63, t / 64));
         int two = Math.max(0, Math.min(63, t - one * 64));
         return String.valueOf("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".charAt(one)) + String.valueOf("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".charAt(two));
      }
   }

   public static final short pointToTile(Point p) {
      return (short)(p.x / 16 % 16 + p.x / 256 * 512 + p.y / 16 * 16 % 512);
   }
}
