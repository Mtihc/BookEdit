package com.mtihc.bookedit.v1.plugin;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import com.mtihc.bookedit.v1.Book;
import com.mtihc.bookedit.v1.BookManager;
import com.mtihc.bookedit.v1.IBookRepository;
import com.mtihc.bookedit.v1.util.commands.CommandException;

public class BookEditPlugin extends JavaPlugin {

	private BookManager manager;
	private BookCommand cmd;

	/* (non-Javadoc)
	 * @see org.bukkit.plugin.java.JavaPlugin#onCommand(org.bukkit.command.CommandSender, org.bukkit.command.Command, java.lang.String, java.lang.String[])
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		String lbl = label.toLowerCase();
		if(cmd.getLabel().equals(lbl) || cmd.getAliases().contains(lbl)) {
			
			try {
				cmd.execute(sender, args);
			} catch (CommandException e) {
				Throwable error = e;
				while(error instanceof CommandException) {
					sender.sendMessage(ChatColor.RED + error.getMessage());
					error = error.getCause();
				}
				
				String echo = "/" + label;
				for (String arg : args) {
					echo += " " + arg;
				}
				sender.sendMessage(echo);
			}
			
			return true;
		}
		else {
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see org.bukkit.plugin.java.JavaPlugin#onEnable()
	 */
	@Override
	public void onEnable() {
		
		IBookRepository<String, Book> books = new BookRepository<Book>(getDataFolder() + File.separator + "books");
		manager = new BookManager(this, books, new BookPermission());
		
		ArrayList<String> aliases = new ArrayList<String>();
		PluginCommand command = getCommand("book");
		aliases.add(command.getLabel());
		for (String alias : command.getAliases()) {
			aliases.add(alias);
		}
		
		cmd = new BookCommand(manager, null, aliases.toArray(new String[aliases.size()]));
	}

	

}
