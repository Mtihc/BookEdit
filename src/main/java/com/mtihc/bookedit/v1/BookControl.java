package com.mtihc.bookedit.v1;

import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BookControl {

	private BookManager manager;

	public BookControl(BookManager manager) {
		this.manager = manager;
	}

	public BookManager getManager() {
		return manager;
	}
	
	public ItemStack getBookInHand(Player player) throws BookException {
		ItemStack item = player.getItemInHand();
		if(item.getType() != Material.BOOK_AND_QUILL && item.getType() != Material.WRITTEN_BOOK) {
			throw new BookException("You should be holding a " + Material.BOOK_AND_QUILL.name().toLowerCase() + " or " + Material.WRITTEN_BOOK.name().toLowerCase() + " in your hands.");
		}
		return item;
	}
	
	public ItemStack getWrittenBookInHand(Player player) throws BookException {
		ItemStack item = player.getItemInHand();
		if(item.getType() != Material.WRITTEN_BOOK) {
			throw new BookException("You should be holding a " + Material.WRITTEN_BOOK.name().toLowerCase() + " in your hands.");
		}
		return item;
	}
	
	public ItemStack getBookAndQuillInHand(Player player) throws BookException {
		ItemStack item = player.getItemInHand();
		if(item.getType() != Material.BOOK_AND_QUILL) {
			throw new BookException("You should be holding a " + Material.BOOK_AND_QUILL.name().toLowerCase() + " in your hands.");
		}
		return item;
	}
	
	public void save(Player player, String id) throws BookException, BookPermissionException {
		
		if(!manager.getPermissions().hasPermission(player, BookAction.SAVE)) {
			throw new BookPermissionException("You don't have permission to save books.");
		}
		
		ItemStack item = getBookInHand(player);
		
		IBookRepository<String, Book> books = manager.getBooks();
		if(books.has(id)) {
			throw new BookException("Book \"" + id + "\" already exists.");
		}
		
		Book book = new Book(item);
		
		if(!book.getAuthor().equals(player.getName())) {
			throw new BookPermissionException("You can only save books, of which you are the author.");
		}
		
		book.setAuthor(player.getName());
		book.setTitle(id);
		book.setType(Material.WRITTEN_BOOK);
		books.set(id, book);
		
		player.sendMessage(ChatColor.GREEN + "Book \"" + id + "\" saved.");
	}
	
	public void load(Player player, String id) throws BookException, BookPermissionException {
		
		if(!manager.getPermissions().hasPermission(player, BookAction.LOAD)) {
			throw new BookPermissionException("You don't have permission to load books.");
		}
		
		IBookRepository<String, Book> books = manager.getBooks();
		
		if(!books.has(id)) {
			throw new BookException("Book \"" + id + "\" doesn't exist.");
		}
		Book book = books.get(id);
		if(book == null) {
			throw new BookException("Failed to load book \"" + id + "\".");
		}
		
		if(!player.getName().equalsIgnoreCase(book.getAuthor()) && !manager.getPermissions().hasPermission(player, BookAction.LOAD_UNOWNED)) {
			throw new BookPermissionException("You don't have permission to load books of other authors.");
		}
		
		ItemStack item = book.getItem();
		boolean added = player.getInventory().addItem(item).isEmpty();
		
		if(!added) {
			player.sendMessage(ChatColor.RED + "Failed to add book to your inventory!");
		}
		else {
			player.sendMessage(ChatColor.GOLD + "Book \"" + id + "\" added to your inventory.");
		}
		
	}
	
	public void edit(Player player) throws BookException, BookPermissionException {
		if(!manager.getPermissions().hasPermission(player, BookAction.EDIT)) {
			throw new BookPermissionException("You don't have permission to edit books.");
		}
		ItemStack item = getWrittenBookInHand(player);
		Book book = new Book(item);
		if(!player.getName().equals(book.getAuthor()) && !manager.getPermissions().hasPermission(player, BookAction.EDIT_UNOWNED)) {
			throw new BookPermissionException("You don't have permission to edit books of other authors.");
		}
		book.setAuthor("");
		book.setTitle("");
		item = book.getItem();
		item.setType(Material.BOOK_AND_QUILL);
		player.setItemInHand(item);
		player.sendMessage(ChatColor.GREEN + "The book in your hand, can now be edited again.");
	}
	
	public void delete(CommandSender sender, String id) throws BookException, BookPermissionException {
		
		if(!manager.getPermissions().hasPermission(sender, BookAction.DELETE)) {
			throw new BookPermissionException("You don't have permission to delete books.");
		}
		
		IBookRepository<String, Book> books = manager.getBooks();
		if(!books.has(id)) {
			throw new BookException("Book \"" + id + "\" doesn't exist.");
		}
		
		boolean allowUnowned = manager.getPermissions().hasPermission(sender, BookAction.DELETE_UNOWNED);
		
		Book book = books.get(id);
		if(book == null) {
			if(!allowUnowned) {
				throw new BookException("Failed to delete book \"" + id + "\". Unable to determine if you are author of this book. Because the book could not be loaded.");
			}
		}
		else {
			if(!allowUnowned && !sender.getName().equalsIgnoreCase(book.getAuthor())) {
				throw new BookPermissionException("You don't have permission to delete books of other authors.");
			}
		}

		books.remove(id);
		sender.sendMessage(ChatColor.YELLOW + "Book \"" + id + "\" deleted.");
	}
	
	public void sendList(CommandSender sender, int page) throws BookException, BookPermissionException {
		
		if(!manager.getPermissions().hasPermission(sender, BookAction.LIST)) {
			throw new BookPermissionException("You don't have permission to list saved book id's.");
		}
		
		IBookRepository<String, Book> books = manager.getBooks();
		
		Set<String> ids = books.getKeys();
		if(ids.isEmpty()) {
			if(page > 1) {
				throw new BookException("There are no saved books.");
			}
			else {
				sender.sendMessage(ChatColor.RED + "There are no saved books.");
				return;
			}
		}
		int total = ids.size();
		int totalPerPage;
		
		
		if(page <= 0) {
			page = 1;
			totalPerPage = total;
		}
		else {
			totalPerPage = 10;
		}
		
		int startIndex = (page - 1) * totalPerPage;
		int endIndex = startIndex + totalPerPage;

		int totalPages = (int) Math.ceil((float) total / totalPerPage);
		if (page > totalPages || page < 1) {
			throw new BookException("Page " + page + " does not exist.");
		}
		sender.sendMessage(ChatColor.GREEN + "Saved books (page "
				+ page + "/" + totalPages + "):");

		String[] idArray = ids.toArray(new String[total]);
		
		for (int i = startIndex; i < endIndex && i < total; i++) {
			sender.sendMessage(ChatColor.GREEN + "  " + String.valueOf(i + 1) + ": " + ChatColor.WHITE + idArray[i]);
		}
	}
	
	public void sendInfo(CommandSender sender, String id, int page) throws BookException, BookPermissionException {
		
		if(!manager.getPermissions().hasPermission(sender, BookAction.INFO)) {
			throw new BookPermissionException("You don't have permission to get info about books.");
		}
		
		IBookRepository<String, Book> books = manager.getBooks();
		
		if(!books.has(id)) {
			throw new BookException("Book \"" + id + "\" doesn't exist.");
		}
		Book book = books.get(id);
		if(book == null) {
			throw new BookException("Failed to load book \"" + id + "\".");
		}

		if(!sender.getName().equalsIgnoreCase(book.getAuthor()) && !manager.getPermissions().hasPermission(sender, BookAction.INFO_UNOWNED)) {
			throw new BookPermissionException("You don't have permission to get info about books of other authors.");
		}
		
		sender.sendMessage(ChatColor.GREEN + "ID: " + ChatColor.WHITE + id);
		sender.sendMessage(ChatColor.GREEN + "Title: " + ChatColor.WHITE + book.getTitle());
		sender.sendMessage(ChatColor.GREEN + "Author: " + ChatColor.WHITE + book.getAuthor());
		
		if(page > 0) {
			String lines = book.getPages().get(page - 1);
			sender.sendMessage(ChatColor.GREEN + "Page " + page + ": ");
			sender.sendMessage(ChatColor.WHITE + "  " + lines);
		}
	}
}
