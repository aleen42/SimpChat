package com.Simp;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class SimpChat extends JFrame {

	private JPanel contentPane;
	private int point_x, point_y;
	private boolean isDraging = false;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SimpChat frame = new SimpChat();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public SimpChat() {
		
		setUndecorated(true);	 // Without Border
		
		this.addMouseListener(new MouseAdapter() {		/* geting mouse_point and judge whether it's draging */
			public void mousePressed(MouseEvent e) {
			    isDraging = true;
			    point_x = e.getX();
			    point_y = e.getY();
			   }
			public void mouseReleased(MouseEvent e) {
			    isDraging = false;
			   }
			  });
		
		this.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
			    if (isDraging) { 
			     int left = getLocation().x;
			     int top = getLocation().y;
			     setLocation(left + e.getX() - point_x, top + e.getY() - point_y);
			    } 
			   }
			  }); 
		
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnNewButton = new JButton("New button");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		btnNewButton.setBounds(74, 133, 93, 23);
		contentPane.add(btnNewButton);
	}
}
