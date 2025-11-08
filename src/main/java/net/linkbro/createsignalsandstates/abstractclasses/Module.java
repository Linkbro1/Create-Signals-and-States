package net.linkbro.createsignalsandstates.abstractclasses;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

public abstract class Module {
    public String itemName;
    public int width;
    public Port[] input;
    public Port[] output;

    public abstract void process();

    public static CompoundTag serializeNBT(Module module) { //TODO: move these, these aren't meant to be here
        CompoundTag tag = new CompoundTag();
        tag.putString("itemName", module.itemName);
        
        ListTag inputList = new ListTag();
        for (Port inPort : module.input) {
            inputList.add(Port.serializeNBT(inPort));
        }
        tag.put("inputList", inputList);


        ListTag outputList = new ListTag();
        for (Port outPort : module.output) {
            inputList.add(Port.serializeNBT(outPort));                
        }
        tag.put("outputList", outputList);

        return tag;
    }

    public static Module deserializeNBT(CompoundTag compoundTag) {
        String itemName = compoundTag.getString("itemName");
        Module module = ModuleFactory.moduleFromItemName(itemName);
        
        ListTag inputList = compoundTag.getList("inputList", Tag.TAG_COMPOUND);
        for (int i = 0; i < inputList.size() && i < module.input.length; i++) {
            module.input[i] = Port.deserializeNBT(inputList.getCompound(i));
        }

        ListTag outputList = compoundTag.getList("outputList", Tag.TAG_COMPOUND);
        for (int i = 0; i < outputList.size() && i < module.output.length; i++) {
            module.output[i] = Port.deserializeNBT(outputList.getCompound(i));
        }

        return module;
    }
}