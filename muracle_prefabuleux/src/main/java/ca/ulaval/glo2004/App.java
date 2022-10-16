package ca.ulaval.glo2004;


import ca.ulaval.glo2004.gui.MainMenu;
import ca.ulaval.glo2004.gui.MainWindow;

import javax.swing.*;
import java.awt.*;


public class App {
    //Exemple de creation d'une fenetre et d'un bouton avec swing. Lorsque vous allez creer votre propre GUI
    // Vous n'aurez pas besoin d'ecrire tout ce code, il sera genere automatiquement par intellij ou netbeans
    // Par contre vous aurez a creer les actions listener pour vos boutons et etc.
    public static void main(String[] args) {

        JFrame frame = new JFrame("Muracle Prefabuleux");

        MainWindow mainWindow = new MainWindow();
        MainMenu menu = new MainMenu();

        frame.setMinimumSize(new Dimension(800, 500));
        frame.setContentPane(mainWindow.rootPanel);
        frame.setJMenuBar(menu);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);


    }
}

