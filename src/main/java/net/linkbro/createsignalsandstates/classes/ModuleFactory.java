package net.linkbro.createsignalsandstates.classes;

import net.linkbro.createsignalsandstates.component.SNSDataComponents;
import net.linkbro.createsignalsandstates.item.SNSItems;
import net.linkbro.createsignalsandstates.modules.AndModule;
import net.linkbro.createsignalsandstates.modules.BiasModule;
import net.linkbro.createsignalsandstates.modules.LinkModule;
import net.linkbro.createsignalsandstates.modules.NotModule;
import net.linkbro.createsignalsandstates.modules.OrModule;
import net.linkbro.createsignalsandstates.modules.SplitModule;
import net.linkbro.createsignalsandstates.modules.SumModule;
import net.minecraft.world.item.ItemStack;

public class ModuleFactory {
    public static Module moduleFromItemstack(ItemStack itemStack) {
        String moduleType = itemStack.get(SNSDataComponents.MODULE_TYPE);
        if (moduleType != null) {
            switch (moduleType) {
                case "SUM":
                    return new SumModule();
                case "SPLIT":
                    return new SplitModule();
                case "AND":
                    return new AndModule();
                case "OR":
                    return new OrModule();
                case "NOT":
                    return new NotModule();
                case "BIAS":
                    return new BiasModule();
                case "LINK":
                    return new LinkModule();
                default:
                    break;
            }
        }
        return null;
    }

    public static Module moduleFromItemName(String itemName) {
        if (itemName != null) {
            switch (itemName) {
                case "sum_module":
                    return new SumModule();
                case "split_module":
                    return new SplitModule();
                case "and_module":
                    return new AndModule();
                case "or_module":
                    return new OrModule();
                case "not_module":
                    return new NotModule();
                case "bias_module":
                    return new BiasModule();
                case "link_module":
                    return new LinkModule();
                default:
                    break;
            }
        }
        return null;
    }

    public static ItemStack ItemStackFromModule(Module module) {
        if (module == null) {
            return ItemStack.EMPTY;
        }
        String itemName = module.itemName;
        switch (itemName) {
            case "sum_module":
                return new ItemStack(SNSItems.SUM_MODULE.asItem());
            case "split_module":
                return new ItemStack(SNSItems.SPLIT_MODULE.asItem());
            case "and_module":
                return new ItemStack(SNSItems.AND_MODULE.asItem());
            case "or_module":
                return new ItemStack(SNSItems.OR_MODULE.asItem());
            case "not_module":
                return new ItemStack(SNSItems.NOT_MODULE.asItem());
            case "bias_module":
                return new ItemStack(SNSItems.BIAS_MODULE.asItem());
            case "link_module":
                return new ItemStack(SNSItems.LINK_MODULE.asItem());
            default:
                break;
        }
        return ItemStack.EMPTY;
    }
}
