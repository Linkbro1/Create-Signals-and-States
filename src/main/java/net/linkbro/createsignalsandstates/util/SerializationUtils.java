package net.linkbro.createsignalsandstates.util;

import net.linkbro.createsignalsandstates.classes.ModuleFactory;
import net.linkbro.createsignalsandstates.classes.Port;
import net.linkbro.createsignalsandstates.classes.Module;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

public class SerializationUtils {

    public static CompoundTag serializeModule(Module module) {
        CompoundTag tag = new CompoundTag();
        tag.putString("itemName", module.itemName);

        ListTag inputList = new ListTag();
        for (Port inPort : module.input ) {
            inputList.add(serializePort(inPort));
        }
        tag.put("inputList", inputList);

        ListTag outputList = new ListTag();
        for (Port outPort : module.output) {
            inputList.add(serializePort(outPort));
        }
        tag.put("outputList", outputList);

        return tag;
    }

    public static Module deserializeModule(CompoundTag compoundTag) {
        String itemName = compoundTag.getString("itemName");
        Module module = ModuleFactory.moduleFromItemName(itemName);

        ListTag inputList = compoundTag.getList("inputList", Tag.TAG_COMPOUND);
        for (int i = 0; i < inputList.size() && i < module.input.length; i++) {
            module.input[i] = deserializePort(inputList.getCompound(i));
        }

        ListTag outputList = compoundTag.getList("outputList", Tag.TAG_COMPOUND);
        for (int i = 0; i < outputList.size() && i < module.output.length; i++) {
            module.output[i] = deserializePort(outputList.getCompound(i));
        }

        return module;
    }

    public static CompoundTag serializePort(Port port) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("value", port.value);
        tag.putInt("posX", (int) port.position.x);
        tag.putInt("posY", (int) port.position.y);
        return tag;
    }

    public static Port deserializePort(CompoundTag compoundTag) {
        int portX = compoundTag.getInt("posX");
        int portY = compoundTag.getInt("posY");

        Port port = new Port(portX, portY);
        port.value = compoundTag.getInt("value");
        return port;
    }
}
