package Utils;

import org.lwjgl.util.vector.Vector3f;

/**
 * Created by usr on 11/14/2016.
 *
 */
public class Point {
    public float x,y,z;
    public float r,g,b;
    public Point(float x,float y,float z,float r,float g,float b){
        this.x=x;
        this.y=y;
        this.z=z;
        this.r=r;
        this.g=g;
        this.b=b;
    }
    public Point(float x,float y,float z){
        this.x=x;
        this.y=y;
        this.z=z;
        this.r=1;
        this.g=1;
        this.b=1;
    }

    public Point(Vector3f v){
        this.x=v.getX();
        this.y=v.getY();
        this.z=v.getZ();
        this.r=1;
        this.g=1;
        this.b=1;
    }
    public Point(){
        this.x=0;
        this.y=0;
        this.z=0;
        this.r=1;
        this.g=1;
        this.b=1;
    }
    public Point findOnLine(Point b,float dist){
        float dt=distance(b);
        float t=dist/dt;
        return new Point(
                (1-t)*x+t*b.x,
                (1-t)*y+t*b.y,
                (1-t)*z+t*b.z
        );
    }
    public Point midpoint(Point B){
        return new Point((x+B.x)/2,(y+B.y)/2,(z+B.z)/2);//,(r+B.r)/2,(g+B.g)/2,(b+B.b)/2);
    }
    public boolean isPointOnRay(Point a, Point B) {
        float d1,d2,d3,ans;
        d1=a.distance(B);
        d2=a.distance(this);
        d3=B.distance(this);
        ans=Math.abs((d1+d2)-d3);
        return ans<0.00001;//abs((d1+d2)-length)<# close to zero
    }
    public float distance(Point a){
        float px = a.x - this.x;
        float py = a.y - this.y;
        float pz = a.z - this.z;
        return (float)Math.sqrt(px * px + py * py + pz * pz);
    }
    public boolean equal(Point a){
        return a.x==x&&a.y==y&&a.z==z;
    }
    public void setXYZ(float x,float y,float z){
        this.x=x;
        this.y=y;
        this.z=z;
    }public void setRGB(float x,float y,float z){
        this.r=x;
        this.g=y;
        this.b=z;
    }
    public void makep(){
        x=Math.abs(x);
        y=Math.abs(y);
        z=Math.abs(z);
    }
    public Point div(Point B,float d){
        return new Point((x+B.x)/d,(y+B.y)/d,(z+B.z)/d);
    }
    public Point sub(Point p){
        return new Point(x-p.x,y-p.y,z-p.z);
    }
    public Point sum(Point p){
        return new Point(x+p.x,y+p.y,z+p.z);
    }
    public void sum(float x,float y,float z){
        this.x+=x;
        this.y+=y;
        this.z+=z;
    }
    public void normalize(){
        float v=(float) Math.sqrt(x*x+y*y+z*z);
        setXYZ(x/v,y/v,z/v);
    }
    public Point round(){
        setXYZ((float) Math.floor(x),(float)Math.floor(y),(float)Math.floor(z));
        return this;
    }
    @Override
    public String toString() {
        return "("+x+","+y+","+z+")";
    }

}
