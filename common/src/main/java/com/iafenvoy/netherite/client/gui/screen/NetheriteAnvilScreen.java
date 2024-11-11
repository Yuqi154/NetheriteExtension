package com.iafenvoy.netherite.client.gui.screen;

import com.iafenvoy.netherite.screen.NetheriteAnvilScreenHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.ForgingScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.RenameItemC2SPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class NetheriteAnvilScreen extends ForgingScreen<NetheriteAnvilScreenHandler> {
    private static final Identifier TEXT_FIELD_TEXTURE = new Identifier("container/anvil/text_field");
    private static final Identifier TEXT_FIELD_DISABLED_TEXTURE = new Identifier("container/anvil/text_field_disabled");
    private static final Identifier TEXTURE = new Identifier("textures/gui/container/anvil.png");
    private static final Text TOO_EXPENSIVE_TEXT = Text.translatable("container.repair.expensive");
    private final PlayerEntity player;
    private TextFieldWidget nameField;

    public NetheriteAnvilScreen(NetheriteAnvilScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title, TEXTURE);
        this.player = inventory.player;
        this.titleX = 60;
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        super.drawForeground(context, mouseX, mouseY);
        int level = this.handler.getLevelCost();
        if (level > 0) {
            int color = 0x80ff20;
            Text text;
            if (level >= 40 && !this.player.getAbilities().creativeMode) {
                text = TOO_EXPENSIVE_TEXT;
                color = 0xff6060;
            } else if (!this.handler.getSlot(2).hasStack())
                text = null;
            else {
                text = Text.translatable("container.repair.cost", level);
                if (!this.handler.getSlot(2).canTakeItems(this.player))
                    color = 0xff6060;
            }

            if (text != null) {
                int k = this.backgroundWidth - 8 - this.textRenderer.getWidth(text) - 2;
                context.fill(k - 2, 67, this.backgroundWidth - 8, 79, 0x4f000000);
                context.drawTextWithShadow(this.textRenderer, text, k, 69, color);
            }
        }
    }

    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        super.drawBackground(context, delta, mouseX, mouseY);
        context.drawGuiTexture(this.handler.getSlot(0).hasStack() ? TEXT_FIELD_TEXTURE : TEXT_FIELD_DISABLED_TEXTURE, this.x + 59, this.y + 20, 110, 16);
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256) {
            assert this.client != null && this.client.player != null;
            this.client.player.closeHandledScreen();
        }
        return this.nameField.keyPressed(keyCode, scanCode, modifiers) || this.nameField.isActive() || super.keyPressed(keyCode, scanCode, modifiers);
    }

    private void onRenamed(String name) {
        if (!name.isEmpty()) {
            String s = name;
            Slot slot = this.handler.getSlot(0);
            if (slot != null && slot.hasStack() && !slot.getStack().contains(DataComponentTypes.CUSTOM_NAME) && name.equals(slot.getStack().getName().getString()))
                s = "";
            this.handler.setNewItemName(s);
            assert this.client != null && this.client.player != null;
            this.client.player.networkHandler.sendPacket(new RenameItemC2SPacket(s));
        }
    }

    @Override
    public void onSlotUpdate(ScreenHandler handler, int slotId, ItemStack stack) {
        if (slotId == 0) {
            this.nameField.setText(stack.isEmpty() ? "" : stack.getName().getString());
            this.nameField.setEditable(!stack.isEmpty());
            this.setFocused(this.nameField);
        }
    }

    @Override
    public void renderForeground(DrawContext context, int mouseX, int mouseY, float delta) {
        this.nameField.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        String string = this.nameField.getText();
        this.init(client, width, height);
        this.nameField.setText(string);
    }

    @Override
    protected void drawInvalidRecipeArrow(DrawContext context, int x, int y) {
        if ((this.handler.getSlot(0).hasStack() || this.handler.getSlot(1).hasStack())
                && !this.handler.getSlot(this.handler.getResultSlotIndex()).hasStack())
            context.drawTexture(TEXTURE, x + 99, y + 45, this.backgroundWidth, 0, 28, 21);
    }

    @Override
    protected void setup() {
        int i = (this.width - this.backgroundWidth) / 2;
        int j = (this.height - this.backgroundHeight) / 2;
        this.nameField = new TextFieldWidget(this.textRenderer, i + 62, j + 24, 103, 12, Text.translatable("container.repair"));
        this.nameField.setFocusUnlocked(false);
        this.nameField.setEditableColor(-1);
        this.nameField.setUneditableColor(-1);
        this.nameField.setDrawsBackground(false);
        this.nameField.setMaxLength(50);
        this.nameField.setChangedListener(this::onRenamed);
        this.nameField.setText("");
        this.addSelectableChild(this.nameField);
        this.setInitialFocus(this.nameField);
        this.nameField.setEditable(false);
    }
}
