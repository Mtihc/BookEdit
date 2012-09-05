package com.mtihc.bookedit.v1.plugin;

import java.util.HashMap;

import org.bukkit.command.CommandSender;

import com.mtihc.bookedit.v1.BookAction;
import com.mtihc.bookedit.v1.IBookPermission;

public class BookPermission implements IBookPermission {

	private HashMap<BookAction, String> perms;

	public BookPermission() {
		perms = new HashMap<BookAction, String>();
		
		perms.put(BookAction.DELETE, 			Permission.DELETE);
		perms.put(BookAction.DELETE_UNOWNED, 	Permission.DELETE_UNOWNED);
		perms.put(BookAction.EDIT, 				Permission.EDIT);
		perms.put(BookAction.EDIT_UNOWNED, 		Permission.EDIT_UNOWNED);
		perms.put(BookAction.INFO, 				Permission.INFO);
		perms.put(BookAction.INFO_UNOWNED, 		Permission.INFO_UNOWNED);
		perms.put(BookAction.LIST, 				Permission.LIST);
		perms.put(BookAction.LOAD, 				Permission.LOAD);
		perms.put(BookAction.LOAD_UNOWNED, 		Permission.LOAD_UNOWNED);
		perms.put(BookAction.SAVE, 				Permission.SAVE);
		perms.put(BookAction.SAVE_UNOWNED, 		Permission.SAVE_UNOWNED);
	}

	@Override
	public boolean hasPermission(CommandSender sender, BookAction action) {
		if(sender == null) {
			return false;
		}
		String perm = perms.get(action);
		if(perm == null || perm.isEmpty()) {
			return true;
		}
		return sender.hasPermission(perm);
	}

}
