package socketApplication;

import java.util.List;
import java.util.ArrayList;

class Item{
	private String name;
	private boolean is_folder;

	public Item(String name, boolean is_folder){
		this.name = name;
		this.is_folder = is_folder;
	}

	public String toString(){
		return name;
	}

	public boolean isFolder(){
		return is_folder;
	}
}

public class Directory{
	
	private List<Item> items = new ArrayList<Item>();

	public Directory(){}

	public void add_item(Item item){items.add(item);}
	public void add_item(String name, boolean is_folder){items.add(new Item(name, is_folder));}

	public void printDirectory(){
		System.out.println("List of files and directories that can be sent:");
		int i = 0;
		for(Item item : items) {
			if(item.isFolder())System.out.println(i + ":" +  item + "  Folder");
			else System.out.println(i + ":" + item + "  Folder");
		}
	}

	public Item getItem(int  index){
		return items.get(index);
	}

	public int length(){
		return items.size();
	}
}