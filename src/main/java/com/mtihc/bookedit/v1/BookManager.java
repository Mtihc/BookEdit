package com.mtihc.bookedit.v1;

import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

public class BookManager {
	
	static {
		ConfigurationSerialization.registerClass(Book.class);
	}

	protected final IBookRepository<String, Book> books;
	private BookControl control;
	private IBookPermission permissions;

	public BookManager(JavaPlugin plugin, IBookRepository<String, Book> books, IBookPermission permissions) {
		this.books = books;
		this.control = new BookControl(this);
		this.permissions = permissions;
	}
	
	public IBookRepository<String, Book> getBooks() {
		return books;
	}
	
	public BookControl getControl() {
		return control;
	}
	
	public IBookPermission getPermissions() {
		return permissions;
	}

}
