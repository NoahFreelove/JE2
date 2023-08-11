package org.JE.JE2.Objects;

import org.JE.JE2.IO.Filepath;
import org.JE.JE2.IO.Logging.Errors.JE2Error;
import org.JE.JE2.IO.Logging.Logger;
import org.JE.JE2.Objects.Scripts.Transform;
import org.JE.JE2.Rendering.Renderers.SpriteRenderer;
import org.JE.JE2.Rendering.Renderers.TextureSegment;
import org.JE.JE2.Rendering.Shaders.ShaderProgram;
import org.JE.JE2.Rendering.Texture;
import org.JE.JE2.Resources.DataLoader;
import org.joml.Vector2f;

import java.util.Arrays;

import static org.lwjgl.opengl.GL11.GL_TRIANGLE_FAN;

public class CharacterCreator {

    public static TextureSegment[] createCharacter(Texture[] texts, Texture[] normals, Vector2f[][] fragPos, Vector2f[][] UVs, Transform[] relativeTransforms){
        if(texts.length != normals.length || normals.length != relativeTransforms.length || texts.length != fragPos.length || fragPos.length != UVs.length)
        {
            Logger.log(new JE2Error("Character Creator: Provided textures, normals, and transforms are not of the same array length"));
            return new TextureSegment[0];
        }
        TextureSegment[] segs = new TextureSegment[texts.length];
        for (int i = 0; i < texts.length; i++) {
            segs[i] = new TextureSegment(fragPos[i], UVs[i], relativeTransforms[i],GL_TRIANGLE_FAN, texts[i], normals[i]);
        }
        return segs;
    }

    public static GameObject createCharacterFull(Texture[] texts, Texture[] normals, Vector2f[][] fragPos, Vector2f[][] UVs, Transform[] relativeTransforms, ShaderProgram[] shaders, Identity[] ids){
        if(texts.length != normals.length || normals.length != relativeTransforms.length || texts.length != fragPos.length || fragPos.length != UVs.length)
        {
            Logger.log(new JE2Error("Character Creator: Provided textures, normals, and transforms are not of the same array length"));
            return new GameObject();
        }
        GameObject parent = new GameObject();
        GameObject[] children = new GameObject[texts.length];
        SpriteRenderer[] renderers = new SpriteRenderer[texts.length];
        for (int i = 0; i < texts.length; i++) {
            children[i] = new GameObject();
            renderers[i] = new SpriteRenderer(shaders[i]);
            renderers[i].setTexture(texts[i]);
            renderers[i].setNormalTexture(normals[i]);
            renderers[i].getTextureSegment().getVao().setVertices(fragPos[i]);
            renderers[i].getTextureSegment().getCoords().setVertices(UVs[i]);
            renderers[i].getTextureSegment().setRelativeTransform(relativeTransforms[i]);
            children[i].setIdentity(ids[i].name, ids[i].tag);
            children[i].addScript(renderers[i]);
            children[i].setParent(parent);
        }
        return parent;
    }
    public static TextureSegment[] createCharacter(Texture[] texts, Transform[] relativeTransforms){
        Texture[] normals = new Texture[texts.length];
        for (int i = 0; i < normals.length; i++) {
            normals[i] = new Texture();
        }
        Vector2f[][] points = new Vector2f[texts.length][];
        Arrays.fill(points, TextureSegment.squareSpritePoints);
        return createCharacter(texts,normals,points,points, relativeTransforms);
    }
    public static TextureSegment[] loadFromFile(Filepath fp){
        String[] file = DataLoader.readTextFile(fp);

        return null;
    }
}
