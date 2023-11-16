package ninja.crinkle.mod.undergarment.common;


import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import ninja.crinkle.mod.undergarment.common.capabilities.UndergarmentProvider;
import ninja.crinkle.mod.undergarment.common.config.UndergarmentConfig;
import ninja.crinkle.mod.undergarment.common.network.UndergarmentChannel;

public class UndergarmentCommonRegistration {
    public static void register() {
        MinecraftForge.EVENT_BUS.addGenericListener(ItemStack.class, UndergarmentProvider::attach);
        UndergarmentConfig.register();
        UndergarmentChannel.register();
    }
}
