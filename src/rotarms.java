

import Utils.Point;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL15;
import org.lwjgl.util.glu.Sphere;

import javax.swing.*;
import java.nio.FloatBuffer;
import java.util.LinkedList;

import static org.lwjgl.util.glu.GLU.*;
import static org.lwjgl.opengl.GL11.*;



public class rotarms {
    private int VBOVertexHandle;
    private int VBOColorHandle;

    long lastFrame;
    int fps;
    long lastFPS;

    Armsinput armsinput;

    public void start() {



        String x=("******************** controls *******************\n"+
                "-use WASD to move forward/back and right/left\n"+
                "-click window then use mouse to look around\n"+
                "-Q and E gives 5 times more or less movement speed\n" +
                "-scroll wheel to control speed\n" +
                "-use the form to edit the rotational-speed of the arms\n" +
                "-P pauses the arms");
        JOptionPane.showMessageDialog(null, x);
        armsinput=new Armsinput(rs);
        try {
            Display.setDisplayMode(new DisplayMode(800, 600));
            Display.create();
            Display.setResizable(true);
        } catch (LWJGLException e) {
            e.printStackTrace();
            System.exit(0);
        }
        Init();
        initGL();
        getDelta();
        lastFPS = getTime();

        Camera.create();
        Camera.setZ(10);
        Camera.setY(3);
        Camera.setX(2);
        Camera.setRotationX(30);

        while (!Display.isCloseRequested()) {
            int delta = getDelta();

            Camera.apply();
            Camera.acceptInput(delta);

            update(delta);
            // renderGL();

            Display.update();
            Display.sync(100); // cap fps to 60fps
            if (Display.wasResized()) {
                glViewport(0, 0, Display.getWidth(), Display.getHeight());
                initGL();
            }
        }

        Display.destroy();
        System.exit(0);
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

        gluPerspective(45f, (float) Display.getWidth()
                / (float) Display.getHeight(), 0.1f, 800f);
        glMatrixMode(GL_MODELVIEW);

        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);


        //----------- Variables & method calls added for Lighting Test -----------//
        initLightArrays();
        glShadeModel(GL_SMOOTH);
        glMaterial(GL_FRONT, GL_SPECULAR, matSpecular);				// sets specular material color
        glMaterialf(GL_FRONT, GL_SHININESS, 25f);					// sets shininess

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
    private void Init(){

        for (int i = 0; i <rs.length/3; i++) {
            System.out.println(rs[i]+":"+rs[i+1]+":"+rs[i+2]);
        }
    }

    float ticks = 0, xxx = 45, rotation = 0;
    boolean running=true;
    LinkedList<Point> points=new LinkedList<>();
    float[] rs=new float[]{rand(),rand(),rand(),rand(),rand(),rand(),rand(),rand(),rand()};
    private void update(int delta) {
        if(running)
            rotation += 1.5;


        initGL();
        updateFPS();
        getInput();
        glClear(GL_COLOR_BUFFER_BIT
                | GL_DEPTH_BUFFER_BIT);

        drawLine(new Point(0, 1000, 0, 0, 0, 1), new Point(0, -1000, 0, 0, 0, 1));
        drawLine(new Point(0, 0, 1000, 0, 1, 0), new Point(0, 0, - 1000, 0, 1, 0));
        drawLine(new Point(1000, 0, 0, 1, 0, 0), new Point(-1000, 0, 0, 1, 0, 0));

        if(armsinput.beRandom){
            armsinput.data=new double[]{rand(),rand(),rand(),rand(),rand(),rand(),rand(),rand(),rand()};
            armsinput.isApplied=true;
            armsinput.beRandom=false;
            armsinput.updateSpinners();
        }
        if(armsinput.isApplied) {
            for (int i = 0; i < rs.length; i++) {
                rs[i] = (float) armsinput.data[i];
            }
            points.clear();
            armsinput.isApplied=false;
        }

        Point a=makeArm(new Point(0,0,0,0,1,0),2, new float[]{rotation*rs[0],rotation*rs[1],rotation*rs[2]},new float[]{1,0,1});
        Point b=makeArm(a,2,                      new float[]{rotation*rs[3],rotation*rs[4],rotation*rs[5]},new float[]{0,1,1});
        Point p=makeArm(b,2,                      new float[]{rotation*rs[6],rotation*rs[7],rotation*rs[8]},new float[]{1,1,0});
        if (!points.stream().anyMatch(point -> point.compare(p))&&armsinput.a3a)
            points.add(p);
        if (!points.stream().anyMatch(point -> point.compare(a))&&armsinput.a1a)
            points.add(a);
        if (!points.stream().anyMatch(point -> point.compare(b))&&armsinput.a2a)
            points.add(b);
        for (Point pp :points) {
            drawSphere(pp);
        }


        ticks += 1;
    }
    private float rand(){
        return ((int) (Math.random()*10)-5)*((Math.random()*100)<20?0:1);
    }
    private void drawSphere(Point p){
        glPushMatrix();
        glTranslatef(p.x, p.y, p.z);
        glColor3d(p.r,p.g,p.b);
        Sphere s = new Sphere();
        s.draw(0.04f, 10, 10);
        glPopMatrix();
    }



    private Point makeArm(Point or,float length,float[] th,float[] color){
        float x=or.x,y=or.y,z=or.z;
        int c=0;
        for (Float f :th) {
            if(f!=0)
                c++;
        }
        if(c==0)c=1;
        length/=c;
        if(th[0]!=0){
            y+=cos(th[0])*length;
            z+=sin(th[0])*length;
        }
        if(th[1]!=0){
            x+=cos(th[1])*length;
            z+=sin(th[1])*length;
        }
        if(th[2]!=0){
            x+=cos(th[2])*length;
            y+=sin(th[2])*length;
        }
        Point end=new Point(x,y,z,color[0],color[1],color[2]);
        drawLine(or,end);
        return end;
    }
    private void drawLine(Point point, Point point2) {
        glBegin(GL_LINE_STRIP);

        glColor3f(point.r, point.g, point.b);
        glVertex3f(point.x, point.y, point.z);
        glColor3f(point2.r, point2.g, point2.b);
        glVertex3f(point2.x, point2.y, point2.z);
        glEnd();
    }

    float dw = 0.001f;

    private void getInput() {
        if (Keyboard.isKeyDown(Keyboard.KEY_UP)) xxx += 0.1;
        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) xxx -= 0.1;
        if (Keyboard.isKeyDown(Keyboard.KEY_P))running=!running;
        dw += (Mouse.getDWheel() / 1000000f);
        dw = (dw < 0) ? 0 : dw;
        Camera.setSpeed(dw);
    }


    private void renderGL() {
        glPushMatrix();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBOVertexHandle);
        glVertexPointer(3, GL_FLOAT, 0, 0L);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBOColorHandle);
        glColorPointer(3, GL_FLOAT, 0, 0L);
        glDrawArrays(GL_QUADS, 0, 24);
        glPopMatrix();
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
            Display.setTitle("FPS: " + fps+":"+dw);
            fps = 0;
            lastFPS += 1000;
        }
        fps++;
    }

    float sin(float a) {
        return (float)Math.sin(Math.toRadians(a));
    }

    float cos(float a) {
        return (float)Math.cos(Math.toRadians(a));
    }


    //----------- Variables added for Lighting Test -----------//
    private FloatBuffer matSpecular;
    private FloatBuffer lightPosition;
    private FloatBuffer whiteLight;
    private FloatBuffer lModelAmbient;
    //----------- END: Variables added for Lighting Test -----------//

    //------- Added for Lighting Test----------//
    private void initLightArrays() {
        matSpecular = BufferUtils.createFloatBuffer(4);
        matSpecular.put(1.0f).put(1.0f).put(1.0f).put(1.0f).flip();

        lightPosition = BufferUtils.createFloatBuffer(4);
        lightPosition.put(1.0f).put(1.0f).put(1.0f).put(0.0f).flip();

        whiteLight = BufferUtils.createFloatBuffer(4);
        whiteLight.put(0.5f).put(0.5f).put(0.5f).put(0.5f).flip();

        lModelAmbient = BufferUtils.createFloatBuffer(4);
        lModelAmbient.put(0.5f).put(0.5f).put(0.5f).put(1.0f).flip();
    }
}
/*

-5.0:-4.0:0.0
-4.0:0.0:1.0
0.0:1.0:-5.0
 */