package com.mtihc.bookedit.v1.plugin;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.mtihc.bookedit.v1.Book;
import com.mtihc.bookedit.v1.BookException;
import com.mtihc.bookedit.v1.BookManager;
import com.mtihc.bookedit.v1.util.commands.Command;
import com.mtihc.bookedit.v1.util.commands.CommandException;
import com.mtihc.bookedit.v1.util.commands.ICommand;
import com.mtihc.bookedit.v1.util.commands.SimpleCommand;

public class BookCommand extends SimpleCommand {

	private BookManager manager;

	public BookCommand(BookManager manager, ICommand parent, String[] aliases) {
		super(parent, aliases, "", null, "This is the main " + aliases[0] + " command.", null);
		this.manager = manager;
		setNested("help");
		setNested("edit");
		setNested("info");
		setNested("list");
		setNested("load");
		setNested("save");
		setNested("delete");
	}
	
	private Player checkPlayer(CommandSender sender) throws CommandException {
		if(!(sender instanceof Player)) {
			throw new CommandException("This command must be executed by a player, in game.");
		}
		return (Player) sender;
	}

	@Command(aliases = { "help" }, args = "", desc = "Gives you a book with commands.", help = { }, perm = "")
	public void help(CommandSender sender, String[] args) throws CommandException {
		if(!(sender instanceof Player)) {
			int page;
			try {
				page = Integer.parseInt(args[0]);
			} catch(NumberFormatException e) {
				throw new CommandException("Expected a page number instead of text.");
			} catch(Exception e) {
				page = 0;
			}
			sendHelp(sender, this, page);
			return;
		}
		else {
			if(args != null && args.length > 0) {
				throw new CommandException("Expected no arguments.");
			}
		}
		Book book = new Book(new ItemStack(Material.WRITTEN_BOOK));
		book.setTitle("/" + getUniqueLabel());
		book.setAuthor("Mtihc");
		
		String[] list = getNestedLabels();
		
		String result = "";
		for (String string : list) {
			result += getNested(string).getUsage() + "\n";
		}
		
		List<String> pages = book.getPages();
		pages.add(result);
		book.setPages(pages);
		
		ItemStack item = book.getItem();
		((Player) sender).getInventory().addItem(item);
		sender.sendMessage(ChatColor.GOLD + "You received a book with commands.");
		sender.sendMessage(ChatColor.GOLD + "To see help in chat, execute " + ChatColor.WHITE + "/" + getUniqueLabel() + " ?");
		
	}
	
	@Command(aliases = { "delete" }, args = "<id>", desc = "", help = { "" }, perm = Permission.DELETE)
	public void delete(CommandSender sender, String[] args) throws CommandException {

		String id;
		try {
			id = args[0];
		} catch(Exception e) {
			throw new CommandException("Expected a book id.");
		}
		
		try {
			manager.getControl().delete(sender, id);
		} catch (BookException e) {
			throw new CommandException(e.getMessage(), e);
		}
	}
	
	@Command(aliases = { "edit" }, args = "", desc = "Unsign a signed book.", help = { }, perm = Permission.EDIT)
	public void edit(CommandSender sender, String[] args) throws CommandException {
		Player player = checkPlayer(sender);
		
		try {
			manager.getControl().edit(player);
		} catch (BookException e) {
			throw new CommandException(e.getMessage(), e);
		}
	}
	
	@Command(aliases = { "info" }, args = "<id> [page]", desc = "Show a saved book's info.", help = { }, perm = Permission.INFO)
	public void info(CommandSender sender, String[] args) throws CommandException {
		String id;
		try {
			id = args[0];
		} catch(Exception e) {
			throw new CommandException("Expected a book id, and optionally a page number.");
		}
		int page;
		try {
			page = Integer.parseInt(args[1]);
		} catch(NumberFormatException e) {
			throw new CommandException("Expected a book id, and a page number instead of text.");
		} catch(Exception e) {
			page = 0;
		}
		try {
			manager.getControl().sendInfo(sender, id, page);
		} catch (BookException e) {
			throw new CommandException(e.getMessage(), e);
		}
	}
	
	@Command(aliases = { "list" }, args = "[page]", desc = "List saved books.", help = { }, perm = Permission.LIST)
	public void list(CommandSender sender, String[] args) throws CommandException {
		int page;
		try {
			page = Integer.parseInt(args[0]);
		} catch(NumberFormatException e) {
			throw new CommandException("Expected a page number, instead of text.");
		} catch(Exception e) {
			page = 0;
		}
		
		try {
			manager.getControl().sendList(sender, page);
		} catch (BookException e) {
			throw new CommandException(e.getMessage(), e);
		}
		
	}
	
	@Command(aliases = { "load" }, args = "<id>", desc = "Load a book.", help = { }, perm = Permission.LOAD)
	public void load(CommandSender sender, String[] args) throws CommandException {
		Player player = checkPlayer(sender);
		
		String id;
		try {
			id = args[0];
		} catch(Exception e) {
			throw new CommandException("Expected a book id.");
		}
		
		try {
			manager.getControl().load(player, id);
		} catch (BookException e) {
			throw new CommandException(e.getMessage(), e);
		}
	}
	
	@Command(aliases = { "save" }, args = "<id>", desc = "Save the book in your hand.", help = { }, perm = Permission.SAVE)
	public void save(CommandSender sender, String[] args) throws CommandException {
		Player player = checkPlayer(sender);
		
		String id;
		try {
			id = args[0];
		} catch(Exception e) {
			throw new CommandException("Expected a book id.");
		}
		
		try {
			manager.getControl().save(player, id);
		} catch (BookException e) {
			throw new CommandException(e.getMessage(), e);
		}
	}
}
