package ca.ulaval.glo2004.gui;

import ca.ulaval.glo2004.classes.*;
import ca.ulaval.glo2004.classes.dto.AccessoireDTO;
import ca.ulaval.glo2004.classes.dto.MurDTO;
import ca.ulaval.glo2004.classes.dto.SalleDTO;
import ca.ulaval.glo2004.classes.dto.SeparateurDTO;
import ca.ulaval.glo2004.gestion.Conversion;
import ca.ulaval.glo2004.gestion.GestionnaireSalle;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.lang.annotation.Target;
import java.util.Locale;

public class MainWindow {
    public JPanel rootPanel;
    private JPanel propertiesPanel;
    private JPanel rightPanel;
    private JPanel controlPanel;
    public JPanel mainPanel;
    private JPanel buttonsPanel;
    private JOptionPane alertPanel;
    private JButton btnSave;
    private JButton btnUndo;
    private JButton btnRedo;
    private JButton btnGrille;
    private JButton btnFenetre;
    private JButton btnPorte;
    private JButton btnPrise;
    private JButton btnRetourAir;
    private JButton btnSupprimer;
    private JButton btnSeparateur;
    private JButton btnSelection;

    private JButton btnMove;
    private JPanel DimensionsPanel;
    private JButton btnDimensionsCollapse;
    private JPanel dimensionPanelContent;
    private JPanel dimLargeurPanel;
    private JTextField tbLargeur;
    private JTextField tbProfondeur;
    private JTextField tbHauteur;
    private JTextField tbEpaisseurMurs;
    private JTextField tbLargeurPli;
    private JTextField tbPliSoudure;

    private JButton btnElvNordEXT;
    private JButton btnElvNordINT;
    private JButton btnElvEstEXT;
    private JButton btnElvEstINT;
    private JButton btnElvSudEXT;
    private JButton btnELVSudINT;
    private JButton btnElvOuestEXT;
    private JButton btnElvOuestINT;
    private JButton btnPlan;

    private JOptionPane alertTextBox;
    public JPanel starterPanel;
    private JButton creerUnNouveauProjetButton;
    private JButton ouvrirUnProjectExistantButton;
    private JLabel title;
    private Box container;

    private PanelProprietes proprietesSalle;
    private PanelProprietes proprietesMur;
    private PanelProprietes proprietesAccessoire;
    private PanelProprietes proprietesSeparateur;

    private MainWindow mainWindow;
    private DrawingPanel panel;
    GestionnaireSalle gestionnaireSalle;
    private String filePath;
    Utilitaire.AccessoireEnum AccessoireEnum = Utilitaire.AccessoireEnum.Selection;

    Utilitaire.Direction direction;
    private boolean interieur;
    private boolean plan;
    public MainWindow(GestionnaireSalle gestionnaireSalle) {
        this.interieur = false;
        this.plan = true;
        this.gestionnaireSalle = gestionnaireSalle;
        mainWindow = this;
        direction = null;
        interieur = false;
        panel = new DrawingPanel(this);
        creerUnNouveauProjetButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                gestionnaireSalle.creerSalleDefaut();
                mainWindow = new MainWindow(gestionnaireSalle);
                JFileChooser fc = new JFileChooser();
                fc.setSelectedFile(new File("sale.ser"));
                int returnFcVal = fc.showSaveDialog(rootPanel.getParent());
                if (returnFcVal == JFileChooser.APPROVE_OPTION) {
                    try {
                        File file = fc.getSelectedFile();
                        setHomePage(e);
                        mainWindow.gestionnaireSalle.enregistrerSalle(file.getPath());
                    } catch (Exception error) {
                        System.out.println(error);
                    }
                }
            }
        });

        ouvrirUnProjectExistantButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mainWindow = new MainWindow(gestionnaireSalle);
                JFileChooser fc = new JFileChooser("d:");
                int returnFcVal = fc.showOpenDialog(rootPanel.getParent());
                if (returnFcVal == JFileChooser.APPROVE_OPTION) {
                    try {
                        File file = fc.getSelectedFile();
                        setHomePage(e);
                        mainWindow.gestionnaireSalle.chargerSalle(file.getPath());
                    } catch (Exception error) {
                        System.out.println(error);
                    }
                }
            }
        });
        btnSave.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                gestionnaireSalle.enregistrerSalle(null);
            }
        });
        this.$$$getRootComponent$$$().registerKeyboardAction((ActionListener) e -> {
            gestionnaireSalle.enregistrerSalle(null);
        }, KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK), JComponent.WHEN_IN_FOCUSED_WINDOW);

        this.mainPanel.addMouseWheelListener(e -> {
            gestionnaireSalle.zoomer(-e.getWheelRotation(), e.getX(), e.getY());
            this.mainPanel.validate();
            this.mainPanel.repaint();
        });

        btnElvEstINT.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
               gestionnaireSalle.ChangementDeVueVersCote(true);
               gestionnaireSalle.setmCoteCourant(Utilitaire.Direction.EST);
               direction = Utilitaire.Direction.EST;
               interieur = true;
               updatePanels();
                resetButtonView();
                AccessoireEnum = null;
                resetButtonAccessoires();
                btnElvEstINT.setBorder(BorderFactory.createLineBorder(Color.blue));
               panel.setAfficheur(new AfficheurElevationCote(mainWindow.gestionnaireSalle.getSalleActive(), Utilitaire.Direction.EST, false));
            }
        });

        btnElvEstEXT.addMouseListener(new MouseAdapter() {
            //TODO enlever : retour d'air et prise de courant des accessoires
            @Override
            public void mousePressed(MouseEvent e) {
                gestionnaireSalle.ChangementDeVueVersCote(false);
                gestionnaireSalle.setmCoteCourant(Utilitaire.Direction.EST);
                direction = Utilitaire.Direction.EST;
                interieur = false;
                updatePanels();
                resetButtonView();
                AccessoireEnum = null;
                resetButtonAccessoires();
                btnElvEstEXT.setBorder(BorderFactory.createLineBorder(Color.blue));
                panel.setAfficheur(new AfficheurElevationCote(mainWindow.gestionnaireSalle.getSalleActive(), (Utilitaire.Direction.EST), true));
            }
        });

        btnELVSudINT.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                gestionnaireSalle.ChangementDeVueVersCote(true);
                gestionnaireSalle.setmCoteCourant(Utilitaire.Direction.SUD);
                direction = Utilitaire.Direction.SUD;
                interieur = true;
                updatePanels();
                resetButtonView();
                AccessoireEnum = null;
                resetButtonAccessoires();
                btnELVSudINT.setBorder(BorderFactory.createLineBorder(Color.blue));
                panel.setAfficheur(new AfficheurElevationCote(mainWindow.gestionnaireSalle.getSalleActive(), (Utilitaire.Direction.SUD), false));
            }
        });
        btnElvSudEXT.addMouseListener(new MouseAdapter() {
            //TODO enlever : retour d'air et prise de courant des accessoires
            @Override
            public void mousePressed(MouseEvent e) {
                gestionnaireSalle.ChangementDeVueVersCote(false);
                gestionnaireSalle.setmCoteCourant(Utilitaire.Direction.SUD);
                direction = Utilitaire.Direction.SUD;
                interieur = false;
                updatePanels();
                AccessoireEnum = null;
                resetButtonView();
                resetButtonAccessoires();
                btnElvSudEXT.setBorder(BorderFactory.createLineBorder(Color.blue));
                panel.setAfficheur(new AfficheurElevationCote(mainWindow.gestionnaireSalle.getSalleActive(), (Utilitaire.Direction.SUD), true));

            }
        });
        btnElvOuestINT.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                gestionnaireSalle.ChangementDeVueVersCote(true);
                gestionnaireSalle.setmCoteCourant(Utilitaire.Direction.OUEST);
                direction = Utilitaire.Direction.OUEST;
                interieur = true;
                updatePanels();
                AccessoireEnum = null;
                resetButtonView();
                resetButtonAccessoires();
                btnElvOuestINT.setBorder(BorderFactory.createLineBorder(Color.blue));
                panel.setAfficheur(new AfficheurElevationCote(mainWindow.gestionnaireSalle.getSalleActive(), (Utilitaire.Direction.OUEST), false));

            }
        });
        btnElvOuestEXT.addMouseListener(new MouseAdapter() {
            //TODO enlever : retour d'air et prise de courant des accessoires
            @Override
            public void mousePressed(MouseEvent e) {
                gestionnaireSalle.ChangementDeVueVersCote(false);
                gestionnaireSalle.setmCoteCourant(Utilitaire.Direction.OUEST);
                direction = Utilitaire.Direction.OUEST;
                interieur = false;
                updatePanels();
                AccessoireEnum = null;
                resetButtonView();
                resetButtonAccessoires();
                btnElvOuestEXT.setBorder(BorderFactory.createLineBorder(Color.blue));
                panel.setAfficheur(new AfficheurElevationCote(mainWindow.gestionnaireSalle.getSalleActive(), (Utilitaire.Direction.OUEST), true));
            }
        });

        btnElvNordINT.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                gestionnaireSalle.ChangementDeVueVersCote(true);
                gestionnaireSalle.setmCoteCourant(Utilitaire.Direction.NORD);
                direction = Utilitaire.Direction.NORD;
                interieur = true;
                updatePanels();
                AccessoireEnum = null;
                resetButtonView();
                resetButtonAccessoires();
                btnElvNordINT.setBorder(BorderFactory.createLineBorder(Color.blue));
                panel.setAfficheur(new AfficheurElevationCote(mainWindow.gestionnaireSalle.getSalleActive(), (Utilitaire.Direction.NORD), false));
            }
        });

        btnElvNordEXT.addMouseListener(new MouseAdapter() {
            //TODO enlever : retour d'air et prise de courant des accessoires
            @Override
            public void mousePressed(MouseEvent e) {
                gestionnaireSalle.ChangementDeVueVersCote(false);
                gestionnaireSalle.setmCoteCourant(Utilitaire.Direction.OUEST);
                direction = Utilitaire.Direction.NORD;
                interieur = false;
                updatePanels();
                AccessoireEnum = null;
                resetButtonView();
                resetButtonAccessoires();
                btnElvNordEXT.setBorder(BorderFactory.createLineBorder(Color.blue));
                panel.setAfficheur(new AfficheurElevationCote(mainWindow.gestionnaireSalle.getSalleActive(), (Utilitaire.Direction.NORD), true));
            }
        });

        btnPlan.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                gestionnaireSalle.ChangementDeVueVersPlan();
                gestionnaireSalle.setmCoteCourant(null);
                direction = null;
                interieur = false;
                updatePanels();
                AccessoireEnum = null;
                resetButtonView();
                resetButtonAccessoires();
                btnPlan.setBorder(BorderFactory.createLineBorder(Color.blue));
                panel.setAfficheur( new AfficheurVueDessus(gestionnaireSalle.getSalleActive()));

            }
        });

        btnPorte.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                AccessoireEnum = Utilitaire.AccessoireEnum.Porte;
                resetButtonAccessoires();
            }
        });

        btnPrise.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                AccessoireEnum = Utilitaire.AccessoireEnum.PriseElectrique;
                resetButtonAccessoires();
            }
        });

        btnRetourAir.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                AccessoireEnum = Utilitaire.AccessoireEnum.RetourAir;
                resetButtonAccessoires();
            }
        });

         btnSelection.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                AccessoireEnum = Utilitaire.AccessoireEnum.Selection;
                resetButtonAccessoires();
            }
        });

         btnMove.addMouseListener(new MouseAdapter() {
             @Override
             public void mousePressed(MouseEvent e) {
                 super.mousePressed(e);
                 AccessoireEnum = Utilitaire.AccessoireEnum.Move;
                 resetButtonAccessoires();
             }
         });

        btnFenetre.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                AccessoireEnum = Utilitaire.AccessoireEnum.Fenetre;
                resetButtonAccessoires();
            }
        });

        btnSupprimer.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent e) {
                AccessoireEnum = Utilitaire.AccessoireEnum.Supprimer;
                resetButtonAccessoires();
            }
        });
        btnSeparateur.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent e) {
                AccessoireEnum = Utilitaire.AccessoireEnum.Separateur;
                resetButtonAccessoires();
            }
        });
        this.mainPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {

                if(e.getButton() != MouseEvent.BUTTON1)
                    return;
                if(AccessoireEnum != null){
                    if (direction != null) {
                        switch (AccessoireEnum){
                            case Fenetre:
                                if(!gestionnaireSalle.AjouterFenetre(e.getX(), e.getY(),direction,interieur)){setWarningMsg("impossible d'ajouter une fenetre");}
                                break;
                            case RetourAir:
                                if(!gestionnaireSalle.AjouterRetourAirElevation(e.getX(), e.getY(),direction,interieur)){setWarningMsg("Impossible d'ajouter un retour d'air");};
                                break;
                            case Supprimer:
                                gestionnaireSalle.SupprimerElevation(e.getX(), e.getY(),direction,interieur);
                                break;
                            case Porte:
                                if(!gestionnaireSalle.AjouterPorte(e.getX(), e.getY(),direction,interieur)){setWarningMsg("Impossible d'ajouter une porte");};
                                break;
                            case PriseElectrique:
                                if(!gestionnaireSalle.AjouterPriseElectrique(e.getX(), e.getY(),direction,interieur)){setWarningMsg("Impossible d'ajouter une prise électrique");};
                                break;
                            case Separateur:
                                //TODO BOOLEAN QUAND AJOUTER UN SEPARATEUR MARCHE PAS
                                gestionnaireSalle.AjouterSeparateurVueElevation(e.getX(), e.getY(),interieur,direction);
                                break;
                            case Selection:
                                gestionnaireSalle.selectionnerElementElevantion(e.getX(), e.getY(),direction,interieur);
                                break;
                            /*case Move:
                                gestionnaireSalle.dragAndDropElement(e.getX(), e.getY());
                                break;*/
                        }
                    }
                    else{

                        switch (AccessoireEnum){
                            case RetourAir:
                                if(!gestionnaireSalle.AjouterRetourAirPlan(e.getX(), e.getY())){setWarningMsg("Impossible d'ajouter un retour d'air");};
                                break;
                            case Supprimer:
                                gestionnaireSalle.SupprimerPlan(e.getX(), e.getY());
                                break;
                            case Separateur:
                                gestionnaireSalle.AjouterSeparateurVuePlan(e.getX(), e.getY());
                                break;
                            case Selection:
                                gestionnaireSalle.selectionnerElementPlan(e.getX(), e.getY(),direction,interieur);
                                break;
                            /*case Move:
                                gestionnaireSalle.dragAndDropElement();
                                break;*/
                        }
                    }
                }

                updatePanels();
                mainPanel.validate();
                mainPanel.repaint();
            }

        });

        MouseAdapter DnD = new MouseAdapter() {

        //this.mainPanel.addMouseListener(new MouseAdapter()

            Point m_pointDepart = null;
            Polygone m_dragTarget = null;

            Element dragTargetElement = null;
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                System.out.println("dans mousePressed");

                m_pointDepart = e.getPoint();

                if(e.getButton() == MouseEvent.BUTTON1){
                    Element element = gestionnaireSalle.getSalleActive().getElementSelectionne();
                    //MurDTO murSelect = gestionnaireSalle.getMurSelectionne();
                    if(element instanceof Mur){
                        //TODO m_dragTarget deviens le mur. il faut obtenir le polygone de ce mur..
                        if(direction != null){m_dragTarget = ((Mur) element).getPolygoneElvRetourAir();
                            dragTargetElement = element;}
                        else {m_dragTarget = ((Mur) element).getPolygonePlanRetourAir();}
                    }

                    if (element instanceof Separateur){
                        //TODO m_dragTrget deviens le séparateur. Il faut obtenir le polygone de ce séparateur..
                        if (direction != null){
                            //TODO OBTENIR POLYGONE ELV DE SEPARATEUR;
                            m_dragTarget = ((Separateur) element).getmPolygoneElevation();
                            dragTargetElement = element;
                        }
                        else {m_dragTarget = ((Separateur) element).getmPolygonePlan();}
                    }

                    AccessoireDTO accessoireSelect = gestionnaireSalle.getAccessoireSelectionne();
                    if (element instanceof Accessoire){
                        //TODO m_dragTarget deviens l'accessoire. Il faut obtenir le polygone de cet accessoire...
                        if (direction != null){
                        m_dragTarget = ((Accessoire) element).getmPolygoneElevation(interieur);
                        dragTargetElement = element;
                        }
                        else {m_dragTarget = ((Accessoire) element).getmPolygonePlan();}
                    }
                    m_pointDepart = e.getPoint();
                    System.out.println("dans if mousePressed,");
                    System.out.println(m_pointDepart);
                    System.out.println(m_dragTarget);
                    System.out.println(dragTargetElement);
                    System.out.println(dragTargetElement.getmY());
                    System.out.println(dragTargetElement.getmX());
                    System.out.println(accessoireSelect);

                }
            }

            public void mouseReleased(MouseEvent e){
                super.mouseReleased(e);
                System.out.println("   dans mouseRelease  ");

                if(e.getButton() == MouseEvent.BUTTON1){
                    System.out.println("  dans if mouseRelease  ");
                    System.out.println (m_pointDepart);
                    System.out.println();
                    //dragTargetElement = null;
                }
            }
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                System.out.print("  dans mouseDragged  ");
                if (m_dragTarget != null){
                    Point point = e.getPoint();
                    int differenceX = m_pointDepart.x - point.x;
                    int differenceY = m_pointDepart.y - point.y;

                    System.out.println(m_pointDepart);
                   // System.out.println(differenceX);
                    //System.out.println(differenceY);
                    AccessoireDTO accessoireSelect = gestionnaireSalle.getAccessoireSelectionne();

                    PointImperial finPoint = Conversion.getConversion().trouverCoordonneImperial(point.x, point.y);
                    PointImperial debutPoint = Conversion.getConversion().trouverCoordonneImperial(m_pointDepart.x, m_pointDepart.y);

                    Imperial differenceXX = debutPoint.getmX().substract(finPoint.getmX());
                    Imperial differenceYY = debutPoint.getmY().substract(finPoint.getmY());
/*
                    PointImperial dragTargetPoint = m_dragTarget.getPoints().get(0);
                    Imperial dragTargetmX = dragTargetPoint.getmX();
                    Imperial dragTargetmY = dragTargetPoint.getmY();*/

                    /*dragTargetmX = dragTargetmY.substract(differenceXX);
                    dragTargetmY = dragTargetmY.substract(differenceYY);

                    dragTargetPoint.setmX(dragTargetmX);
                    dragTargetPoint.setmY(dragTargetmY);


                    dragTargetElement.setmY(dragTargetmY);
                    dragTargetElement.setmX(dragTargetmX);*/
                    if (accessoireSelect != null){
                        //TODO m_dragTarget deviens l'accessoire. Il faut obtenir le mX de l'accessoire...
                        if (direction != null){

                            System.out.println(" dans if accessoire non null ");
                            Imperial pointElementX = accessoireSelect.getX();
                            Imperial pointElementY = accessoireSelect.getY();
                            System.out.println(differenceXX);
                            System.out.println(differenceYY);
                            System.out.println(pointElementX);
                            System.out.println(pointElementY);
                            pointElementX = pointElementX.substract(differenceXX);
                            pointElementY = pointElementY.substract(differenceYY);
                            System.out.println(pointElementX);
                            System.out.println(pointElementY);
                            System.out.println(differenceXX);
                            System.out.println(differenceYY);
                           gestionnaireSalle.editAccessoireSelectionne(new AccessoireDTO(pointElementX, pointElementY, accessoireSelect.getHauteur(), accessoireSelect.getLargeur(), accessoireSelect.getBordureFenetre(), accessoireSelect.getTypeAccessoire()));
                            mainPanel.validate();
                            mainPanel.repaint();}
                        else {}
                    }
                }
            }

        };
        this.mainPanel.addMouseListener(DnD);
        this.mainPanel.addMouseMotionListener(DnD);

        updatePanels();

           /* @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                System.out.print("  dans mouseDragged  ");
                if (m_dragTarget != null){
                    Point point = e.getPoint();
                    int differenceX = m_pointDepart.x - point.x;
                    int differenceY = m_pointDepart.y - point.y;
                    m_pointDepart = point;

                   //TODO modifier la position de l'objet selectionner
                    /*MurDTO murSelect = gestionnaireSalle.getMurSelectionne();
                    if(murSelect != null){
                        //TODO modifier position x du mur et redessiner tous les murs du coté en conséquence... à voir
                    }
                    SeparateurDTO separateurSelect = gestionnaireSalle.getSeparateurSelectionne();
                    if (separateurSelect != null){
                        //TODO modifier position x du separateur

                    }

                    AccessoireDTO accessoireSelect = gestionnaireSalle.getAccessoireSelectionne();
                    if (accessoireSelect != null){
                        //TODO modifier la position x et y de l'accessoire selectionner

                        Imperial posXOriginel = accessoireSelect.getX();
                        Imperial posYOriginel = accessoireSelect.getY();
                        int newX = posXOriginel.getEntier() + differenceX;
                        int newY = posYOriginel.getEntier() + differenceY;
                        gestionnaireSalle.editAccessoireSelectionne(new AccessoireDTO(new Imperial(newX), new Imperial(newY),accessoireSelect.getHauteur(), accessoireSelect.getLargeur(), accessoireSelect.getBordureFenetre(), accessoireSelect.getTypeAccessoire()));


                    }*/

                    //mainPanel.validate();
                    //mainPanel.repaint();

/*
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                System.out.println(" dans mouseMove  ");
                if (m_dragTarget != null){
                    System.out.println(" dans if mouseMove ");
                    int dragX = dragTargetElement.getmX().getEntier();
                    int dragY = dragTargetElement.getmY().getEntier();
                    int departX = m_pointDepart.x;
                    int departY = m_pointDepart.y;
                    int finX = e.getX();
                    int finY = e.getY();
                    System.out.println(dragY);
                    System.out.println(dragX);

                    dragX += finX - departX;
                    dragY += finY - departY;
                    dragTargetElement.setmX(new Imperial(dragX));
                    dragTargetElement.setmY(new Imperial(dragY));
                    System.out.println(dragX);
                    System.out.println(dragY);
                }

            }*/





        MouseAdapter mouvementCameraAdapter = new MouseAdapter() {

            Point lastPoint = null;

            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);

                if (e.getButton() == MouseEvent.BUTTON2) {
                    lastPoint = e.getPoint();
                }

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);

                if (e.getButton() == MouseEvent.BUTTON2) {
                    lastPoint = null;
                }
            }


            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);

                if (lastPoint != null) {
                    Point point = e.getPoint();
                    int offsetX = point.x - lastPoint.x;
                    int offsetY = point.y - lastPoint.y;
                    lastPoint = point;

                    Conversion.getConversion().pan(-offsetX, -offsetY);
                    mainPanel.validate();
                    mainPanel.repaint();
                }
            }
        };

        this.mainPanel.addMouseListener(mouvementCameraAdapter);
        this.mainPanel.addMouseMotionListener(mouvementCameraAdapter);
        updatePanels();
    }

    private void updatePanels()
    {
        propertiesPanel.removeAll();

        SalleDTO salleSelect = gestionnaireSalle.getSalleSelectionne();
        if(salleSelect != null)
        {
            proprietesSalle = new PanelProprietes("DIMENSIONS DE LA SALLE", 150);
            proprietesSalle.addProperty("largeur", "LARGEUR :", salleSelect.getLargeur().toString(), false);
            proprietesSalle.addProperty("profondeur", "PROFONDEUR :", salleSelect.getProfondeur().toString(), false);
            proprietesSalle.addProperty("hauteur", "HAUTEUR :", salleSelect.getHauteur().toString(), false);
            proprietesSalle.addProperty("epaisseurMur", "ÉPAISSEUR MURS :", salleSelect.getEpaisseurMurs().toString(), false);
            proprietesSalle.addProperty("largeurPli", "LARGEUR DE PLI :", salleSelect.getLargeurPli().toString(), false);
            proprietesSalle.addProperty("pliSoudure", "PLI DE SOUDURE :", salleSelect.getAnglePliSoudure() + "", false);
            proprietesSalle.addProperty("hauteurRetourAir", "RETOUR AIR :", salleSelect.getHauteurRetourAir().toString(), false);
            proprietesSalle.addProperty("positionRetourAir", "POS RETOUR AIR :", salleSelect.getPositionRetourAir().toString(), false);
            proprietesSalle.addProperty("hauteurTrouRetourAir", "TROU RETOUR AIR :", salleSelect.getHauteurTrouRetourAir().toString(), false);
            proprietesSalle.generateLayout();
            propertiesPanel.add(proprietesSalle);

            proprietesSalle.setOnChangeListener(values -> {
                Imperial largeur = proprietesSalle.getImperial("largeur");
                Imperial profondeur = proprietesSalle.getImperial("profondeur");
                Imperial hauteur = proprietesSalle.getImperial("hauteur");
                Imperial epaisseurMur = proprietesSalle.getImperial("epaisseurMur");
                Imperial largeurPli = proprietesSalle.getImperial("largeurPli");
                int pliSoudure = proprietesSalle.getInt("pliSoudure");
                Imperial hauteurRetourAir = proprietesSalle.getImperial("hauteurRetourAir");
                Imperial positionRetourAir = proprietesSalle.getImperial("positionRetourAir");
                Imperial hauteurTrouRetourAir = proprietesSalle.getImperial("hauteurTrouRetourAir");

                if(largeur == null || profondeur == null || hauteur == null || epaisseurMur == null || largeurPli == null ||
                        pliSoudure == -1 || hauteurRetourAir == null || positionRetourAir == null || hauteurTrouRetourAir == null)
                    return;

                int result = gestionnaireSalle.editSalleSelectionne(new SalleDTO(largeur, profondeur, hauteur, epaisseurMur, largeurPli, pliSoudure, hauteurRetourAir, positionRetourAir, hauteurTrouRetourAir));

                if(result == 0)
                {

                    mainPanel.validate();
                    mainPanel.repaint();
                }

                proprietesSalle.setError("largeur", result == 1);
                proprietesSalle.setError("profondeur", result == 2);
                proprietesSalle.setError("hauteurTrouRetourAir", result == 3);
                proprietesSalle.setError("positionRetourAir", result == 4);
                proprietesSalle.setError("hauteurRetourAir", result == 4);

                if(result == 1){
                    setWarningMsg("La largeur du retour d'air est invalide");
                }
                if (result == 2){
                    setWarningMsg("la profondeur du retour d'air est invalide");
                }
                if (result == 3 ){
                    setWarningMsg("La hauteur du trou du retour d'air est invalide");
                }
                if (result == 4){
                    setWarningMsg("La position ou la hauteur du retour d'air est invalide");
                }

            });
        }
        else
        {
            return;
        }


        MurDTO murSelect = gestionnaireSalle.getMurSelectionne();
        if(murSelect != null)
        {
            proprietesMur = new PanelProprietes("DIMENSIONS DU MUR", 0);
            proprietesMur.addProperty("x", "POSITION X :", murSelect.getX().toString(), true);
            proprietesMur.addProperty("y", "POSITION Y :", "", true);
            proprietesMur.addProperty("largeur", "LARGEUR :", "", true);
            if(murSelect.aRetourAir())
                proprietesMur.addProperty("largeurRetourAir", "RETOUR AIR :", murSelect.getLargeurRetourAir().toString(), false);
            proprietesMur.generateLayout();
            propertiesPanel.add(proprietesMur);

            proprietesMur.setOnChangeListener(values -> {
                Imperial largeurRetourAir = proprietesMur.getImperial("largeurRetourAir");

                if(largeurRetourAir != null)
                {
                    boolean result = gestionnaireSalle.editMurSelectionne(largeurRetourAir);

                    proprietesMur.setError("largeurRetourAir", !result);
                    mainPanel.validate();
                    mainPanel.repaint();
                }
            });

            proprietesMur.setValue("x", murSelect.getX().toString());
            proprietesMur.setValue("y", murSelect.getY().toString());
            proprietesMur.setValue("largeur", murSelect.getLargeur().toString());
            proprietesMur.updateValues();
        }

        SeparateurDTO sepSelect = gestionnaireSalle.getSeparateurSelectionne();
        if(sepSelect != null)
        {
            proprietesSeparateur = new PanelProprietes("SÉPARATEUR", 100);
            proprietesSeparateur.addProperty("pos", "POSITION :", sepSelect.getPosition().toString(), true);
            proprietesSeparateur.addProperty("posRel", "SEP. PRÉCÉDENT :", sepSelect.getPositionRelative().toString(), false);
            proprietesSeparateur.generateLayout();
            propertiesPanel.add(proprietesSeparateur);

            proprietesSeparateur.setOnChangeListener(values -> {

                        Imperial posRel = proprietesSeparateur.getImperial("posRel");

                        if (posRel != null && gestionnaireSalle.editSeparateurSelectionne(posRel)) {
                            proprietesSeparateur.setError("posRel", false);
                            SeparateurDTO newValue = gestionnaireSalle.getSeparateurSelectionne();
                            proprietesSeparateur.setValue("pos", newValue.getPosition().toString());

                            mainPanel.validate();
                            mainPanel.repaint();
                        } else{
                            proprietesSeparateur.setError("posRel", true);
                       // setWarningMsg("La position du separateur est invalide.");
                            }
                    });
        }

        AccessoireDTO accessoireSelect = gestionnaireSalle.getAccessoireSelectionne();
        if (accessoireSelect != null)
        {
            proprietesAccessoire = new PanelProprietes("ACCESSOIRE", 100);
            proprietesAccessoire.addProperty("x", "POS X :", accessoireSelect.getX().toString(), false);
            if(accessoireSelect.getTypeAccessoire() != Utilitaire.AccessoireEnum.Porte)
                proprietesAccessoire.addProperty("y", "POS Y :", accessoireSelect.getY().toString(), false);
            proprietesAccessoire.addProperty("largeur", "LARGEUR :", accessoireSelect.getLargeur().toString(), false);
            proprietesAccessoire.addProperty("hauteur", "HAUTEUR :", accessoireSelect.getHauteur().toString(), false);
            if(accessoireSelect.getTypeAccessoire() == Utilitaire.AccessoireEnum.Fenetre)
                proprietesAccessoire.addProperty("brodureFenetre", "BORDURE", accessoireSelect.getBordureFenetre().toString(), false);
            proprietesAccessoire.generateLayout();
            propertiesPanel.add(proprietesAccessoire);

            proprietesAccessoire.setOnChangeListener(values -> {
                Imperial x = proprietesAccessoire.getImperial("x");
                Imperial y = proprietesAccessoire.getImperial("y");
                Imperial largeur = proprietesAccessoire.getImperial("largeur");
                Imperial hauteur = proprietesAccessoire.getImperial("hauteur");
                Imperial bordure = proprietesAccessoire.getImperial("brodureFenetre");

                int result = gestionnaireSalle.editAccessoireSelectionne(new AccessoireDTO(x, y, hauteur, largeur, bordure, accessoireSelect.getTypeAccessoire()));

                proprietesAccessoire.setError("x", result == -1);
                proprietesAccessoire.setError("y", result == -1);
                proprietesAccessoire.setError("largeur", result == -1);
                proprietesAccessoire.setError("hauteur", result == -1);
                proprietesAccessoire.setError("brodureFenetre", result == -1);

                if(result == -1)
                {
                    setWarningMsg("Dimension de l'accessoire invalide.");
                }
                mainPanel.validate();
                mainPanel.repaint();
            });
        }

        propertiesPanel.validate();
        propertiesPanel.repaint();
    }
    public void resetButtonView(){
        btnElvEstINT.setBorder(null);
        btnElvEstEXT.setBorder(null);
        btnElvNordEXT.setBorder(null);
        btnElvNordINT.setBorder(null);
        btnElvOuestEXT.setBorder(null);
        btnElvOuestINT.setBorder(null);
        btnElvSudEXT.setBorder(null);
        btnELVSudINT.setBorder(null);
        btnPlan.setBorder(null);
    }
    public void resetButtonAccessoires(){
        Border border = BorderFactory.createLineBorder(Color.red);
        btnSelection.setBorder(AccessoireEnum == Utilitaire.AccessoireEnum.Selection ? border : null);
        btnMove.setBorder(AccessoireEnum == Utilitaire.AccessoireEnum.Move ? border:null);
        btnSupprimer.setBorder(AccessoireEnum == Utilitaire.AccessoireEnum.Supprimer ? border : null);
        btnRetourAir.setBorder(AccessoireEnum == Utilitaire.AccessoireEnum.RetourAir ? border : null);
        btnPrise.setBorder(AccessoireEnum == Utilitaire.AccessoireEnum.PriseElectrique ? border : null);
        btnPorte.setBorder(AccessoireEnum == Utilitaire.AccessoireEnum.Porte ? border : null);
        btnFenetre.setBorder(AccessoireEnum == Utilitaire.AccessoireEnum.Fenetre ? border : null);
        btnSeparateur.setBorder(AccessoireEnum == Utilitaire.AccessoireEnum.Separateur ? border : null);

        boolean estEnVuePlan = gestionnaireSalle.GetvuePlan();
        btnRetourAir.setVisible(estEnVuePlan || interieur);
        btnPrise.setVisible(!estEnVuePlan && interieur);
        btnPorte.setVisible(!estEnVuePlan);
        btnFenetre.setVisible(!estEnVuePlan);
    }



    {
        $$$setupUI$$$();
    }
    private void setHomePage(MouseEvent e){
        Component component = (Component) e.getComponent();
        JFrame frame = (JFrame) SwingUtilities.getRoot(component);
        frame.setContentPane(mainWindow.rootPanel);
        frame.pack();
//        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }

    public static void setWarningMsg(String text){
        //Toolkit.getDefaultToolkit().beep();
        JOptionPane alertTextBox = new JOptionPane(text, JOptionPane.WARNING_MESSAGE);
      //  alertTextBox.setBackground(Color.red);
        JDialog dialog = alertTextBox.createDialog("ERREUR!");
        //dialog.setLocation(1000,100);

        dialog.setBackground(Color.red);

        dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(1000);
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
            dialog.setVisible(false);
            }
        }).start();

        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);

    }

    public static void addWarningMsg(String text){
        //TODO message en haut a droite qui affiche les erreurs dans une liste : label qui affiche les erreurs
    }

    public static void removeWarningMsg(String text){
        //TODO enlève les erreurs résolue dans le warning message
    }
    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        starterPanel = new JPanel();
        starterPanel.setBackground(new Color(60,64,65));
        title = new JLabel();
        title.setHorizontalAlignment((int)JPanel.CENTER_ALIGNMENT);
        title.setFont(new Font("Segoe Script",0,48));
        title.setForeground(new Color(255,255,255));
        title.setText("Muracle");
        creerUnNouveauProjetButton = new JButton();
        creerUnNouveauProjetButton.setText("Créer un Nouveau Projet");
        creerUnNouveauProjetButton.setForeground(new Color(255,255,255));
        creerUnNouveauProjetButton.setBackground(new Color(60,64,65));
        ouvrirUnProjectExistantButton  = new JButton();
        ouvrirUnProjectExistantButton.setText("Ouvrir un projet Existant");
        ouvrirUnProjectExistantButton.setForeground(new Color(255,255,255));
        ouvrirUnProjectExistantButton.setBackground(new Color(60,64,65));

        btnDimensionsCollapse = new JButton();
        DimensionsPanel = new JPanel();
        propertiesPanel = new JPanel();
        dimensionPanelContent = new JPanel();
        dimLargeurPanel = new JPanel();
        tbProfondeur = new JTextField();
        mainPanel = new JPanel();
        btnSupprimer = new JButton();
        btnRetourAir = new JButton();
        btnPrise = new JButton();
        btnPorte = new JButton();
        btnFenetre = new JButton();
        btnGrille = new JButton();
        btnRedo = new JButton();
        btnUndo = new JButton();
        btnSave = new JButton();
        btnSeparateur = new JButton();
        btnSelection = new JButton();
        btnMove = new JButton();
        buttonsPanel = new JPanel();
        controlPanel = new JPanel();
        rightPanel = new JPanel();
        tbPliSoudure = new JTextField();
        tbLargeurPli = new JTextField();
        tbEpaisseurMurs = new JTextField();
        tbHauteur = new JTextField();

        container = Box.createVerticalBox();
        container.add(title);
        container.add(creerUnNouveauProjetButton);
        container.add(Box.createRigidArea(new Dimension(0,5)));
        container.add(ouvrirUnProjectExistantButton);
        starterPanel.add(container);

        rootPanel = new JPanel();
        rootPanel.setLayout(new BorderLayout(0, 0));
        rootPanel.setBackground(new Color(-12763843));
        propertiesPanel.setLayout(new BoxLayout(propertiesPanel, BoxLayout.PAGE_AXIS));
        propertiesPanel.setBackground(new Color(-8882056));
        propertiesPanel.setEnabled(true);
        propertiesPanel.setMinimumSize(new Dimension(235, 0));
        propertiesPanel.setPreferredSize(new Dimension(235, 700));



        JScrollPane propertiesScroll = new JScrollPane(propertiesPanel);
        propertiesScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        propertiesScroll.setPreferredSize(new Dimension(235, 0));
        rootPanel.add(propertiesScroll, BorderLayout.WEST);

        /*proprietesSalle = new PanelProprietes("DIMENSIONS DE LA SALLE", 150);
        proprietesSalle.addProperty("largeur", "LARGEUR :");
        proprietesSalle.addProperty("profondeur", "PROFONDEUR :");
        proprietesSalle.addProperty("hauteur", "HAUTEUR :");
        proprietesSalle.addProperty("epaisseurMur", "ÉPAISSEUR MURS :");
        proprietesSalle.addProperty("largeurPli", "LARGEUR DE PLI :");
        proprietesSalle.addProperty("pliSoudure", "PLI DE SOUDURE :");
        proprietesSalle.addProperty("hauteurRetourAir", "RETOUR AIR :");
        proprietesSalle.addProperty("positionRetourAir", "POS RETOUR AIR :");
        proprietesSalle.addProperty("hauteurTrouRetourAir", "TROU RETOUR AIR :");
        proprietesSalle.generateLayout();
        propertiesPanel.add(proprietesSalle);

        proprietesSalle.setOnChangeListener(values -> {
            Imperial largeur = proprietesSalle.getImperial("largeur");
            Imperial profondeur = proprietesSalle.getImperial("profondeur");
            Imperial hauteur = proprietesSalle.getImperial("hauteur");
            Imperial epaisseurMur = proprietesSalle.getImperial("epaisseurMur");
            Imperial largeurPli = proprietesSalle.getImperial("largeurPli");
            int pliSoudure = proprietesSalle.getInt("pliSoudure");
            Imperial hauteurRetourAir = proprietesSalle.getImperial("hauteurRetourAir");
            Imperial positionRetourAir = proprietesSalle.getImperial("positionRetourAir");
            Imperial hauteurTrouRetourAir = proprietesSalle.getImperial("hauteurTrouRetourAir");

            if(largeur == null || profondeur == null || hauteur == null || epaisseurMur == null || largeurPli == null ||
                    pliSoudure == -1 || hauteurRetourAir == null || positionRetourAir == null || hauteurTrouRetourAir == null)
                return;

            int result = gestionnaireSalle.editSalleSelectionne(new SalleDTO(largeur, profondeur, hauteur, epaisseurMur, largeurPli, pliSoudure, hauteurRetourAir, positionRetourAir, hauteurTrouRetourAir));

            if(result == 0)
            {
                mainPanel.validate();
                mainPanel.repaint();
            }

            proprietesSalle.setError("largeur", result == 1);
            proprietesSalle.setError("profondeur", result == 2);
            proprietesSalle.setError("hauteurTrouRetourAir", result == 3);
            proprietesSalle.setError("positionRetourAir", result == 4);
            proprietesSalle.setError("hauteurRetourAir", result == 4);

            if(result ==1){
                setWarningMsg("largeur invalide");
            }
            if(result == 2){
                setWarningMsg("profondeur invalide");
            }
            if(result == 3 ){
                setWarningMsg("L'hauteur du trou de 'Retour d'air' est invalide");
            }
            if (result == 4){
                setWarningMsg("position ou hauteur du retour d'air invalide");
            }

        });

        proprietesMur = new PanelProprietes("DIMENSIONS DU MUR", 0);
        proprietesMur.addProperty("x", "POSITION X :", "", true);
        proprietesMur.addProperty("y", "POSITION Y :", "", true);
        proprietesMur.addProperty("largeur", "LARGEUR :", "", true);
        proprietesMur.generateLayout();
        propertiesPanel.add(proprietesMur);

        proprietesMur.setOnChangeListener(values -> {
            Imperial largeurRetourAir = proprietesMur.getImperial("largeurRetourAir");

            if(largeurRetourAir != null)
            {
                boolean result = gestionnaireSalle.editMurSelectionne(largeurRetourAir);

                proprietesMur.setError("largeurRetourAir", !result);
                mainPanel.validate();
                mainPanel.repaint();
            }
        });

        proprietesSeparateur = new PanelProprietes("SÉPARATEUR", 100);
        proprietesSeparateur.addProperty("pos", "POSITION :", "", true);
        proprietesSeparateur.addProperty("posRel", "SEP. PRÉCÉDENT :");
        proprietesSeparateur.generateLayout();
        propertiesPanel.add(proprietesSeparateur);

        proprietesSeparateur.setOnChangeListener(values -> {

            Imperial posRel = proprietesSeparateur.getImperial("posRel");

            if(posRel != null && gestionnaireSalle.editSeparateurSelectionne(posRel))
            {
                proprietesSeparateur.setError("posRel", false);
                SeparateurDTO newValue = gestionnaireSalle.getSeparateurSelectionne();
                proprietesSeparateur.setValue("pos", newValue.getPosition().toString());

                mainPanel.validate();
                mainPanel.repaint();
            }
            else
                proprietesSeparateur.setError("posRel", true);
                setWarningMsg("separateur en erreur");

        });*/

        rightPanel.setLayout(new BorderLayout(0, 0));
        rootPanel.add(rightPanel, BorderLayout.CENTER);
        controlPanel.setLayout(new BorderLayout(0, 0));
        controlPanel.setBackground(new Color(-12829632));
        controlPanel.setMinimumSize(new Dimension(24, 230));
        controlPanel.setOpaque(true);
        controlPanel.setPreferredSize(new Dimension(24, 230));
        rightPanel.add(controlPanel, BorderLayout.NORTH);
        buttonsPanel.setLayout(new GridBagLayout());
        buttonsPanel.setBackground(new Color(-12829633));
        controlPanel.add(buttonsPanel, BorderLayout.WEST);
        btnSave.setBackground(new Color(-12829636));
        btnSave.setIcon(new ImageIcon(getClass().getResource("/buttons/save.png")));
        btnSave.setMargin(new Insets(0, 0, 0, 0));
        btnSave.setMaximumSize(new Dimension(50, 50));
        btnSave.setMinimumSize(new Dimension(50, 50));
        btnSave.setPreferredSize(new Dimension(50, 50));
        btnSave.setText("");
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.insets = new Insets(2, 2, 2, 2);
        buttonsPanel.add(btnSave, gbc);
        btnUndo.setBackground(new Color(-12829636));
        btnUndo.setIcon(new ImageIcon(getClass().getResource("/buttons/undo.png")));
        btnUndo.setMargin(new Insets(0, 0, 0, 0));
        btnUndo.setMaximumSize(new Dimension(50, 50));
        btnUndo.setMinimumSize(new Dimension(50, 50));
        btnUndo.setPreferredSize(new Dimension(50, 50));
        btnUndo.setText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(2, 2, 2, 2);
        buttonsPanel.add(btnUndo, gbc);
        btnRedo.setBackground(new Color(-12829636));
        btnRedo.setIcon(new ImageIcon(getClass().getResource("/buttons/redo.png")));
        btnRedo.setMargin(new Insets(0, 0, 0, 0));
        btnRedo.setMaximumSize(new Dimension(50, 50));
        btnRedo.setMinimumSize(new Dimension(50, 50));
        btnRedo.setPreferredSize(new Dimension(50, 50));
        btnRedo.setText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(2, 2, 2, 2);
        buttonsPanel.add(btnRedo, gbc);
        btnGrille.setBackground(new Color(-12829636));
        btnGrille.setIcon(new ImageIcon(getClass().getResource("/buttons/grille.png")));
        btnGrille.setMargin(new Insets(0, 0, 0, 0));
        btnGrille.setMaximumSize(new Dimension(50, 50));
        btnGrille.setMinimumSize(new Dimension(50, 50));
        btnGrille.setPreferredSize(new Dimension(50, 50));
        btnGrille.setText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(2, 2, 2, 2);
        buttonsPanel.add(btnGrille, gbc);
        btnSelection.setBorder(BorderFactory.createLineBorder(Color.red));
        btnSelection.setBackground(new Color(-12829636));
        btnSelection.setIcon(new ImageIcon(getClass().getResource("/buttons/selection.png")));
        btnSelection.setMargin(new Insets(0, 0, 0, 0));
        btnSelection.setMaximumSize(new Dimension(50, 50));
        btnSelection.setMinimumSize(new Dimension(50, 50));
        btnSelection.setPreferredSize(new Dimension(50, 50));
        btnSelection.setText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(2, 2, 2, 2);
        buttonsPanel.add(btnSelection, gbc);
        btnMove.setBorder(BorderFactory.createLineBorder(Color.red));
        btnMove.setBackground(new Color(-12829636));
        btnMove.setIcon(new ImageIcon(getClass().getResource("/buttons/move.png")));
        btnMove.setMargin(new Insets(0,0,0,0));
        btnMove.setMaximumSize(new Dimension(50,50));
        btnMove.setMinimumSize(new Dimension(50,50));
        btnMove.setPreferredSize(new Dimension(50,50));
        btnMove.setText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets (2,2,2,2);
        buttonsPanel.add(btnMove, gbc);
        btnFenetre.setBackground(new Color(-12829636));
        btnFenetre.setVisible(false);
        btnFenetre.setIcon(new ImageIcon(getClass().getResource("/buttons/fenetre.png")));
        btnFenetre.setMargin(new Insets(0, 0, 0, 0));
        btnFenetre.setMaximumSize(new Dimension(50, 50));
        btnFenetre.setMinimumSize(new Dimension(50, 50));
        btnFenetre.setPreferredSize(new Dimension(50, 50));
        btnFenetre.setText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(2, 2, 2, 2);
        buttonsPanel.add(btnFenetre, gbc);
        btnPorte.setBackground(new Color(-12829636));
        btnPorte.setVisible(false);
        btnPorte.setIcon(new ImageIcon(getClass().getResource("/buttons/porte.png")));
        btnPorte.setMargin(new Insets(0, 0, 0, 0));
        btnPorte.setMaximumSize(new Dimension(50, 50));
        btnPorte.setMinimumSize(new Dimension(50, 50));
        btnPorte.setPreferredSize(new Dimension(50, 50));
        btnPorte.setText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(2, 2, 2, 2);
        buttonsPanel.add(btnPorte, gbc);
        btnPrise.setBackground(new Color(-12829636));
        btnPrise.setVisible(false);
        btnPrise.setIcon(new ImageIcon(getClass().getResource("/buttons/prise.png")));
        btnPrise.setMargin(new Insets(0, 0, 0, 0));
        btnPrise.setMaximumSize(new Dimension(50, 50));
        btnPrise.setMinimumSize(new Dimension(50, 50));
        btnPrise.setPreferredSize(new Dimension(50, 50));
        btnPrise.setText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(2, 2, 2, 2);
        buttonsPanel.add(btnPrise, gbc);
        btnRetourAir.setBackground(new Color(-12829636));
        btnRetourAir.setIcon(new ImageIcon(getClass().getResource("/buttons/air.png")));
        btnRetourAir.setMargin(new Insets(0, 0, 0, 0));
        btnRetourAir.setMaximumSize(new Dimension(50, 50));
        btnRetourAir.setMinimumSize(new Dimension(50, 50));
        btnRetourAir.setPreferredSize(new Dimension(50, 50));
        btnRetourAir.setText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(2, 2, 2, 2);
        buttonsPanel.add(btnRetourAir, gbc);
        btnSupprimer.setBackground(new Color(-12829636));
        btnSupprimer.setIcon(new ImageIcon(getClass().getResource("/buttons/supprimer.png")));
        btnSupprimer.setMargin(new Insets(0, 0, 0, 0));
        btnSupprimer.setMaximumSize(new Dimension(50, 50));
        btnSupprimer.setMinimumSize(new Dimension(50, 50));
        btnSupprimer.setPreferredSize(new Dimension(50, 50));
        btnSupprimer.setText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(2, 2, 2, 2);
        buttonsPanel.add(btnSupprimer, gbc);
        mainPanel.setLayout(new BorderLayout(0, 0));
        mainPanel.setAutoscrolls(true);
        mainPanel.setBackground(new Color(-1));
        mainPanel.setMinimumSize(new Dimension(200, 24));
        mainPanel.setPreferredSize(new Dimension(200, 24));
        gbc = new GridBagConstraints();
        gbc.gridx = 6;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(2, 2, 2, 2);
        buttonsPanel.add(btnSeparateur, gbc);
        btnSeparateur.setMargin(new Insets(0, 0, 0, 0));
        btnSeparateur.setMaximumSize(new Dimension(70, 50));
        btnSeparateur.setMinimumSize(new Dimension(70, 50));
        btnSeparateur.setPreferredSize(new Dimension(70, 50));
        btnSeparateur.setText("Separateur");
        btnSeparateur.setBackground(Color.white);
        rightPanel.add(mainPanel, BorderLayout.CENTER);


        btnElvOuestEXT = new JButton();
        btnElvOuestEXT.setBackground(new Color(-12829636));
        btnElvOuestEXT.setIcon(new ImageIcon(getClass().getResource("/buttons/exterieurOuest.png")));
        btnElvOuestEXT.setMargin(new Insets(0,0,0,0));
        btnElvOuestEXT.setMaximumSize(new Dimension(30,50));
        btnElvOuestEXT.setMinimumSize(new Dimension(30,50));
        btnElvOuestEXT.setPreferredSize(new Dimension(30,50));
        btnElvOuestEXT.setText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 11;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(2,2,2,2);
        buttonsPanel.add(btnElvOuestEXT, gbc);

        btnElvOuestINT = new JButton();
        btnElvOuestINT.setBackground(new Color(-12829636));
        btnElvOuestINT.setIcon(new ImageIcon(getClass().getResource("/buttons/interieurOuest.png")));
        btnElvOuestINT.setMargin(new Insets(0,0,0,0));
        btnElvOuestINT.setMaximumSize(new Dimension(30,50));
        btnElvOuestINT.setMinimumSize(new Dimension(30,50));
        btnElvOuestINT.setPreferredSize(new Dimension(30,50));
        btnElvOuestINT.setText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 12;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(2,2,2,2);
        buttonsPanel.add(btnElvOuestINT, gbc);

        btnElvNordEXT = new JButton();
        btnElvNordEXT.setBackground(new Color(-12829636));
        btnElvNordEXT.setIcon(new ImageIcon(getClass().getResource("/buttons/exterieurNord.png")));
        btnElvNordEXT.setMargin(new Insets(0,0,0,0));
        btnElvNordEXT.setMaximumSize(new Dimension(30,30));
        btnElvNordEXT.setMinimumSize(new Dimension(30,30));
        btnElvNordEXT.setPreferredSize(new Dimension(30,30));
        btnElvNordEXT.setText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 13;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(50,2,0,2);
        buttonsPanel.add(btnElvNordEXT, gbc);

        btnElvNordINT = new JButton();
        btnElvNordINT.setBackground(new Color(-12829636));
        btnElvNordINT.setIcon(new ImageIcon(getClass().getResource("/buttons/interieurNord.png")));
        btnElvNordINT.setMargin(new Insets(0,0,0,0));
        btnElvNordINT.setMaximumSize(new Dimension(50,30));
        btnElvNordINT.setMinimumSize(new Dimension(50,30));
        btnElvNordINT.setPreferredSize(new Dimension(50,30));
        btnElvNordINT.setText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 13;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(2,2,18,2);
        buttonsPanel.add(btnElvNordINT, gbc);

        btnElvSudEXT = new JButton();
        btnElvSudEXT.setBackground(new Color(-12829636));
        btnElvSudEXT.setIcon(new ImageIcon(getClass().getResource("/buttons/exterieurSud.png")));
        btnElvSudEXT.setMargin(new Insets(0,0,0,0));
        btnElvSudEXT.setMaximumSize(new Dimension(30,30));
        btnElvSudEXT.setMinimumSize(new Dimension(30,30));
        btnElvSudEXT.setPreferredSize(new Dimension(30,30));
        btnElvSudEXT.setText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 13;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0,2,50,2);
        buttonsPanel.add(btnElvSudEXT, gbc);

        btnELVSudINT = new JButton();
        btnELVSudINT.setBackground(new Color(-12829636));
        btnELVSudINT.setIcon(new ImageIcon(getClass().getResource("/buttons/interieurSud.png")));
        btnELVSudINT.setMargin(new Insets(0,0,0,0));
        btnELVSudINT.setMaximumSize(new Dimension(50,30));
        btnELVSudINT.setMinimumSize(new Dimension(50,30));
        btnELVSudINT.setPreferredSize(new Dimension(50,30));
        btnELVSudINT.setText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 13;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(18,2,2,2);
        buttonsPanel.add(btnELVSudINT, gbc);

        btnElvEstEXT = new JButton();
        btnElvEstEXT.setBackground(new Color(-12829636));
        btnElvEstEXT.setIcon(new ImageIcon(getClass().getResource("/buttons/exterieurEst.png")));
        btnElvEstEXT.setMargin(new Insets(0,0,0,0));
        btnElvEstEXT.setMaximumSize(new Dimension(30,50));
        btnElvEstEXT.setMinimumSize(new Dimension(30,50));
        btnElvEstEXT.setPreferredSize(new Dimension(30,50));
        btnElvEstEXT.setText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 15;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(2,2,2,2);
        buttonsPanel.add(btnElvEstEXT, gbc);

        btnElvEstINT = new JButton();
        btnElvEstINT.setIcon(new ImageIcon(getClass().getResource("/buttons/interieurEst.png")));
        btnElvEstINT.setBackground(new Color(-1));
        btnElvEstINT.setMargin(new Insets(0,0,0,0));
        btnElvEstINT.setMaximumSize(new Dimension(30,50));
        btnElvEstINT.setMinimumSize(new Dimension(30,50));
        btnElvEstINT.setPreferredSize(new Dimension(30,50));
        btnElvEstINT.setText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 14;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(2,2,2,2);
        buttonsPanel.add(btnElvEstINT, gbc);

        btnPlan = new JButton();
        btnPlan.setIcon(new ImageIcon(getClass().getResource("/buttons/plan.png")));
        btnPlan.setBackground(new Color(-1));
        btnPlan.setMargin(new Insets(0,0,0,0));
        btnPlan.setMaximumSize(new Dimension(30,50));
        btnPlan.setMinimumSize(new Dimension(30,50));
        btnPlan.setPreferredSize(new Dimension(30,50));
        btnPlan.setText("");
        btnPlan.setBorder(BorderFactory.createLineBorder(Color.blue));
        gbc = new GridBagConstraints();
        gbc.gridx = 13;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(2,2,2,2);
        buttonsPanel.add(btnPlan, gbc);

    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return rootPanel;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
