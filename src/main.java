

import Shapes.Cube;
import Shapes.Point;
import Shapes.Polygon;
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

import static org.lwjgl.opengl.GL11.*;

public class main {

    long lastFrame;
    int fps;
    long lastFPS;
    Cube c;Cube cc;
    Polygon p;
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
//        c.isWrieframe=true;
//        cc.isWrieframe=true;

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
        lightPosition.put(0f).put(5f).put(0f).put(0.0f).flip();

        whiteLight = BufferUtils.createFloatBuffer(4);
        whiteLight.put(1.0f).put(1.0f).put(1.0f).put(1.0f).flip();

        lModelAmbient = BufferUtils.createFloatBuffer(4);
        lModelAmbient.put(0.5f).put(0.5f).put(0.5f).put(1.0f).flip();
        //----------- Variables & method calls added for Lighting Test -----------//
        glShadeModel(GL_SMOOTH);
        glMaterial(GL_FRONT, GL_SPECULAR, matSpecular);				// sets specular material color
        glMaterialf(GL_FRONT, GL_SHININESS, 50.0f);					// sets shininess

        glLight(GL_LIGHT1, GL_POSITION, lightPosition);				// sets light position
        glLight(GL_LIGHT1, GL_SPECULAR, whiteLight);				// sets specular light to white
        glLight(GL_LIGHT1, GL_DIFFUSE, whiteLight);					// sets diffuse light to white
        glLightModel(GL_LIGHT_MODEL_AMBIENT, lModelAmbient);		// global ambient light

        glEnable(GL_LIGHTING);										// enables lighting
        glEnable(GL_LIGHT1);										// enables light0

        glEnable(GL_COLOR_MATERIAL);								// enables opengl to use glColor3f to define material color
        glColorMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE);			// tell opengl glColor3f effects the ambient and diffuse properties of material
        //----------- END: Variables & method calls added for Lighting Test -----------//
    }

    private void initGL() {

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
        if(tl)
            initLight();

    }


    double ticks =0;
    boolean tl=false;

    private void update(int delta) {
        initGL();
        updateFPS();
        getInput();

        glClear(GL_COLOR_BUFFER_BIT
                | GL_DEPTH_BUFFER_BIT);

        cc.setOrigin(new Point(c.getOrigin().x+(float)(cos(ticks)*5),
                c.getOrigin().y+(float)(sin(ticks)*5)
                ,0));

        c.draw();
        cc.draw();
        cc.rotate(1, 1, 1, 1);

        drawLine(new Point(0, 50, 0, 0, 0, 1), new Point(0, -50, 0, 0, 0, 1));
        drawLine(new Point(0, 0, 50, 0, 1, 0), new Point(0, 0, 0 - 50, 0, 1, 0));
        drawLine(new Point(50, 0, 0, 1, 0, 0), new Point(-50, 0, 0, 1, 0, 0));

        ticks +=1;
    }
    private void drawLine(Point point, Point point2) {
        glBegin(GL_LINE_STRIP);
        pointset(point);
        pointset(point2);
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
