import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glTranslatef;

public class Camera {
    public static float moveSpeed = 0.5f;

    private static float maxLook = 85;

    private static float mouseSensitivity = 0.05f;

    private static Vector3f pos;
    private static Vector3f rotation;
    public static int dm=0;
    public static boolean ismoving=false;
    private static boolean mouse=true;

    public static void create() {
        pos = new Vector3f(0, 0, 0);
        rotation = new Vector3f(0, 0, 0);
        moveSpeed = 0.05f;
        maxLook = 85;
        mouseSensitivity = 0.005f;
    }

    static void apply() {
        if(rotation.y / 360 > 1) {
            rotation.y -= 360;
        } else if(rotation.y / 360 < -1) {
            rotation.y += 360;
        }
        glLoadIdentity();
        glRotatef(rotation.x, 1, 0, 0);
        glRotatef(rotation.y, 0, 1, 0);
        glRotatef(rotation.z, 0, 0, 1);
        glTranslatef(-pos.x, -pos.y, -pos.z);
    }

    static void acceptInput(float delta) {
        acceptInputRotate(delta);
        acceptInputGrab();
        acceptInputMove(delta);
    }

    public static void acceptInputRotate(float delta) {
        if(Mouse.isGrabbed()&&mouse) {
            float mouseDX = Mouse.getDX();
            float mouseDY = -Mouse.getDY();
            rotation.y += mouseDX * mouseSensitivity * delta;
            rotation.x += mouseDY * mouseSensitivity * delta;
            rotation.x = Math.max(-maxLook, Math.min(maxLook, rotation.x));
        }
    }

    public static void acceptInputGrab() {
        if(Mouse.isInsideWindow() && Mouse.isButtonDown(0)) {
            Mouse.setGrabbed(true);
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            Mouse.setGrabbed(false);
        }
    }

    public static void acceptInputMove(float delta) {
        boolean keyUp = Keyboard.isKeyDown(Keyboard.KEY_W);
        boolean keyDown = Keyboard.isKeyDown(Keyboard.KEY_S);
        boolean keyRight = Keyboard.isKeyDown(Keyboard.KEY_D);
        boolean keyLeft = Keyboard.isKeyDown(Keyboard.KEY_A);
        boolean keyFast = Keyboard.isKeyDown(Keyboard.KEY_Q);
        boolean keySlow = Keyboard.isKeyDown(Keyboard.KEY_E);
        boolean keyFlyUp = Keyboard.isKeyDown(Keyboard.KEY_SPACE);
        boolean keyFlyDown = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);


        float speed =moveSpeed/5f;

//        if(keyFast) {
//            speed = moveSpeed * 5;
//        }
//        else if(keySlow) {
//            speed = moveSpeed / 5;
//        }
//        else {
//            speed = moveSpeed;
//        }

        speed *= delta;

        if(keyFlyUp) {
            pos.y += speed;
        }
        if(keyFlyDown) {
            pos.y -= speed;
        }


        if(keyLeft)
            rotation.y -= mouseSensitivity * delta*15;
        if(keyRight)
            rotation.y += mouseSensitivity * delta*15;

        if(keyDown) {
            pos.x -= Math.sin(Math.toRadians(rotation.y)) * speed;
            pos.z += Math.cos(Math.toRadians(rotation.y)) * speed;
            ismoving=true;
        }
        if(keyUp) {
            pos.x += Math.sin(Math.toRadians(rotation.y)) * speed;
            pos.z -= Math.cos(Math.toRadians(rotation.y)) * speed;
            ismoving=true;
        } if(keyFast) {
            pos.x += Math.sin(Math.toRadians(rotation.y - 90)) * speed;
            pos.z -= Math.cos(Math.toRadians(rotation.y - 90)) * speed;
            ismoving=true;
        }
        if(keySlow) {
            pos.x += Math.sin(Math.toRadians(rotation.y + 90)) * speed;
            pos.z -= Math.cos(Math.toRadians(rotation.y + 90)) * speed;
            ismoving=true;
        }
        if(keyDown||keyFast||keyLeft||keyRight||keyUp||keySlow)
            dm++;
            else
        ismoving=false;

    }

    public static void setSpeed(float speed) {
        moveSpeed = speed;
    }

    public static void setPos(Vector3f pos) {
        Camera.pos = pos;
    }

    public static Vector3f getPos() {
        return pos;
    }

    public static void setX(float x) {
        pos.x = x;
    }

    public static float getX() {
        return pos.x;
    }

    public static void addToX(float x) {
        pos.x += x;
    }

    public static void setY(float y) {
        pos.y = y;
    }

    public static float getY() {
        return pos.y;
    }

    public static void addToY(float y) {
        pos.y += y;
    }

    public static void setZ(float z) {
        pos.z = z;
    }

    public static float getZ() {
        return pos.z;
    }

    public static void addToZ(float z) {
        pos.z += z;
    }

    public static void setRotation(Vector3f rotation) {
        Camera.rotation = rotation;
    }

    public static Vector3f getRotation() {
        return rotation;
    }

    public static void setRotationX(float x) {
        rotation.x = x;
    }

    public static float getRotationX() {
        return rotation.x;
    }

    public static void addToRotationX(float x) {
        rotation.x += x;
    }

    public static void setRotationY(float y) {
        rotation.y = y;
    }

    public static float getRotationY() {
        return rotation.y;
    }

    public static void addToRotationY(float y) {
        rotation.y += y;
    }

    public static void setRotationZ(float z) {
        rotation.z = z;
    }

    public static float getRotationZ() {
        return rotation.z;
    }

    public static void addToRotationZ(float z) {
        rotation.z += z;
    }

    public static void setMaxLook(float maxLook) {
        Camera.maxLook = maxLook;
    }

    public static float getMaxLook() {
        return maxLook;
    }

    public static void setMouseSensitivity(float mouseSensitivity) {
        Camera.mouseSensitivity = mouseSensitivity;
    }

    public static float getMouseSensitivity() {
        return mouseSensitivity;
    }
}