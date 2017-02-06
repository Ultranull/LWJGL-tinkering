package Shapes;

import javax.swing.*;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by usr on 11/14/2016.
 *
 */
public class Polygon {

    private Point origin;
    private Point[] verts;
    private Point[] normals;

    public Polygon(Point[] v) {
        verts=v;
        initnormals();
    }
    public Polygon(Point[] v,Point o) {
        verts=v;
        origin=o;
        initnormals();
    }
    private void initnormals(){
        normals=new Point[verts.length-2];
        for(int i=0;i<normals.length;i++){
            if(origin!=null)
                normals[i]=Polygon.calcnormal(verts[i+2].sub(origin),verts[i+1].sub(origin),verts[0].sub(origin));
            else
                normals[i]=Polygon.calcnormal(verts[i+2],verts[i+1],verts[0]);
        }

    }

    public void draw(){
        glBegin(GL_POLYGON);
        for (Point vert : verts) {
            glColor3f(vert.r, vert.g, vert.b);
            glVertex3f(vert.x, vert.y, vert.z);
        }
        for(Point norm:normals)
            glNormal3f(norm.x,norm.y,norm.z);
        glEnd();
    }
    public static void draw(Point[] verts,boolean wire){
        Point[] normals=new Point[verts.length-2];
        for(int i=0;i<normals.length;i++){
            normals[i]=Polygon.calcnormal(verts[i+2],verts[i+1],verts[0]);
        }
        if(wire)
            glBegin(GL_LINE_LOOP);
        else
            glBegin(GL_POLYGON);
        for (Point vert : verts) {
            glColor3f(vert.r, vert.g, vert.b);
            glVertex3f(vert.x, vert.y, vert.z);
        }
        for(Point norm:normals)
            glNormal3f(norm.x,norm.y,norm.z);
        glEnd();
    }
    public static Point calcnormal(Point A,Point B,Point C){
        Point V1= B.sub(A);
        Point V2= C.sub(A);
        Point surfaceNormal=new Point(0,0,0);
        surfaceNormal.x = (V1.y*V2.z) - (V1.z*V2.y);
        surfaceNormal.y = (V1.z*V2.x) - (V1.x*V2.z);
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
    public Point[] getVertsp(){
        return verts;
    }
    public Point[] getNormalsp(){
        return normals;
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
}
