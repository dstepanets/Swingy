package unit.swingy.view.gui;

import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;

public class TextStyle {

	static public final SimpleAttributeSet norm;
	static public final SimpleAttributeSet bold;
	static public final SimpleAttributeSet italic;

	static public final SimpleAttributeSet red;
	static public final SimpleAttributeSet green;
	static public final SimpleAttributeSet blue;
	static public final SimpleAttributeSet cyan;

	static {
		norm = new SimpleAttributeSet();

		bold = new SimpleAttributeSet(norm);
		StyleConstants.setBold(bold, true);

		italic = new SimpleAttributeSet(norm);
		StyleConstants.setItalic(italic, true);

		red = new SimpleAttributeSet(norm);
		StyleConstants.setForeground(red, Color.RED);

		green = new SimpleAttributeSet(norm);
		StyleConstants.setForeground(green, Color.GREEN);

		blue = new SimpleAttributeSet(norm);
		StyleConstants.setForeground(blue, Color.BLUE);

		cyan = new SimpleAttributeSet(norm);
		StyleConstants.setForeground(cyan, Color.CYAN);
	}


}
