package com.mtihc.bookedit.v1;

import org.bukkit.command.CommandSender;

public interface IBookPermission {

	public boolean hasPermission(CommandSender sender, BookAction action);
	
}
