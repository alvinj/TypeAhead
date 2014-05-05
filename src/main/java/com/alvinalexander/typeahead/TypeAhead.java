package com.alvinalexander.typeahead;

import javax.swing.*;
import java.awt.BorderLayout;

public class TypeAhead {

	public static void main(String[] args) {
		JFrame f = new JFrame("Type Ahead");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.getContentPane().setLayout(new BorderLayout());

		DocumentLookAhead lookAhead = new DocumentLookAhead();
		JScrollPane scrollPane = new JScrollPane();
		LookAheadTextPane tf = new LookAheadTextPane(lookAhead);
		scrollPane.getViewport().add(tf);
		scrollPane.getViewport().setPreferredSize(tf.getPreferredSize());

		try {
			f.getContentPane().add(scrollPane, java.awt.BorderLayout.CENTER);
			f.pack();
			f.setLocationRelativeTo(null);
			f.setVisible(true);
		} catch(RuntimeException e) {
			// do nothing
		}
	}

}
