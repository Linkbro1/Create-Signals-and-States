package net.linkbro.createsignalsandstates.classes;

public abstract class Module {
    public String itemName;
    public int width;
    public Port[] input;
    public Port[] output;

    public abstract void process();

}