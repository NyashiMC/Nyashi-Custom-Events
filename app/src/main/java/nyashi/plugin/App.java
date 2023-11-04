/*
 * Plugin de Nyashi
 * En colaboracion con AgustinPro
 * Servidor: na.olseros.com
 * Eventos Personalizados
 */

package nyashi.plugin;
import java.io.File;
import java.io.IOException;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
public class App extends JavaPlugin {
    public FileConfiguration mensajes;
    public File mensajesFile;
    @Override
    public void onEnable(){
        ArcoCooldown arcocd = new ArcoCooldown(this);
        configurar();
        
        getServer().getPluginManager().registerEvents(arcocd, this);
    }
    public FileConfiguration getMsg(){
        return this.mensajes;
    }
    public static boolean hasPapi(){
        return Bukkit.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI");
    }

    public void configurar(){
        mensajesFile = new File(getDataFolder(),"messages.yml");
        if(!mensajesFile.exists()){
            mensajesFile.getParentFile().mkdirs();
            saveResource("messages.yml", true);
        }
        mensajes = new YamlConfiguration();
        try{
            mensajes.load(mensajesFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            
        }
    }
}
