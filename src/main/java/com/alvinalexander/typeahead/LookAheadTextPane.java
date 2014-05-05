package com.alvinalexander.typeahead;

import javax.swing.*;
import javax.swing.text.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.*;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;

/**
 * @author Alvin Alexander, AlvinAlexander.com
 * 
 * This is a very heavily-modified version of a class found in the Core
 * Swing book.
 * 
 * I think this can be a drop-in replacement for a TextPane.
 * 
 */
public class LookAheadTextPane extends JTextPane {
	
	int endOfCurrentSuggestion = 0;

	public LookAheadTextPane() {
		this(null);
		initUi();
	}

	public LookAheadTextPane(int columns) {
		this(null);
		initUi();
	}
	
	private void configureFont() {
		Font font = new Font("Monaco", Font.PLAIN, 13);
        this.setFont(font);
	}
	
	private void initUi() {
		configureFont();
        this.setMargin(new Insets(20, 20, 20, 20));
        this.setBackground(new Color(218, 235, 218));
        this.addKeyListener(new KeyListener(){
            @Override
            public void keyPressed(KeyEvent e){
            	// move to the end of the current suggestion
                if(e.getKeyCode() == KeyEvent.VK_ESCAPE || e.getKeyCode() == KeyEvent.VK_TAB) {
                	e.consume();
                	moveCaretPosition(endOfCurrentSuggestion);
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyReleased(KeyEvent e) {}
        });
	}

	public LookAheadTextPane(TextLookAhead lookAhead) {
		initUi();
		setLookAhead(lookAhead);
		this.setPreferredSize(new Dimension(754, 890));
		this.getDocument().addDocumentListener(new DocumentListener() {
			public void actionPerformed(ActionEvent e) {
				// remove any existing selection
				setCaretPosition(getDocument().getLength());
			}

			public void changedUpdate(DocumentEvent e) {}
			public void insertUpdate(DocumentEvent e) {}
			public void removeUpdate(DocumentEvent e) {}
		});

		addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {}
			public void focusLost(FocusEvent e) {
				if (e.isTemporary() == false) {
					// Remove any existing selection
					setCaretPosition(getDocument().getLength());
				}
			}
		});
	}

	public void setLookAhead(TextLookAhead lookAhead) {
		this.lookAhead = lookAhead;
	}

	public TextLookAhead getLookAhead() {
		return lookAhead;
	}

	public void replaceSelection(String content) {
		super.replaceSelection(content);

		if (isEditable() == false || isEnabled() == false) {
			return;
		}

		Document doc = getDocument();

		int charsToLookBack = 10;
		if (doc != null && lookAhead != null) {
			try {
				// go back to previous whitespace
				int docLength = doc.getLength();
				if (docLength < charsToLookBack)
					charsToLookBack = docLength - 1;
				String recentDocText = null;
				String oldContent = null;

				recentDocText = doc.getText(0, getCaretPosition());

				// pass the look-ahead algorithm all of the doc except the
				// partial word you're currently working on
				// this may be a bad approach; need to look for whitespace at
				// beginning and
				// end of words, periods, etc.
				if (doc.getLength() > charsToLookBack) {
					lookAhead.setText(doc.getText(0, doc.getLength() - charsToLookBack));
				}

				// find last whitespace character, and make sure I keep looking
				// for that
				// same character throughout the rest of the code
				// REFACTOR THIS SECTION
				int lastBlank = recentDocText.lastIndexOf(" ");
				int lastTab = recentDocText.lastIndexOf("\t");
				int lastNewline = recentDocText.lastIndexOf("\n");
				int lastFF = recentDocText.lastIndexOf("\f");
				int lastR = recentDocText.lastIndexOf("\r");

				int lastWhitespaceLoc = 0;
				String lastWhitespaceString = "";
				if (lastBlank > lastTab && lastBlank > lastNewline) {
					lastWhitespaceLoc = lastBlank;
					lastWhitespaceString = " ";
				} else if (lastTab > lastBlank && lastTab > lastNewline) {
					lastWhitespaceLoc = lastTab;
					lastWhitespaceString = "\t";
				} else if (lastNewline > lastBlank && lastNewline > lastTab) {
					lastWhitespaceLoc = lastNewline;
					lastWhitespaceString = "\n";
				}

				if (lastWhitespaceLoc > 0
						&& doc.getLength() > (charsToLookBack - 1)) {
					// get caret position
					int caretPosition = getCaretPosition();
					// look at last 10 characters
					int scanBackPosition = caretPosition - charsToLookBack;
					if (scanBackPosition <= 0) return;
					String recentChars = doc.getText(scanBackPosition, charsToLookBack);
					// if any characters are blanks, get the characters since
					// the last blank
					int lastWhitespacePosition = recentChars
							.lastIndexOf(lastWhitespaceString);
					if (lastWhitespacePosition <= 0) return;
					String charsSinceLastBlank = recentChars.substring(lastWhitespacePosition + 1, charsToLookBack);

					String newContent = lookAhead.doLookAhead(charsSinceLastBlank);
					if (newContent != null) {
						int lengthOfAddedContent = newContent.length() - charsSinceLastBlank.length();
						String newContentSubstring = newContent.substring(
								charsSinceLastBlank.length(),
								newContent.length());
						doc.insertString(getCaretPosition(), newContentSubstring, null);

						// highlight the added text
						endOfCurrentSuggestion = caretPosition + lengthOfAddedContent;
						setCaretPosition(caretPosition + lengthOfAddedContent);
						moveCaretPosition(caretPosition);
					}
				} else {
					oldContent = recentDocText;
					String newContent = lookAhead.doLookAhead(oldContent);
					if (newContent != null) {
						int lengthOld = oldContent.length();
						String newContentSubstring = newContent.substring(lengthOld);
						doc.insertString(getCaretPosition(), newContentSubstring, null);

						// highlight the added text
						setCaretPosition(newContent.length());
						moveCaretPosition(oldContent.length());
					}
				}

			} catch (BadLocationException e) {
				System.err.println(e.getMessage());
			}
		}
	}

	protected TextLookAhead lookAhead;

	// TODO move this out of here
	public interface TextLookAhead {
		public String doLookAhead(String key);
		public void setText(String text);
		public void addWord(String word);
	}

}
