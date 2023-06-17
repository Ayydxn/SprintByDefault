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

@Mixin(ControlsOptionsScreen.class)
public abstract class ControlsOptionsScreenMixin extends GameOptionsScreen
{
    private final SimpleOption<Boolean> sprintByDefaultOption = SimpleOption.ofBoolean("options.sprintByDefault",
            SprintByDefaultConfig.getInstance().isSprintByDefaultEnabled);

    public ControlsOptionsScreenMixin(Screen parent, GameOptions gameOptions, Text title)
    {
        super(parent, gameOptions, title);
    }

    /**
     * @author YoArcane
     * @reason Move the "Done" button downwards and add the Sprint By Default option button.
     *<p/>
     * I don't like to use Overwrite, but I couldn't figure out how to do it any other way.
     */
    @Overwrite
    public void init()
    {
        super.init();

        Validate.isTrue(this.client != null, "Failed to create Sprint By Default option button! The client instance (somehow) is null!");

        int width = this.width / 2 - 155;
        int height = this.height / 6 - 12;

        this.addDrawableChild(ButtonWidget.builder(Text.translatable("options.mouse_settings"),
                button -> this.client.setScreen(new MouseOptionsScreen(this, this.gameOptions)))
                .dimensions(width, height, 150, 20)
                .build());

        this.addDrawableChild(ButtonWidget.builder(Text.translatable("controls.keybinds"),
                button -> this.client.setScreen(new KeybindsScreen(this, this.gameOptions)))
                .dimensions(width + 160, height, 150, 20)
                .build());

        this.addDrawableChild(this.gameOptions.getSneakToggled().createWidget(this.gameOptions, width, height += 24, 150));
        this.addDrawableChild(this.gameOptions.getSprintToggled().createWidget(this.gameOptions, width + 160, height, 150));
        this.addDrawableChild(this.gameOptions.getAutoJump().createWidget(this.gameOptions, width, height += 24, 150));
        this.addDrawableChild(this.gameOptions.getOperatorItemsTab().createWidget(this.gameOptions, width + 160, height, 150));

        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, button -> this.client.setScreen(this.parent))
                .dimensions(this.width / 2 - 100, height + 50, 200, 20)
                .build());

        ClickableWidget sprintByDefaultOptionWidget = sprintByDefaultOption.createWidget(this.gameOptions, this.width / 2 - 155, this.height / 6 - 12 + 72, 150, (newValue) ->
        {
            SprintByDefaultConfig.getInstance().isSprintByDefaultEnabled = newValue;

            SprintByDefaultConfig.getInstance().writeConfig();
        });
        sprintByDefaultOptionWidget.setTooltip(Tooltip.of(Text.translatable("options.sprintByDefault.tooltip")));

        this.addDrawableChild(sprintByDefaultOptionWidget);
    }
}
