package Utils;


import java.util.*;

/**
 * Created by usr on 2/11/2017.
 *
 */
public class Path {
    private LinkedList<Point> path;
    private int index=0;
    private boolean loops;
    private boolean done=false;
     Path(Point[] locs,boolean l){
        loops=l;
        path=new LinkedList<>();
        Collections.addAll(path, locs);
    }
    public Path(){
        loops=false;
        path=new LinkedList<>();
    }
    private static class node{
        Point p;
        node camefrom;
        node(Point j,node c){p=j;camefrom=c;}
        List<Point> list(){
            List<Point> l=new LinkedList<>();
            if(camefrom==null)
                return l;
            l.addAll(camefrom.list());
            l.add(p);
            return l;
        }
    }
    private static node findmin(List<node> l,Point p,int[][] map){
        int min=0;
        for(int i=0;i<l.size();i++){
            Point c=l.get(i).p.round(),m=l.get(min).p.round();
            if(c.distance(p)+map[(int)c.x][(int)c.z]<m.distance(p)+map[(int)m.x][(int)m.z])
                min=i;
        }
        return l.get(min);
    }
    private static List<node> getneighbors(node B,int[][] map) {
        List<node> n = new LinkedList<>();
        for (int r = -1;r < 2; r++)
            for (int c = -1; c < 2; c++) {
                if(r == c || r + c == 0 )
                    continue;
                if((int)B.p.x+r<map.length&&(int)B.p.z+c<map[0].length) {
                    n.add(new node(new Point(B.p.x+r,B.p.y,B.p.z+c),B));
                }
            }
        return n;
    }
    public static Path findPath(Point A,Point B,int[][] map){
        List<Point> path  =new LinkedList<>();
        List<node> open  =new LinkedList<>();
        List<node> closed=new LinkedList<>();
        int cc=0;
        B.y=A.y;
        A.round();
        B.round();
        open.add(new node(A,null));
        if(map[(int)B.x][(int)B.z]<0xffff)
            while(true){
                if(cc++>1000)
                    break;
                node min=findmin(open,B,map);
                if(min.p.equal(B)) {
                    path=min.list();
                    break;
                }
                open.remove(min);
                closed.add(min);
                for(node n:getneighbors(min,map)){
                    if(!closed.stream().anyMatch(point -> point.p.equal(n.p))){
                        if(n.p.distance(B)+map[(int)n.p.x][(int)n.p.z]>=0xffff){
                            closed.add(n);
                            continue;
                        }
                        if(!open.stream().anyMatch(point -> point.p.equal(n.p))) {
                            open.add(n);
                        }
                    }
                }

            }
        else{
            path.add(A);
        }
            path.add(0,A);
        System.out.println();
        Point[] points=new Point[path.size()];
        for (int i = 0; i < path.size(); i++) {
            points[i]=path.get(i);
        }
        return new Path(points,false);
    }
     float movementDirection(){
        int i=index+1>=path.size()?0:index+1;
        return (float) Math.toDegrees(Math.atan2(path.get(index).z-path.get(i).z
                ,path.get(index).x-path.get(i).x));
    }
     void next(){
        index++;
        if(index>=path.size())
            if(loops)
                index=0;
            else {
                index=path.size()-1;
                done=true;
            }

    }
     void normalize(float dist){
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
    public void translate(float x,float y,float z){
        for (Point p : path) p.sum(x, y, z);
    }
     Point get(){
        return path.get(index);
    }
    public int size(){
        return path.size();
    }
    public void reset(){
        index=0;
    }
     boolean isDone(){return done;}
}
