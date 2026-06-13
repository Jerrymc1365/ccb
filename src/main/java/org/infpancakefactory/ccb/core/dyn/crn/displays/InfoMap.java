package org.infpancakefactory.ccb.core.dyn.crn.displays;

import de.mrjulsen.crn.CreateRailwaysNavigator;
import de.mrjulsen.crn.block.blockentity.AdvancedDisplayBlockEntity;
import de.mrjulsen.crn.block.display.properties.PassengerInformationDetailedSettings;
import de.mrjulsen.crn.client.ber.AdvancedDisplayRenderInstance;
import de.mrjulsen.crn.client.ber.variants.AbstractAdvancedDisplayRenderer;
import de.mrjulsen.crn.config.ModClientConfig;
import de.mrjulsen.crn.data.train.portable.TrainStopDisplayData;
import de.mrjulsen.crn.util.ModUtils;
import de.mrjulsen.mcdragonlib.client.ber.BERLabel;
import de.mrjulsen.mcdragonlib.client.util.DLGraphics;
import de.mrjulsen.mcdragonlib.client.util.DLTexture;
import de.mrjulsen.mcdragonlib.client.util.RenderUtils;
import de.mrjulsen.mcdragonlib.util.DLColor;
import de.mrjulsen.mcdragonlib.util.TextUtils;
import de.mrjulsen.mcdragonlib.util.math.Point;
import de.mrjulsen.mcdragonlib.util.properties.NumberProperty;
import net.minecraft.ChatFormatting;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import org.infpancakefactory.ccb.core.dyn.crn.apis.Apis;
import org.infpancakefactory.ccb.core.dyn.crn.apis.Resources;
import org.infpancakefactory.ccb.core.dyn.crn.apis.Station;
import de.mrjulsen.mcdragonlib.client.ber.BERGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InfoMap implements AbstractAdvancedDisplayRenderer<PassengerInformationDetailedSettings> {

    public static ResourceLocation COLOR_ORGGRY = Resources.fromNamespaceAndPath(CreateRailwaysNavigator.MOD_ID, "textures/gui/map/orgw.png");
    public static ResourceLocation INLP = Resources.fromNamespaceAndPath(CreateRailwaysNavigator.MOD_ID, "textures/gui/map/inl4.png");

    ArrayList<Station> stations = new ArrayList<>();

    ArrayList<BERLabel> speedLabels = new ArrayList<>();
    ArrayList<BERLabel> exitSideLabels = new ArrayList<>();
    ArrayList<BERLabel> nextStationTextLabels = new ArrayList<>();

    ArrayList<BERLabel> stationLabels = new ArrayList<>();
    ArrayList<BERLabel> waitingAtStationLabels = new ArrayList<>();
    ArrayList<BERLabel> timeLabels = new ArrayList<>();

    float uv256 = 1f / 256f;

    public static class BERLabelIn {
        BERLabel label;
        public BERLabelIn(BERLabel label) {
            this.label = label;
        }
        public static BERLabelIn create() {
            return new BERLabelIn(new BERLabel());
        }

        public void render(DLGraphics graphics) {
            label.render(graphics);
        }

        public BERLabel getLabel() {
            return label;
        }
    }

    long lastGameTime = 0;
    int stat = 0;

    @Override
    public void update(Level level, BlockPos pos, BlockState state, AdvancedDisplayBlockEntity blockEntity, AdvancedDisplayRenderInstance parent, AdvancedDisplayBlockEntity.EUpdateReason data) {
        stations.clear();
        stationLabels.clear();
        waitingAtStationLabels.clear();
        timeLabels.clear();
        speedLabels.clear();
        exitSideLabels.clear();
        nextStationTextLabels.clear();
        addStations(blockEntity);
        renderStationText(blockEntity);
        renderWaitingAtStationText(blockEntity);
        renderSpeedLabel(blockEntity);
    }
    public void addStations(AdvancedDisplayBlockEntity blockEntity) {
        List<TrainStopDisplayData> allStops = blockEntity.getTrainData().getAllStops();

        for (int renderIndex = 0, realIndex = 0, allStopsSize = allStops.size(); renderIndex < allStopsSize; renderIndex++, realIndex++) {
            TrainStopDisplayData x = allStops.get(realIndex);
            ArrayList<Station> fakeStations = Apis.getFakeStations(x.getRealTimeStation().tagName());
            stations.add(new Station(x.getRealTimeStation().tagName(), "real_time_station", realIndex, renderIndex));
            for (int tempIndex = 1, getIndex = 0, fakeStationsSize = fakeStations.size(); tempIndex <= fakeStationsSize; tempIndex++, getIndex++) {
                Station station = fakeStations.get(getIndex);
                allStopsSize += 1;

                renderIndex += 1;
                station.setRenderIndex(renderIndex);
                station.setRealIndex(realIndex);

                stations.add(station);
            }
        }
    }
    public void renderSpeedLabel(AdvancedDisplayBlockEntity blockEntity) {
        var l1 = new BERLabel();
        l1.text.set(TextUtils.text((int) Math.abs(Math.round((ModUtils.calcSpeed(blockEntity.getTrainData().getSpeed(), ModClientConfig.SPEED_UNIT.get())))) + "").withStyle(ChatFormatting.BOLD))
        l1.position.set(Point.center(Point.of(blockEntity.getXSizeScaled() * 16f - 5f, 3f)));
        l1.verticalMaxScale.set(0.09f);
        l1.verticalMinScale.set(0.06f);
        l1.horizontalMaxScale.set(0.09f);
        var l2 = new BERLabel();
        l2.text.set(TextUtils.text("Speed (km / s)"));
        l2.position.set(Point.center(Point.of(blockEntity.getXSizeScaled() * 16f - 9f, 2.55f)));
        l2.verticalMaxScale.set(0.07f);
        l2.verticalMinScale.set(0.05f);
        l2.horizontalMaxScale.set(0.07f);

        speedLabels.add(l1);
        speedLabels.add(l2);
        int doorOpenSide = blockEntity.getTrainData().getNextStopExitSide().getAsByte();
        var l3 = new BERLabel();
        l3.text.set(TextUtils.text(doorOpenSide == 1 ? "右" : doorOpenSide == -1 ? "左" : ""))
        l3.verticalMaxScale.set(0.12f);
        l3.horizontalMaxScale.set(0.12f);
        l3.horizontalMinScale.set(0.04f);
        l3.preferredWidth.set(blockEntity.getXSizeScaled() * 6f);
        l3.position.set(Point.center(Point.of(blockEntity.getXSizeScaled() * 11.4F , 4f)));

        var l4 = new BERLabel();
        l4.text.set(TextUtils.text(doorOpenSide == 1 ? "Right" : doorOpenSide == -1 ? "Left" : ""))
        l4.setScale(0.06f, 0.04f);
        l4.setYScale(0.06f);
        l4.setCentered(true);
        l4.setMaxWidth(blockEntity.getXSizeScaled() * 6f, BERLabel.BoundsHitReaction.SCALE_SCROLL)
        .setPos(blockEntity.getXSizeScaled() * 11.4F , 5f);
        exitSideLabels.add(l3);
        exitSideLabels.add(l4);
    }
    public void renderWaitingAtStationText(BERGraphics<AdvancedDisplayBlockEntity> graphics, int light) {
        AdvancedDisplayBlockEntity blockEntity = graphics.blockEntity();
        RenderUtils.renderTexture(
                COLOR_ORGGRY,
                graphics,
                false,
                blockEntity.getXSizeScaled() * 2f,
                graphics.blockEntity().getYSizeScaled() * 8f - 1.5F,
                0.001f,
                3f,
                3F,
                uv256 * 0,
                uv256 * 0,
                uv256 * (256),
                uv256 * (256),
                graphics.blockEntity().getBlockState().getValue(HorizontalDirectionalBlock.FACING),
                (0xFF << 24) | (getDisplaySettings(graphics.blockEntity()).getFontColor() & 0x00FFFFFF),
                light
        );
    }
    public void renderWaitingAtStationText(AdvancedDisplayBlockEntity blockEntity) {
        if (stations.isEmpty()) return;
        Station station = stations.get(0);
        for (int i = stations.size() - 1; i >= 0; i--) {
            station = stations.get(i);
            if (station.getRealIndex() == blockEntity.getTrainData().getCurrentStopIndex()) {
                break;
            }
        }
        ArrayList<String> stationTexts = station.getStationTexts();
        waitingAtStationLabels.add(
                new BERLabel()
                        .setText(TextUtils.text(stationTexts.get(0)))
                        .setScale(0.15f, 0.03f)
                        .setYScale(0.15f)
                        .setCentered(true)
                        .setMaxWidth(blockEntity.getXSizeScaled() * 8 + 0.5f, BERLabel.BoundsHitReaction.SCALE_SCROLL)
                        .setPos(blockEntity.getXSizeScaled() * 4f,
                                blockEntity.getYSizeScaled() * 8f));
        if (stationTexts.size() > 1) {
            waitingAtStationLabels.add(
                    new BERLabel()
                            .setText(TextUtils.text(stationTexts.get(1)))
                            .setScale(0.1f, 0.05f)
                            .setYScale(0.1f)
                            .setCentered(true)
                            .setMaxWidth(blockEntity.getXSizeScaled() * 8 + 0.5f, BERLabel.BoundsHitReaction.SCALE_SCROLL)
                            .setPos(blockEntity.getXSizeScaled() * 4f,
                                    blockEntity.getYSizeScaled() * 8f - 0.5F));
        }
        ArrayList<String> goTos = station.getGoTos();
        for (int i = 0, goTosSize = goTos.size(); i < goTosSize; i++) {
            String text = goTos.get(i);
            waitingAtStationLabels.add(new BERLabel()
                    .setText(TextUtils.text(text))
                    .setScale(0.2f, 0.04f)
                    .setYScale(0.2f)
                    .setCentered(true)
                    .setMaxWidth(blockEntity.getXSizeScaled() + 2f, BERLabel.BoundsHitReaction.SCALE_SCROLL)
                    .setPos(blockEntity.getXSizeScaled() * 16f - 8f, blockEntity.getYSizeScaled() * 16 - 3f - i * 2f)
            );
        }
        if (!goTos.isEmpty()) {
            waitingAtStationLabels.add(new BERLabel()
                    .setText(TextUtils.translate("map.org.rj.mcm.ecrn.transfer_zh"))
                    .setScale(0.1f, 0.04f)
                    .setYScale(0.1f)
                    .setCentered(true)
                    .setMaxWidth(blockEntity.getXSizeScaled() + 2f, BERLabel.BoundsHitReaction.SCALE_SCROLL)
                    .setPos(blockEntity.getXSizeScaled() * 16f - 8f, blockEntity.getYSizeScaled() * 16 - 3.4f - goTos.size() * 1f)
            );
            waitingAtStationLabels.add(new BERLabel()
                    .setText(TextUtils.translate("map.org.rj.mcm.ecrn.transfer_en"))
                    .setScale(0.04f, 0.04f)
                    .setYScale(0.04f)
                    .setCentered(true)
                    .setMaxWidth(blockEntity.getXSizeScaled() + 2f, BERLabel.BoundsHitReaction.SCALE_SCROLL)
                    .setPos(blockEntity.getXSizeScaled() * 16f - 8f, blockEntity.getYSizeScaled() * 16 - 2.6f - goTos.size() * 1f)
            );
        }
    }
    public void renderStationText(AdvancedDisplayBlockEntity blockEntity) {

        float objectStart = 4F;
        float oneObject = (blockEntity.getXSizeScaled() * 16F - 8F) / (stations.size() -1 );
        float yPosBase = (blockEntity.getYSizeScaled() * 16) - 4f;

        stations.forEach(station -> {
            ArrayList<String> stationTexts = station.getStationTexts();
            if (stationTexts.isEmpty()) return;
            float yPos;// = yPosBase;
            if (station.getRenderIndex() % 2 == 0) {
                yPos = yPosBase + 0.4f;
            } else {
                yPos = yPosBase - 1f;
            }
            String color = "gry";
            if (station.getStatus().equals("real_time_station"))
                color = getColor(blockEntity.getTrainData().isWaitingAtStation(), blockEntity.getTrainData().getCurrentStopIndex(), station.getRealIndex());
            if (Objects.equals(color, "gld")) {
                stationLabels.add(
                        new BERLabel()
                                .setText(TextUtils.translate("map.org.rj.mcm.ecrn.next_station.next_zh", stationTexts.get(0)))
                                .setScale(0.1f, 0.04f)
                                .setYScale(0.1f)
                                .setCentered(false)
                                .setMaxWidth(blockEntity.getXSizeScaled() * 6f, BERLabel.BoundsHitReaction.SCALE_SCROLL)
                                .setPos(3f , 2.5f)
                );
            }
            stationLabels.add(
                    new BERLabel()
                            .setText(TextUtils.text(stationTexts.get(0)))
                            .setScale(0.07f, 0.03f)
                            .setYScale(0.07f)
                            .setCentered(true)
                            .setMaxWidth(blockEntity.getXSizeScaled() + 0.5f, BERLabel.BoundsHitReaction.SCALE_SCROLL)
                            .setPos(objectStart + oneObject * station.getRenderIndex() - (blockEntity.getXSizeScaled()) / 2f , yPos)
            );
            if (stationTexts.size() > 1) {
                stationLabels.add(
                        new BERLabel()
                                .setText(TextUtils.text(stationTexts.get(1)))
                                .setScale(0.04f, 0.01f)
                                .setYScale(0.04f)
                                .setCentered(true)
                                .setMaxWidth(blockEntity.getXSizeScaled() + 0.5f, BERLabel.BoundsHitReaction.SCALE_SCROLL)
                                .setPos(objectStart + oneObject * station.getRenderIndex() - (blockEntity.getXSizeScaled()) / 2f, yPos + 0.5f)
                );
                if (stationTexts.size() <= 1) {
                    stationTexts.add(stationTexts.get(0));
                }
                if (Objects.equals(color, "gld")) {
                    stationLabels.add(
                            new BERLabel()
                                    .setText(TextUtils.translate("map.org.rj.mcm.ecrn.next_station.next_en", stationTexts.get(1)))
                                    .setScale(0.06f, 0.01f)
                                    .setYScale(0.06f)
                                    .setCentered(false)
                                    .setMaxWidth(blockEntity.getXSizeScaled() * 6f, BERLabel.BoundsHitReaction.SCALE_SCROLL)
                                    .setPos(3f , 3.5f)
                    );
                }

            }
            ArrayList<String> goTos = station.getGoTos();
            for (int i = 0, goTosSize = goTos.size(); i < goTosSize; i++) {
                String text = goTos.get(i);
                stationLabels.add(new BERLabel()
                        .setText(TextUtils.text(text))
                        .setScale(0.04f, 0.01f)
                        .setYScale(0.04f)
                        .setBackground(ColorUtils.argb(256, 192, 256, 256), true)
                        .setCentered(true)
                        .setMaxWidth(blockEntity.getXSizeScaled() + 0.5f, BERLabel.BoundsHitReaction.SCALE_SCROLL)
                        .setPos(objectStart + oneObject * station.getRenderIndex() - (blockEntity.getXSizeScaled()) / 2f, yPos - 1.2f - i * 0.5f)
                );
            }
        });
    }

    @Override
    public void renderTick(float deltaTime) {}

    @Override
    public void render(BERGraphics<AdvancedDisplayBlockEntity> graphics, float partialTick, AdvancedDisplayRenderInstance parent, int light, boolean backSide) {
        if (!(graphics.blockEntity().getTrainData().isWaitingAtStation() && getDisplaySettings(graphics.blockEntity()).showStats())){
            renderLine(graphics, light);
            stationLabels.forEach(x -> {
                graphics.poseStack().pushPose();
                x.render(graphics, light);
                graphics.poseStack().popPose();
            });
            speedLabels.forEach(x -> x.render(graphics, light));
            exitSideLabels.forEach(x -> x.render(graphics, light));
        } else if (graphics.blockEntity().getTrainData().isWaitingAtStation() && getDisplaySettings(graphics.blockEntity()).showStats()) {
            renderWaitingAtStationText(graphics, light);
            waitingAtStationLabels.forEach(x -> x.render(graphics, light));
        }
        getDisplaySettings(graphics.blockEntity()).showDoNotBoardText();
        //graphics.blockEntity().getTrainData().getState().
    }

    public void renderLine(BERGraphics<AdvancedDisplayBlockEntity> graphics, int light) {
        float objectStart = 4F;
        float oneObject =  (graphics.blockEntity().getXSizeScaled() * 16F - 8F) / (stations.size() - 1);
        for (int i = 0, stationsSize = stations.size(); i < stationsSize; i++) {
            Station station = stations.get(i);
            boolean isFakeStation = false;
            String color = getColor(graphics.blockEntity().getTrainData().isWaitingAtStation(), graphics.blockEntity().getTrainData().getCurrentStopIndex() ,station.getRealIndex());
            String color2 = "gry";
            if (i + 1 < stationsSize) {
                color2 = getColor(false, graphics.blockEntity().getTrainData().getCurrentStopIndex() + 1, station.getRealIndex() + 1);
            }

            ResourceLocation inlstation;
            if (color.equals("gld")) {
                inlstation = COLOR_ORGGRY;
            } else if (color.equals("grn")) {
                inlstation = Resources.COLOR_GRN;
            } else {
                inlstation = Resources.COLOR_GRY;
            }
            if (Objects.equals(station.getStatus(), "fake_station")) {
                isFakeStation = true;
                inlstation = Resources.COLOR_RED;
            }
            if (i != 0) {
                RenderUtils.renderTexture(
                        !Objects.equals(color, "gry") ? Resources.LINE : !(isFakeStation && graphics.blockEntity().getTrainData().isWaitingAtStation() && !Objects.equals(color2, "gry")) ? Resources.LINEU : Resources.LINE,
                        graphics,
                        false,
                        0.25F + objectStart + (i - 1F) * oneObject,
                        (graphics.blockEntity().getYSizeScaled() * 16) - 4f,
                        0.0f,
                        oneObject,
                        0.35F,
                        uv256 * 0,
                        uv256 * 0,
                        uv256 * 256,
                        uv256 * 256,// 0 + 256
                        graphics.blockEntity().getBlockState().getValue(HorizontalDirectionalBlock.FACING),
                        (0xFF << 24) | (getDisplaySettings(graphics.blockEntity()).getFontColor() & 0x00FFFFFF),
                        light
                );
                RenderUtils.renderTexture(
                        Objects.equals(color, "gld") ? !isFakeStation ? INLP : Resources.INLGRN : Objects.equals(color, "grn") ? Resources.INLGRN : Resources.INL0,
                        graphics,
                        false,
                        objectStart + oneObject / 2 + (i - 1F) * (oneObject),
                        (graphics.blockEntity().getYSizeScaled() * 16) - 4f,
                        0.001f,
                        0.3f,
                        0.3F,
                        uv256 * 0,
                        uv256 * 0,
                        uv256 * 256,
                        uv256 * 256,
                        graphics.blockEntity().getBlockState().getValue(HorizontalDirectionalBlock.FACING),
                        (0xFF << 24) | (getDisplaySettings(graphics.blockEntity()).getFontColor() & 0x00FFFFFF),
                        light
                );
            }

            RenderUtils.renderTexture(
                    inlstation,
                    graphics,
                    new Vector3f(
                            objectStart + i * oneObject,
                            (graphics.blockEntity().getYSizeScaled() * 16) - 4.15f,
                            0.001f
                    ),
                    0.5f,
                    0.5F,
                    uv256 * 0f,
                    uv256 * 0f,
                    uv256 * (256f),
                    uv256 * (256f),
                    graphics.blockEntity().getBlockState().getValue(HorizontalDirectionalBlock.FACING),
                    DLColor.fromInt((0xFF << 24) | (getDisplaySettings(graphics.blockEntity()).getFontColor().getAsARGB() & 0x00FFFFFF)),
                    light,
                    false
            );
        }
    }

    @Override
    public void tick(Level level, BlockPos pos, BlockState state, AdvancedDisplayBlockEntity pBlockEntity, AdvancedDisplayRenderInstance parent) {
        changeIcon(level);
    }

    public String getColor(AdvancedDisplayBlockEntity be, int currentStopIndex, int currentRenderIndex) {
        if (currentRenderIndex < currentStopIndex || (currentRenderIndex == currentStopIndex && be.getTrainData().isWaitingAtStation())) {
            return "gry";
        } else if (currentRenderIndex == currentStopIndex) {
            return "gld";
        }
        return "grn";
    }
    public String getColor(boolean isWaitingAtStation, int currentStopIndex, int currentRenderIndex) {
        if (currentRenderIndex < currentStopIndex || (currentRenderIndex == currentStopIndex && isWaitingAtStation)) {
            return "gry";
        } else if (currentRenderIndex == currentStopIndex) {
            return "gld";
        }
        return "grn";
    }
    public void changeIcon(Level level) {
        long cGameTime = level.getGameTime();
        if (lastGameTime - cGameTime == 10 || cGameTime % 10f == 0f) {
            stat += 1;
        } else if (lastGameTime - cGameTime > 10) {
            lastGameTime = level.getGameTime();
            stat += 1;
        }
        switch (stat) {
            case 0:
                INLP = Resources.INL0;
                COLOR_ORGGRY = Resources.COLOR_GRY;
                break;
            case 1:
                INLP = Resources.INL1;
                COLOR_ORGGRY = Resources.COLOR_ORG;
                break;
            case 2:
                INLP = Resources.INL2;
                COLOR_ORGGRY = Resources.COLOR_GRY;
                break;
            case 3:
                INLP = Resources.INL3;
                COLOR_ORGGRY = Resources.COLOR_ORG;
                break;
            case 4:
                INLP = Resources.INL4;
                COLOR_ORGGRY = Resources.COLOR_GRY;
                break;
            case 5:
                INLP = Resources.INL0;
                COLOR_ORGGRY = Resources.COLOR_ORG;
                break;
            default:
                stat = 0;
        }
    }
}

