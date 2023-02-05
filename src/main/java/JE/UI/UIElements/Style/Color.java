package JE.UI.UIElements.Style;

import org.joml.Vector4f;
import org.lwjgl.nuklear.NkColor;

import java.io.Serializable;

public class Color implements Serializable {

    public static final Color TRANSPARENT = Color.createColor(0,0,0,0);
    public static final Color WHITE = Color.createColor(1,1,1,1);
    public static final Color BLACK = Color.createColor(0,0,0,1);
    public static final Color GREY = Color.createColor(0.6f,0.6f,0.6f);
    public static final Color DARK_GREY = Color.createColor(0.3f,0.3f,0.3f);

    public static final Color RED = Color.createColor(1,0,0,1);
    public static final Color GREEN = Color.createColor(0,1,0,1);
    public static final Color BLUE = Color.createColor(0,0,1,1);

    public static final Color YELLOW = Color.createColor(1,1,0,1);
    public static final Color CYAN = Color.createColor(0,1,1,1);
    public static final Color MAGENTA = Color.createColor(1,0,1,1);

    float r;
    float g;
    float b;
    float a;
    transient NkColor nkColor = NkColor.create();
    private Color(){}
    private Color(float r, float g, float b, float a){
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public static Color createColor(float r, float g, float b, float a){
        return new Color(r,g,b,a);
    }
    public static Color createColor(float r, float g, float b){
        return new Color(r,g,b,1);
    }

    public static Color createColor255(int r, int g, int b, int a){
        return new Color(r/255f,g/255f,b/255f,a/255f);
    }
    public static Color createColor255(int r, int g, int b){
        return new Color(r/255f,g/255f,b/255f,1);
    }
    public static Color createColorHSV(float h, float s, float v, float a){
        float c = v*s;
        float x = c*(1-Math.abs((h/60)%2-1));
        float m = v-c;
        float r = 0;
        float g = 0;
        float b = 0;
        if(h >= 0 && h < 60){
            r = c;
            g = x;
            b = 0;
        }else if(h >= 60 && h < 120){
            r = x;
            g = c;
            b = 0;
        }else if(h >= 120 && h < 180){
            r = 0;
            g = c;
            b = x;
        }else if(h >= 180 && h < 240){
            r = 0;
            g = x;
            b = c;
        }else if(h >= 240 && h < 300){
            r = x;
            g = 0;
            b = c;
        }else if(h >= 300 && h < 360){
            r = c;
            g = 0;
            b = x;
        }
        return new Color(r+m,g+m,b+m,a);
    }
    public static Color createColorHex(String hex){
        hex = hex.replace("#","");
        int r = Integer.parseInt(hex.substring(0,2),16);
        int g = Integer.parseInt(hex.substring(2,4),16);
        int b = Integer.parseInt(hex.substring(4,6),16);
        int a = 255;
        if(hex.length() == 8)
            a = Integer.parseInt(hex.substring(6,8),16);
        return createColor255(r,g,b,a);
    }

    public static Color getComplementaryColor(Color c){
        return new Color(1-c.r,1-c.g,1-c.b,c.a);
    }

    public NkColor nkColor(){
        return nkColor.set((byte) (r*255), (byte) (g*255), (byte) (b*255), (byte) (a*255));
    }
    public float r(){
        return r;
    }
    public float g(){
        return g;
    }
    public float b(){
        return b;
    }
    public float a(){
        return a;
    }
    public int r255(){
        return (int) (r*255);
    }
    public int g255(){
        return (int) (g*255);
    }
    public int b255(){
        return (int) (b*255);
    }
    public int a255(){
        return (int) (a*255);
    }
    public int getRGB(){
        return (r255() << 16 | g255() << 8 | b255());
    }
    public String getHex(){
        return String.format("#%02x%02x%02x",r255(),g255(),b255());
    }
    public Vector4f getVec4(){
        return new Vector4f(r,g,b,a);
    }
}
