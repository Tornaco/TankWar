import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

/**
 * Created @2017/3/12 12:05
 */
public class Tank {
    public static final int XSPEED = 5;
    public static final int YSPEED = 5;
    public static final int WIDTH = 30;
    public static final int HEIGHT = 30;

    int x, y;
    private boolean live = true;

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    Random r = new Random();

    private boolean bL = false, bR = false, bU = false, bD = false;

    enum Direction {L, LU, U, RU, R, RD, D, LD, STOP};

    private Direction dir = Direction.STOP;
    private Direction ptdir = Direction.D;
    private boolean good = true;

    TankClient tc;


    public Tank(int x, int y, boolean good) {
        this.x = x;
        this.y = y;
        this.good = good;
    }

    public Tank(int x, int y, boolean good, Direction dir, TankClient tc) {
        this(x, y, good);
        this.dir = dir;
        this.tc = tc;
    }

    public void draw(Graphics g) {
        if(!live) {
           if(!good) {
               tc.tanks.remove(this);
           }
           else return;
        }
        Color c = g.getColor();
        if(good) g.setColor(Color.red);
        else g.setColor(Color.BLUE);
        g.fillOval(x, y, WIDTH, HEIGHT);
        g.setColor(c);
        move();
        switch (ptdir) {
            case L:
                g.drawLine(x + Tank.WIDTH / 2, y + Tank.HEIGHT / 2, x, y + Tank.HEIGHT / 2);
                break;
            case LU:
                g.drawLine(x + Tank.WIDTH / 2, y + Tank.HEIGHT / 2, x, y);
                break;
            case U:
                g.drawLine(x + Tank.WIDTH / 2, y + Tank.HEIGHT / 2, x + Tank.WIDTH / 2, y);
                break;
            case RU:
                g.drawLine(x + Tank.WIDTH / 2, y + Tank.HEIGHT / 2, x + Tank.WIDTH, y);
                break;
            case R:
                g.drawLine(x + Tank.WIDTH / 2, y + Tank.HEIGHT / 2, x + Tank.WIDTH, y + Tank.HEIGHT /2 );
                break;
            case RD:
                g.drawLine(x + Tank.WIDTH / 2, y + Tank.HEIGHT / 2, x + Tank.WIDTH, y + Tank.HEIGHT );
                break;
            case D:
                g.drawLine(x + Tank.WIDTH / 2, y + Tank.HEIGHT / 2, x + Tank.WIDTH / 2, y + Tank.HEIGHT );
                break;
            case LD:
                g.drawLine(x + Tank.WIDTH / 2, y + Tank.HEIGHT / 2, x, y + Tank.HEIGHT );
                break;
        }
    }

    void move() {
        switch (dir) {
            case L:
                x -= XSPEED;
                break;
            case LU:
                x -= XSPEED;
                y -= YSPEED;
                break;
            case U:
                y -= YSPEED;
                break;
            case RU:
                x += XSPEED;
                y -= YSPEED;
                break;
            case R:
                x += XSPEED;
                break;
            case RD:
                x += XSPEED;
                y += YSPEED;
                break;
            case D:
                y += YSPEED;
                break;
            case LD:
                x -= XSPEED;
                y += YSPEED;
                break;
            case STOP:
                break;

        }

        if(this.dir != Direction.STOP) {
            this.ptdir = this.dir;
        }

        if(x <0) x=0;
        if(y <30) y = 30;
        if(x + Tank.WIDTH > TankClient.WINDOW_WIDTH) x = TankClient.WINDOW_WIDTH - Tank.WIDTH;
        if(y + Tank.HEIGHT> TankClient.WINDOW_HEIGHT) y = TankClient.WINDOW_HEIGHT - Tank.HEIGHT;


        if(!good) {
            Direction[] dirs = Direction.values();
            int rn = r.nextInt(dirs.length);
            dir = dirs[rn];
        }
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {

            case KeyEvent.VK_LEFT:
                bL = true;
                break;
            case KeyEvent.VK_RIGHT:
                bR = true;
                break;
            case KeyEvent.VK_UP:
                bU = true;
                break;
            case KeyEvent.VK_DOWN:
                bD = true;
                break;
        }
        locateDirection();
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_CONTROL:
                fire();
                break;
            case KeyEvent.VK_LEFT:
                bL = false;
                break;
            case KeyEvent.VK_RIGHT:
                bR = false;
                break;
            case KeyEvent.VK_UP:
                bU = false;
                break;
            case KeyEvent.VK_DOWN:
                bD = false;
                break;
        }
        locateDirection();
    }

    public void locateDirection() {
        if (bL && !bR && !bU && !bD) dir = Direction.L;
        else if (bL && !bR && bU && !bD) dir = Direction.LU;
        else if (!bL && bR && !bU && !bD) dir = Direction.R;
        else if (!bL && bR && bU && !bD) dir = Direction.RU;
        else if (!bL && !bR && bU && !bD) dir = Direction.U;
        else if (bL && !bR && !bU && bD) dir = Direction.LD;
        else if (!bL && bR && !bU && bD) dir = Direction.RD;
        else if (!bL && !bR && !bU && bD) dir = Direction.D;
        else if (!bL && !bR && !bU && !bD) dir = Direction.STOP;
    }

    public Missile fire() {
        int x = this.x + (Tank.WIDTH - Missile.WIDTH) / 2;
        int y = this.y + (Tank.WIDTH - Missile.HEIGHT) / 2;
        Missile m = new Missile(x, y, ptdir, tc);
        tc.missiles.add(m);
        return m;
    }

    public Rectangle getRectangle() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }
}
