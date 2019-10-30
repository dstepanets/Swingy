package unit.swingy.view.gui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import lombok.Getter;
import unit.swingy.controller.Game;
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

	private JButton bPlay;
	private JButton bAddHero;
	private JButton bDeleteHero;


	public ChooseHeroGui() {

		this.frame = new JFrame("Choose your hero");
		$$$setupUI$$$();
		updateTable();

		//		select table row event
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent event) {
				hero = heroesList.get(table.getSelectedRow());
				displayAvatar();
				displayHeroStats();
			}
		});

		// '+' button event - create New Hero
		bAddHero.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				createNewHero();
			}
		});

		// '-' button event - remove selected Hero
		bDeleteHero.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				deleteHero();
			}
		});

		// set selected Hero for the Game and close the window
		bPlay.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				if (hero != null) {
					setHeroAndClose();
				}
			}
		});
	}


	public void chooseHero() {

		System.out.println(">> Starting GUI ChooseHero method...");

		//init frame

		frame.setContentPane(this.getMainPanel());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);


	}

	private void updateTable() {

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

		// make cells uneditable
		DefaultTableModel tableModel = new DefaultTableModel(data, columns) {
			@Override
			public boolean isCellEditable(int row, int column) {
				//all cells false
				return false;
			}
		};

		table.setModel(tableModel);

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
		ImageIcon icon = new ImageIcon(hero.getClas().getAvatar());
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

//		GET THE HERO CLASS FROM A DIALOG

//		classes list
		final JFrame f = new JFrame();
		DefaultListModel<String> lm = new DefaultListModel<>();
		for (HeroClass c : HeroClass.values()) {
			lm.addElement(c.toString());
		}
		final JList<String> list = new JList<>(lm);
		list.setBounds(20, 20, 150, 256);

//		add avatar
		final JLabel lAvatar = new JLabel();
		lAvatar.setBounds(224, 20, 256, 256);

//		add description
		final JLabel lInfo = new JLabel();
		lInfo.setBounds(20, 282, 400, 128);

//		buttons
		JButton bCancel = new JButton("Cancel");
		bCancel.setBounds(50, 430, 120, 40);
		JButton bOk = new JButton("Create Hero");
		bOk.setBounds(330, 430, 120, 40);

		f.add(list);
		f.add(lAvatar);
		f.add(lInfo);
		f.add(bCancel);
		f.add(bOk);
		f.setSize(500, 500);
		f.setLayout(null);
		f.setVisible(true);

		// display avatar and description when a list item is selected
		list.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent event) {
				HeroClass clas = HeroClass.valueOf(list.getSelectedValue());
				lAvatar.setIcon(new ImageIcon(clas.getAvatar()));
				String description = "<html>" + clas.getDescription() + "<br><br>" +
						clas.getStartingStatsInfo() + "</html>";
				lInfo.setText(description);
			}
		});

		//CreateHero button listener
		bOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (list.getSelectedIndex() != -1) {
					hb.setClas(list.getSelectedValue());
					db.addHero(hb.getHero());

//						frame.removeAll();
//						createTable();
//						frame.repaint();
//						frame.revalidate();
//						((DefaultTableModel) table.getModel()).fireTableDataChanged();
					f.dispose();
					updateTable();
				}
			}
		});

		//Cancel button listener
		bCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (list.getSelectedIndex() != -1) {
					hb.reset();
					f.dispose();
				}
			}
		});

	}

	private void deleteHero() {

		// Confirmation dialog
		Object[] options = {"MUST DIE!!!11", "Cancel"};
		int n = JOptionPane.showOptionDialog(frame,
				"Are you sure you want to eliminate this jerk\nFOREVER AND EVER, with no going back?",
				"Delete Hero?",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,
				options,
				options[1]);

		System.out.println("> pressed = " + n);

		//	remove hero from the Database
		if (n == 0) {
			int row = table.getSelectedRow();
			System.out.println("> selected row = " + row);
			int id = heroesList.get(row).getId();
			System.out.println("> hero's id = " + id);
			db.removeHero(id);
			updateTable();
		}

	}


	private void setHeroAndClose() {
			Game.getInstance().setHero(hero);
			frame.dispose();
	}







	/**
	 * Method generated by IntelliJ IDEA GUI Designer
	 * >>> IMPORTANT!! <<<
	 * DO NOT edit this method OR call it in your code!
	 *
	 * @noinspection ALL
	 */
	private void $$$setupUI$$$() {
		mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayoutManager(4, 4, new Insets(0, 0, 0, 0), -1, -1));
		bPlay = new JButton();
		bPlay.setText("Play");
		mainPanel.add(bPlay, new GridConstraints(3, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final Spacer spacer1 = new Spacer();
		mainPanel.add(spacer1, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
		bDeleteHero = new JButton();
		bDeleteHero.setText("-");
		mainPanel.add(bDeleteHero, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(27, 30), null, 0, false));
		bAddHero = new JButton();
		bAddHero.setText("+");
		mainPanel.add(bAddHero, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		bioPane = new JScrollPane();
		mainPanel.add(bioPane, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, new Dimension(-1, 128), 0, false));
		bio = new JTextPane();
		bio.setEditable(false);
		bioPane.setViewportView(bio);
		statsPane = new JScrollPane();
		mainPanel.add(statsPane, new GridConstraints(1, 3, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(256, 256), new Dimension(320, -1), null, 0, false));
		statsPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), null));
		stats = new JTextPane();
		stats.setEditable(false);
		Font statsFont = this.$$$getFont$$$("Ayuthaya", Font.BOLD, 14, stats.getFont());
		if (statsFont != null) stats.setFont(statsFont);
		stats.setText("Select a hero");
		statsPane.setViewportView(stats);
		avatar = new JLabel();
		avatar.setText("");
		mainPanel.add(avatar, new GridConstraints(0, 0, 2, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(256, 256), new Dimension(320, 320), null, 0, false));
		paneLeft = new JScrollPane();
		mainPanel.add(paneLeft, new GridConstraints(2, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
		paneLeft.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), null));
		table = new JTable();
		table.setEnabled(true);
		table.setFillsViewportHeight(true);
		Font tableFont = this.$$$getFont$$$("Ayuthaya", -1, 14, table.getFont());
		if (tableFont != null) table.setFont(tableFont);
		paneLeft.setViewportView(table);
		avatar.setLabelFor(bio);
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
		return new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
	}

	/**
	 * @noinspection ALL
	 */
	public JComponent $$$getRootComponent$$$() {
		return mainPanel;
	}

	private void createUIComponents() {
		// TODO: place custom component creation code here
//		createTable();

	}


}
