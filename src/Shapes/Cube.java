package Shapes;

import org.lwjgl.BufferUtils;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL11.*;

import java.nio.FloatBuffer;

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
    private Point[] verts;


    private float rx=0;
    private float ry=0;
    private float rz=0;

    public Cube(float x,float y,float z){
        origin=new Point(x,y,z);
        vertInit();
    }
    private void vertInit(){
        Point A=new Point(origin.x-1,origin.y+1,origin.z+1,1,0,0);
        Point B=new Point(origin.x+1,origin.y+1,origin.z+1,1,1,0);
        Point C=new Point(origin.x-1,origin.y-1,origin.z+1,0,1,0);
        Point D=new Point(origin.x+1,origin.y-1,origin.z+1,0,0,1);
        Point E=new Point(origin.x-1,origin.y+1,origin.z-1,1,0,1);
        Point F=new Point(origin.x+1,origin.y+1,origin.z-1,0,1,1);
        Point G=new Point(origin.x-1,origin.y-1,origin.z-1,1,1,1);
        Point H=new Point(origin.x+1,origin.y-1,origin.z-1,0,0,0);
        verts=new Point[]{A,B,C,D,E,F,G,H};
    }

    public void CreateVBO() {
        Point A=verts[0];
        Point B=verts[1];
        Point C=verts[2];
        Point D=verts[3];
        Point E=verts[4];
        Point F=verts[5];
        Point G=verts[6];
        Point H=verts[7];
        glPushMatrix();
        glTranslatef(origin.x,origin.y,origin.z);
        float amount=rx!=0?rx:ry!=0?ry:rz!=0?rz:0;
        float tx=rx!=0?1:0,ty=ry!=0?1:0,tz=rz!=0?1:0;
        glRotatef(amount,tx,ty,tz);
        glTranslatef(-origin.x,-origin.y,-origin.z);
        int VBOColorHandle = glGenBuffers();
        int VBOVertexHandle = glGenBuffers();
        FloatBuffer VertexPositionData = BufferUtils.createFloatBuffer(24 * 3);
        VertexPositionData.put(new float[] {
                F.x, F.y, F.z,//F
                E.x, E.y, E.z,//E     top face
                A.x, A.y, A.z,//A
                B.x, B.y, B.z,//B

                D.x, D.y, D.z,//D
                C.x, C.y, C.z,//C     bottom face
                G.x, G.y, G.z,//G
                H.x, H.y, H.z,//H

                B.x, B.y, B.z,//B
                A.x, A.y, A.z,//A      front face
                C.x, C.y, C.z,//C
                D.x, D.y, D.z,//D

                H.x, H.y, H.z,//H
                G.x, G.y, G.z,//G      back face
                E.x, E.y, E.z,//E
                F.x, F.y, F.z,//F

                A.x, A.y, A.z,//A
                E.x, E.y, E.z,//E       left face
                G.x, G.y, G.z,//G
                C.x, C.y, C.z,//C

                F.x, F.y, F.z,//F
                B.x, B.y, B.z,//B        right face
                D.x, D.y, D.z,//D
                H.x, H.y, H.z,//H
        });
        VertexPositionData.flip();
        FloatBuffer VertexColorData = BufferUtils.createFloatBuffer(24 * 3);
        VertexColorData.put(new float[] { F.r, F.g, F.b, //F
                E.r, E.g, E.b,//E     top face
                A.r, A.g, A.b,//A
                B.r, B.g, B.b,//B

                D.r, D.g, D.b,//D
                C.r, C.g, C.b,//C     bottom face
                G.r, G.g, G.b,//G
                H.r, H.g, H.b,//H

                B.r, B.g, B.b,//B
                A.r, A.g, A.b,//A      front face
                C.r, C.g, C.b,//C
                D.r, D.g, D.b,//D

                H.r, H.g, H.b,//H
                G.r, G.g, G.b,//G      back face
                E.r, E.g, E.b,//E
                F.r, F.g, F.b,//F

                A.r, A.g, A.b,//A
                E.r, E.g, E.b,//E       left face
                G.r, G.g, G.b,//G
                C.r, C.g, C.b,//C

                F.r, F.g, F.b,//F
                B.r, B.g, B.b,//B        right face
                D.r, D.g, D.b,//D
                H.r, H.g, H.b,//H
        });
        VertexColorData.flip();

        glBindBuffer(GL_ARRAY_BUFFER, VBOVertexHandle);
        glBufferData(GL_ARRAY_BUFFER, VertexPositionData,GL_STATIC_DRAW);
        glVertexPointer(3, GL_FLOAT, 0, 0L);
        glBindBuffer(GL_ARRAY_BUFFER, VBOColorHandle);
        glBufferData(GL_ARRAY_BUFFER, VertexColorData,GL_STATIC_DRAW);
        glColorPointer(3, GL_FLOAT, 0, 0L);
        glDrawArrays(GL_QUADS, 0, 4 * 6);
        glPopMatrix();
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
}
