import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import static java.lang.Math.*;

/**
 * Created by usr on 1/19/2017.
 *
 */
public class field extends JPanel {

     field() {
        Timer t = new Timer(0, e -> repaint());
        t.start();
        addMouseListener(new MA());
        addKeyListener(new KA());
setFocusable(true);
    }

    ArrayList<particle> p = new ArrayList<>();
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLUE);
        for (particle c : p) c.display(g);

    }


    class vec {
        int  x = 0,  y = 0;
        int ox = 0, oy = 0;
        int mx = 0, my = 0;
        double thta = 0, mag = 0;

        vec(int xx, int yy) {
            ox = xx;
            oy = yy;
            cxy();
            ctm();
        }

        vec(int xx, int yy, double t, double m) {
            ox = xx;
            oy = yy;
            thta = t;
            mag = m;
            cxy();
        }

        void ctm() {
            thta = (atan(y / x) * PI )/180;
            mag = sqrt(pow(x - ox, 2) + pow(y - oy, 2));
        }

        void cxy() {
            x = ox + (int) (cos(toRadians(thta)) * mag);
            y = oy + (int) (sin(toRadians(thta)) * mag);
            cmp();
        }
        void cmp(){
            mx=(ox+x)/2;
            my=(oy+y)/2;
        }
        void display(Graphics g) {
            g.setColor(mag>0?Color.red:Color.BLUE);
            drawtri(g,this);
            g.drawLine(ox, oy, x, y);
        }
    }

    class particle {
        double charge;
        vec loc;
        ArrayList<vec> vecs;

        public particle(int x, int y, double c) {
            loc = new vec(x, y);
            charge = c;
            vecs = new ArrayList<>();
            for (int i = 0; i < 360; i++) {
                if (i % (360 / (abs(findMag() / 10) + 5)) == 0)
                    vecs.add(new vec(loc.x, loc.y, i, findMag()));
            }
        }

        void display(Graphics g) {
            for (vec c : vecs) c.display(g);
            g.fillOval(loc.x - 5, loc.y - 5, 10, 10);
        }

        int findMag() {
            return (int) (charge * 100);
        }
    }

    void drawtri(Graphics g,vec loc){
        int x1,y1,x2,y2,x3,y3,size=5;
        x1=(int)(loc.mx+cos(toRadians(loc.thta))*size);
        y1=(int)(loc.my+sin(toRadians(loc.thta))*size);

        x2=(int)(loc.mx+cos(toRadians(loc.thta+90))*size);
        y2=(int)(loc.my+sin(toRadians(loc.thta+90))*size);

        x3=(int)(loc.mx+cos(toRadians(loc.thta-90))*size);
        y3=(int)(loc.my+sin(toRadians(loc.thta-90))*size);
        g.fillPolygon(new int[]{x1,x2,x3},new int[]{y1,y2,y3},3);

    }

    public static void main(String... a) {
        JFrame frame = new JFrame();
        frame.setContentPane(new field());
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    class MA extends MouseAdapter {
        @Override
        public void mouseReleased(MouseEvent e) {
            super.mouseReleased(e);
            if(e.getButton()==MouseEvent.BUTTON1)
                p.add(new particle(e.getX(), e.getY(), -0.25));
            if(e.getButton()==MouseEvent.BUTTON3)
                p.add(new particle(e.getX(), e.getY(), 1));
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            super.mouseMoved(e);
        }
    }
int used=0;
    class KA extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {
            int i = e.getKeyCode();
            switch (i) {
                case KeyEvent.VK_SPACE:
                    p.clear();
                    break;
                case KeyEvent.VK_A:used++;
                    break;
                case KeyEvent.VK_Z:used--;
                    break;

            }
        }
    }
}

