package com.lx862.jcm.mod.data.pids.preset;

import com.lx862.jcm.mod.Constants;
import com.lx862.jcm.mod.block.entity.PIDSBlockEntity;
import com.lx862.jcm.mod.config.ConfigEntry;
import com.lx862.jcm.mod.data.pids.preset.components.*;
import com.lx862.jcm.mod.data.pids.preset.components.base.DrawCall;
import com.lx862.jcm.mod.data.pids.preset.components.base.TextComponent;
import com.lx862.jcm.mod.data.pids.preset.components.base.TextureComponent;
import com.lx862.jcm.mod.render.RenderHelper;
import com.lx862.jcm.mod.render.TextOverflowMode;
import com.lx862.jcm.mod.render.text.TextAlignment;
import com.lx862.jcm.mod.render.text.TextRenderingManager;
import org.mtr.core.operation.ArrivalsResponse;
import org.mtr.mapping.holder.Direction;
import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.holder.RenderLayer;
import org.mtr.mapping.holder.World;
import org.mtr.mapping.mapper.GraphicsHolder;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LCDPIDSPreset extends PIDSPresetBase {
    private static final int PIDS_MARGIN = 5;
    private static final float ARRIVAL_TEXT_SCALE = 1.3F;
    public LCDPIDSPreset() {
        super("lcd_pids", "Hong Kong LCD PIDS", true);
    }

    public void render(PIDSBlockEntity be, GraphicsHolder graphicsHolder, World world, Direction facing, ArrivalsResponse arrivals, boolean[] rowHidden, float tickDelta, int x, int y, int width, int height) {
        int startX = PIDS_MARGIN;
        int contentWidth = width - (PIDS_MARGIN * 2);
        // Draw Background
        graphicsHolder.createVertexConsumer(RenderLayer.getText(getBackground()));
        RenderHelper.drawTexture(graphicsHolder, getBackground(), 0, 0, 0, width, height, facing, ARGB_WHITE, MAX_RENDER_LIGHT);

        // Debug View Texture
        if(ConfigEntry.DEBUG_MODE.getBool() && ConfigEntry.NEW_TEXT_RENDERER.getBool()) {
            //TextureTextRenderer.stressTest(5);
            drawAtlasBackground(graphicsHolder, width, height, facing);
        }

        graphicsHolder.translate(startX, 0, -0.5);

        List<DrawCall> components = getComponents(arrivals, be.getCustomMessages(), rowHidden, 0, 6, contentWidth, height - 2, be.getRowAmount(), be.platformNumberHidden());
        List<DrawCall> textureComponents = components.stream().filter(e -> e instanceof TextureComponent).collect(Collectors.toList());
        List<DrawCall> textComponents = components.stream().filter(e -> e instanceof TextComponent).collect(Collectors.toList());

        // Texture
        for(DrawCall component : textureComponents) {
            graphicsHolder.push();
            component.render(graphicsHolder, null, world, facing);
            graphicsHolder.pop();
        }

        // Text
        graphicsHolder.translate(0, 0, -0.5);
        TextRenderingManager.bind(graphicsHolder);
        for(DrawCall component : textComponents) {
            graphicsHolder.push();
            component.render(graphicsHolder, null, world, facing);
            graphicsHolder.pop();
        }
    }

    @Override
    public List<DrawCall> getComponents(ArrivalsResponse arrivals, String[] customMessages, boolean[] rowHidden, int x, int y, int screenWidth, int screenHeight, int rows, boolean hidePlatform) {
        List<DrawCall> components = new ArrayList<>();

        /* Arrivals */
        int arrivalIndex = 0;
        double rowY = y;
        for(int i = 0; i < rows; i++) {
            if(arrivalIndex >= arrivals.getArrivals().size()) continue;

            if(!customMessages[i].isEmpty()) {
                components.add(new CustomTextComponent(getFont(), TextOverflowMode.STRETCH, TextAlignment.LEFT, getTextColor(), customMessages[i], x, rowY, 78 * ARRIVAL_TEXT_SCALE, 10, ARRIVAL_TEXT_SCALE));
            } else {
                if(!rowHidden[i]) {
                    float destinationMaxWidth = !hidePlatform ? (44 * ARRIVAL_TEXT_SCALE) : (54 * ARRIVAL_TEXT_SCALE);
                    components.add(new DestinationComponent(arrivals, TextOverflowMode.STRETCH, TextAlignment.LEFT, arrivalIndex, getFont(), getTextColor(), x, rowY, destinationMaxWidth, 10, ARRIVAL_TEXT_SCALE));
                    components.add(new ETAComponent(arrivals, TextOverflowMode.STRETCH, TextAlignment.RIGHT, arrivalIndex, getFont(), getTextColor(), screenWidth, rowY, 22 * ARRIVAL_TEXT_SCALE, 20, ARRIVAL_TEXT_SCALE));
                    arrivalIndex++;
                }
            }

            rowY += (screenHeight / 5.25) * ARRIVAL_TEXT_SCALE;
        }
        return components;
    }

    @Override
    public String getFont() {
        return "jsblock:pids_lcd";
    }

    @Override
    public @Nonnull Identifier getBackground() {
        return new Identifier(Constants.MOD_ID, "textures/block/pids/black.png");
    }

    @Override
    public int getTextColor() {
        return 0xFFEADD9A;
    }

    @Override
    public boolean isRowHidden(int row) {
        return false;
    }
}
