package com.Simp;

import javax.swing.JPanel;
import javax.swing.JLabel;

import java.awt.Color;
import java.awt.Font;

public class UserList_Item extends JPanel {

	/**
	 * Create the panel.
	 */
	public UserList_Item(String User_name) {
		setOpaque(false);
		JLabel lblA = new JLabel(User_name);
		lblA.setForeground(Color.WHITE);
		lblA.setFont(new Font("Arial", Font.PLAIN, 18));
		add(lblA);
		
	}

}
