package unit.swingy.view.gui;

import unit.swingy.controller.Game;
import unit.swingy.model.Map;
import unit.swingy.model.MapTile;
import unit.swingy.model.artifacts.AArtifact;
import unit.swingy.model.artifacts.Armor;
import unit.swingy.model.artifacts.Helm;
import unit.swingy.model.artifacts.Weapon;
import unit.swingy.model.characters.Enemy;
import unit.swingy.model.characters.Hero;
import unit.swingy.view.IExploration;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import darrylbu.icon.StretchIcon;
import lombok.Getter;


public class ExplorationGui implements IExploration {

	private Game game;
	private Hero hero;
	private BattleGui battle;
	private Map map;
	private MapTile[][] grid;
	private JLabel[][] labels;

	private final int ICON_SIZE = 64;
	private final int SIDE_PANE_WIDTH = 256;
	private final int VIEW_SIZE = 9;
	private final int VIEW_DISTANCE = VIEW_SIZE / 2;
	private final int INFO_PANEL_HEIGHT = 100;
	private int winWidth;
	private int winHeight;

	//	test
	@Getter
	private boolean clicked = false;
	@Getter
	private boolean choice = false;

	@Getter
	private JFrame frame;
	private JPanel mainPanel;
	private JPanel mapBack;
	private JPanel mapHolder;

	private JScrollPane enemyPane;
	private JLabel enemyAvatar;
	private JLabel enemyClass;
	private JLabel enemyLevel;
	private JTextPane enemyStats;

	private JButton bFight;
	private JButton bFlee;
	private JButton bCons;

	private JScrollPane heroPane;
	private JLabel heroAvatar;
	private JLabel heroName;
	private JLabel heroClass;
	private JProgressBar expBar;
	private JProgressBar hpBar;
	private JTextPane heroStats;

	private JButton bN;
	private JButton bS;
	private JButton bE;
	private JButton bW;


	private JScrollPane infoPane;
	private JTextPane info;


	/*	------------------------- FORMATTING AND STYLING ------------------------- */
	//	TODO: clean the mess with style attributes

	private Border whiteBorder = BorderFactory.createLineBorder(Color.white);
	private Border blueBorder = BorderFactory.createLineBorder(Color.blue);
	private Border blackBorder = BorderFactory.createLineBorder(Color.black);
	private Border redBorder = BorderFactory.createLineBorder(Color.red);

	/*	-------------------------------------------------------------------------- */

//	TODO Pack the images
//	The location of the image is also important. If the image is external to the application
//	(somewhere on the file system), you can use ImageIO.read(new File("/path/to/image")).
//	However, if the the image is embedded within your application (stored within the Jar for example),
//	you will need to use something more like ImageIO.read(getClass().getResource("/path/to/image")) instead...

	public ExplorationGui() {
		System.out.println(">> Constructing Exploration GUI...");

		game = Game.getInstance();
		hero = game.getHero();

		$$$setupUI$$$();
		setupUIManual();
	}


	private void setupUIManual() {

		//		resize window
		winWidth = (VIEW_SIZE * ICON_SIZE) + (SIDE_PANE_WIDTH * 2);
		winHeight = VIEW_SIZE * ICON_SIZE + INFO_PANEL_HEIGHT;
		mainPanel.setPreferredSize(new Dimension(winWidth, winHeight));

		buildMap();
		updateMap();
		buildHeroPane();

//		init frame
		frame = new JFrame("World Exploration");
		frame.setContentPane(mainPanel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);    // center window on the screen
		frame.setResizable(false);
//		frame.setVisible(true);

		createEventsListeners();
	}

	private void createEventsListeners() {

		bN.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				game.moveHero('n');
			}
		});

		bS.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				game.moveHero('s');
			}
		});

		bW.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				game.moveHero('w');
			}
		});

		bE.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				game.moveHero('e');
			}
		});

		bFight.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				game.initBattle();
				resetButtons();
			}
		});

		bFlee.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (game.tryToFlee()) {
					game.escapeBattle();
				} else {
					printMessage("Sadly, your running is so sloooow...", TextStyle.red);
					game.initBattle();
				}
				resetButtons();
			}
		});

		bCons.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				game.setGuiMode(false);
				game.switchView();
			}
		});

		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				game.exitGame();
			}
		});

	}

	private void buildHeroPane() {
		heroAvatar.setIcon(hero.getClas().getAvatar());
		heroAvatar.setToolTipText(hero.getClas().getDescription());
		heroName.setText(hero.getName());
		updateHeroPane();
	}

	public void updateHeroPane() {
		heroClass.setText(hero.getClas().getClassName() + " (" + hero.getLevel() + " lvl)");

		expBar.setMaximum(hero.getExpToLevelUp());
		expBar.setValue(hero.getExp());
		expBar.setString("EXP: " + hero.getExp() + "/" + hero.getExpToLevelUp());

		hpBar.setMaximum(hero.getMaxHp());
		hpBar.setValue(hero.getHp());
		hpBar.setString("HP: " + hero.getHp() + "/" + hero.getMaxHp());

		heroStats.setText("");
		Document statsDoc = heroStats.getStyledDocument();
		try {
			statsDoc.insertString(statsDoc.getLength(), "Attack: " + hero.getAttack() + "\n", TextStyle.bold);
			statsDoc.insertString(statsDoc.getLength(), "Defence: " + hero.getDefence() + "\n\n", TextStyle.bold);
			statsDoc.insertString(statsDoc.getLength(), "Weapon: " + hero.getWeapon() + "\n", TextStyle.bold);
			statsDoc.insertString(statsDoc.getLength(), "Armor: " + hero.getArmor() + "\n", TextStyle.bold);
			statsDoc.insertString(statsDoc.getLength(), "Helm: " + hero.getHelm() + "\n", TextStyle.bold);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	public void updateEnemyPane() {

		Enemy enemy = game.getEnemy();

		if (enemy == null) {
			enemyAvatar.setIcon(null);
			enemyAvatar.setToolTipText(null);
			enemyClass.setText(null);
			enemyLevel.setText(null);
			enemyStats.setText(null);
		} else {
			enemyAvatar.setIcon(enemy.getClas().getAvatar());
			enemyAvatar.setToolTipText(enemy.getClas().getDescription());
			enemyClass.setText(enemy.getClas().getClassName());
			enemyLevel.setText("Level " + enemy.getLevel());

			enemyStats.setText("");
			Document statsDoc = enemyStats.getStyledDocument();
			try {
				statsDoc.insertString(statsDoc.getLength(), "HP: " + enemy.getHp() + " / " + enemy.getMaxHp() + "\n", TextStyle.bold);
				statsDoc.insertString(statsDoc.getLength(), "Attack: " + enemy.getAttack() + "\n", TextStyle.bold);
				statsDoc.insertString(statsDoc.getLength(), "Defence: " + enemy.getDefence() + "\n\n", TextStyle.bold);
				statsDoc.insertString(statsDoc.getLength(), enemy.getClas().getDescription(), TextStyle.italic);
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		}
	}

	public void printMessage(String msg, SimpleAttributeSet atr) {
		Document doc = info.getStyledDocument();
		try {
			doc.insertString(doc.getLength(), msg + "\n", atr);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	private class MapBack extends JPanel {
		Image img;

		public MapBack() {
			try {
				img = ImageIO.read(new File("src/main/resources/img/mapBack/Grass00.jpg"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void paintComponent(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;
			g2.drawImage(img, 0, 0, this);
		}
	}

	public void buildMap() {

//		set map background and create grid to hold labels
		mapBack = new MapBack();
		mapBack.setLayout(new GridLayout(VIEW_SIZE, VIEW_SIZE));
//		mapBack.setPreferredSize(new Dimension(VIEW_SIZE * ICON_SIZE, VIEW_SIZE * ICON_SIZE));
		mapHolder.add(mapBack, new GridConstraints(0, 0, 1, 1,
				GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK |
				GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
				null, new Dimension(VIEW_SIZE * ICON_SIZE, VIEW_SIZE * ICON_SIZE), null, 0, false));

//		create array of labels that will contain game icons
		labels = new JLabel[VIEW_SIZE][VIEW_SIZE];
		for (int y = 0; y < VIEW_SIZE; y++) {
			for (int x = 0; x < VIEW_SIZE; x++) {
				labels[y][x] = new JLabel();
				labels[y][x].setHorizontalAlignment(SwingConstants.CENTER);
				labels[y][x].setVerticalAlignment(SwingConstants.CENTER);
				labels[y][x].setPreferredSize(new Dimension(ICON_SIZE, ICON_SIZE));
				mapBack.add(labels[y][x]);
			}
		}
	}

	public void updateMap() {

//		update references if the map has changed
		map = game.getMap();
		grid = map.getGrid();

//		draw only part of the map around the hero
		int y = game.getY() - VIEW_DISTANCE;
		for (int i = 0; i < VIEW_SIZE; i++, y++) {
			int x = game.getX() - VIEW_DISTANCE;
			for (int j = 0; j < VIEW_SIZE; j++, x++) {

				if (x < 0 || y < 0 || x >= map.getSize() || y >= map.getSize()) {
					labels[i][j].setIcon(new StretchIcon("src/main/resources/img/obstacles/space.png"));
					labels[i][j].setBorder(null);
//					if explored set corresponding icon
				} else if (grid[y][x].isExplored()) {
					if (grid[y][x].getObstacle() != null) {
						labels[i][j].setIcon(new StretchIcon("src/main/resources/img/obstacles/" + grid[y][x].getObstacle() + ".png"));
						labels[i][j].setBorder(blackBorder);
					} else if (grid[y][x].getHero() != null) {
						labels[i][j].setIcon(hero.getClas().getIcon());
						labels[i][j].setBorder(blueBorder);
					} else if (grid[y][x].getEnemy() != null) {
						labels[i][j].setIcon(grid[y][x].getEnemy().getClas().getIcon());
						labels[i][j].setBorder(redBorder);
					} else {
						labels[i][j].setIcon(null);
						labels[i][j].setBorder(whiteBorder);
					}
//				if not explored, set fog
				} else {
					labels[i][j].setIcon(new StretchIcon("src/main/resources/img/obstacles/fog.png"));
					labels[i][j].setBorder(null);
				}
			}
		}
	}

	public void fightOrFlee(Enemy e) {

//		enable fight/flee buttons, disable all others
		bFight.setEnabled(true);
		bFight.setForeground(Color.RED);
		bFlee.setEnabled(true);
		bFlee.setForeground(Color.CYAN);

		bN.setEnabled(false);
		bS.setEnabled(false);
		bW.setEnabled(false);
		bE.setEnabled(false);
		bCons.setEnabled(false);

		updateEnemyPane();

		printMessage("You encounter an enemy! Fight it bravely?" +
				" Or run away like a coward?", TextStyle.bold);
	}

	private void resetButtons() {
		bFight.setEnabled(false);
		bFlee.setEnabled(false);
		bN.setEnabled(true);
		bS.setEnabled(true);
		bW.setEnabled(true);
		bE.setEnabled(true);
		bCons.setEnabled(true);
	}

	public void escapeBattle(String msg) {
		updateEnemyPane();
		printMessage(msg, TextStyle.cyan);
	}

	public void initBattle() {
		frame.setEnabled(false);
		battle = new BattleGui(hero, game.getEnemy());
	}

	public void battleRound(int eDamage, int hDamage) {
		battle.updateStats();
		String msg = game.getEnemy().getClas().getClassName() + " takes " + eDamage + " damage.";
		battle.logMessage(msg, TextStyle.green);
		msg = hero.getName() + " takes " + hDamage + " damage.";
		battle.logMessage(msg, TextStyle.red);

	}

	public void enableExitBattle(int expReward) {
		battle.enableExit(expReward);
	}

	public void winBattle(int expReward) {
		battle = null;
		frame.setEnabled(true);
		printMessage("Glory to the victor! And " + expReward + " EXP!", TextStyle.green);

		dropArtifact();

		updateMap();
		updateHeroPane();
		updateEnemyPane();

	}

	private void dropArtifact() {
		AArtifact art = game.dropArtifact();
		if (art != null) {

			AArtifact.ArtifactType type = art.getType();
			StringBuilder msg = new StringBuilder("You have found " + type.toString() + ":\n" + art.getName() + " (");
			String msg2 = null;

			switch (type) {
				case WEAPON:
					msg.append("Attack +" + art.getPower() + ")\n");
					AArtifact w = hero.getWeapon();
					if (w != null)
						msg2 = "You have " + w.getName() + " (Attack +" + w.getPower() + ")";
					break;
				case ARMOR:
					msg.append("Defence +" + art.getPower() + ")\n");
					AArtifact a = hero.getArmor();
					if (a != null)
						msg2 = "You have " + a.getName() + " (Armor +" + a.getPower() + ")";
					break;
				case HELM:
					msg.append("HP +" + art.getPower() + ")\n");
					AArtifact h = hero.getHelm();
					if (h != null)
						msg2 = "You have " + h.getName() + " (HP +" + h.getPower() + ")";
					break;
			}

			if (msg2 == null)
				msg2 = "You don't have an artifact of this type.";
			msg.append(msg2);

			ImageIcon icon = new ImageIcon("src/main/resources/img/icons/gameWin.png");
			Object[] options = {"Nuh, rubish", "Equip"};
			int n = JOptionPane.showOptionDialog(frame, msg,
					"You Found an Artifact",
					JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE,
					icon, options, options[1]);

			hero.equipArtifact(art);
		}

	}


	public void youDie(String msg) {

		battle = null;
		frame.setEnabled(true);

		printMessage("YOU'RE DEAD, LOL :D", TextStyle.red);

		ImageIcon icon = new ImageIcon("src/main/resources/img/icons/death.png");
		JOptionPane.showMessageDialog(frame, msg, "Remember: use drugs responsibly!",
				JOptionPane.WARNING_MESSAGE, icon);
	}


	public void winMap(String msg, int expReward) {
		printMessage("EDGE OF THE WORLD! You earned " + expReward + " EXP.", TextStyle.blue);
		ImageIcon icon = new ImageIcon("src/main/resources/img/icons/mapWin.png");
		JOptionPane.showMessageDialog(frame, msg, "End of the Nightmare",
				JOptionPane.INFORMATION_MESSAGE, icon);
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
		mainPanel.setLayout(new GridLayoutManager(9, 6, new Insets(0, 0, 0, 0), -1, -1));
		Font mainPanelFont = this.$$$getFont$$$("Apple SD Gothic Neo", -1, -1, mainPanel.getFont());
		if (mainPanelFont != null) mainPanel.setFont(mainPanelFont);
		mainPanel.setPreferredSize(new Dimension(1200, 800));
		mainPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), null));
		enemyPane = new JScrollPane();
		mainPanel.add(enemyPane, new GridConstraints(3, 0, 4, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, 1, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(256, -1), new Dimension(256, -1), new Dimension(256, -1), 0, false));
		enemyStats = new JTextPane();
		enemyStats.setEditable(false);
		Font enemyStatsFont = this.$$$getFont$$$("AppleGothic", -1, 14, enemyStats.getFont());
		if (enemyStatsFont != null) enemyStats.setFont(enemyStatsFont);
		enemyPane.setViewportView(enemyStats);
		heroPane = new JScrollPane();
		mainPanel.add(heroPane, new GridConstraints(5, 3, 2, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(256, -1), new Dimension(256, -1), new Dimension(256, -1), 0, false));
		heroStats = new JTextPane();
		heroStats.setEditable(false);
		Font heroStatsFont = this.$$$getFont$$$("AppleGothic", -1, 14, heroStats.getFont());
		if (heroStatsFont != null) heroStats.setFont(heroStatsFont);
		heroPane.setViewportView(heroStats);
		heroAvatar = new JLabel();
		heroAvatar.setText("");
		mainPanel.add(heroAvatar, new GridConstraints(0, 3, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(256, 256), null, 0, false));
		mapHolder = new JPanel();
		mapHolder.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1, true, true));
		mainPanel.add(mapHolder, new GridConstraints(0, 2, 6, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
		bW = new JButton();
		bW.setText("West");
		mainPanel.add(bW, new GridConstraints(8, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		bS = new JButton();
		bS.setText("South");
		mainPanel.add(bS, new GridConstraints(8, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		bE = new JButton();
		bE.setText("East");
		mainPanel.add(bE, new GridConstraints(8, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		bN = new JButton();
		bN.setText("North");
		mainPanel.add(bN, new GridConstraints(7, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		bCons = new JButton();
		bCons.setText("TextMode");
		mainPanel.add(bCons, new GridConstraints(8, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		bFlee = new JButton();
		bFlee.setEnabled(false);
		bFlee.setText("Flee");
		mainPanel.add(bFlee, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		bFight = new JButton();
		bFight.setEnabled(false);
		bFight.setText("Fight");
		mainPanel.add(bFight, new GridConstraints(7, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		enemyAvatar = new JLabel();
		enemyAvatar.setText("");
		mainPanel.add(enemyAvatar, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(256, 256), null, 0, false));
		enemyClass = new JLabel();
		Font enemyClassFont = this.$$$getFont$$$("Apple SD Gothic Neo", Font.BOLD, 16, enemyClass.getFont());
		if (enemyClassFont != null) enemyClass.setFont(enemyClassFont);
		enemyClass.setText("Class");
		mainPanel.add(enemyClass, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		enemyLevel = new JLabel();
		Font enemyLevelFont = this.$$$getFont$$$("Apple SD Gothic Neo", Font.BOLD, 16, enemyLevel.getFont());
		if (enemyLevelFont != null) enemyLevel.setFont(enemyLevelFont);
		enemyLevel.setText("Level");
		mainPanel.add(enemyLevel, new GridConstraints(2, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		heroName = new JLabel();
		Font heroNameFont = this.$$$getFont$$$("Apple SD Gothic Neo", Font.BOLD, 16, heroName.getFont());
		if (heroNameFont != null) heroName.setFont(heroNameFont);
		heroName.setHorizontalAlignment(10);
		heroName.setHorizontalTextPosition(11);
		heroName.setText("Name");
		mainPanel.add(heroName, new GridConstraints(1, 3, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		heroClass = new JLabel();
		Font heroClassFont = this.$$$getFont$$$("Apple SD Gothic Neo", Font.BOLD, 16, heroClass.getFont());
		if (heroClassFont != null) heroClass.setFont(heroClassFont);
		heroClass.setText("Class");
		mainPanel.add(heroClass, new GridConstraints(2, 3, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		expBar = new JProgressBar();
		Font expBarFont = this.$$$getFont$$$("Herculanum", -1, 16, expBar.getFont());
		if (expBarFont != null) expBar.setFont(expBarFont);
		expBar.setString("EXP");
		expBar.setStringPainted(true);
		mainPanel.add(expBar, new GridConstraints(3, 3, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		hpBar = new JProgressBar();
		Font hpBarFont = this.$$$getFont$$$("Herculanum", -1, 16, hpBar.getFont());
		if (hpBarFont != null) hpBar.setFont(hpBarFont);
		hpBar.setForeground(new Color(-8355712));
		hpBar.setIndeterminate(false);
		hpBar.setName("");
		hpBar.setString("HP");
		hpBar.setStringPainted(true);
		hpBar.setToolTipText("");
		mainPanel.add(hpBar, new GridConstraints(4, 3, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		infoPane = new JScrollPane();
		mainPanel.add(infoPane, new GridConstraints(6, 2, 3, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(-1, 100), null, 0, false));
		info = new JTextPane();
		info.setEditable(false);
		infoPane.setViewportView(info);
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


}
