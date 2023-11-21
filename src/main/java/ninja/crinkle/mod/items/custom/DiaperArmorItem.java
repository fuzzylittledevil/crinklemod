package ninja.crinkle.mod.items.custom;

import com.mojang.logging.LogUtils;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import ninja.crinkle.mod.capabilities.IUndergarment;
import ninja.crinkle.mod.client.renderers.DiaperArmorRenderer;
import ninja.crinkle.mod.config.UndergarmentConfig;
import ninja.crinkle.mod.undergarment.Undergarment;
import ninja.crinkle.mod.undergarment.UndergarmentSettings;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.UUID;
import java.util.function.Consumer;

public class DiaperArmorItem extends ArmorItem implements GeoItem {
    private static final Logger LOGGER = LogUtils.getLogger();
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
    public DiaperArmorItem(ArmorMaterial pMaterial, Type pType, Properties pProperties) {
        super(pMaterial, pType, pProperties.rarity(Rarity.EPIC).durability(1000));
    }

    @Override
    public void initializeClient(@NotNull Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private DiaperArmorRenderer renderer = null;


            @Override
            public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack,
                                                                   EquipmentSlot equipmentSlot,
                                                                   HumanoidModel<?> original) {
                if (this.renderer == null) {
                    this.renderer = new DiaperArmorRenderer();
                }
                this.renderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);
                return this.renderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0,
                this::predicate));
    }

    private PlayState predicate(AnimationState<DiaperArmorItem> state) {
        Undergarment undergarment = Undergarment.of(state.getData(DataTickets.ITEMSTACK));
        if (undergarment.getSolids() > 0) {
            return state.setAndContinue(RawAnimation.begin().thenPlayAndHold("animation.diaper.test"));
        }
        return PlayState.STOP;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return geoCache;
    }
}
