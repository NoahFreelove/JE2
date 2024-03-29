package org.JE.JE2.UI.UIElements.Style;

import org.JE.JE2.Annotations.ActPublic;
import org.joml.Vector4f;
import org.lwjgl.nuklear.NkColor;

import java.io.Serializable;
import java.util.Random;

public class Color implements Serializable {

    //region Pre-made Colors
    public static final Color TRANSPARENT = Color.createColor(0,0,0,0);
    public static Color TRANSPARENT(){return TRANSPARENT.clone();}
    public static final Color WHITE = Color.createColor(1,1,1,1);
    public static Color WHITE(){return WHITE.clone();}
    public static final Color SUPER_WHITE = Color.createColor(5,5,5,1);
    public static Color SUPER_WHITE(){return SUPER_WHITE.clone();}
    public static final Color BLACK = Color.createColor(0,0,0,1);
    public static Color BLACK(){return BLACK.clone();}
    public static final Color GREY = Color.createColor(0.6f,0.6f,0.6f);
    public static Color GREY(){return GREY.clone();}
    public static final Color DARK_GREY = Color.createColor(0.3f,0.3f,0.3f);
    public static Color DARK_GREY(){return DARK_GREY.clone();}

    public static final Color RED = Color.createColor(1,0,0,1);
    public static Color RED(){return RED.clone();}
    public static final Color GREEN = Color.createColor(0,1,0,1);
    public static Color GREEN(){return GREEN.clone();}
    public static final Color BLUE = Color.createColor(0,0,1,1);
    public static Color BLUE(){return BLUE.clone();}

    public static final Color YELLOW = Color.createColor(1,1,0,1);
    public static Color YELLOW(){return YELLOW.clone();}
    public static final Color CYAN = Color.createColor(0,1,1,1);
    public static Color CYAN(){return CYAN.clone();}
    public static final Color MAGENTA = Color.createColor(1,0,1,1);
    public static Color MAGENTA(){return MAGENTA.clone();}

    public static final Color BROWN = Color.createColor(0.6f,0.4f,0.2f);
    public static Color BROWN(){return BROWN.clone();}
    public static final Color PURPLE = Color.createColor(0.6f,0.2f,0.6f);
    public static Color PURPLE(){return PURPLE.clone();}
    public static final Color ORANGE = Color.createColor(1,0.5f,0);
    public static Color ORANGE(){return ORANGE.clone();}
    public static final Color PINK = Color.createColor(1,0.5f,0.5f);
    public static Color PINK(){return PINK.clone();}
    public static final Color TEAL = Color.createColor(0,0.5f,0.5f);
    public static Color TEAL(){return TEAL.clone();}
    public static final Color LIME = Color.createColor(0.5f,1,0);
    public static Color LIME(){return LIME.clone();}

    public static final Color PASTEL_RED = Color.createColor(1,0.5f,0.5f);
    public static Color PASTEL_RED(){return PASTEL_RED.clone();}
    public static final Color PASTEL_GREEN = Color.createColor(0.5f,1,0.5f);
    public static Color PASTEL_GREEN(){return PASTEL_GREEN.clone();}
    public static final Color PASTEL_BLUE = Color.createColor(0.5f,0.5f,1);
    public static Color PASTEL_BLUE(){return PASTEL_BLUE.clone();}
    public static final Color PASTEL_YELLOW = Color.createColor(1,1,0.5f);
    public static Color PASTEL_YELLOW(){return PASTEL_YELLOW.clone();}
    public static final Color PASTEL_CYAN = Color.createColor(0.5f,1,1);
    public static Color PASTEL_CYAN(){return PASTEL_CYAN.clone();}
    public static final Color PASTEL_MAGENTA = Color.createColor(1,0.5f,1);
    public static Color PASTEL_MAGENTA(){return PASTEL_MAGENTA.clone();}
    //endregion

    private float r;
    private float g;
    private float b;
    private float a;

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

    public Color clone() {
        return Color.createColor(r,g,b,a);
    }

    public Color a(float val){
        this.a = val;
        return this;
    }
    public Color r(float val){
        this.r = val;
        return this;
    }
    public Color g(float val){
        this.g = val;
        return this;
    }
    public Color b(float val){
        this.b = val;
        return this;
    }

    public void set(float r, float g, float b, float a){
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public void set(Color c){
        this.r = c.r;
        this.g = c.g;
        this.b = c.b;
        this.a = c.a;
    }

    public void set(float r, float g, float b){
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public void set255(int r, int g, int b, int a){
        this.r = r/255f;
        this.g = g/255f;
        this.b = b/255f;
        this.a = a/255f;
    }

    public void set255(int r, int g, int b){
        this.r = r/255f;
        this.g = g/255f;
        this.b = b/255f;
    }

    public NkColor nkColor(){
        return nkColor.set((byte) (r*255), (byte) (g*255), (byte) (b*255), (byte) (a*255));
    }
    public void setNkColor(NkColor color){
        this.nkColor = color;
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

    static Random rand = new Random(System.currentTimeMillis());
    public static Color random(){
        return new Color(rand.nextFloat(),rand.nextFloat(),rand.nextFloat(),1);
    }

    @Override
    public String toString(){
        return "("+r + ", " + g + ", " + b + ", " + a + ")";
    }

    public String serialize(){
        return r + "," + g + "," + b + "," + a;
    }

    public static Color deserialize(String s){
        String[] split = s.split(",");
        return Color.createColor(Float.parseFloat(split[0]),Float.parseFloat(split[1]),Float.parseFloat(split[2]),Float.parseFloat(split[3]));
    }
    public Color deserializeThis(String s){
        String[] split = s.split(",");
        this.r = Float.parseFloat(split[0]);
        this.g = Float.parseFloat(split[1]);
        this.b = Float.parseFloat(split[2]);
        this.a = Float.parseFloat(split[3]);
        return this;
    }
}
