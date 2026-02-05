package helpers;

public enum Era {
	PAST("past", "/bg_past.jpeg"),
    PRESENT("present", "/bg_present.jpg"),
    FUTURE("future", "/bg_future.jpg");
	private final String folder;
	private final String backgroundPath;
	/**
	 * The Era enum defines the available game eras (Past, Present, Future) and
	 * stores the folder name and background image path used for loading era-specific visuals.
	 * @param folder The folder name where the era’s image assets are stored.
	 * @param backgroundPath The file path of the background image for the era.
	 */
	Era(String folder, String backgroundPath) {
        this.folder = folder;
        this.backgroundPath = backgroundPath;
    }
	/**
	 * builds and returns the correct file path for an image 
	 * sprite by combining the era folder name with the given object name.
	 * @param name
	 * @return the file path of a sprite image for the given object name in the selected era.
	 */
    public String getSpritePath(String name) {
    	return "/gui/assets/" + folder + "/" + name + ".jpeg";
    }
    /**
     * 
     * @return the file path of the background image for the selected era.
     */
    public String getBackgroundPath() {
        return backgroundPath;
    }
}