package org.JE.JE2.UI;

import org.JE.JE2.Annotations.GLThread;
import org.JE.JE2.IO.FileInput.IOUtil;
import org.JE.JE2.IO.Logging.Errors.IOError;
import org.JE.JE2.IO.Logging.Logger;
import org.JE.JE2.Manager;
import org.lwjgl.BufferUtils;
import org.lwjgl.nuklear.NkUserFont;
import org.lwjgl.nuklear.NkUserFontGlyph;
import org.lwjgl.stb.STBTTAlignedQuad;
import org.lwjgl.stb.STBTTFontinfo;
import org.lwjgl.stb.STBTTPackContext;
import org.lwjgl.stb.STBTTPackedchar;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.nuklear.Nuklear.NK_UTF_INVALID;
import static org.lwjgl.nuklear.Nuklear.nnk_utf_decode;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_UNSIGNED_INT_8_8_8_8_REV;
import static org.lwjgl.stb.STBTruetype.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.system.MemoryUtil.memFree;

public class Font {
    private NkUserFont nkFont = NkUserFont.create();
    private ByteBuffer fontData;
    public boolean created = false;
    public Font(String filepath, boolean createNow){
        try {
            fontData = IOUtil.ioResourceToByteBuffer(filepath, 512*1024);
        }
        catch (Exception e){
            Logger.log(new IOError("Error loading font: " + filepath));
            fontData = BufferUtils.createByteBuffer(1);
        }
        if(!createNow)
        {
            Manager.queueGLFunction(this::generateFont);
        }
        else {
            generateFont();
        }
    }

    public Font(byte[] data, boolean createNow){
        try {
            fontData = BufferUtils.createByteBuffer(data.length);
            fontData.put(data);
            fontData.flip();
        }
        catch (Exception e){
            Logger.log(new IOError("Error loading font from byte buffer. Size: " + data.length));
            fontData = BufferUtils.createByteBuffer(1);
        }
        if(!createNow)
        {
            Manager.queueGLFunction(this::generateFont);
        }
        else {
            generateFont();
        }
    }

    @GLThread
    private void generateFont(){
        int BITMAP_W = 1024;
        int BITMAP_H = 1024;

        int FONT_HEIGHT = 18;
        int fontTexID   = glGenTextures();

        STBTTFontinfo fontInfo = STBTTFontinfo.create();
        STBTTPackedchar.Buffer cdata    = STBTTPackedchar.create(95);

        float scale;
        float descent;

        try (MemoryStack stack = stackPush()) {
            stbtt_InitFont(fontInfo, fontData);
            scale = stbtt_ScaleForPixelHeight(fontInfo, FONT_HEIGHT);

            IntBuffer d = stack.mallocInt(1);
            stbtt_GetFontVMetrics(fontInfo, null, d, null);
            descent = d.get(0) * scale;

            ByteBuffer bitmap = memAlloc(BITMAP_W * BITMAP_H);

            STBTTPackContext pc = STBTTPackContext.malloc(stack);
            stbtt_PackBegin(pc, bitmap, BITMAP_W, BITMAP_H, 0, 1, NULL);
            stbtt_PackSetOversampling(pc, 4, 4);
            stbtt_PackFontRange(pc, fontData, 0, FONT_HEIGHT, 32, cdata);
            stbtt_PackEnd(pc);

            // Convert R8 to RGBA8
            ByteBuffer texture = memAlloc(BITMAP_W * BITMAP_H * 4);
            for (int i = 0; i < bitmap.capacity(); i++) {
                texture.putInt((bitmap.get(i) << 24) | 0x00FFFFFF);
            }
            texture.flip();

            glBindTexture(GL_TEXTURE_2D, fontTexID);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, BITMAP_W, BITMAP_H, 0, GL_RGBA, GL_UNSIGNED_INT_8_8_8_8_REV, texture);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

            memFree(texture);
            memFree(bitmap);
        }
        nkFont.width((handle, h, text, len) -> {
            float text_width = 0;
            try (MemoryStack stack = stackPush()) {
                IntBuffer unicode = stack.mallocInt(1);

                int glyph_len = nnk_utf_decode(text, memAddress(unicode), len);
                int text_len  = glyph_len;

                if (glyph_len == 0) {
                    return 0;
                }

                IntBuffer advance = stack.mallocInt(1);
                while (text_len <= len && glyph_len != 0) {
                    if (unicode.get(0) == NK_UTF_INVALID) {
                        break;
                    }

                    /* query currently drawn glyph information */
                    stbtt_GetCodepointHMetrics(fontInfo, unicode.get(0), advance, null);
                    text_width += advance.get(0) * scale;

                    /* offset next glyph */
                    glyph_len = nnk_utf_decode(text + text_len, memAddress(unicode), len - text_len);
                    text_len += glyph_len;
                }
            }
            return text_width;
        })
                .height(FONT_HEIGHT)
                .query((handle, font_height, glyph, codepoint, next_codepoint) -> {
                    try (MemoryStack stack = stackPush()) {
                        FloatBuffer x = stack.floats(0.0f);
                        FloatBuffer y = stack.floats(0.0f);

                        STBTTAlignedQuad q       = STBTTAlignedQuad.malloc(stack);
                        IntBuffer        advance = stack.mallocInt(1);

                        stbtt_GetPackedQuad(cdata, BITMAP_W, BITMAP_H, codepoint - 32, x, y, q, false);
                        stbtt_GetCodepointHMetrics(fontInfo, codepoint, advance, null);

                        NkUserFontGlyph ufg = NkUserFontGlyph.create(glyph);

                        ufg.width(q.x1() - q.x0());
                        ufg.height(q.y1() - q.y0());
                        ufg.offset().set(q.x0(), q.y0() + (FONT_HEIGHT + descent));
                        ufg.xadvance(advance.get(0) * scale);
                        ufg.uv(0).set(q.s0(), q.t0());
                        ufg.uv(1).set(q.s1(), q.t1());
                    }
                })
                .texture(it -> it
                        .id(fontTexID));
        created = true;
    }

    public NkUserFont getFont(){
        return nkFont;
    }
    public ByteBuffer getFontData(){
        return fontData;
    }
}
