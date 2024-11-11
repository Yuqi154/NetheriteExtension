package com.iafenvoy.netherite.client.gui.screen;

import com.google.common.collect.Lists;
import com.iafenvoy.netherite.NetheriteExtension;
import com.iafenvoy.netherite.network.UpdateNetheriteBeaconC2SPacket;
import com.iafenvoy.netherite.screen.NetheriteBeaconScreenHandler;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.architectury.networking.NetworkManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerListener;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Optional;

public class NetheriteBeaconScreen extends HandledScreen<NetheriteBeaconScreenHandler> {
    public static final RegistryEntry<StatusEffect>[][] EFFECTS_BY_LEVEL = new RegistryEntry[][]{
            {StatusEffects.SPEED, StatusEffects.HASTE},
            {StatusEffects.RESISTANCE, StatusEffects.JUMP_BOOST},
            {StatusEffects.STRENGTH},
            {StatusEffects.REGENERATION},
            {StatusEffects.GLOWING}
    };
    private static final Identifier TEXTURE = Identifier.of(NetheriteExtension.MOD_ID, "textures/gui/container/netherite_beacon.png");
    private static final Text PRIMARY_TEXT = Text.translatable("block.minecraft.beacon.primary");
    private static final Text SECONDARY_TEXT = Text.translatable("block.minecraft.beacon.secondary");
    private static final Text TERTIARY_TEXT = Text.translatable("block.netherite_ext.netherite_beacon.tertiary");
    private final List<BeaconButtonWidget> buttons = Lists.newArrayList();
    private RegistryEntry<StatusEffect> primaryEffect;
    private RegistryEntry<StatusEffect> secondaryEffect;
    private RegistryEntry<StatusEffect> tertiaryEffect;

    public NetheriteBeaconScreen(final NetheriteBeaconScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundWidth = 230;
        this.backgroundHeight = 219;
        handler.addListener(new ScreenHandlerListener() {
            @Override
            public void onSlotUpdate(ScreenHandler _handler, int slotId, ItemStack stack) {
            }

            @Override
            public void onPropertyUpdate(ScreenHandler _handler, int property, int value) {
                NetheriteBeaconScreen.this.primaryEffect = handler.getPrimaryEffect();
                NetheriteBeaconScreen.this.secondaryEffect = handler.getSecondaryEffect();
                NetheriteBeaconScreen.this.tertiaryEffect = handler.getTertiaryEffect();
            }
        });
    }

    private <T extends ClickableWidget & BeaconButtonWidget> void addButton(T button) {
        this.addDrawableChild(button);
        this.buttons.add(button);
    }

    @Override
    protected void init() {
        super.init();
        this.buttons.clear();

        this.addButton(new DoneButtonWidget(this.x + 164, this.y + 107));
        this.addButton(new CancelButtonWidget(this.x + 190, this.y + 107));

        for (int mainEffectIndex = 0; mainEffectIndex <= 2; ++mainEffectIndex) {
            int levelEffectCount = EFFECTS_BY_LEVEL[mainEffectIndex].length;
            int spacing = levelEffectCount * 22 + (levelEffectCount - 1) * 2;

            for (int levelEffectIndex = 0; levelEffectIndex < levelEffectCount; ++levelEffectIndex) {
                RegistryEntry<StatusEffect> effect = EFFECTS_BY_LEVEL[mainEffectIndex][levelEffectIndex];
                EffectButtonWidget widget = new EffectButtonWidget(this.x + 76 + levelEffectIndex * 24 - spacing / 2, this.y + 22 + mainEffectIndex * 25, effect, 0, mainEffectIndex);
                widget.active = false;
                this.addButton(widget);
            }
        }

        int additionalEffectsStartIndex = 3;
        for (int additionalEffectIndex = additionalEffectsStartIndex; additionalEffectIndex < EFFECTS_BY_LEVEL.length; additionalEffectIndex++) {
            int levelEffectCount = EFFECTS_BY_LEVEL[additionalEffectIndex].length + 1;
            int spacing = levelEffectCount * 22 + (levelEffectCount - 1) * 2;

            for (int levelEffectIndex = 0; levelEffectIndex < levelEffectCount - 1; ++levelEffectIndex) {
                RegistryEntry<StatusEffect> effect = EFFECTS_BY_LEVEL[additionalEffectIndex][levelEffectIndex];
                EffectButtonWidget widget = new EffectButtonWidget(this.x + 175 + levelEffectIndex * 24 - spacing / 2, this.y + 22 + (additionalEffectIndex - additionalEffectsStartIndex) * 50, effect, (additionalEffectIndex - additionalEffectsStartIndex) + 1, 3);
                widget.active = false;
                this.addButton(widget);
            }

            EffectButtonWidget widget = new AdditionalEffectButtonWidget(this.x + 175 + (levelEffectCount - 1) * 24 - spacing / 2, this.y + 22 + (additionalEffectIndex - additionalEffectsStartIndex) * 50, (additionalEffectIndex - additionalEffectsStartIndex) + 1, EFFECTS_BY_LEVEL[0][0]);
            widget.visible = false;
            this.addButton(widget);
        }
    }

    @Override
    public void handledScreenTick() {
        super.handledScreenTick();
        this.tickButtons();
    }

    void tickButtons() {
        int i = this.handler.getProperties();
        this.buttons.forEach(button -> button.tick(i));
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        context.drawCenteredTextWithShadow(this.textRenderer, PRIMARY_TEXT, 62, 10, 14737632);
        context.drawCenteredTextWithShadow(this.textRenderer, SECONDARY_TEXT, 169, 10, 14737632);
        context.drawCenteredTextWithShadow(this.textRenderer, TERTIARY_TEXT, 169, 58, 14737632);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int i = (this.width - this.backgroundWidth) / 2;
        int j = (this.height - this.backgroundHeight) / 2;
        context.drawTexture(TEXTURE, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);
        context.getMatrices().push();
        context.getMatrices().translate(0.0F, 0.0F, 100.0F);
        context.drawItem(new ItemStack(Items.NETHERITE_INGOT), i + 42 + 66, j + 109);
        context.getMatrices().pop();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(context, mouseX, mouseY);
    }

    @Environment(EnvType.CLIENT)
    interface BeaconButtonWidget {
        void tick(int level);
    }

    @Environment(EnvType.CLIENT)
    abstract static class IconButtonWidget extends BaseButtonWidget {
        private final int u;
        private final int v;

        protected IconButtonWidget(int x, int y, int u, int v, Text text) {
            super(x, y, text);
            this.u = u;
            this.v = v;
        }

        @Override
        protected void renderExtra(DrawContext graphics) {
            graphics.drawTexture(TEXTURE, this.getX() + 2, this.getY() + 2, this.u, this.v, 18, 18);
        }
    }

    @Environment(EnvType.CLIENT)
    abstract static class BaseButtonWidget extends PressableWidget implements BeaconButtonWidget {
        private boolean disabled;

        protected BaseButtonWidget(int x, int y) {
            super(x, y, 22, 22, ScreenTexts.EMPTY);
        }

        protected BaseButtonWidget(int x, int y, Text text) {
            super(x, y, 22, 22, text);
        }

        @Override
        public void render(DrawContext graphics, int mouseX, int mouseY, float delta) {
            RenderSystem.setShaderTexture(0, NetheriteBeaconScreen.TEXTURE);
            int uStart = 0;
            if (!this.active) uStart += this.width * 2;
            else if (this.disabled) uStart += this.width;
            else if (this.isHovered()) uStart += this.width * 3;
            graphics.drawTexture(TEXTURE, this.getX(), this.getY(), uStart, 219, this.width, this.height);
            this.renderExtra(graphics);
        }

        protected abstract void renderExtra(DrawContext graphics);

        public boolean isDisabled() {
            return this.disabled;
        }

        public void setDisabled(boolean disabled) {
            this.disabled = disabled;
        }

        @Override
        protected void appendClickableNarrations(NarrationMessageBuilder builder) {
            this.appendDefaultNarrations(builder);
        }
    }

    @Environment(EnvType.CLIENT)
    class CancelButtonWidget extends IconButtonWidget {
        public CancelButtonWidget(int x, int y) {
            super(x, y, 112, 220, ScreenTexts.CANCEL);
        }

        @Override
        public void onPress() {
            assert NetheriteBeaconScreen.this.client != null && NetheriteBeaconScreen.this.client.player != null;
            NetheriteBeaconScreen.this.client.player.closeHandledScreen();
        }

        @Override
        public void tick(int level) {
        }
    }

    @Environment(EnvType.CLIENT)
    class DoneButtonWidget extends IconButtonWidget {
        public DoneButtonWidget(int x, int y) {
            super(x, y, 90, 220, ScreenTexts.DONE);
        }

        @Override
        public void onPress() {
            NetworkManager.sendToServer(new UpdateNetheriteBeaconC2SPacket(Optional.ofNullable(NetheriteBeaconScreen.this.primaryEffect),
                    Optional.ofNullable(NetheriteBeaconScreen.this.secondaryEffect),
                    Optional.ofNullable(NetheriteBeaconScreen.this.tertiaryEffect)));
        }

        @Override
        public void tick(int level) {
            this.active = NetheriteBeaconScreen.this.handler.hasPayment() && NetheriteBeaconScreen.this.primaryEffect != null;
        }
    }

    @Environment(EnvType.CLIENT)
    class EffectButtonWidget extends BaseButtonWidget {
        protected final int effectIndex;
        private final int level;
        private RegistryEntry<StatusEffect> effect;
        private Sprite sprite;

        public EffectButtonWidget(int x, int y, RegistryEntry<StatusEffect> statusEffect, int effectIndex, int level) {
            super(x, y);
            this.effectIndex = effectIndex;
            this.level = level;
            this.init(statusEffect);
        }

        protected void init(RegistryEntry<StatusEffect> statusEffect) {
            this.effect = statusEffect;
            this.sprite = MinecraftClient.getInstance().getStatusEffectSpriteManager().getSprite(statusEffect);
        }

        protected MutableText getEffectName(StatusEffect statusEffect) {
            return Text.translatable(statusEffect.getTranslationKey());
        }

        public void onPress() {
            if (!this.isDisabled()) {
                switch (this.effectIndex) {
                    case 0 -> NetheriteBeaconScreen.this.primaryEffect = this.effect;
                    case 1 -> NetheriteBeaconScreen.this.secondaryEffect = this.effect;
                    case 2 -> NetheriteBeaconScreen.this.tertiaryEffect = this.effect;
                    default -> throw new RuntimeException("Unknown Netherite Beacon effect index");
                }
                NetheriteBeaconScreen.this.tickButtons();
            }
        }

        @Override
        protected void renderExtra(DrawContext context) {
            RenderSystem.setShaderTexture(0, this.sprite.getAtlasId());
            context.drawSprite(this.getX() + 2, this.getY() + 2, 0, 18, 18, this.sprite);
        }

        @Override
        public void tick(int level) {
            this.active = this.level < level;
            this.setDisabled(this.effect == switch (this.effectIndex) {
                case 0 -> NetheriteBeaconScreen.this.primaryEffect;
                case 1 -> NetheriteBeaconScreen.this.secondaryEffect;
                case 2 -> NetheriteBeaconScreen.this.tertiaryEffect;
                default -> throw new RuntimeException("Unknown Netherite Beacon effect index");
            });
        }

        protected MutableText getNarrationMessage() {
            return this.getEffectName(this.effect.value());
        }
    }

    @Environment(EnvType.CLIENT)
    class AdditionalEffectButtonWidget extends EffectButtonWidget {
        public AdditionalEffectButtonWidget(int i, int j, int effectIndex, RegistryEntry<StatusEffect> statusEffect) {
            super(i, j, statusEffect, effectIndex, 3);
        }

        @Override
        protected MutableText getEffectName(StatusEffect statusEffect) {
            return Text.translatable(statusEffect.getTranslationKey()).append(" " + "I".repeat(this.effectIndex + 1));
        }

        @Override
        public void tick(int level) {
            if (NetheriteBeaconScreen.this.primaryEffect != null) {
                this.visible = true;
                this.init(this.effectIndex == 1 || NetheriteBeaconScreen.this.secondaryEffect == null ? NetheriteBeaconScreen.this.primaryEffect : NetheriteBeaconScreen.this.secondaryEffect);
                super.tick(level);
            } else this.visible = false;
        }
    }
}
