package unit.swingy.view.gui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import darrylbu.icon.StretchIcon;
import lombok.Getter;
import unit.swingy.controller.Game;
import unit.swingy.model.Map;
import unit.swingy.model.MapTile;
import unit.swingy.model.characters.Enemy;
import unit.swingy.model.characters.Hero;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

public class ExplorationGui {

	private Game game;
	private Hero hero;
	private Map map;
	private MapTile[][] grid;
	private JLabel[][] labels;

	private final int ICON_SIZE = 64;
	private final int SIDE_PANE_WIDTH = 256;
	private final int VIEW_SIZE = 9;
	private final int VIEW_DISTANCE = VIEW_SIZE / 2;
	private int winWidth;
	private int winHeight;

	@Getter private boolean clicked = false;
	@Getter private boolean choice = false;

	private JFrame frame;
	private JPanel mainPanel;
	private JPanel mapBack;
	private JPanel mapHolder;

	private JScrollPane enemyPane;
	private JLabel enemyAvatar;
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


//	TODO Pack the images
//	The location of the image is also important. If the image is external to the application
//	(somewhere on the file system), you can use ImageIO.read(new File("/path/to/image")).
//	However, if the the image is embedded within your application (stored within the Jar for example),
//	you will need to use something more like ImageIO.read(getClass().getResource("/path/to/image")) instead...

	public ExplorationGui() {
		System.out.println(">> Constructing Exploration GUI...");

		game = Game.getInstance();
		hero = game.getHero();
		map = game.getMap();
		grid = map.getGrid();

		$$$setupUI$$$();

		buildMap();
		updateMap();

		buildHeroPane();

		resizePanels();
		initFrame();

		createEventsListeners();

	}

	private void resizePanels() {
		winWidth = (VIEW_SIZE * ICON_SIZE) + (SIDE_PANE_WIDTH * 2);
		winHeight = VIEW_SIZE * ICON_SIZE;
		mainPanel.setPreferredSize(new Dimension(winWidth, winHeight));
	}

	private void initFrame() {
		//init frame
		frame = new JFrame("World Exploration");

		frame.setContentPane(mainPanel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

	private void createEventsListeners() {
		bN.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				game.moveHero('n');
//				updateMap();
			}
		});

		bS.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				game.moveHero('s');
//				updateMap();
			}
		});

		bW.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				game.moveHero('w');
//				updateMap();
			}
		});

		bE.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				game.moveHero('e');
//				updateMap();
			}
		});

		bFight.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				choice = true;
				clicked = true;
				game.notify();
			}
		});

		bFlee.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				choice = false;
				clicked = true;
				game.notify();
			}
		});
	}


	public void destroyWindow() {
		frame.dispose();
	}


	private void buildHeroPane() {
		heroAvatar.setIcon(hero.getClas().getAvatar());
		heroAvatar.setToolTipText(hero.getClas().getDescription());

		heroName.setText(hero.getName());
		updateHeroPane();
	}

	private void updateHeroPane() {
		heroClass.setText(hero.getClas().getClassName() + " (" + hero.getLevel() + " lvl)");

		expBar.setMaximum(hero.getExpToLevelUp());
		expBar.setValue(hero.getExp());
		expBar.setString("EXP: " + hero.getExp() + "/" + hero.getExpToLevelUp());

		hpBar.setMaximum(hero.getMaxHp());
		hpBar.setValue(hero.getHp());
		hpBar.setString("HP: " + hero.getHp() + "/" + hero.getMaxHp());

		SimpleAttributeSet atr = new SimpleAttributeSet();
		Document statsDoc = heroStats.getStyledDocument();
		try {
			statsDoc.insertString(statsDoc.getLength(), "Attack: " + hero.getAttack() + "\n", atr);
			statsDoc.insertString(statsDoc.getLength(), "Defence: " + hero.getDefence() + "\n\n", atr);
			statsDoc.insertString(statsDoc.getLength(), "Weapon: " + hero.getWeapon() + "\n", atr);
			statsDoc.insertString(statsDoc.getLength(), "Armor: " + hero.getArmor() + "\n", atr);
			statsDoc.insertString(statsDoc.getLength(), "Helm: " + hero.getHelm() + "\n", atr);
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

/*
	public void updateMap(int x, int y, int nx, int ny) {
		Border whiteBorder = BorderFactory.createLineBorder(Color.white);
		Border blueBorder = BorderFactory.createLineBorder(Color.blue);
		Border blackBorder = BorderFactory.createLineBorder(Color.black);
		Border redBorder = BorderFactory.createLineBorder(Color.red);

		if (grid[y][x].getHero() == null) {
			labels[y][x].setIcon(null);
		}

		if (grid[ny][nx].isExplored()) {
			if (grid[ny][nx].getObstacle() != null) {
				labels[ny][nx].setIcon(new StretchIcon("src/main/resources/img/obstacles/" + grid[ny][nx].getObstacle() + ".png"));
				labels[ny][nx].setBorder(blackBorder);
			} else if (grid[ny][nx].getHero() != null) {
				labels[ny][nx].setIcon(hero.getClas().getIcon());
				labels[ny][nx].setBorder(blueBorder);
			} else if (grid[ny][nx].getEnemy() != null) {
				labels[ny][nx].setIcon(grid[ny][nx].getEnemy().getClas().getIcon());
				labels[ny][nx].setBorder(redBorder);
			} else {
				labels[ny][nx].setIcon(null);
				labels[ny][nx].setBorder(whiteBorder);
			}
		}

	}
*/


	public void updateMap() {

		Border whiteBorder = BorderFactory.createLineBorder(Color.white);
		Border blueBorder = BorderFactory.createLineBorder(Color.blue);
		Border blackBorder = BorderFactory.createLineBorder(Color.black);
		Border redBorder = BorderFactory.createLineBorder(Color.red);

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
		bFlee.setEnabled(true);
		bN.setEnabled(false);
		bS.setEnabled(false);
		bW.setEnabled(false);
		bE.setEnabled(false);
		bCons.setEnabled(false);

//		while (!clicked) {
////			try {
////				Thread.sleep(1000);
////			} catch (InterruptedException ex) {
////				ex.printStackTrace();
////			}
//		}
//		return choice;
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
		mainPanel.setLayout(new GridLayoutManager(8, 6, new Insets(0, 0, 0, 0), -1, -1));
		Font mainPanelFont = this.$$$getFont$$$("Apple SD Gothic Neo", -1, -1, mainPanel.getFont());
		if (mainPanelFont != null) mainPanel.setFont(mainPanelFont);
		mainPanel.setPreferredSize(new Dimension(1200, 800));
		mainPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), null));
		enemyPane = new JScrollPane();
		mainPanel.add(enemyPane, new GridConstraints(1, 0, 5, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, 1, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(256, -1), new Dimension(256, -1), new Dimension(256, -1), 0, false));
		heroPane = new JScrollPane();
		mainPanel.add(heroPane, new GridConstraints(5, 3, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(256, -1), new Dimension(256, -1), new Dimension(256, -1), 0, false));
		heroStats = new JTextPane();
		heroStats.setEditable(false);
		Font heroStatsFont = this.$$$getFont$$$("AppleGothic", -1, 14, heroStats.getFont());
		if (heroStatsFont != null) heroStats.setFont(heroStatsFont);
		heroPane.setViewportView(heroStats);
		heroAvatar = new JLabel();
		heroAvatar.setText("");
		mainPanel.add(heroAvatar, new GridConstraints(0, 3, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(256, -1), null, 0, false));
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
		mapHolder = new JPanel();
		mapHolder.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1, true, true));
		mainPanel.add(mapHolder, new GridConstraints(0, 2, 8, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
		bW = new JButton();
		bW.setText("West");
		mainPanel.add(bW, new GridConstraints(7, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		bS = new JButton();
		bS.setText("South");
		mainPanel.add(bS, new GridConstraints(7, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		bE = new JButton();
		bE.setText("East");
		mainPanel.add(bE, new GridConstraints(7, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		bN = new JButton();
		bN.setText("North");
		mainPanel.add(bN, new GridConstraints(6, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		bCons = new JButton();
		bCons.setText("TextMode");
		mainPanel.add(bCons, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		bFlee = new JButton();
		bFlee.setEnabled(false);
		bFlee.setText("Flee");
		mainPanel.add(bFlee, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		bFight = new JButton();
		bFight.setEnabled(false);
		bFight.setText("Fight");
		mainPanel.add(bFight, new GridConstraints(6, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		enemyAvatar = new JLabel();
		enemyAvatar.setText("Enemy");
		mainPanel.add(enemyAvatar, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(256, -1), null, 0, false));
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
