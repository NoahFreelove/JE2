package JE.UI.UIElements.Style;

import org.lwjgl.nuklear.NkColor;

public class Color {
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
        return nkColor.set((byte) (r*127), (byte) (g*127), (byte) (b*127), (byte) (a*127));
    }
}
