import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Created @2017/3/12 10:36
 */
public class TankClient extends Frame {
    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 600;
    public static final int WINDOW_X = 400;
    public static final int WINDOW_Y = 300;

    Tank myTank = new Tank(50, 50,true, Tank.Direction.STOP,  this);
    List<Missile> missiles = new ArrayList<Missile>();
    List<Explode> explodes = new ArrayList<Explode>();
    List<Tank> tanks = new ArrayList<Tank>();

    Image offScreenImage = null;

    @Override
    public void paint(Graphics g) {
        g.drawString("Missiles count:" + missiles.size(), 10, 50);
        g.drawString("Explodes count:" + explodes.size(), 200, 50);
        g.drawString("Tanks count:" + tanks.size(), 400, 50);
        myTank.draw(g);
        for (int i = 0; i < missiles.size(); i++) {
            Missile m = missiles.get(i);
            //if(!m.getLive()) missiles.remove(m);
            //else m.draw(g);
            m.draw(g);
            m.hitTanks(tanks);
        }
        for(int i = 0; i < explodes.size(); i ++) {
            Explode e = explodes.get(i);
            e.draw(g);
        }

        for(int i = 0; i < tanks.size(); i ++) {
            Tank t = tanks.get(i);
            t.draw(g);
        }
    }

    @Override
    public void update(Graphics g) {
        if (offScreenImage == null) {
            offScreenImage = createImage(WINDOW_WIDTH, WINDOW_HEIGHT);
        }
        Graphics graphics = offScreenImage.getGraphics();
        Color c = graphics.getColor();
        graphics.setColor(Color.green);
        graphics.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        graphics.setColor(c);
        paint(graphics);
        g.drawImage(offScreenImage, 0, 0, null);
    }


    public void launchFrame() {
        for(int i = 0; i < 10; i ++) {
            tanks.add(new Tank(50 + 40 * (i + 1), 50, false,  Tank.Direction.D, this));
        }
        this.setLocation(WINDOW_X, WINDOW_Y);
        this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                myTank.keyPressed(e);
            }


            @Override
            public void keyReleased(KeyEvent e) {
                myTank.keyReleased(e);
            }
        });
        setBackground(Color.green);
        setVisible(true);
        setResizable(false);
        new Thread(new PaintThread()).start();
    }

    public static void main(String[] args) {
        TankClient tc = new TankClient();
        tc.launchFrame();
    }

    private class PaintThread implements Runnable {

        public void run() {
            while (true) {
                repaint();
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
