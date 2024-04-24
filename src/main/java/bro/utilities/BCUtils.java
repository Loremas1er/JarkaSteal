package bro.utilities;

import com.sun.jna.platform.win32.Crypt32Util;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class BCUtils {
   public static String encryptedBinaryStreamToDecryptedString(InputStream var0, String var1) throws Exception {
      StringBuilder var2 = new StringBuilder();
      String var3 = streamToString(var0);
      byte[] var4 = Base64.getDecoder().decode(var1);
      byte[] var5 = Arrays.copyOfRange(var4, 5, var4.length);
      byte[] var6 = Crypt32Util.cryptUnprotectData(var5);
      byte[] var7 = hexStringToByteArray(var3);
      byte[] var8 = Arrays.copyOfRange(var7, 3, 15);
      byte[] var9 = Arrays.copyOfRange(var7, 15, var7.length);
      Cipher var10 = Cipher.getInstance("AES/GCM/NoPadding");
      GCMParameterSpec var11 = new GCMParameterSpec(128, var8);
      SecretKeySpec var12 = new SecretKeySpec(var6, "AES");
      var10.init(2, var12, var11);
      byte[] var13;
      byte[] var14 = var13 = var10.doFinal(var9);
      int var15 = var14.length;

      for(int var16 = 0; var16 < var15; var16++) {
         byte var17 = var13[var16];
         var2.append((char)var17);
      }

      return var2.toString();
   }

   public static String streamToString(InputStream var0) throws IOException {
      StringBuilder var1 = new StringBuilder();

      while(var0.available() > 0) {
         String var2 = String.format("%s", Integer.toHexString(var0.read()));
         if (var2.length() == 1) {
            var1.append("0").append(var2);
         } else {
            var1.append(var2);
         }
      }

      var0.close();
      return var1.toString();
   }

   public static byte[] hexStringToByteArray(String s) {
      int len = s.length();
      byte[] data = new byte[len / 2];
      for (int i = 0; i < len; i += 2) {
         data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                 + Character.digit(s.charAt(i+1), 16));
      }
      return data;
   }
   //public static byte[] dec(byte[] var0, byte[] var1, byte[] var2) {
   //   for(int var3 = 0; var3 < var0.length; var3++) {
   //      var2[var3] = (byte)(var0[var3] ^ var1[var3 % var1.length]);
   //   }
   //   return var2;
   //}
}
