package Shapes;

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
        this.r=0;
        this.g=0;
        this.b=0;
    }

    public Point(){
        this.x=0;
        this.y=0;
        this.z=0;
        this.r=0;
        this.g=0;
        this.b=0;
    }
    public boolean isPointOnRay(Point a, Point B)
    {
        double d1,d2,d3,ans;
        d1=a.distance(B);
        d2=a.distance(this);
        d3=B.distance(this);
        ans=Math.abs((d1+d2)-d3);
        return ans<0.00001;//abs((d1+d2)-length)<# close to zero
    }
    public double distance(Point a){
        double px = a.x - this.x;
        double py = a.y - this.y;
        return Math.sqrt(px * px + py * py);
    }
    public boolean compare(Point a){
        return a.x==x&&a.y==y&&a.z==z;
    }

    public void setXYZ(float x,float y,float z){
        this.x=x;
        this.y=y;
        this.z=z;
    }

    @Override
    public String toString() {
        return "("+x+","+y+")";
    }

}
