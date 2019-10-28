package unit.swingy.view.gui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import lombok.Getter;
import unit.swingy.model.characters.DataBase;
import unit.swingy.model.characters.Hero;
import unit.swingy.model.characters.HeroBuilder;
import unit.swingy.model.characters.HeroClass;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

@Getter
public class ChooseHeroGui {

	private DataBase db = DataBase.getInstance();
	private HeroBuilder hb = new HeroBuilder();

	private Hero hero = null;
	private ArrayList<Hero> heroesList;

	private JFrame frame;
	private JPanel mainPanel;
	private JScrollPane paneLeft;
	private JLabel avatar;
	private JTable table;

	private JScrollPane bioPane;
	private JTextPane bio;
	private JScrollPane statsPane;
	private JTextPane stats;

	private JButton play;
	private JButton addHero;
	private JButton deleteHero;


	public ChooseHeroGui() {

		$$$setupUI$$$();
		frame = new JFrame("Choose your hero");

		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent event) {
				hero = heroesList.get(table.getSelectedRow());
				displayAvatar();
				displayHeroStats();
			}
		});

		addHero.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				createNewHero();
			}
		});
	}

	public Hero chooseHero() {

		System.out.println(">> Starting GUI ChooseHero method...");


		//init frame

		frame.setContentPane(this.getMainPanel());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);


		return hero;
	}

	private void createTable() {

		heroesList = db.getHeroesList(hb);
		int rows = heroesList.size();

		String[] columns = {"NAME", "CLASS", "LEVEL", "EXP"};
		Object[][] data = new Object[rows][4];
		for (int r = 0; r < rows; r++) {
			data[r][0] = heroesList.get(r).getName();
			data[r][1] = heroesList.get(r).getClas();
			data[r][2] = heroesList.get(r).getLevel();
			data[r][3] = heroesList.get(r).getExp();
		}

		DefaultTableModel tableModel = new DefaultTableModel(data, columns) {
			@Override
			public boolean isCellEditable(int row, int column) {
				//all cells false
				return false;
			}
		};

		table = new JTable(tableModel);
		table.setFillsViewportHeight(true);

	}

	private void displayHeroStats() {

		bio.setText("");
		stats.setText("");

		SimpleAttributeSet atr = new SimpleAttributeSet();
		StyleConstants.setFontSize(atr, 13);
		StyleConstants.setItalic(atr, true);

		Document bioDoc = bio.getStyledDocument();
		Document statsDoc = stats.getStyledDocument();

		try {
			bioDoc.insertString(statsDoc.getLength(), hero.getClas().getDescription() + "\n\n", atr);
			StyleConstants.setFontSize(atr, 14);
			StyleConstants.setItalic(atr, false);

			statsDoc.insertString(statsDoc.getLength(), "Name: " + hero.getName() + "\n", atr);
			statsDoc.insertString(statsDoc.getLength(), "Class: " + hero.getClas() + "\n\n", atr);
			statsDoc.insertString(statsDoc.getLength(), "Level: " + hero.getLevel() + "\n", atr);
			statsDoc.insertString(statsDoc.getLength(), "Exp: " + hero.getExp() + "\n\n", atr);
			statsDoc.insertString(statsDoc.getLength(), "HP: " + hero.getHp() + "\n", atr);
			statsDoc.insertString(statsDoc.getLength(), "Attack: " + hero.getAttack() + "\n", atr);
			statsDoc.insertString(statsDoc.getLength(), "Defence: " + hero.getDefence() + "\n\n", atr);
			statsDoc.insertString(statsDoc.getLength(), "Weapon: " + hero.getWeapon() + "\n", atr);
			statsDoc.insertString(statsDoc.getLength(), "Armor: " + hero.getArmor() + "\n", atr);
			statsDoc.insertString(statsDoc.getLength(), "Helm: " + hero.getHelm() + "\n", atr);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	private void displayAvatar() {
		String path = "src/main/resources/img/" + hero.getClas().getClassName() + ".jpg";
		ImageIcon icon = new ImageIcon(path);
		avatar.setIcon(icon);
	}

	private void createNewHero() {

//		get a name from input
		String input = "";
		do {
			input = (String) JOptionPane.showInputDialog(frame, "Enter your Hero's name:",
					"New Hero's Name", JOptionPane.PLAIN_MESSAGE);
			if (input == null)
				return;
		} while (input.trim().isEmpty());

		hb.reset();
		hb.setName(input);

//		get a class from dialog
		Object[] options = new Object[HeroClass.count];
		int i = 0;
		for (HeroClass c : HeroClass.values()) {
			options[i++] = c.getClassName();
		}

//		TODO: this dialog is not working yet. But looks promising:)
		input = "";
		HeroClass clas = HeroClass.REGULAR;
		do {
			String msg = clas.getDescription() + "\n" + clas.getStartingStatsInfo() + "\n";
			String path = "src/main/resources/img/" + clas.getClassName() + ".jpg";
			ImageIcon icon = new ImageIcon(path);
			input = (String) JOptionPane.showInputDialog(frame, msg, "New Hero's class",
					JOptionPane.PLAIN_MESSAGE, icon, options, "REGULAR");
			if (input == null) {
				hero = null;
				return;
			}
			clas = HeroClass.valueOf(input);
		} while (true);


//		hero = hb.getHero();
	}






	/**
	 * Method generated by IntelliJ IDEA GUI Designer
	 * >>> IMPORTANT!! <<<
	 * DO NOT edit this method OR call it in your code!
	 *
	 * @noinspection ALL
	 */
	private void $$$setupUI$$$() {
		createUIComponents();
		mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayoutManager(2, 4, new Insets(0, 0, 0, 0), -1, -1));
		paneLeft = new JScrollPane();
		mainPanel.add(paneLeft, new GridConstraints(0, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
		paneLeft.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), null));
		paneLeft.setViewportView(table);
		statsPane = new JScrollPane();
		mainPanel.add(statsPane, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(200, -1), null, 0, false));
		statsPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), null));
		stats = new JTextPane();
		stats.setEditable(false);
		stats.setText("Select a hero");
		statsPane.setViewportView(stats);
		play = new JButton();
		play.setText("Play");
		mainPanel.add(play, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final Spacer spacer1 = new Spacer();
		mainPanel.add(spacer1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
		deleteHero = new JButton();
		deleteHero.setText("-");
		mainPanel.add(deleteHero, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(27, 30), null, 0, false));
		addHero = new JButton();
		addHero.setText("+");
		mainPanel.add(addHero, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
	}

	/**
	 * @noinspection ALL
	 */
	public JComponent $$$getRootComponent$$$() {
		return mainPanel;
	}

	private void createUIComponents() {
		// TODO: place custom component creation code here
		createTable();

	}


}
