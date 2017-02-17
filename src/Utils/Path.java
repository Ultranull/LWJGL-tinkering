package Utils;

import sun.awt.image.ImageWatched;

import java.util.Collections;
import java.util.LinkedList;

/**
 * Created by usr on 2/11/2017.
 *
 */
public class Path {
    private LinkedList<Point> path;
    private int index=0;
    private boolean loops;
    public Path(Point[] locs,boolean l){
        loops=l;
        path=new LinkedList<>();
        for(Point p:locs)
            path.add(p);
    }
    public void next(){
        index++;
        if(index>=path.size())
            if(loops)
                index=0;
            else index=path.size()-1;

    }
    public LinkedList<Point> makeSmooth(Point A,Point B,int smoothness){
        LinkedList<Point> subpath=new LinkedList<>();
        Point mid=A.midpoint(B);
        if(smoothness==1) {
            subpath.add(A);
            subpath.add(mid);
            subpath.add(B);
        }else{
            LinkedList<Point> temp=makeSmooth(A,mid,smoothness-1);
            temp.removeLast();
            temp.addAll(makeSmooth(mid,B,smoothness-1));
            subpath.addAll(temp);
        }
        return subpath;
    }
    public void makeSmooth(int smoop){
        if(smoop<=0)
            return;
        LinkedList<Point> newpath=new LinkedList<>();
        for (int i = 0; i <path.size(); i++) {
            if(i+1>=path.size())
                break;
            newpath.addAll(makeSmooth(path.get(i),path.get(i+1),smoop));
        }
        path=newpath;
    }
    public void normalize(float dist){
        LinkedList<Point> newpath=new LinkedList<>();
        newpath.add(path.get(0));
        for (int i = 1; i < path.size(); i++) {
            Point a=path.get(i-1);
            Point b=path.get(i);
            int am=(int)(a.distance(b)/dist);
            am=am<=0?1:am;
            for(int c=0;c<am-1;c++)
                newpath.add(a.findOnLine(b,dist+dist*c));
            newpath.add(b);
        }
        path=newpath;
    }
    public void display(){
        for (Point p :path) {
            System.out.print(p+" ");
        }
        System.out.println();
    }
    public Point get(){
        return path.get(index);
    }
    public int size(){
        return path.size();
    }
    public void reset(){
        index=0;
    }
}
