

import Utils.*;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector3f;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static org.lwjgl.opengl.GL20.*;


import javax.swing.*;
import java.nio.FloatBuffer;
import java.util.LinkedList;

import static org.lwjgl.opengl.GL11.*;

class main {

    private long lastFrame;
    private int fps;
    private long lastFPS;

    private LinkedList<Cube> walls;
    private Point cen;
    private Polygon gui;
    private Toaster toaster;
    private int[][] colmap;

    void start() {

        JProgressBar p=new JProgressBar(0,100);
        p.setValue(0);
        p.setStringPainted(true);
        JPanel panel=new JPanel();
        panel.add(p);
        JFrame frame=new JFrame("loading");
        frame.setContentPane(panel);
        frame.setSize(30,50+50);
        frame.setVisible(true);
        frame.setLocation(300,300);

        try {
            Display.setDisplayMode(new DisplayMode(800, 600));
            Display.create();
            Display.setResizable(true);
        } catch (LWJGLException e) {
            e.printStackTrace();
            System.exit(0);
        }

        p.setValue(10);
        Material.init(new String[]{
                "skypz","images\\sky_pos_z.png",
                "skynz","images\\sky_neg_z.png",
                "skypx","images\\sky_pos_x.png",
                "skynx","images\\sky_neg_x.png",
                "brick","images\\brick.jpg",
                "tile","images\\marble_tile.jpg",
                "gui","images\\GUI.png",
        });
        toaster=new Toaster(Material.addMat("toaster","images\\toaster.png",32,32,8,4,2),new Point(2,0,7));
        walls=new LinkedList<>();
        String[] map={//        111111111122
                //    0123456789012345678901
                /*0*/"######################" ,
                /*1*/"##-------------------#" ,
                /*2*/"##--###########-####-#" ,
                /*3*/"#---#----#-------#---#" ,
                /*4*/"#--------#-------#---#" ,
                /*5*/"##-####-##########-###" ,
                /*6*/"#---------#----------#" ,
                /*7*/"#--------------------#" ,
                /*8*/"#---------#----------#" ,
                /*9*/"######################"};
        cen=new Point(map.length/2,0,map[0].length()/2);
        for(int r=0;r<map.length;r++)
            for(int c=0;c<map[0].length();c++){
                if(map[r].charAt(c)=='#')
                    walls.add(new Cube(c,.5f,r,.5f,.5f,.5f,Material.get("brick")));
                walls.add(new Cube(c,0,r,.5f,.5f,.125f,Material.get("tile")));
            }
        p.setValue(50);
        colmap=makecolmap(map);

        gui=new Polygon(new Point[]{
                new Point(0,1,0),
                new Point(0,0,0),
                new Point(1,0,0),
                new Point(1,1,0),
        },new Point(-.5f,-.5f,-1));
        gui.HasTex(new Point[]{
                new Point(0,0,0),
                new Point(0,1,0),
                new Point(1,1,0),
                new Point(1,0,0),

        },Material.get("gui"));

        p.setValue(90);

        initGL();
        getDelta();
        lastFPS = getTime();

        Camera.create();
        Camera.setPos(new Vector3f(3,.5f,3));
        toaster.setPath(Path.findPath(toaster.getOr(),new Point(Camera.getPos()),colmap));
        Camera.apply();

        p.setValue(100);
        frame.dispose();
        while (!Display.isCloseRequested()) {
            int delta = getDelta();

            Camera.acceptInput(delta);
            Camera.apply();

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

        glBlendFunc (GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();

        GLU.gluPerspective(45f, (float) Display.getWidth()
                / (float) Display.getHeight(), 0.1f, 800f);
//        glTranslatef(0f, 0f, -50f);
        glMatrixMode(GL_MODELVIEW);
        if(tl)
            initLight();

    }
    private int ticks =0;
    private boolean tl=false;
    private boolean pf=false;
    private Point player;
    private Point oplayer=new Point(0,0,0);
    private void update(int delta) {
        initGL();
        updateFPS();
        getInput();
        player=new Point(Camera.getPos()).round();
        if(!oplayer.equal(player)&&pf) {
            System.out.println(player+","+oplayer);
            oplayer.setXYZ(player.x,player.y,player.z);
            toaster.setPath(Path.findPath(toaster.getOr(), player, colmap));
        }
        glClear(GL_COLOR_BUFFER_BIT
                | GL_DEPTH_BUFFER_BIT);
        drawskybox();
        for (Cube c:walls)
            c.draw();

        toaster.draw(Camera.getRotationX(),Camera.getRotationY(),Camera.getRotationZ(),ticks);

        glPushMatrix();
        glDisable(GL_DEPTH_TEST);
        glTranslatef(Camera.getX(),Camera.getY(),Camera.getZ());
        glRotatef(-Camera.getRotationX(),1,0,0);
        glRotatef(-Camera.getRotationY(),0,1,0);
        glRotatef(-Camera.getRotationZ(),0,0,1);
        if(Camera.ismoving)
            gui.setOrigin(gui.getOrigin().sum(new Point(0,cos(Camera.dm*10)/(float) Math.pow(2,9),0)));
        gui.draw();
        glEnable(GL_DEPTH_TEST);
        glPopMatrix();

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
    private void drawLine(Point point, Point point2,float r,float g,float b) {
        glBegin(GL_LINE_STRIP);
        glColor3f(r,g,b);
        glVertex3f(point.x, point.y,point.z);
        glColor3f(r,g,b);
        glVertex3f(point2.x, point2.y,point2.z);
        glEnd();
    }
    private void drawskybox(){
        int size=100;
        glPushMatrix();
        glTranslatef(cen.x,-size/2,cen.z-size/2);
        Polygon.draw(new Point[]{
                new Point(size/2,size,0),
                new Point(size/2,0,0),
                new Point(-size/2,0,0),
                new Point(-size/2,size,0),
        },new Point[]{
                new Point(0,0,0),
                new Point(0,1,0),
                new Point(1,1,0),
                new Point(1,0,0),

        }, Material.get("skynz"));
        Polygon.draw(new Point[]{
                new Point(-size/2,size,size),
                new Point(-size/2,0,size),
                new Point(size/2,0,size),
                new Point(size/2,size,size),
        },new Point[]{
                new Point(0,0,0),
                new Point(0,1,0),
                new Point(1,1,0),
                new Point(1,0,0),

        },Material.get("skypz"));
        Polygon.draw(new Point[]{
                new Point(size/2,size,size),
                new Point(size/2,0,size),
                new Point(size/2,0,0),
                new Point(size/2,size,0),
        },new Point[]{
                new Point(0,0,0),
                new Point(0,1,0),
                new Point(1,1,0),
                new Point(1,0,0),

        },Material.get("skypx"));
        Polygon.draw(new Point[]{
                new Point(-size/2,size,0),
                new Point(-size/2,0,0),
                new Point(-size/2,0,size),
                new Point(-size/2,size,size),
        },new Point[]{
                new Point(0,0,0),
                new Point(0,1,0),
                new Point(1,1,0),
                new Point(1,0,0),

        },Material.get("skynx"));
        glPopMatrix();

    }
    private void pointset(Point point){
        glColor3f(point.r,point.g,point.b);
        glVertex3f(point.x, point.y,point.z);
    }

    float dw=0.01f,dd=2,wd=0;
    private void getInput(){
        if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
            Display.destroy();
            System.exit(0);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_F)) {
            pf = !pf;
            System.out.println(pf);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)){
            dd+=0.01;}
        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)){ dd-=0.01;}
        dw+=(Mouse.getDWheel()/100000f);
        dw=(dw<0)?0:dw;
        Camera.setSpeed(dw);
    }
    int getDelta() {
        long time = getTime();
        int delta = (int) (time - lastFrame);
        lastFrame = time;

        return delta;
    }
    long getTime() {
        return (Sys.getTime() * 1000) / Sys.getTimerResolution();
    }
    void updateFPS() {
        if (getTime() - lastFPS > 1000) {
            Display.setTitle("FPS: " + fps);
            fps = 0;
            lastFPS += 1000;
        }
        fps++;
    }

    float sin(float a){return(float) Math.sin(Math.toRadians(a));}
    float cos(float a){return(float) Math.cos(Math.toRadians(a));}
    private int[][] makecolmap(String[] m){
        int[][] map=new int[m[0].length()][m.length];
        for(int r=0;r<m.length;r++)
            for(int c=0;c<m[0].length();c++) {
                if (m[r].charAt(c) == '#')
                    map[c][r]=0xffff;
                else
                    map[c][r]=1;
            }
        return map;
    }



}
