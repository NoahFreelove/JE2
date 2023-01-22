package JE.UI.UIElements.Style;

import org.lwjgl.nuklear.NkColor;

public class Color {
    public static final Color WHITE = Color.createColor(1,1,1,1);
    public static final Color BLACK = Color.createColor(0,0,0,1);
    public static final Color RED = Color.createColor(1,0,0,1);
    public static final Color GREEN = Color.createColor(0,1,0,1);
    public static final Color BLUE = Color.createColor(0,0,1,1);

    float r;
    float g;
    float b;
    float a;
    NkColor nkColor = NkColor.create();
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
    public float r255(){
        return r*255;
    }
    public float g255(){
        return g*255;
    }
    public float b255(){
        return b*255;
    }
    public float a255(){
        return a*255;
    }
}
