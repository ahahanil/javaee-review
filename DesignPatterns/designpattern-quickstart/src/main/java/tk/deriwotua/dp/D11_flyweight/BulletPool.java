package tk.deriwotua.dp.D11_flyweight;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 子弹
 */
class Bullet{
    public UUID id = UUID.randomUUID();
    /**
     * 子弹状态是否发射了
     */
    boolean living = true;

    @Override
    public String toString() {
        return "Bullet{" +
                "id=" + id +
                '}';
    }
}

/**
 * 子弹池
 */
public class BulletPool {
    List<Bullet> bullets = new ArrayList<>();
    {
        for(int i=0; i<5; i++) bullets.add(new Bullet());
    }

    /**
     * 获取还未发射的子弹
     * @return
     */
    public Bullet getBullet() {
        for(int i=0; i<bullets.size(); i++) {
            Bullet b = bullets.get(i);
            if(!b.living) return b;
        }
        /**
         * 都发射了再创建
         */
        return new Bullet();
    }

    /**
     * 坦克大战中射出的子弹没必要每次射击都创建一个新子弹对象
     * 而是先初始化一些子弹到子弹池里射击时直接取(共享的)
     * @param args
     */
    public static void main(String[] args) {
        BulletPool bp = new BulletPool();

        for(int i=0; i<10; i++) {
            Bullet b = bp.getBullet();
            System.out.println(b);
        }
    }

}
