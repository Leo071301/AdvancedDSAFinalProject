import java.awt.geom.Point2D;

public class Tester {
    public static void main(String[] args) {
        Colony farm = new Colony("Farm", new Point2D.Double(100, 200), 5);
        farm.addResource(new Food("Beans", 20, "Canned"));

        TradeRequest tr2 = new TradeRequest(farm, new Medicine("Antibiotics", 20, "Pills"));

        System.out.println(tr2);
    }
}
