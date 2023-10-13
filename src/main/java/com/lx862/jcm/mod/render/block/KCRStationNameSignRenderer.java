package com.lx862.jcm.mod.render.block;

import com.lx862.jcm.mod.block.entity.KCRStationNameSignBlockEntity;
import org.mtr.mapping.mapper.GraphicsHolder;

public class KCRStationNameSignRenderer extends JCMBlockEntityRenderer<KCRStationNameSignBlockEntity> {
    public KCRStationNameSignRenderer(Argument argument) {
        super(argument);
    }

    @Override
    public void render(KCRStationNameSignBlockEntity blockEntity, float tickDelta, GraphicsHolder graphicsHolder, int light, int i1) {
        graphicsHolder.push();
        scaleCentered(graphicsHolder, 0.018F, 0.018F, 0.018F);
        rotateToBlockDirection(graphicsHolder, blockEntity);
        // TODO: Render with IDrawing
        graphicsHolder.pop();
    }
}