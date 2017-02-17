package Utils;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by usr on 2/8/2017.
 * 
 * 
 */
public class Sprite {

    private Polygon im;
    private Point or;
    private Path path;
    private boolean hasPath=false;
    private int[] texs;
    private int ts;
    private int te;
    private int index=0;
    int rate=30;
    int moverate=2;
    public Sprite(int[] t,Point o,int tt,int ttt){
        or=o;
        texs=t;
        ts=tt;
        te=ttt;
        im=new Polygon(new Point[]{
                new Point(-.5f,1,0),
                new Point(-.5f,0,0),
                new Point(.5f,0,0),
                new Point(.5f,1,0)});
        im.HasTex(new Point[]{
                new Point(0,0,0),
                new Point(0,1,0),
                new Point(1,1,0),
                new Point(1,0,0),
        },texs[ts]);

    }
public void setPath(Path p){
    hasPath=true;
    path=p;
    path.normalize(1/16f);

}
    public  void draw(float rx,float ry,float rz,int ticks){
        or=path.get();
        if(hasPath&&ticks%moverate==0){
            path.next();
        }
        if(ticks%rate==0) {
            index++;
            if(index>te)
                index=ts;
            im.setTexid(texs[index]);
        }
        glPushMatrix();
        glTranslatef(or.x,or.y,or.z);
        glRotatef(-rx,1,0,0);
        glRotatef(-ry,0,1,0);
        im.draw();
        glPopMatrix();
    }
    
}
