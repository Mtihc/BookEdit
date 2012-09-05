package com.mtihc.bookedit.v1.plugin;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import com.mtihc.bookedit.v1.Book;
import com.mtihc.bookedit.v1.IBookRepository;
import com.mtihc.bookedit.v1.util.Repository;

public class BookRepository<V extends Book> extends Repository<String, V> implements IBookRepository<String, V> {

	public BookRepository(File directory, Logger logger) {
		super(directory, logger);
	}

	public BookRepository(File directory) {
		super(directory);
	}

	public BookRepository(String directory, Logger logger) {
		super(directory, logger);
	}

	public BookRepository(String directory) {
		super(directory);
	}

	@Override
	protected String getPathByKey(String key) {
		return directory + File.separator + key + ".yml";
	}

	@Override
	public Set<String> getKeys() {
		final Set<String> result = new HashSet<String>();
		directory.list(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				if(name.endsWith(".yml")) {
					result.add(name.substring(0, name.length() - ".yml".length()));
				}
				return false;
			}
		});
		return result;
	}
	
}
