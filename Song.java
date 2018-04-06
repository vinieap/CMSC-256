/**
 * Vinit Patel
 * 03/24/2018
 * CMSC 256-ALL
 * Project #4 - SongReader
 * Project Purpose : Read a playlist file and create Song Objects
 */

public class Song implements Comparable<Song>{

    private String title;
    private String artist;
    private String album;

    /**
     * Default constructor, set the class variables to empty strings
     */
    public Song() {
        setTitle("");
        setArtist("");
        setAlbum("");
    }

    /**
     * Song constructor with 3 parameters
     * @param title - title of the song
     * @param artist - artist of the song
     * @param album - album of the song
     */
    public Song(String title,String artist,String album) {
        setTitle(title);
        setAlbum(album);
        setArtist(artist);
    }

    /**
     * Gets Title
     * @return - title of the song
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets Title
     * @p - sets the title of the song
     */
    public void setTitle(String title) {
        try{
            this.title = title.trim();
        }
        catch(NullPointerException n) {
            this.title = "";
        }
    }

    /**
     * Gets Artist
     * @return - artist of the song
     */
    public String getArtist() {
        return artist;
    }

    /**
     * Sets Artist
     * @p - sets artist of the song
     */
    public void setArtist(String artist) {
        try{
            this.artist = artist.trim();
        }
        catch(NullPointerException n) {
            this.artist = "";
        }
    }

    /**
     * Gets Album
     * @return - album of the song
     */
    public String getAlbum() {
        return album;
    }

    /**
     * Sets Album
     * @p - sets album of the song
     */
    public void setAlbum(String album) {
        try{
            this.album = album.trim();
        }
        catch(NullPointerException n) {
            this.album = "";
        }
    }

    /**
     * Overrides toString() and returns artist, album, and title
     * @return - song information
     */
    @Override
    public String toString() {
        return ("1. " + artist + "\n2. " + album + "\n3. " + title + "\n");
    }

    /**
     * Checks if 2 song objects are of equality
     * @param o - Object passed in
     * @return - equal or not
     */
    @Override
    public boolean equals(Object o) {
        if(o == this) {
            return true;
        }

        if(!(o instanceof Song)) {
            return false;
        }

        Song s = (Song)o;

        return this.compareTo(s) == 0;

    }

    /**
     * Checks which song comes first or if they are equal
     * @param o - Song object passed in
     * @return - -1, 0, or 1
     */
    @Override
    public int compareTo(Song o) {

        if(o == null) {
            throw new NullPointerException();
        }

        if(!this.getArtist().equalsIgnoreCase(o.getArtist())) {
            return this.getArtist().compareTo(o.getArtist());
        }
        if(!this.getAlbum().equalsIgnoreCase(o.getAlbum())) {
            return this.getAlbum().compareTo(o.getAlbum());
        }
        if(!this.getTitle().equalsIgnoreCase(o.getTitle())) {
            return this.getTitle().compareTo(o.getTitle());
        }

        assert this.equals(o);

        return 0;
    }
}
