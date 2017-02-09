package Utils;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by usr on 2/8/2017.
 * 
 * 
 */
public class Sprite {

    static boolean ffff=false;
    public static void draw(float rx,float ry,float rz,int ticks){

        if(ticks%40==0)
            ffff=!ffff;
        glPushMatrix();
        glTranslatef(5,0,5);
        glRotatef(-rx,1,0,0);
        glRotatef(-ry,0,1,0);
        glRotatef(-rz,0,0,1);
        Polygon.draw(new Point[]{
                new Point(-.5f,1,0),
                new Point(-.5f,0,0),
                new Point(.5f,0,0),
                new Point(.5f,1,0),
        },new Point[]{
                new Point(0,0,0),
                new Point(0,1,0),
                new Point(1,1,0),
                new Point(1,0,0),
        }, Textureloader.loadImage("images\\toaster.png",32,32,8,4,2)[1+((ffff)?0:4)]);
        glPopMatrix();
        
    }
    
}
