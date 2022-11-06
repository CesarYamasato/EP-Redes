package socketApplication;

public class Directory{
	private String directory;
	private boolean isFolder;
	
	public Directory(String directory, boolean isFolder) {
		this.directory = directory;
		this.isFolder = isFolder;
	}
	public boolean getFolder() {
		return isFolder;
	}
	public String getList() {
		return directory;
	}
}