package bro.utilities;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.imageio.ImageIO;

public class FileUtils {
   public static void writeText(File var0, String var1) {
      try {
         FileWriter var2 = new FileWriter(var0);
         var2.write(var1);
      } catch (IOException ignored) {
      }

   }

   public static void copyDirectory(String var0, String var1) throws IOException {
      try {
         Files.copy(Paths.get(var0), Paths.get(var1));
      } catch (IOException ignored) {
      }
      //Files.walk(Paths.get(var0)).forEach(FileUtils::lambda$copyDirectory);
   }

   public static void writeImage(File var0, BufferedImage var1) {
      try {
         if (var0.exists()) {
            var0.delete();
         }

         ImageIO.write(var1, "jpg", var0);
      } catch (IOException var3) {
      }

   }

   //public static void lambda$copyDirectory(String var0, String var1, Path var2) {
   //   Path var3 = Paths.get(var0, var2.toString().substring(var1.length()));
   //   try {
   //      Files.copy(var2, var3);
   //   } catch (IOException ignored) {
   //
   //}
}
