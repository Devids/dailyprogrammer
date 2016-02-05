/**
 * Created by David nagy on 2/5/2016.
 */
public class Entity {

    String value;
    int fit;
    int copulations = 0;

    public Entity(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getFit() {
        return fit;
    }

    public void setFit(int fit) {
        this.fit = fit;
    }

    public void copulate() {
        copulations++;
    }

    public int getCopulations() {
        return copulations;
    }
}
