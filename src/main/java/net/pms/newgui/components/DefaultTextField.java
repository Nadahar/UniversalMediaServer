/*
 * Universal Media Server, for streaming any medias to DLNA
 * compatible renderers based on the http://www.ps3mediaserver.org.
 * Copyright (C) 2012 UMS developers.
 *
 * This program is a free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; version 2
 * of the License only.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package net.pms.newgui.components;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JTextField;
import javax.swing.UIManager;

public class DefaultTextField extends JTextField {

	private static final long serialVersionUID = 7922351776271240001L;
	protected String defaultText;
	protected int defaultFontStyle = -1;
	protected Color defaultFontColor = UIManager.getColor("textInactiveText");

	/**
	 * Constructs a new <code>DefaultTextField</code>. A default model is
	 * created, the initial string is <code>null</code>, and the number of
	 * columns is set to 0.
	 */
	public DefaultTextField(String defaultText) {
		super(null, null, 0);
		this.defaultText = defaultText;
	}

	/**
	 * Constructs a new {@link DefaultTextField} initialized with the specified
	 * text. A default model is created and the number of columns is 0.
	 *
	 * @param text
	 *            the text to be displayed, or <code>null</code>
	 */
	public DefaultTextField(String text, String defaultText) {
		super(null, text, 0);
		this.defaultText = defaultText;
	}

	/**
	 * Constructs a new empty {@link DefaultTextField} with the specified number
	 * of columns. A default model is created and the initial string is set to
	 * <code>null</code>.
	 *
	 * @param columns
	 *            the number of columns to use to calculate the preferred width;
	 *            if columns is set to zero, the preferred width will be
	 *            whatever naturally results from the component implementation
	 */
	public DefaultTextField(int columns, String defaultText) {
		super(null, null, columns);
		this.defaultText = defaultText;
	}

	/**
	 * Constructs a new {@link DefaultTextField} initialized with the specified
	 * text and columns. A default model is created.
	 *
	 * @param text
	 *            the text to be displayed, or <code>null</code>
	 * @param columns
	 *            the number of columns to use to calculate the preferred width;
	 *            if columns is set to zero, the preferred width will be
	 *            whatever naturally results from the component implementation.
	 */
	public DefaultTextField(String text, int columns, String defaultText) {
		super(null, text, columns);
		this.defaultText = defaultText;
	}

	/**
	 * @param defaultText
	 *            the {@link String} to show when the field is blank.
	 */
	public void setDefaultText(String defaultText) {
		this.defaultText = defaultText;
		repaint();
	}

	/**
	 * @return
	 *            The {@link String} shown when the field is blank.
	 */
	public String getDefaultText() {
		return defaultText;
	}

	/**
	 * Sets the font style to apply to the default text. Use the constants
	 * from the {@link Font} class like {@link Font#BOLD}, {@link Font#ITALIC}
	 * or {@link Font#MONOSPACED}. Set to a negative value to disable style
     * override.
	 *
	 * @param
	 *            defaultFontStyle any valid constant from {@link Font}.
	 */
	public void setDefaultFontStyle(int defaultFontStyle) {
		this.defaultFontStyle = defaultFontStyle;
		repaint();
	}

	/**
	 * Get the font style integer applied to the default text. The integer
	 * value effect can be found from the {@link Font} constants. A negative
	 * value means that style override is disabled.
	 */
	public int getDefaultFontStyle() {
		return defaultFontStyle;
	}

	/**
	 * Sets the default text font color to whatever color {@link UIManager}
	 * returns for the given {@link String}. If the {@link String} is unknown,
	 * default font color is set to {@code null}.
	 *
	 * @param uiManagerName
	 *            the {@link String} consisting of a valid {@link UIManager}
	 *            font name.
	 */
	public void setDefaultFontColor(String uiManagerName) {
		defaultFontColor = UIManager.getColor("textInactiveText");
		repaint();
	}

	/**
	 * Sets the default text font color. Set to {@code null} to disable default
	 * text color override.
	 *
	 * @param
	 *            color the {@link Color} to set for the default text.
	 */
	public void setDefaultFontColor(Color color) {
		defaultFontColor = color;
		repaint();
	}

	/**
	 * @return
	 *         The default text font color or {@code null} if color override
	 *         is disabled.
	 */
	public Color getDefaultFontColor() {
		return defaultFontColor;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);


		if (this.getText().equals("")) {
			int height = this.getHeight();
			Font prevFont = g.getFont();
			Font defaultFont;
			if (defaultFontStyle >= 0) {
				defaultFont = prevFont.deriveFont(defaultFontStyle);
			} else {
				defaultFont = prevFont;
			}
			Color prevColor = g.getColor();
			Color defaultColor;
			if (defaultFontColor != null) {
				defaultColor = defaultFontColor;
			} else {
				defaultColor = prevColor;
			}
			g.setFont(defaultFont);
			g.setColor(defaultColor);
			int h = g.getFontMetrics().getHeight();
			int textBottom = (height - h) / 2 + h - 4;
			int x = this.getInsets().left;
			Graphics2D g2d = (Graphics2D) g;
			RenderingHints hints = g2d.getRenderingHints();
			g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			g2d.drawString(defaultText, x, textBottom);
			g2d.setRenderingHints(hints);
			g.setFont(prevFont);
			g.setColor(prevColor);
		}
	}

}
