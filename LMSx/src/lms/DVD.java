package lms;

public class DVD extends Item {
    private String director;
    private int duration; // Duration in minutes

    public DVD(int itemId, String title, String director, int duration) {
        super(itemId, title);
        this.director = director;
        this.duration = duration;
    }

    public String getDirector() {
        return director;
    }

    public int getDuration() {
        return duration;
    }

    @Override
    public String getType() {
        return "DVD";
    }

    @Override
    public String toString() {
        return super.toString() + "\nDirector: " + director + "\nDuration: " + duration + " minutes";
    }
}
