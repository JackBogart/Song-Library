package songlib.app;

/*
 * Song
 * John Bogart & Connor Holm
 * Rutgers University
 * 2023
 * 
 * TO-DO
 * General bug testing
 */


public class Song implements Comparable<Song>{
    private String name; 
    private String artist;
    private String album; 
    private String year; 
    private String concatenate; //concatenate of name+artist; displayed in ListView

    /*public Song(String name, String artist) {
        this.name = name;
        this.artist = artist;
        concatenate = name + " | " + artist;
    }

    public Song(String name, String artist, String albumORyear) {
        this(name, artist);
        if (isNumeric(albumORyear)) {
            this.year=albumORyear; 
        }
        else{
            this.album=albumORyear; 
        }
    }*/
    public Song(String name, String artist, String album, String year) {
        this.name = name;
        this.artist = artist;
        concatenate = name + " | " + artist;
        this.album = album; 
        this.year = year; 
    }

    public String getName() {
        return this.name; 
    }

    public String getArtist() {
        return this.artist; 
    }

    public String getAlbum() {
        return this.album; 
    }

    public String getYear() {
        return this.year; 
    }

    public String getConcatenate() {
        return concatenate; 
    }

    /*public static boolean isNumeric(String string) {
        if(string == null || string.equals("")) {
            return false;
        }
        try {
            Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e) {
        }
        return false;
    }*/

    public String toString() {
    return this.name+" | "+this.artist;
    }

    @Override
    public int compareTo(Song o) {
        return getConcatenate().compareToIgnoreCase(o.getConcatenate());
    }
}

