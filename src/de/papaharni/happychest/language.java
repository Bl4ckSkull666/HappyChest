package de.papaharni.happychest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class language {
    private final Map<String, String> _lang = new HashMap<>();
    
    public language() {
        load();
    }
    
    public String getString(String key) {
        return (_lang.containsKey(key)?_lang.get(key):"No Message for " + key + " is set yet.");
    }
    
    private void load() {
        copyResource("/language.yml", new File(HappyChest.getInstance().getDataFolder(), "language.yml"));
        File file = new File(HappyChest.getInstance().getDataFolder(), "language.yml");
        FileConfiguration conf = YamlConfiguration.loadConfiguration(file);
        for(String key : conf.getConfigurationSection("lang").getKeys(false)) {
            _lang.put(key, conf.getString("lang." + key));
        }
    }
    
    private static void copyResource(String internalPath, File externalFile) {
        try {
            final InputStream stream = language.class.getResourceAsStream(internalPath);
            if(!externalFile.exists()) {
                externalFile.createNewFile();
                FileOutputStream fis = new FileOutputStream(externalFile);
                byte[] buffer = new byte[1024];
                int len;
                while((len = stream.read(buffer)) > 0) {
                    fis.write(buffer, 0, len);
                }
                fis.close();
                stream.close();
            }
        } catch (IOException ex) {
            HappyChest.getInstance().getLogger().log(Level.SEVERE, "Fehler beim auslesen der languange.yml. Bitte ggf aus der jar file manuell herauskopieren.", ex);
        }
    }

    private static String getResource(String internalPath) {
        final InputStream stream = language.class.getResourceAsStream(internalPath);
        final Scanner sc = new Scanner(stream);
        final StringBuilder sb = new StringBuilder();
        while(sc.hasNext()) {
            sb.append(sc.nextLine());
            sb.append("\n");
        }
        return sb.toString();
    }
}
