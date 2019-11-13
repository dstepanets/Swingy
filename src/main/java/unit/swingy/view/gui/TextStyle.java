package unit.swingy.view.gui;

import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;

public class TextStyle {

	static public final SimpleAttributeSet styleNorm;
	static public final SimpleAttributeSet styleBold;
	static public final SimpleAttributeSet styleItalic;
	static public final SimpleAttributeSet styleRed;
	static public final SimpleAttributeSet styleGreen;

	static {
		styleNorm = new SimpleAttributeSet();

		styleBold = new SimpleAttributeSet(styleNorm);
		StyleConstants.setBold(styleBold, true);

		styleItalic = new SimpleAttributeSet(styleNorm);
		StyleConstants.setItalic(styleItalic, true);

		styleRed = new SimpleAttributeSet(styleNorm);
		StyleConstants.setForeground(styleRed, Color.RED);

		styleGreen = new SimpleAttributeSet(styleNorm);
		StyleConstants.setForeground(styleGreen, Color.GREEN);
	}


}
