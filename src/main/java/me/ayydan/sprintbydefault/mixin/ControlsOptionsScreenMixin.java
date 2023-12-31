package me.ayydan.sprintbydefault.mixin;

import me.ayydan.sprintbydefault.config.SprintByDefaultConfig;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.ControlsOptionsScreen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
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

    @ModifyArg(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/option/ControlsOptionsScreen;addDrawableChild(Lnet/minecraft/client/gui/Element;)Lnet/minecraft/client/gui/Element;", ordinal = 6))
    public Element resizeAndRepositionDoneButton(Element par1)
    {
        return ButtonWidget.builder(ScreenTexts.DONE, (button) ->
        {
            this.client.setScreen(this.parent);
        }).dimensions(this.width / 2 - 155, this.height / 3 - 23, 150, 20).build();
    }
}
