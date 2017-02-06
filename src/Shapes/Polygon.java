package Shapes;

/**
 * Created by usr on 11/14/2016.
 *
 */
public class Polygon {

    private Point origin;
    private Point[] verts;
    private Point[] normals;
    private boolean isdis;

    public Polygon(Point[] v,boolean i) {
        verts=v;
        isdis=i;
        initnormals();
    }

    private void initnormals(){
        normals=new Point[verts.length-2];
        for(int i=0;i<normals.length;i++){
            normals[i]=calcnormal(verts[0],verts[i+1],verts[i+2]);
        }
    }

    public void display(){

    }
    private Point calcnormal(Point A,Point B,Point C){
        Point V1= B.sub(A);
        Point V2 = C.sub(A);
        Point surfaceNormal=new Point(0,0,0);
        surfaceNormal.x = (V1.y*V2.z) - (V1.z-V2.y);
        surfaceNormal.y = - ( (V2.z * V1.x) - (V2.x * V1.z) );
        surfaceNormal.z = (V1.x*V2.y) - (V1.y*V2.x);
        surfaceNormal.normalize();
        return surfaceNormal;
    }
    public float[] getVerts(){
        float[] ans=new float[verts.length*3];
        for (int i=0;i<ans.length;i+=3){
            ans[i]=verts[i/3].x;
            ans[i+1]=verts[i/3].y;
            ans[i+2]=verts[i/3].z;
        }
        return ans;
    }
    public float[] getColors(){
        float[] ans=new float[verts.length*3];
        for (int i=0;i<ans.length;i+=3){
            ans[i]=verts[i/3].r;
            ans[i+1]=verts[i/3].g;
            ans[i+2]=verts[i/3].b;
        }
        return ans;
    }
    public float[] getNormals(){
        float[] ans=new float[normals.length*3];
        for (int i=0;i<ans.length;i+=3){
            ans[i]=normals[i/3].x;
            ans[i+1]=normals[i/3].y;
            ans[i+2]=normals[i/3].z;
        }
        return ans;
    }
    public Point getOrigin() {
        return origin;
    }

    public void setOrigin(Point origin) {
        this.origin = origin;
    }

    public boolean isdis() {
        return isdis;
    }

    public void setIsdis(boolean isdis) {
        this.isdis = isdis;
    }
}
