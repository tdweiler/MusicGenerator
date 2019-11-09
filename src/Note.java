public class Note {
    private String note;
    private double length;

    public Note(String note, double length) {
        this.note = note;
        this.length = length;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }
}
