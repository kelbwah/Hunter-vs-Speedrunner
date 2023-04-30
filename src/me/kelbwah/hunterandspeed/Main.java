package me.kelbwah.hunterandspeed;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import net.md_5.bungee.api.ChatColor;

public class Main extends JavaPlugin implements Listener{
	
	public static ArrayList<Player> players = new ArrayList<Player>();
	
	@Override
	public void onEnable()
	{
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
	}
	
	public void onDisable(){}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(label.equalsIgnoreCase("runner"))
		{
			if(sender instanceof Player)
			{
				Player runner = (Player) sender;
				players.add(0, runner);
				if(runner.hasPermission("runner.use"))
				{
					runner.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "You are now the speedrunner");
					return true;
				}
				runner.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "You do not have permission to use this");
				return true;
			}
		}
		else if(label.equalsIgnoreCase("hunter"))
		{
			if(sender instanceof Player)
			{
				Player hunter = (Player) sender;
				if(hunter.hasPermission("hunter.use"))
				{
					ItemStack compass = new ItemStack(Material.COMPASS);
					hunter.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "You are now a hunter");
					hunter.getInventory().addItem(compass);
					
					BukkitScheduler scheduler = getServer().getScheduler();
					scheduler.scheduleSyncRepeatingTask(this,  new Runnable() {
						@Override
						public void run() {
							hunter.setCompassTarget(players.get(0).getLocation());
						}
					}, 0L, 10L);
					
					return true;
				}
				hunter.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "You do not have permission to use this");
				return true;
			}
		}
		return true;	
	}
	
	

	@EventHandler
	public void onItemDrop(ItemSpawnEvent event)
	{
		boolean isCompass = event.getEntity().getItemStack().getType() == Material.COMPASS;
		if(isCompass == true)
		{
			event.getEntity().remove();
		}
	}
	
	
	//Keeps the hunter's compass even if they die
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent player)
	{
		ItemStack compass = new ItemStack(Material.COMPASS);
		Player thePlayer = player.getPlayer();
		if(thePlayer != players.get(0))
		{
			thePlayer.getInventory().addItem(compass);
		}
	}
	
	
	
}
