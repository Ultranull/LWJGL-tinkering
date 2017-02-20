package Utils;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by usr on 2/17/2017.
 *
 */
public class Toaster {
    private enum DIR{
        NTH(90),
        STH(270),
        WST(180),
        EST(0),
        NULL(-1);
        public int val;
        DIR(int i){val=i;}
    }
    private Path path;
    private Sprite sprite;
    private boolean hasPath=false;
    private Point or;
    private DIR dir;
    int moverate=2;
    public Toaster(int[] t,Point o){
        or=o;
        sprite=new Sprite(t,o,4,5);
        dir=DIR.NTH;
    }


    public void setPath(Path p){
        hasPath=true;
        path=p;
        path.normalize(1/16f);

    }
    private DIR find(float an){
        DIR d;
        if(an<0) an=360+an;
        if(135>an&&an>45)
            d=DIR.NTH;
        else if(315>an&&an>225)
            d=DIR.STH;
        else if(45>an||an>315)
            d=DIR.WST;
        else if(225>an&&an>135)
            d=DIR.EST;
        else
            d=DIR.NULL;
        return d;
    }
    private void dirhandle(float ry){
        DIR player;
        if(hasPath)
            if(!path.isDone()) {
                float an = path.movementDirection();
                dir = find(an);
            }
        player=find(find(ry).val-dir.val);
        switch (player){
            case STH:
                sprite.settex(2,3);//front
                break;
            case NTH:
                sprite.settex(0,1);//back
                break;
            case WST:
                sprite.settex(6,7);//left
                break;
            case EST:
                sprite.settex(4,5);
                break;
        }
    }
    public  void draw(float rx,float ry,float rz,int ticks){
        if(hasPath) {
            or = path.get();
            if (ticks % moverate == 0) {
                path.next();
            }
        }
        dirhandle(ry);
        sprite.setOr(or);
        sprite.draw(rx,ry,rz,ticks);
    }

    public Point getOr() {
        return or;
    }
}
