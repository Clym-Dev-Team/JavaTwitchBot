package main.system.panelSystem;

import com.sun.jdi.event.StepEvent;

import java.util.ArrayList;

public class MenuItemList {
    private static final ArrayList<MenuItem> menuList = new ArrayList<>();

    public static ArrayList<MenuItem> getMenuList() {
        return menuList;
    }

    public static void addMenuItem(MenuItem menuItem) {
        menuList.add(menuItem);
    }

    public record MenuItem(String topicName, String link, ArrayList<MenuItem> subItems) {
        public MenuItem(String topicName, String link) {
            this(topicName, link, null);
        }
    }

}

