

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
import org.lwjgl.util.glu.Sphere;


import java.nio.FloatBuffer;

import java.util.LinkedList;

import static org.lwjgl.opengl.GL11.*;

public class main {

    long lastFrame;
    int fps;
    long lastFPS;
    Cube c;Cube cc;
    public void start() {
        try {
            Display.setDisplayMode(new DisplayMode(800, 600));
            Display.create();
            Display.setResizable(true);
        } catch (LWJGLException e) {
            e.printStackTrace();
            System.exit(0);
        }

        c=new Cube(2,3,0,1,1,1);
        cc=new Cube(0,0,0,1,1,1);
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


    private void initLight(){
        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);

        FloatBuffer matSpecular;
        FloatBuffer lightPosition;
        FloatBuffer whiteLight;
        FloatBuffer lModelAmbient;
        matSpecular = BufferUtils.createFloatBuffer(4);
        matSpecular.put(1.0f).put(1.0f).put(1.0f).put(1.0f).flip();

        lightPosition = BufferUtils.createFloatBuffer(4);
        lightPosition.put(1.0f).put(5.0f).put(1.0f).put(0.0f).flip();

        whiteLight = BufferUtils.createFloatBuffer(4);
        whiteLight.put(1.0f).put(1.0f).put(1.0f).put(1.0f).flip();

        lModelAmbient = BufferUtils.createFloatBuffer(4);
        lModelAmbient.put(0.5f).put(0.5f).put(0.5f).put(1.0f).flip();
        //----------- Variables & method calls added for Lighting Test -----------//
        glShadeModel(GL_SMOOTH);
        glMaterial(GL_FRONT, GL_SPECULAR, matSpecular);				// sets specular material color
        glMaterialf(GL_FRONT, GL_SHININESS, 50.0f);					// sets shininess

        glLight(GL_LIGHT0, GL_POSITION, lightPosition);				// sets light position
        glLight(GL_LIGHT0, GL_SPECULAR, whiteLight);				// sets specular light to white
        glLight(GL_LIGHT0, GL_DIFFUSE, whiteLight);					// sets diffuse light to white
        glLightModel(GL_LIGHT_MODEL_AMBIENT, lModelAmbient);		// global ambient light

        glEnable(GL_LIGHTING);										// enables lighting
        glEnable(GL_LIGHT0);										// enables light0

        glEnable(GL_COLOR_MATERIAL);								// enables opengl to use glColor3f to define material color
        glColorMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE);			// tell opengl glColor3f effects the ambient and diffuse properties of material
        //----------- END: Variables & method calls added for Lighting Test -----------//
    }

    private void initGL() {
        glEnable(GL_TEXTURE_2D);

        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClearDepth(1.0);
        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LEQUAL);

        glEnableClientState(GL_NORMAL_ARRAY);
        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_COLOR_ARRAY);

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();

        GLU.gluPerspective(45f, (float) Display.getWidth()
                / (float) Display.getHeight(), 0.1f, 800f);
        //glTranslatef(0f, 0f, -50f);
        glMatrixMode(GL_MODELVIEW);

initLight();

    }


    double ticks =0;

    private void update(int delta) {



        initGL();
        updateFPS();
        getInput();

        glClear(GL_COLOR_BUFFER_BIT
                | GL_DEPTH_BUFFER_BIT);

        cc.setOrigin(new Point(c.getOrigin().x+(float)(cos(ticks)*5),
                c.getOrigin().y+(float)(sin(ticks)*5)
                ,0));

//        if(false)
        {
            c.CreateVBO();
            cc.CreateVBO();
            cc.rotate(1, 1, 1, 1);
        }

        drawLine(new Point(0, 50, 0, 0, 0, 1), new Point(0, -50, 0, 0, 0, 1));
        drawLine(new Point(0, 0, 50, 0, 1, 0), new Point(0, 0, 0 - 50, 0, 1, 0));
        drawLine(new Point(50, 0, 0, 1, 0, 0), new Point(-50, 0, 0, 1, 0, 0));

        //rott2(rott1(new Point(0,0,0)));

        ticks +=1;
    }
    private void drawLine(Point point, Point point2) {
        glBegin(GL_LINE_STRIP);
        pointset(point);
        pointset(point2);
        glEnd();
    }
    LinkedList<Point> l=rott1(new Point(0,0,0));
    private LinkedList<Point> rott1(Point p) {
        LinkedList<Point> points = new LinkedList<>();
        int s = 360, ss = 100;
        for (int i = 0; i < s*10; i++) {
//            if (i % (360 / s) == 0)
            {
                float x =p.x+(float) (cos(dd*cos((i/10))*cos(i)) * ss);
                float y =p.y+(float) (sin(dd*sin((i/10))*sin(i)) * ss);
                float z =p.z+(float) (sin(i)* ss);
                points.push(new Point(x, y, z, i%2, i%3, i%4));
            }
        }
        return points;
    }
    private void rott2(LinkedList<Point> points){
        LinkedList<Point> norm=calculateNormals();
//        glBegin(GL_POLYGON);
//        for (float i = 0; i < points.size(); i+=1) {
//            Point n=norm.get((int)i);
//            glNormal3f(n.x,n.y,n.z);
//            pointset(points.get((int) i));
//        }
//        glEnd();
        glBegin(GL_LINE_LOOP);
        for (float i = 0; i < points.size(); i+=1) {
            pointset(points.get((int) i));
        }
        glEnd();
    }
    public LinkedList<Point> calculateNormals() {
        LinkedList<Point> listNormals = new LinkedList<>();

        for (int i = 0; i < l.size(); i += 3) {
            Point p1 = l.get(i);
            Point p2 = l.get(i + 1);
            Point p3 = l.get(i + 2);

            //Get the UV data
            Point u = new Point(p2.x - p1.x, p2.y - p1.y, p2.z - p1.z);
            Point v = new Point(p3.x - p1.x, p3.y - p1.y, p3.z - p1.z);

            //Calculate normals
            float nx = (u.y * v.z) - (u.z * v.y);
            float ny = (u.z * v.x) - (u.x * v.z);
            float nz = (u.x * v.y) - (u.y * v.x);

            //Normalise them
            float nf = (float) Math.sqrt((nx * nx) + (ny * ny) + (nz * nz)); //Normalisation factor
            nx = nx / nf;
            ny = ny / nf;
            nz = nz / nf;

            listNormals.add(new Point(nx, ny, nz));
            listNormals.add(new Point(nx, ny, nz));
            listNormals.add(new Point(nx, ny, nz));
        }
        return listNormals;
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
