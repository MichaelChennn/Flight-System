package de.tum.nihaoshijie.flightsystem.common.model.service;

import java.util.HashSet;

public class Menu {
    private HashSet<Service> menuElements;

    public Menu () {
        menuElements = new HashSet<Service>();
    }

    public void addMenuElement (Service service) {
        menuElements.add(service);
    }

    public HashSet<Service> getMenuElements() {
        return menuElements;
    }

    public void setMenuElements(HashSet<Service> menuElements) {
        this.menuElements = menuElements;
    }

}