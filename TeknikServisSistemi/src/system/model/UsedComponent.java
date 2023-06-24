package system.model;

public class UsedComponent {

    private final Component component;
    private int piece;

    public UsedComponent(Component component, int piece) {
        this.component = component;
        this.piece = piece;
    }

    public Component getComponent() {
        return component;
    }

    public int getPiece() {
        return piece;
    }

    public void setPiece(int piece) {
        this.piece = piece;
    }

    @Override
    public String toString() {
        return "UsedComponent{" +
                "component=" + component +
                ", piece=" + piece +
                '}';
    }
}
