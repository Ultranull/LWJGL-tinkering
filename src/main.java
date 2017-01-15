

import Shapes.Cube;
import Shapes.Point;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.glu.GLU;


import java.nio.FloatBuffer;

import java.util.LinkedList;

import static org.lwjgl.opengl.GL11.*;

public class main {

    long lastFrame;
    int fps;
    long lastFPS;

    public void start() {
        try {
            Display.setDisplayMode(new DisplayMode(800, 600));
            Display.create();
            Display.setResizable(true);
        } catch (LWJGLException e) {
            e.printStackTrace();
            System.exit(0);
        }

        initGL();
        getDelta();
        lastFPS = getTime();

        Camera.create();
        while (!Display.isCloseRequested()) {
            int delta = getDelta();

            Camera.apply();
            Camera.acceptInput(delta);

            update(delta);

            Display.update();
            Display.sync(60); // cap fps to 60fps
            if (Display.wasResized()) {
                glViewport(0, 0, Display.getWidth(), Display.getHeight());
                initGL();
            }
        }

        Display.destroy();
    }




    private void initGL() {
        glEnable(GL_TEXTURE_2D);
        glShadeModel(GL_SMOOTH);
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClearDepth(1.0);
        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LEQUAL);
        glEnableClientState(GL_VERTEX_ARRAY);

        glEnableClientState(GL_COLOR_ARRAY);

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();

        GLU.gluPerspective(45f, (float) Display.getWidth()
                / (float) Display.getHeight(), 0.1f, 800f);
        //glTranslatef(0f, 0f, -50f);
        glMatrixMode(GL_MODELVIEW);

        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);

    }


    double ticks =0;
    Cube c=new Cube(2,3,0);
    Cube cc=new Cube(0,0,0);
    private void update(int delta) {



        initGL();
        updateFPS();
        getInput();

        glClear(GL_COLOR_BUFFER_BIT
                | GL_DEPTH_BUFFER_BIT);

        cc.setOrigin(new Point(c.getOrigin().x+(float)(cos(ticks)*5),
                c.getOrigin().y+(float)(sin(ticks)*5)
                ,c.getOrigin().z-(float)(sin(ticks)*5)));



        c.CreateVBO();
        cc.CreateVBO();
        cc.rotate(1,1,1,1);

        drawLine(new Point(0,50,0,0,0,1),new Point(0,-50,0,0,0,1));
        drawLine(new Point(0,0,50,0,1,0),new Point(0,0,0-50,0,1,0));
        drawLine(new Point(50,0,0,1,0,0),new Point(-50,0,0,1,0,0));

        rott2(l);

        ticks +=1;
    }
    private void drawLine(Point point, Point point2) {
        glBegin(GL_LINE_STRIP);
        pointset(point);
        pointset(point2);
        glEnd();
    }
LinkedList l=rott1(new Point(0,0,0));
    private LinkedList<Point> rott1(Point p) {
        LinkedList<Point> points = new LinkedList<>();
        int s = 360, ss = 30;
        for (int i = 0; i < s*10; i++) {
//            if (i % (360 / s) == 0)
            {
                float x = p.x + (float) (cos(i*1.125) * ss);
                float y = p.y + (float) (sin(i) * ss);
                float z = p.z + (float) (sin(i*1.125) * ss);
                points.push(new Point(x, y, z, i%2, i%3, i%4));
            }
        }
        return points;
    }
    private void rott2(LinkedList<Point> points){
        glBegin(GL_POLYGON);
        for (float i = 0; i < points.size(); i+=Math.abs(dd)+1) {
            pointset(points.get((int) i));
        }
        glEnd();
        glBegin(GL_LINE_LOOP);
        for (float i = 0; i < points.size(); i+=1) {
            pointset(points.get((int) i));
        }
        glEnd();
    }


    private void pointset(Point point){
        glColor3f(point.r,point.g,point.b);
        glVertex3f(point.x, point.y,point.z);
    }

    float dw=0.01f,dd=1;
    private void getInput(){

        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)){ c.translate(-1,0,0);dd+=0.5;}
        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)){ c.translate(1,0,0);dd-=0.5;}
        if (Keyboard.isKeyDown(Keyboard.KEY_UP)) c.rotate(1,0,1,1);
        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) c.rotate(-1,0,1,1);
        dw+=(Mouse.getDWheel()/10000f);
        dw=(dw<0)?0:dw;
        Camera.setSpeed(dw);
    }
    public int getDelta() {
        long time = getTime();
        int delta = (int) (time - lastFrame);
        lastFrame = time;

        return delta;
    }
    public long getTime() {
        return (Sys.getTime() * 1000) / Sys.getTimerResolution();
    }
    public void updateFPS() {
        if (getTime() - lastFPS > 1000) {
            Display.setTitle("FPS: " + fps);
            fps = 0;
            lastFPS += 1000;
        }
        fps++;
    }

    double sin(double a){return Math.sin(Math.toRadians(a));}
    double cos(double a){return Math.cos(Math.toRadians(a));}





}
