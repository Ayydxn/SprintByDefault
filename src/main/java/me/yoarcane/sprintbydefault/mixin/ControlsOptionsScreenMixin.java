package me.yoarcane.sprintbydefault.mixin;

import me.yoarcane.sprintbydefault.config.SprintByDefaultConfig;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.ControlsOptionsScreen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.screen.option.KeybindsScreen;
import net.minecraft.client.gui.screen.option.MouseOptionsScreen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import org.apache.commons.lang3.Validate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ControlsOptionsScreen.class)
public abstract class ControlsOptionsScreenMixin extends GameOptionsScreen
{
    @Unique
    private final SimpleOption<Boolean> sprintByDefaultOption = SimpleOption.ofBoolean("options.sprintByDefault",
            SprintByDefaultConfig.getInstance().isSprintByDefaultEnabled);

    public ControlsOptionsScreenMixin(Screen parent, GameOptions gameOptions, Text title)
    {
        super(parent, gameOptions, title);
    }

    @Inject(method = "init", at = @At("HEAD"))
    public void addSprintByDefaultOptionButton(CallbackInfo ci)
    {
        ClickableWidget sprintByDefaultOptionWidget = sprintByDefaultOption.createWidget(this.gameOptions, this.width / 2 + 5, this.height / 6 - 12 + 72, 150, (newValue) ->
        {
            SprintByDefaultConfig.getInstance().isSprintByDefaultEnabled = newValue;

            SprintByDefaultConfig.getInstance().writeConfig();
        });
        sprintByDefaultOptionWidget.setTooltip(Tooltip.of(Text.translatable("options.sprintByDefault.tooltip")));

        this.addDrawableChild(sprintByDefaultOptionWidget);
    }
}
