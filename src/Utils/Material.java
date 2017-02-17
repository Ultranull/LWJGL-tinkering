package Utils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static Utils.Textureloader.*;

/**
 * Created by usr on 2/11/2017.
 *
 */
public class Material {
    public static HashMap<String,Integer> mats;
    public static void init(String[] textures){
        mats=new HashMap<>();
        for(int i=0;i<textures.length;i+=2)
            mats.put(textures[i],loadTexture(loadImage(textures[i+1])));
    }
    public static void addMat(String name,String tex){
        mats.put(name,loadTexture(loadImage(tex)));
    }
    public static int[] addMat(String name,String loc,int ol,int oh,int not,int il,int ih){
        int[] b=loadTexture(loadImage(loc,ol,oh,not,il,ih));
        int ii=0;
        for (Integer i:b)
            mats.put(name+ii++,i);
        return b;
    }
    public static int get(String n){
        return mats.get(n);
    }
}
