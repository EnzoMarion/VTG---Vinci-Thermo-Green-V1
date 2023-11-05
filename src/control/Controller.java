package control;

import Model.*;
import View.*;
import dao.*;
import java.util.ArrayList;

import javax.swing.SwingUtilities;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.text.ParseException;

public class Controller {
	
	private ArrayList<Mesure> lesMesures = new ArrayList<Mesure>();
	private Utilisateur leUser;
	
	private ConsoleGUI laConsole;
	private InterfaceConnexion leLogin;
	
	private MesureDAO myStubMesure;
	private UtilisateurDAO myStubUser;
	
	public Controller() {
        leLogin = new InterfaceConnexion(this);
        leLogin.init();
    }
	
	public void connecteAvecSucces() {
        SwingUtilities.invokeLater(() -> {
            ConsoleGUI monIHM = null;
            try {
                monIHM = new ConsoleGUI(this);
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
            monIHM.setVisible(true);
        });
    }

	public ArrayList<Mesure> filtrerLesMesure(String string) {
		return null;
	}
}
