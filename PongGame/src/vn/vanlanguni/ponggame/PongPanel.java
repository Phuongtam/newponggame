/*
 * PONG GAME REQUIREMENTS
 * This simple "tennis like" game features two paddles and a ball, 
 * the goal is to defeat your opponent by being the first one to gain 3 point,
 *  a player gets a point once the opponent misses a ball. 
 *  The game can be played with two human players, one on the left and one on 
 *  the right. They use keyboard to start/restart game and control the paddles. 
 *  The ball and two paddles should be red and separating lines should be green. 
 *  Players score should be blue and background should be black.
 *  Keyboard requirements:
 *  + P key: start
 *  + Space key: restart
 *  + W/S key: move paddle up/down
 *  + Up/Down key: move paddle up/down
 *  
 *  Version: 0.5
 */
package vn.vanlanguni.ponggame;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.Timer;
import vn.vanlanguni.ponggame.MyDialogResult;

/**
 * 
 * @author Invisible Man
 *
 */
public class PongPanel extends JPanel implements ActionListener, KeyListener, MouseMotionListener, MouseListener {
	private static final long serialVersionUID = -1097341635155021546L;

	private boolean showTitleScreen = true;
	private boolean playing;
	private boolean gameOver;


	/** Background. */
	private Color backgroundColor = Color.BLACK;
	ImageIcon imaBackGround, imaStart, imaOver;

	/** Drawing start button and setting username */
	private static final int WIDTH = 500;
	private static final int HEIGHT = 500;
	Color buttonColor = Color.BLUE;
	Rectangle rect;
	Rectangle rect2;
	Rectangle rect3;
	ImageIcon iconButton = new ImageIcon("ImageBall/button.png");
	ImageIcon iconSetting = new ImageIcon("imageBall/iconSetting.png");
	boolean hover;
	boolean pressed;
	boolean dragged;
	int w = 100, h = 30;
	int x = WIDTH / 2 - w / 2, y = 320;
	int dx, dy;
	JLabel lblUser1 = new JLabel(), lblUser2 = new JLabel();
	JButton btnSetting = new JButton(null, iconSetting);

	/** State on the control keys. */
	private boolean upPressed;
	private boolean downPressed;
	private boolean wPressed;
	private boolean sPressed;

	/** The ball: position, diameter */
	private int ballX = 250;
	private int ballY = 250;
	private int diameter = 30;
	private int ballDeltaX = -1;
	private int ballDeltaY = 3;
	ImageIcon imaBall1, imaBall2, imaBall;
	JRadioButton radBall1 = new JRadioButton("", true), radBall2 = new JRadioButton();
	ButtonGroup btngBall = new ButtonGroup();
	JPanel pnlSelect = new JPanel();

	/** Player 1's paddle: position and size */
	private int playerOneX = 0;
	private int playerOneY = 250;
	private int playerOneWidth = 10;
	private int playerOneHeight = 60;
	ImageIcon imaPaddle1;

	/** Player 2's paddle: position and size */
	private int playerTwoX = 484;
	private int playerTwoY = 250;
	private int playerTwoWidth = 10;
	private int playerTwoHeight = 60;
	ImageIcon imaPaddle2;

	/** Speed of the paddle - How fast the paddle move. */
	private int paddleSpeed = 5;

	/** Player score, show on upper left and right. */
	private int playerOneScore;
	private int playerTwoScore;

	/** Construct a PongPanel. */
	public PongPanel() {
		setBackground(backgroundColor);
		// listen to key presses
		setFocusable(true);
		addKeyListener(this);
		addMouseMotionListener(this);
		addMouseListener(this);

		// call step() 60 fps
		Timer timer = new Timer(1000 / 60, this);
		timer.start();
	}

	/** Implement actionPerformed */
	public void actionPerformed(ActionEvent e) {
		step();
		// select ball
		if (radBall1.isSelected()) {
			// System.out.println(1);
			imaStart = new ImageIcon("ImageBall/anhnenstart.jpg");
			imaBackGround = new ImageIcon("ImageBall/co-nhan-tao-2.jpg");
			imaBall = new ImageIcon("ImageBall/ball.png");
			imaOver = new ImageIcon("ImageBall/imaOver2.jpg");

		} else if (radBall2.isSelected()) {
			imaBackGround = new ImageIcon("ImageBall/imaPlaying2.jpg");
			imaBall = new ImageIcon("ImageBall/ball2.png");
			imaStart = new ImageIcon("ImageBall/anhnenStart2.jpg");
			imaOver = new ImageIcon("ImageBall/imaOver1.jpg");
			// System.out.println(2);
		}
	}

	/** Repeated task */
	public void step() {

		if (playing) {

			/* Playing mode */

			// move player 1
			// Move up if after moving, paddle is not outside the screen
			if (wPressed && playerOneY - paddleSpeed > 0) {
				playerOneY -= paddleSpeed;
			}
			// Move down if after moving paddle is not outside the screen
			if (sPressed && playerOneY + playerOneHeight + paddleSpeed < getHeight()) {
				playerOneY += paddleSpeed;
			}

			// move player 2
			// Move up if after moving paddle is not outside the screen
			if (upPressed && playerTwoY - paddleSpeed > 0) {
				playerTwoY -= paddleSpeed;
			}
			// Move down if after moving paddle is not outside the screen
			if (downPressed && playerTwoY + playerTwoHeight + paddleSpeed < getHeight()) {
				playerTwoY += paddleSpeed;
			}

			/*
			 * where will the ball be after it moves? calculate 4 corners: Left,
			 * Right, Top, Bottom of the ball used to determine whether the ball
			 * was out yet
			 */

			int nextBallLeft = ballX + ballDeltaX;
			int nextBallRight = ballX + diameter + ballDeltaX;
			// FIXME Something not quite right here
			int nextBallTop = ballY + ballDeltaY;
			int nextBallBottom = ballY + diameter + ballDeltaY;

			// Player 1's paddle position
			int playerOneRight = playerOneX + playerOneWidth;
			int playerOneTop = playerOneY;
			int playerOneBottom = playerOneY + playerOneHeight;

			// Player 2's paddle position
			float playerTwoLeft = playerTwoX;
			float playerTwoTop = playerTwoY;
			float playerTwoBottom = playerTwoY + playerTwoHeight;

			// ball bounces off top and bottom of screen
			if (nextBallTop < 0) {
				ballDeltaY = 3;
			} else if (nextBallBottom > 475) {
				ballDeltaY = -3;
			}

			// will the ball go off the left side?
			if (nextBallLeft < playerOneRight) {
				// is it going to miss the paddle?
				if (nextBallTop > playerOneBottom || nextBallBottom < playerOneTop) {
					playerTwoScore++;

					// Player 2 Win, restart the game
					if (playerTwoScore == 3) {
						playing = false;
						gameOver = true;
					}
					ballX = 250;
					ballY = 250;
				} else {
					// dieu chinh goc bat cua bong paddle 1
					if (ballDeltaY == -3) {
						if (nextBallLeft <= playerOneTop + 15 || nextBallLeft >= playerOneBottom - 15) {
							ballDeltaY = -5;
						} else if (nextBallLeft < playerOneTop + 30) {
							ballDeltaY = -4;
						} else if (nextBallLeft < playerOneTop + 45) {
							ballDeltaY = -3;
						}
					} else {
						if (nextBallLeft <= playerOneTop + 15 || nextBallLeft >= playerOneBottom - 15) {
							ballDeltaY = 5;
						} else if (nextBallLeft < playerOneTop + 30) {
							ballDeltaY = 4;
						} else if (nextBallLeft < playerOneTop + 45) {
							ballDeltaY = 3;
						}
					}

					// If the ball hitting the paddle, it will bounce back
					// FIXME Something wrong here
					ballDeltaX *= -1;
					
				}
			}

			// will the ball go off the right side?
			if (nextBallRight > playerTwoLeft) {
				// is it going to miss the paddle?
				if (nextBallTop > playerTwoBottom || nextBallBottom < playerTwoTop) {
					playerOneScore++;

					// Player 1 Win, restart the game
					if (playerOneScore == 3) {
						playing = false;
						gameOver = true;
					}
					ballX = 250;
					ballY = 250;
				} else {
					// dieu chinh goc bat cua bong paddle 2
					if (ballDeltaY == -3) {
						if (nextBallRight <= playerTwoTop + 15 || nextBallRight >= playerTwoBottom - 15) {
							ballDeltaY = -5;
						} else if (nextBallRight < playerTwoTop + 30) {
							ballDeltaY = -4;
						} else if (nextBallRight < playerTwoTop + 45) {
							ballDeltaY = -3;
						}

					} else {
						if (nextBallRight <= playerTwoTop + 15 || nextBallRight >= playerTwoBottom - 15) {
							ballDeltaY = 5;
						} else if (nextBallRight < playerTwoTop + 30) {
							ballDeltaY = 4;
						} else if (nextBallRight < playerTwoTop + 45) {
							ballDeltaY = 3;
						}

					}

					// If the ball hitting the paddle, it will bounce back
					// FIXME Something wrong here
					ballDeltaX *= -1;
				}
			}

			// move the ball
			ballX += ballDeltaX;
			ballY += ballDeltaY;
		}

		// stuff has moved, tell this JPanel to repaint itself
		repaint();
	}

	/** Paint the game screen. */
	public void paintComponent(Graphics g) {

		super.paintComponent(g);
		add(lblUser1);
		add(lblUser2);
		if (showTitleScreen) {
			pnlSelect.setLayout(null);
			pnlSelect.setOpaque(false);
			btngBall.add(radBall1);
			btngBall.add(radBall2);

			pnlSelect.add(radBall1);
			pnlSelect.add(radBall2);

			radBall1.setBounds(0, 0, 20, 20);
			radBall2.setBounds(60, 0, 20, 20);

			radBall1.setContentAreaFilled(false);
			radBall2.setContentAreaFilled(false);

			this.add(pnlSelect);
			pnlSelect.setBounds(200, 280, 100, 25);
			pnlSelect.setVisible(true);
			// background screen
			g.drawImage(imaStart.getImage(), 0, 0, getWidth(), getHeight(), null);

			// draw button start and setting
			// add(btnSetting);
			// btnSetting.setBorderPainted(false);
			// btnSetting.setContentAreaFilled(false);
			// btnSetting.setBounds(450, 10, 40, 40);
			int x0 = 450, y0 = 10, w0 = 40, h0 = 40;

			rect = new Rectangle(x, y, w, h);
			// rect3 = new Rectangle(x0, y0, w0, h0);
			if (hover) {
				if (pressed) {
					g.drawImage(iconButton.getImage(), x, y, x + w, y + h, 0, 214, 371, 214 + 106, null);
					g.setColor(Color.RED);
				} else {
					g.drawImage(iconButton.getImage(), x, y, x + w, y + h, 0, 0, 371, 108, null);
					g.setColor(Color.WHITE);
				}
			} else {
				g.drawImage(iconButton.getImage(), x, y, x + w, y + h, 0, 108, 371, 108 + 106, null);
				g.setColor(Color.WHITE);
			}
			g.setFont(new Font("Tahoma", Font.BOLD, 15));
			g.drawString("Start !!!", x + 30, y + 19);
			// draw Username
			g.drawString("Username 1: ", 180, 150);
			g.drawString("Username 2: ", 180, 180);
			lblUser1.setBounds(280, 135, 200, 20);
			lblUser2.setBounds(280, 165, 200, 20);

			// draw Balllist
			imaBall1 = new ImageIcon("ImageBall/ball.png");
			imaBall2 = new ImageIcon("ImageBall/ball2.png");
			g.drawImage(imaBall1.getImage(), 190, 230, 40, 40, null);
			g.drawImage(imaBall2.getImage(), 250, 230, 40, 40, null);
			/* Show welcome screen */

			// Draw game title and start message
			g.setColor(Color.BLUE);
			g.setFont(new Font(Font.DIALOG, Font.BOLD, 36));
			g.drawString("Pong Game", 130, 100);

			// FIXME Wellcome message below show smaller than game title
			g.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
			g.drawString("Press 'P' to play.", 140, 400);
		} else if (playing) {
			// disable select ball
			// BallSelect(2);
			pnlSelect.setVisible(false);
			// background ion
			g.drawImage(imaBackGround.getImage(), 0, 0, 500, 500, Color.black, null);

			/* Game is playing */

			// set the coordinate limit
			int playerOneRight = playerOneX + playerOneWidth;
			int playerTwoLeft = playerTwoX - 1;

			// draw dashed line down center
			for (int lineY = 0; lineY < getHeight(); lineY += 50) {
				g.setColor(Color.YELLOW);
				g.drawLine(250, lineY, 250, lineY + 25);
			}

			// draw "goal lines" on each side
			g.drawLine(playerOneRight, 0, playerOneRight, getHeight());
			g.drawLine(playerTwoLeft, 0, playerTwoLeft, getHeight());

			// draw the scores and username
			g.setFont(new Font(Font.DIALOG, Font.BOLD, 36));
			g.setColor(Color.GREEN);
			g.drawString(String.valueOf(playerOneScore), 100, 100); // Player 1
																	// score
			g.drawString(String.valueOf(playerTwoScore), 400, 100); // Player 2
																	// score
			lblUser1.setBounds(30, 10, 100, 30);// username1
			lblUser2.setBounds(420, 10, 100, 30);// username2

			// draw the ball
			g.drawImage(imaBall.getImage(), ballX, ballY, diameter, diameter, null);

			// draw the paddles
			imaPaddle1 = new ImageIcon("ImageBall/paddle1.png");
			g.drawImage(imaPaddle1.getImage(), playerOneX, playerOneY, playerOneWidth, playerOneHeight, Color.BLACK,
					null);

			imaPaddle2 = new ImageIcon("ImageBall/paddle2.png");
			g.drawImage(imaPaddle2.getImage(), playerTwoX, playerTwoY, playerTwoWidth, playerTwoHeight, Color.BLACK,
					null);
		} else if (gameOver) {
			// disable select ball
			pnlSelect.setVisible(false);
			// background gameOver
			g.drawImage(imaOver.getImage(), 0, 0, getWidth(), getHeight(), null);

			/* Show End game screen with winner name and score */

			// Draw scores
			// TODO Set Blue color

			g.setFont(new Font(Font.DIALOG, Font.BOLD, 36));
			g.setColor(Color.GREEN);
			g.drawString(String.valueOf(playerOneScore), 100, 100);
			g.drawString(String.valueOf(playerTwoScore), 400, 100);

			// Draw the winner name
			g.setFont(new Font(Font.DIALOG, Font.BOLD, 36));
			if (playerOneScore > playerTwoScore) {
				g.drawString("Player 1 Wins!", 140, 200);
			} else {
				g.drawString("Player 2 Wins!", 140, 200);
			}

			// Draw Restart message
			g.setFont(new Font(Font.DIALOG, Font.BOLD, 18));
			// TODO Draw a restart message
			g.drawString("Press 'SpaceBar' to restart.", 130, 400);
		}
	}

	public void keyTyped(KeyEvent e) {
	}

	public void keyPressed(KeyEvent e) {
		if (showTitleScreen) {
			if (e.getKeyChar() == 'p' || e.getKeyChar() == 'P') {
				showTitleScreen = false;
				playing = true;
				// gameOver = false;
			}
		} else if (playing) {
			if (e.getKeyCode() == KeyEvent.VK_UP) {
				upPressed = true;
			} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				downPressed = true;
			} else if (e.getKeyCode() == KeyEvent.VK_W) {
				wPressed = true;
			} else if (e.getKeyCode() == KeyEvent.VK_S) {
				sPressed = true;
			}

		} else if (gameOver && e.getKeyCode() == KeyEvent.VK_SPACE) {
			gameOver = false;
			showTitleScreen = true;
			playerOneY = 250;
			playerTwoY = 250;
			ballX = 250;
			ballY = 250;
			playerOneScore = 0;
			playerTwoScore = 0;
		}
	}

	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			upPressed = false;
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			downPressed = false;
		} else if (e.getKeyCode() == KeyEvent.VK_W) {
			wPressed = false;
		} else if (e.getKeyCode() == KeyEvent.VK_S) {
			sPressed = false;
		}
	}

	public void Setting() {
		// if (rect.contains(e.getPoint())) {
		SecondWindow w = new SecondWindow();
		w.setLocationRelativeTo(PongPanel.this);
		w.setVisible(true);
		Settings s = w.getSetings();

		// Stop and wait for user input

		if (w.dialogResult == MyDialogResult.YES) {
			lblUser1 = new JLabel(s.getUserName1());
			lblUser2 = new JLabel(s.getUserName2());

			lblUser1.setForeground(Color.BLUE);
			lblUser2.setForeground(Color.BLUE);

			lblUser1.setFont(new Font("Tahoma", Font.BOLD, 15));
			lblUser2.setFont(new Font("Tahoma", Font.BOLD, 15));

		} else {
			System.out.println("User chose to cancel");
		}
	}
	// }

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if (rect.contains(e.getPoint())) {
			Setting();
			showTitleScreen = false;
			playing = true;
		}

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		/*
		 * pressed = true; if (rect.contains(e.getX(), e.getY())) { dx =
		 * e.getX() - x; dy = e.getY() - y; dragged = true; } repaint();
		 */
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		/*
		 * pressed = false; dragged = false; repaint();
		 */
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		/*
		 * if (dragged && PongPanel.this.getBounds().contains(e.getPoint())) { x
		 * = e.getX() - dx; y = e.getY() - dy; repaint(); System.out.format(
		 * "Mouse x: %d , Mouse y: %d, dx: %d, dx: %d", e.getX(), e.getY(), dx,
		 * dy); }
		 */
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		// System.out.format("x=%d - y=%d", e.getX(), e.getY());
		if (rect.contains(e.getX(), e.getY())) {
			// iconSetting = new ImageIcon("imageBall/iconSetting2.png");
			// btnSetting.setIcon(iconSetting);
			buttonColor = Color.RED;
			hover = true;
			setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		} else {
			buttonColor = Color.BLUE;
			hover = false;
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
		repaint();
	}

}
