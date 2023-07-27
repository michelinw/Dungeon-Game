package dungeonmania.entities.buildables;

public class Sceptre extends Buildable {
    private int controlLength;

    public Sceptre(int controlLength) {
        super(null);
        this.controlLength = controlLength;
    }

    public int getControlLength() {
        return this.controlLength;
    }
}
