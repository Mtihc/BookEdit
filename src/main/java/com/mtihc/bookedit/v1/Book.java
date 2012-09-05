package com.mtihc.bookedit.v1;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagList;
import net.minecraft.server.NBTTagString;

import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class Book implements ConfigurationSerializable, Cloneable {

	private String author;
	private String title;
	private List<String> pages;
	private CraftItemStack item;

	public Book(ItemStack item) {
		setItem(item);
	}

	@SuppressWarnings("unchecked")
	public Book(Map<String, Object> values) {
		ItemStack itemStack = (ItemStack) values.get("item-stack");
		CraftItemStack item = new CraftItemStack(itemStack);
		
		Map<?, ?> tagSection = (Map<?, ?>) values.get("tag");
		if(tagSection != null) {
			
			setAuthor(item, (String) tagSection.get("author"));
			setTitle(item, (String) tagSection.get("title"));
			setPages(item, (List<String>) tagSection.get("pages"));
		}
		
		setItem(item);
		
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> values = new LinkedHashMap<String, Object>();
		values.put("item-stack", item);
		Map<String, Object> tagSection = new LinkedHashMap<String, Object>();
		values.put("tag", tagSection);
		tagSection.put("author", getAuthor());
		tagSection.put("title", getTitle());
		tagSection.put("pages", getPages());
		
		return values;
	}
	
	public Material getType() {
		return item.getType();
	}
	
	public int getTypeId() {
		return item.getTypeId();
	}
	
	public void setType(Material type) {
		item.setType(type);
	}
	
	public void setType(int id) {
		item.setTypeId(id);
	}
	
	public NBTTagCompound getTag() {
		NBTTagCompound tag = item.getHandle().getTag();
		if(tag == null) {
			tag = new NBTTagCompound("tag");
			setTag(tag);
		}
		return tag;
	}
	
	public void setTag(NBTTagCompound tag) {
		item.getHandle().setTag(tag);
	}
	
	public String getAuthor() {
		return author;
	}
	
	public void setAuthor(String name) {
		this.author = name;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public List<String> getPages() {
		return pages;
	}
	
	public void setPages(List<String> pages) {
		this.pages = pages;
	}
	
	public ItemStack getItem() {
		CraftItemStack result = item.clone();
		setAuthor(result, author);
		setTitle(result, title);
		setPages(result, pages);
		return result;
	}

	public void setItem(ItemStack item) {
		if(item.getType() != Material.BOOK_AND_QUILL && item.getType() != Material.WRITTEN_BOOK) {
			throw new IllegalArgumentException("Parameter item must be " + Material.BOOK_AND_QUILL.name() + " or " + Material.WRITTEN_BOOK.name() + ".");
		}
		
		if(item instanceof CraftItemStack) {
			this.item = (CraftItemStack) item;
			author = getAuthor(this.item);
			title = getTitle(this.item);
			pages = getPages(this.item);
			this.item = this.item.clone();
		}
		else {
			this.item = new CraftItemStack(item);
			author = null;
			title = null;
			pages = new ArrayList<String>();
		}
	}

	@Override
	public Book clone() {
		return new Book(getItem());
	}
	
	public static String getAuthor(CraftItemStack item) {
		NBTTagCompound tag = item.getHandle().getTag();
		if(tag == null) return null;
		return item.getHandle().getTag().getString("author");
	}
	
	public static void setAuthor(CraftItemStack item, String author) {
		NBTTagCompound tag = item.getHandle().getTag();
		if(tag == null) tag = new NBTTagCompound("tag");
		tag.setString("author", author);
		item.getHandle().setTag(tag);
	}
	
	public static String getTitle(CraftItemStack item) {
		NBTTagCompound tag = item.getHandle().getTag();
		if(tag == null) return null;
		return item.getHandle().getTag().getString("title");
	}
	
	public static void setTitle(CraftItemStack item, String title) {
		NBTTagCompound tag = item.getHandle().getTag();
		if(tag == null) tag = new NBTTagCompound("tag");
		tag.setString("title", title);
		item.getHandle().setTag(tag);
	}
	
	public static NBTTagList getPagesTag(CraftItemStack item) {
		NBTTagCompound tag = item.getHandle().getTag();
		if(tag == null) return new NBTTagList("pages");
		return tag.getList("pages");
	}
	
	public static void setPagesTag(CraftItemStack item, NBTTagList pages) {
		NBTTagCompound tag = item.getHandle().getTag();
		if(tag == null) tag = new NBTTagCompound("tag");
		tag.set("pages", pages);
		item.getHandle().setTag(tag);
	}
	
	public static List<String> getPages(CraftItemStack item) {
		NBTTagList pagesTag = getPagesTag(item);
		int n = pagesTag.size();
		int i = 0;
		List<String> result = new ArrayList<String>();
		while(i < n) {
			
			result.add(
					((NBTTagString) pagesTag.get(i)).data);
			
			i++;
		}
		return result;
	}
	
	public static void setPages(CraftItemStack item, List<String> pages) {

		NBTTagList pagesTag = new NBTTagList();
		int i = 0;
		for (String page : pages) {
			
			pagesTag.add(
					new NBTTagString("page" + i, page));
			
			i++;
		}
		setPagesTag(item, pagesTag);
	}
	
}
