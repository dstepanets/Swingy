package unit.swingy.view.gui;

import unit.swingy.controller.Game;
import unit.swingy.model.Map;
import unit.swingy.model.MapTile;
import unit.swingy.model.artifacts.AArtifact;
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
	private final int SIDE_PANE_WIDTH = 290;
	private final int VIEW_SIZE = 9;
	private final int VIEW_DISTANCE = VIEW_SIZE / 2;
	private final int INFO_PANEL_HEIGHT = 150;
	private int winWidth;
	private int winHeight;

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

	private JLabel heroAvatar;
	private JLabel heroName;
	private JLabel heroClass;
	private JProgressBar expBar;
	private JProgressBar hpBar;

	private JButton bN;
	private JButton bS;
	private JButton bE;
	private JButton bW;

	private JScrollPane infoPane;
	private JTextPane info;
	private JLabel heroAttack;
	private JLabel heroDefence;
	private JLabel helm;
	private JLabel weapon;
	private JLabel armor;
	private JLabel weaponTxt;
	private JLabel armorTxt;
	private JLabel helmTxt;


	/*	------------------------- FORMATTING AND STYLING ------------------------- */

	private Border whiteBorder = BorderFactory.createLineBorder(Color.white);
	private Border blueBorder = BorderFactory.createLineBorder(Color.blue);
	private Border blackBorder = BorderFactory.createLineBorder(Color.black);
	private Border redBorder = BorderFactory.createLineBorder(Color.red);

	/*	---------------------- BUILD AND UPDATE COMPONENTS ----------------------- */


	public ExplorationGui() {

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
					printMessage("Sadly, you were running so sloooowly...", TextStyle.red);
					game.initBattle();
				}
				resetButtons();
			}
		});

		bCons.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				game.setGuiMode(false);
				game.switchView(false);
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

		int maxHp = hero.getMaxHp() + hero.getBonusHp();
		hpBar.setMaximum(maxHp);
		hpBar.setValue(hero.getHp());
		hpBar.setString("HP: " + hero.getHp() + "/" + maxHp);

		heroAttack.setText("Attack: " + (hero.getAttack() + hero.getBonusAttack()));
		heroDefence.setText("Defence: " + (hero.getDefence() + hero.getBonusDefence()));

		if (hero.getWeapon() == null) {
			weapon.setIcon(new StretchIcon("src/main/resources/img/artifacts/weapon.png"));
			weaponTxt.setText("No Weapon");
		} else {
			weapon.setIcon(new StretchIcon(hero.getWeapon().getIconAddr()));
			weaponTxt.setText("<html><body>" + hero.getWeapon().getName() + "<br>(Attack +" + hero.getBonusAttack() + ")</body></html>");
		}

		if (hero.getArmor() == null) {
			armor.setIcon(new StretchIcon("src/main/resources/img/artifacts/armor.png"));
			armorTxt.setText("No Armor");
		} else {
			armor.setIcon(new StretchIcon(hero.getArmor().getIconAddr()));
			armorTxt.setText("<html><body>" + hero.getArmor().getName() + "<br>(Defence +" + hero.getBonusDefence() + ")</body></html>");
		}

		if (hero.getHelm() == null) {
			helm.setIcon(new StretchIcon("src/main/resources/img/artifacts/helm.png"));
			helmTxt.setText("No Helm");
		} else {
			helm.setIcon(new StretchIcon(hero.getHelm().getIconAddr()));
			helmTxt.setText("<html><body>" + hero.getHelm().getName() + "<br>(HP +" + hero.getBonusHp() + ")</body></html>");
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

	/*	----------------------------- UTILS ---------------------------- */

	public void printMessage(String msg, SimpleAttributeSet atr) {
		Document doc = info.getStyledDocument();
		try {
			doc.insertString(doc.getLength(), msg + "\n", atr);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	public void showMessageDialog(String title, String msg, ImageIcon icon) {
		JOptionPane.showMessageDialog(frame, msg, title, JOptionPane.INFORMATION_MESSAGE, icon);
	}

	/*	----------------------------- GAMEPLAY MECHANICS ---------------------------- */


	public void showIntro(String msg) {
		ImageIcon icon = new ImageIcon("src/main/resources/img/icons/intro.png");
		JOptionPane.showMessageDialog(frame,
				msg, "Gosh, where am I?..",
				JOptionPane.INFORMATION_MESSAGE, icon);
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
		if (battle != null)
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

			String msg = game.generateArtifactMessage(art);

			ImageIcon icon = new ImageIcon(art.getIconAddr());
			Object[] options = {"Nuh, rubish", "Equip"};
			int n = JOptionPane.showOptionDialog(frame, msg,
					"You Found an Artifact",
					JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE,
					icon, options, options[1]);

			if (n == 1) {
				game.equipArtifact(art);
			}
		}
	}


	public void youDie(String msg, String msg2) {

		battle = null;
		frame.setEnabled(true);

		printMessage(msg, TextStyle.red);

		ImageIcon icon = new ImageIcon("src/main/resources/img/icons/death.png");
		JOptionPane.showMessageDialog(frame, msg2, "Remember: use drugs responsibly!",
				JOptionPane.WARNING_MESSAGE, icon);
	}


	public void winMap(String msg, int expReward) {
		printMessage("EDGE OF THE WORLD! You earned " + expReward + " EXP.", TextStyle.blue);
		ImageIcon icon = new ImageIcon("src/main/resources/img/icons/mapWin.png");
		JOptionPane.showMessageDialog(frame, msg, "End of the Nightmare",
				JOptionPane.INFORMATION_MESSAGE, icon);
	}

	public void winGame(String title, String outro) {
		ImageIcon icon = new ImageIcon("src/main/resources/img/icons/gameWin.png");
		JOptionPane.showMessageDialog(frame, outro, title, JOptionPane.INFORMATION_MESSAGE, icon);
		game.exitGame();
	}




	/*	----------------------------- AUTOMATICALLY GENERATED GUI INITIAL SETUP ---------------------------- */

	/**
	 * Method generated by IntelliJ IDEA GUI Designer
	 * >>> IMPORTANT!! <<<
	 * DO NOT edit this method OR call it in your code!
	 *
	 * @noinspection ALL
	 */
	private void $$$setupUI$$$() {
		mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayoutManager(11, 6, new Insets(0, 0, 0, 0), -1, -1));
		Font mainPanelFont = this.$$$getFont$$$("Apple SD Gothic Neo", -1, -1, mainPanel.getFont());
		if (mainPanelFont != null) mainPanel.setFont(mainPanelFont);
		mainPanel.setPreferredSize(new Dimension(-1, -1));
		mainPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), null));
		enemyPane = new JScrollPane();
		mainPanel.add(enemyPane, new GridConstraints(3, 0, 6, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(256, -1), new Dimension(256, -1), new Dimension(256, -1), 0, false));
		enemyStats = new JTextPane();
		enemyStats.setEditable(false);
		Font enemyStatsFont = this.$$$getFont$$$("AppleGothic", -1, 14, enemyStats.getFont());
		if (enemyStatsFont != null) enemyStats.setFont(enemyStatsFont);
		enemyPane.setViewportView(enemyStats);
		mapHolder = new JPanel();
		mapHolder.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1, true, true));
		mainPanel.add(mapHolder, new GridConstraints(0, 2, 8, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		bW = new JButton();
		bW.setText("West");
		mainPanel.add(bW, new GridConstraints(10, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(85, 30), null, 0, false));
		bS = new JButton();
		bS.setText("South");
		mainPanel.add(bS, new GridConstraints(10, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(85, 30), null, 0, false));
		bE = new JButton();
		bE.setText("East");
		mainPanel.add(bE, new GridConstraints(10, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(85, 30), null, 0, false));
		bN = new JButton();
		bN.setText("North");
		mainPanel.add(bN, new GridConstraints(9, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(85, 30), null, 0, false));
		bCons = new JButton();
		bCons.setText("TextMode");
		mainPanel.add(bCons, new GridConstraints(10, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(128, 30), null, 0, false));
		bFlee = new JButton();
		bFlee.setEnabled(false);
		bFlee.setText("Flee");
		mainPanel.add(bFlee, new GridConstraints(9, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(128, 30), null, 0, false));
		bFight = new JButton();
		bFight.setEnabled(false);
		bFight.setText("Fight");
		mainPanel.add(bFight, new GridConstraints(9, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(128, 30), null, 0, false));
		infoPane = new JScrollPane();
		mainPanel.add(infoPane, new GridConstraints(8, 2, 3, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(-1, 150), null, 0, false));
		info = new JTextPane();
		info.setEditable(false);
		infoPane.setViewportView(info);
		helm = new JLabel();
		helm.setText("");
		mainPanel.add(helm, new GridConstraints(8, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(64, 64), null, 0, false));
		armor = new JLabel();
		armor.setText("");
		mainPanel.add(armor, new GridConstraints(7, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(64, 64), null, 0, false));
		weapon = new JLabel();
		weapon.setText("");
		mainPanel.add(weapon, new GridConstraints(6, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(64, 64), null, 0, false));
		enemyAvatar = new JLabel();
		enemyAvatar.setText("");
		mainPanel.add(enemyAvatar, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(256, 256), null, 0, false));
		heroAvatar = new JLabel();
		heroAvatar.setText("");
		mainPanel.add(heroAvatar, new GridConstraints(0, 3, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(256, 256), null, 0, false));
		enemyClass = new JLabel();
		Font enemyClassFont = this.$$$getFont$$$("Apple SD Gothic Neo", Font.BOLD, 16, enemyClass.getFont());
		if (enemyClassFont != null) enemyClass.setFont(enemyClassFont);
		enemyClass.setHorizontalAlignment(0);
		enemyClass.setHorizontalTextPosition(0);
		enemyClass.setText("Class");
		mainPanel.add(enemyClass, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(256, -1), null, 0, false));
		enemyLevel = new JLabel();
		Font enemyLevelFont = this.$$$getFont$$$("Apple SD Gothic Neo", Font.BOLD, 16, enemyLevel.getFont());
		if (enemyLevelFont != null) enemyLevel.setFont(enemyLevelFont);
		enemyLevel.setHorizontalAlignment(0);
		enemyLevel.setHorizontalTextPosition(0);
		enemyLevel.setText("Level");
		mainPanel.add(enemyLevel, new GridConstraints(2, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(256, -1), null, 0, false));
		heroName = new JLabel();
		Font heroNameFont = this.$$$getFont$$$("Apple SD Gothic Neo", Font.BOLD, 16, heroName.getFont());
		if (heroNameFont != null) heroName.setFont(heroNameFont);
		heroName.setHorizontalAlignment(0);
		heroName.setHorizontalTextPosition(0);
		heroName.setText("Name");
		mainPanel.add(heroName, new GridConstraints(1, 3, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(256, 16), null, 0, false));
		heroClass = new JLabel();
		Font heroClassFont = this.$$$getFont$$$("Apple SD Gothic Neo", Font.BOLD, 16, heroClass.getFont());
		if (heroClassFont != null) heroClass.setFont(heroClassFont);
		heroClass.setHorizontalAlignment(0);
		heroClass.setHorizontalTextPosition(0);
		heroClass.setText("Class");
		mainPanel.add(heroClass, new GridConstraints(2, 3, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(256, 16), null, 0, false));
		expBar = new JProgressBar();
		Font expBarFont = this.$$$getFont$$$("Herculanum", -1, 16, expBar.getFont());
		if (expBarFont != null) expBar.setFont(expBarFont);
		expBar.setString("EXP");
		expBar.setStringPainted(true);
		mainPanel.add(expBar, new GridConstraints(3, 3, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, new Dimension(256, 16), null, 0, false));
		hpBar = new JProgressBar();
		Font hpBarFont = this.$$$getFont$$$("Herculanum", -1, 16, hpBar.getFont());
		if (hpBarFont != null) hpBar.setFont(hpBarFont);
		hpBar.setForeground(new Color(-8355712));
		hpBar.setIndeterminate(false);
		hpBar.setName("");
		hpBar.setString("HP");
		hpBar.setStringPainted(true);
		hpBar.setToolTipText("");
		mainPanel.add(hpBar, new GridConstraints(4, 3, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, new Dimension(256, 16), null, 0, false));
		heroAttack = new JLabel();
		Font heroAttackFont = this.$$$getFont$$$(null, Font.BOLD, 14, heroAttack.getFont());
		if (heroAttackFont != null) heroAttack.setFont(heroAttackFont);
		heroAttack.setText("Attack");
		mainPanel.add(heroAttack, new GridConstraints(5, 3, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(85, 20), null, 0, false));
		heroDefence = new JLabel();
		Font heroDefenceFont = this.$$$getFont$$$(null, Font.BOLD, 14, heroDefence.getFont());
		if (heroDefenceFont != null) heroDefence.setFont(heroDefenceFont);
		heroDefence.setText("Defence");
		mainPanel.add(heroDefence, new GridConstraints(5, 5, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(85, 20), null, 0, false));
		weaponTxt = new JLabel();
		weaponTxt.setText("No Weapon");
		mainPanel.add(weaponTxt, new GridConstraints(6, 4, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		armorTxt = new JLabel();
		armorTxt.setText("No Armor");
		mainPanel.add(armorTxt, new GridConstraints(7, 4, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		helmTxt = new JLabel();
		helmTxt.setText("No Helm");
		mainPanel.add(helmTxt, new GridConstraints(8, 4, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
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
