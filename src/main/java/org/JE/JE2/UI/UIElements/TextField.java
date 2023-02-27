package org.JE.JE2.UI.UIElements;

import org.JE.JE2.Window.UIHandler;
import org.lwjgl.BufferUtils;
import org.lwjgl.nuklear.NkPluginFilter;
import org.lwjgl.nuklear.NkPluginFilterI;
import org.lwjgl.nuklear.Nuklear;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.charset.StandardCharsets;

import static org.lwjgl.nuklear.Nuklear.*;

public class TextField extends UIElement {
    private final int maxLength;
    private ByteBuffer content;
    private NkPluginFilterI filter;
    private IntBuffer length;

    public TextField(int maxLength) {
        this.maxLength = maxLength;
        content = BufferUtils.createByteBuffer(maxLength + 1);
        length = BufferUtils.createIntBuffer(1);
        filter = NkPluginFilter.create(Nuklear::nnk_filter_ascii);
    }

    @Override
    protected void render() {
        nk_edit_string(UIHandler.nuklearContext, NK_EDIT_FIELD, content, length, maxLength, filter);
    }

    public String getValue() {
        content.mark();
        byte[] bytes = new byte[length.get(0)];
        content.get(bytes, 0, length.get(0));
        content.reset();
        return new String(bytes, StandardCharsets.US_ASCII);
    }
    public void setValue(String value) {
        content.clear();
        content.put(value.getBytes());
        content.flip();
        length.put(0,value.length());
    }
}