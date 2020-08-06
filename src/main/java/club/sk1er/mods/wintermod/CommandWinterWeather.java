package club.sk1er.mods.wintermod;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class CommandWinterWeather extends CommandBase {

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public String getCommandName() {
        return "winterweather";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return EnumChatFormatting.RED + "Usage: /winterweather";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length != 0) {
            sender.addChatMessage(new ChatComponentText(getCommandUsage(sender)));
            return;
        }

        if (WinterWeatherMod.mod.toggleActive()) {
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Winter Weather is now on"));
        } else {
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Winter Weather is now off"));
        }
    }
}
