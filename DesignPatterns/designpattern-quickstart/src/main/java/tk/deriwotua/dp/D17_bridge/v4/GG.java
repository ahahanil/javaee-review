package tk.deriwotua.dp.D17_bridge.v4;

public class GG {
    public void chase(MM mm) {
        /**
         * 温暖的花
         */
        Gift g = new WarmGift(new Flower());
        give(mm, g);
    }

    public void give(MM mm, Gift g) {
        System.out.println(g + "gived!");
    }

}
