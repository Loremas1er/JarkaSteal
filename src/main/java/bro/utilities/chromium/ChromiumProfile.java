package bro.utilities.chromium;

public class ChromiumProfile {
   public final String profileName;
   public String fullPath;
   public final String shortcut_name;

   public ChromiumProfile(String var1, String var2, String var3, String var4) {
      this.profileName = var1;
      this.shortcut_name = var4;
   }

   public void setUserDataPath(String var1) {
      this.fullPath = var1;
   }

   public String getFullPath() {
      return this.fullPath;
   }

   public String getProfileName() {
      return this.profileName;
   }

   public String getShortcutName() {
      return this.shortcut_name;
   }
}
