package Shapes;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL12;

import javax.imageio.ImageIO;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL11.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.Arrays;

/**
 * Created by usr on 11/17/2016.*/
/*
 *     E________F
 *    /|       /|
 *   / |      / |
 *  /  |     /  |
 * A---+----B   |
 *|   G____|___H
 *|  /     |  /
 *| /      | /
 *C/_______D/
 *
 */


public class Cube {


    private Point origin;
    private Polygon[] faces;

    private int VBOColorHandle;
    private int VBOVertexHandle ;
    private int VBONormalHandle ;
    private int VBOTextureHandle ;
    private int VBOTexID ;

    private float l,w,h;

    private float rx=0;
    private float ry=0;
    private float rz=0;

    public boolean isWrieframe=false;

    public Cube(float x,float y,float z){
        origin=new Point(x,y,z);
        l=1;w=1;h=1;
        vertInit();

        VBOTexID=loadTexture(loadImage("images\\brick.jpg"));
    }
    public Cube(float x,float y,float z,float L,float W,float H){
        origin=new Point(x,y,z);
        l=L;w=W;h=H;
        vertInit();

        VBOTexID=loadTexture(loadImage("images\\brick.jpg"));
    }
    /*
 *     E________F
 *    /|       /|
 *   / |      / |
 *  /  |     /  |
 * A---+----B   |
 *|   G____|___H
 *|  /     |  /
 *| /      | /
 *C/_______D/
 *
 */
    private void vertInit(){
        Point A=new Point(origin.x-w,origin.y+h,origin.z+l,1,1,1);
        Point B=new Point(origin.x+w,origin.y+h,origin.z+l,1,1,1);
        Point C=new Point(origin.x-w,origin.y-h,origin.z+l,1,1,1);
        Point D=new Point(origin.x+w,origin.y-h,origin.z+l,1,1,1);
        Point E=new Point(origin.x-w,origin.y+h,origin.z-l,1,1,1);
        Point F=new Point(origin.x+w,origin.y+h,origin.z-l,1,1,1);
        Point G=new Point(origin.x-w,origin.y-h,origin.z-l,1,1,1);
        Point H=new Point(origin.x+w,origin.y-h,origin.z-l,1,1,1);
        faces=new Polygon[]{
                new Polygon(new Point[]{A, C, D, B},origin),//front
                new Polygon(new Point[]{F, H, G, E},origin),//back
                new Polygon(new Point[]{A, B, F, E},origin),
                new Polygon(new Point[]{G, H, D, C},origin),
                new Polygon(new Point[]{B, D, H, F},origin),
                new Polygon(new Point[]{A, C, G, E},origin),//left
        };
        oinit();
    }
    private void oinit(){
        VBOTextureHandle= glGenBuffers();
        VBOColorHandle  = glGenBuffers();
        VBOVertexHandle = glGenBuffers();
        VBONormalHandle = glGenBuffers();

        glBindBuffer(GL_ARRAY_BUFFER, VBOColorHandle);
        glBufferData(GL_ARRAY_BUFFER, makecolorbuff(),GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, VBOVertexHandle);
        glBufferData(GL_ARRAY_BUFFER, makevertbuff(),GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, VBONormalHandle);
        glBufferData(GL_ARRAY_BUFFER, makenormbuff(),GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, VBOTextureHandle);
        glBufferData(GL_ARRAY_BUFFER, maketexbuff(),GL_STATIC_DRAW);
    }
    public void draw() {


        glEnable(GL_TEXTURE_2D);
        glEnableClientState(GL_TEXTURE_COORD_ARRAY);
        glEnableClientState(GL_NORMAL_ARRAY);
        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_COLOR_ARRAY);
        glEnable(GL_COLOR_MATERIAL);

        glBindTexture(GL_TEXTURE_2D, VBOTexID);
        glBindBuffer(GL_ARRAY_BUFFER, VBOTextureHandle);
        glTexCoordPointer(2,GL_FLOAT, 0, 0);

        glBindBuffer(GL_ARRAY_BUFFER, VBOColorHandle);
        glColorPointer(3, GL_FLOAT, 0, 0L);

        glBindBuffer(GL_ARRAY_BUFFER, VBOVertexHandle);
        glVertexPointer(3, GL_FLOAT, 0, 0L);

        glBindBuffer(GL_ARRAY_BUFFER, VBONormalHandle);
        glNormalPointer(GL_FLOAT, 0, 0);

        glPushMatrix();
        glTranslatef(origin.x,origin.y,origin.z);
        float amount=rx!=0?rx:ry!=0?ry:rz!=0?rz:0;
        float tx=rx!=0?1:0,ty=ry!=0?1:0,tz=rz!=0?1:0;
        glRotatef(amount,tx,ty,tz);
        glTranslatef(-origin.x,-origin.y,-origin.z);
        if(isWrieframe)
            glDrawArrays(GL_LINE_LOOP, 0, 4 * 6);
        else
            glDrawArrays(GL_QUADS, 0, 4 * 6);
        glPopMatrix();

        glDisable(GL_TEXTURE_2D);
        glDisableClientState(GL_TEXTURE_COORD_ARRAY);
        glDisableClientState(GL_NORMAL_ARRAY);
        glDisableClientState(GL_VERTEX_ARRAY);
        glDisableClientState(GL_COLOR_ARRAY);
        glDisable(GL_COLOR_MATERIAL);

    }
    public FloatBuffer makecolorbuff(){
        FloatBuffer colors = BufferUtils.createFloatBuffer(24 * 3);
        for (int i = 0; i < faces.length; i++)
            colors.put(faces[i].getColors());
        colors.flip();
        return colors;
    }
    public FloatBuffer makevertbuff(){
        FloatBuffer vertexes = BufferUtils.createFloatBuffer(24 * 3);
        for (int i = 0; i < faces.length; i++)
            vertexes.put(faces[i].getVerts());
        vertexes.flip();
        return vertexes;
    }
    public FloatBuffer maketexbuff(){
        FloatBuffer texture = BufferUtils.createFloatBuffer(24 * 2);
        for(int i=0;i<faces.length;i++){
            texture.put(new float[]{0,0});
            texture.put(new float[]{0,1});
            texture.put(new float[]{1,1});
            texture.put(new float[]{1,0});
        }
        texture.flip();
        return texture;
    }
    public FloatBuffer makenormbuff(){
        FloatBuffer normal = BufferUtils.createFloatBuffer(12 * 3);
        for (int i = 0; i < faces.length; i++)
            normal.put(faces[i].getNormals());
        normal.flip();
        return normal;
    }


    public void rotate(float amount,float tx,float ty, float tz){
        rx+=amount*tx;
        ry+=amount*ty;
        rz+=amount*tz;
    }

    public Point getOrigin() {
        return origin;
    }
    public void translate(float x,float y,float z){
        translate(new Point(x,y,z));
    }
    public void translate(Point de) {
        this.origin.x+=de.x;
        this.origin.y+=de.y;
        this.origin.z+=de.z;
        vertInit();
    }

    public void setOrigin(Point origin) {
        this.origin = origin;
        vertInit();
    }
    private static final int BYTES_PER_PIXEL = 4;//3 for RGB, 4 for RGBA
    public static int loadTexture(BufferedImage image){

        int[] pixels = new int[image.getWidth() * image.getHeight()];
        image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

        ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * BYTES_PER_PIXEL); //4 for RGBA, 3 for RGB

        for(int y = 0; y < image.getHeight(); y++){
            for(int x = 0; x < image.getWidth(); x++){
                int pixel = pixels[y * image.getWidth() + x];
                buffer.put((byte) ((pixel >> 16) & 0xFF));     // Red component
                buffer.put((byte) ((pixel >> 8) & 0xFF));      // Green component
                buffer.put((byte) (pixel & 0xFF));               // Blue component
                buffer.put((byte) ((pixel >> 24) & 0xFF));    // Alpha component. Only for RGBA
            }
        }

        buffer.flip(); //FOR THE LOVE OF GOD DO NOT FORGET THIS

        // You now have a ByteBuffer filled with the color data of each pixel.
        // Now just create a texture ID and bind it. Then you can load it using
        // whatever OpenGL method you want, for example:

        int textureID = glGenTextures(); //Generate texture ID
        glBindTexture(GL_TEXTURE_2D, textureID); //Bind texture ID

        //Setup wrap mode
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

        //Setup texture scaling filtering
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        //Send texel data to OpenGL
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, image.getWidth(), image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

        //Return the texture ID so we can bind it later again
        return textureID;
    }

    public static BufferedImage loadImage(String loc)
    {
        try {
            return ImageIO.read(new File(System.getProperty("user.dir")+"/"+loc));
        } catch (IOException e) {
            //Error Handling Here
        }
        return null;
    }
}
/*
options:
    outlined={r,g,b}
    relative=name


name{option=name,option=value}:{x,y,z}{l,w,h}{r,g,b};

 */