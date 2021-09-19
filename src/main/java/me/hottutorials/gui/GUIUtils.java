package me.hottutorials.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class GUIUtils {

    public static Color backgroundDark = Color.decode("#3E3E3E");
    public static Color background = Color.decode("#474747");
    public static Color backgroundDarkDark = Color.decode("#393939");

    private volatile static int screenX = 0;
    private volatile static int screenY = 0;
    private volatile static int myX = 0;
    private volatile static int myY = 0;

    public static void makeDraggableComponent(JComponent component, JComponent targetComponent) {

        component.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {}

            @Override
            public void mousePressed(MouseEvent e) {
                screenX = e.getXOnScreen();
                screenY = e.getYOnScreen();

                myX = targetComponent.getX();
                myY = targetComponent.getY();
            }

            @Override
            public void mouseReleased(MouseEvent e) {}

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) {}
        });

        component.addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseDragged(MouseEvent e) {
                int deltaX = e.getXOnScreen() - screenX;
                int deltaY = e.getYOnScreen() - screenY;

                targetComponent.setLocation(myX + deltaX, myY + deltaY);
            }

            @Override
            public void mouseMoved(MouseEvent e) { }

        });
    }

    public static void makeDraggableFrame(JComponent component, JFrame targetComponent) {

        component.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {}

            @Override
            public void mousePressed(MouseEvent e) {
                screenX = e.getXOnScreen();
                screenY = e.getYOnScreen();

                myX = targetComponent.getX();
                myY = targetComponent.getY();
            }

            @Override
            public void mouseReleased(MouseEvent e) {}

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) {}
        });

        component.addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseDragged(MouseEvent e) {
                int deltaX = e.getXOnScreen() - screenX;
                int deltaY = e.getYOnScreen() - screenY;

                targetComponent.setLocation(myX + deltaX, myY + deltaY);
            }

            @Override
            public void mouseMoved(MouseEvent e) { }

        });
    }

}
