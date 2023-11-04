package nyashi.plugin;

import java.util.Date;
import java.util.HashMap;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.projectiles.ProjectileSource;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatColor;

import java.util.Map;
public class ArcoCooldown implements Listener{
    public App plugin;
    public ArcoCooldown(JavaPlugin plugin){
        this.plugin = (App) plugin;
    }
    Map<String,Long> cooldown_arco = new HashMap<String,Long>();

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent evento){
        Projectile proyectil = evento.getEntity();
        if(proyectil instanceof Arrow && proyectil.getShooter() instanceof Player){
            Player atacante = (Player) proyectil.getShooter();
            String usuario = atacante.getName();
            Date actual = new Date();
            if(cooldown_arco.containsKey(usuario)){
                long delta = actual.getTime()-cooldown_arco.get(usuario);
                if(delta<5*1000){
                    String mensaje = plugin.getMsg().getString("Mensajes.cooldown");
                    Map<String,Object> variables = new HashMap<String,Object>(){{
                       put("time",((5*1000)-delta)/1000);

                    }};
                    for(Map.Entry<String,Object> variable: variables.entrySet()){
                        mensaje.replace("%"+variable.getKey()+"%", variable.getValue().toString());
                    }
                    if(App.hasPapi()){
                        PlaceholderAPI.setPlaceholders(atacante,mensaje);
                    }
                    atacante.sendMessage(ChatColor.translateAlternateColorCodes('&', mensaje));
                    evento.setCancelled(true);
                }
            }

        }
    }
    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent evento){
        if(evento.getCause() == DamageCause.PROJECTILE){
            
            Entity damager = evento.getDamager();
            Entity damaged = evento.getEntity();
            if(damaged instanceof Player && damager instanceof Arrow){
                Arrow flecha = (Arrow) damager;
                Player victima = (Player) damaged;
                ProjectileSource lanzador = flecha.getShooter();
                if(lanzador instanceof Player){
                    Player atacante = (Player) lanzador;
                    String usuario = atacante.getName();
                    if(victima == atacante){
                        atacante.sendMessage("No puedes atacarte a ti mismo");
                        evento.setCancelled(true);
                        return;
                    }
                    if(cooldown_arco.containsKey(usuario)){
                        cooldown_arco.remove(usuario);
                    }
                    Date tiempo = new Date();
                    long inicial = tiempo.getTime();
                    cooldown_arco.put(usuario,inicial);

                }

            }
        }
    }
}
