package org.JE.JE2.UI.UIElements;

import org.JE.JE2.UI.UIElements.Style.Color;
import org.JE.JE2.UI.UIScaler;
import org.JE.JE2.Utility.Settings.Setting;
import org.JE.JE2.Window.UIHandler;
import org.joml.sampling.Callback2d;
import org.lwjgl.BufferUtils;
import org.lwjgl.nuklear.NkPluginFilter;
import org.lwjgl.nuklear.NkPluginFilterI;
import org.lwjgl.nuklear.NkTextEdit;
import org.lwjgl.nuklear.Nuklear;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.charset.StandardCharsets;

import static org.lwjgl.nuklear.Nuklear.*;

public class TextField extends UIElement {
    private final int maxLength;
    private ByteBuffer content;
    private String prevString = "";
    private NkPluginFilterI filter;
    private IntBuffer length;
    public ElementEventChanged<String> eventChanged = null;
    public Setting<String> watch = null;
    private String title = null;

    private int height;

    public TextField(int maxLength) {
        this.maxLength = maxLength;
        content = BufferUtils.createByteBuffer(maxLength + 1);
        length = BufferUtils.createIntBuffer(1);
        length.put(0);
        filter = NkPluginFilter.create(Nuklear::nnk_filter_ascii);
        height = 32;
    }

    public TextField(int maxLength, int height) {
        this.maxLength = maxLength;
        content = BufferUtils.createByteBuffer(maxLength + 1);
        length = BufferUtils.createIntBuffer(1);
        length.put(0);
        filter = NkPluginFilter.create(Nuklear::nnk_filter_ascii);
        this.height = height;
    }

    public TextField(int maxLength, int height, String defaultValue) {
        this.maxLength = maxLength;
        content = BufferUtils.createByteBuffer(maxLength + 1);
        length = BufferUtils.createIntBuffer(1);
        length.put(0);
        filter = NkPluginFilter.create(Nuklear::nnk_filter_ascii);
        this.height = height;
        setValue(defaultValue);
    }
    public TextField(int maxLength, int height, String defaultValue, String title) {
        this.maxLength = maxLength;
        content = BufferUtils.createByteBuffer(maxLength + 1);
        length = BufferUtils.createIntBuffer(1);
        length.put(0);
        filter = NkPluginFilter.create(Nuklear::nnk_filter_ascii);
        this.height = height;
        setValue(defaultValue);
        this.title = title;
    }
    public TextField(int maxLength, int height, String defaultValue, String title, Setting<String> bindedSetting) {
        this.maxLength = maxLength;
        content = BufferUtils.createByteBuffer(maxLength + 1);
        length = BufferUtils.createIntBuffer(1);
        length.put(0);
        filter = NkPluginFilter.create(Nuklear::nnk_filter_ascii);
        this.height = height;
        setValue(defaultValue);
        this.watch = bindedSetting;
        this.title = title;
    }

    @Override
    protected void render() {
        if(watch != null)
        {
            setValue(watch.getValue());
        }
        // This hacky fix is in place because nuklear library doesn't like non-null terminated strings
        content.flip();
        content.limit(maxLength);
        if (content.get(content.limit()-1) != 0) {
            content.put(length.get(0), (byte) 0);
        }
        length.rewind();
        if(title != null){
            nk_label_colored(UIHandler.nuklearContext, title, NK_TEXT_ALIGN_LEFT, Color.WHITE.nkColor());
        }

        prevString = getValue();

        if(style.font.created)
        {
            nk_style_set_font(UIHandler.nuklearContext, style.font.getFont());
        }

        int options = NK_EDIT_SELECTABLE | NK_EDIT_MULTILINE | NK_EDIT_CLIPBOARD | NK_EDIT_ALLOW_TAB | NK_EDIT_SIG_ENTER | NK_EDIT_AUTO_SELECT | NK_EDIT_ALWAYS_INSERT_MODE | NK_EDIT_CTRL_ENTER_NEWLINE;

        nk_edit_string(UIHandler.nuklearContext, options, content, length, maxLength, filter);

        if(!getValue().equals(prevString))
        {
            if (eventChanged != null) {
                eventChanged.run(getValue());
            }
            if(watch != null)
                watch.setValue(getValue());
        }

    }

    public static String get(ByteBuffer input){
        byte[] bytes = new byte[input.limit()];
        input.get(bytes, 0, input.limit());
        return new String(bytes, StandardCharsets.US_ASCII).replace("\0", "");
    }

    public String getValue() {
        content.mark();
        byte[] bytes = new byte[length.get(0)];
        content.get(bytes, 0, length.get(0));
        content.reset();
        return new String(bytes, StandardCharsets.US_ASCII).replace("\0", "");
    }
    public void setValue(String value) {
        // convert string to bytes
        byte[] bytes = value.getBytes(StandardCharsets.US_ASCII);
        content.clear();
        content.put(bytes);
        content.put((byte) 0);
        content.flip();
        length.put(0, bytes.length);


    }
}