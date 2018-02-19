package com.theprogrammingturkey.nhlapi;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.theprogrammingturkey.nhlapi.criteria.SerchCriteria;
import com.theprogrammingturkey.nhlapi.data.GameData;
import com.theprogrammingturkey.nhlapi.data.PlayData;
import com.theprogrammingturkey.nhlapi.managers.GameManager;

public class ShotLocationExample extends JPanel
{
	private static final long serialVersionUID = 1L;

	private static Map<String, Integer> locations = new HashMap<>();
	private static int max = 0;

	private static BufferedImage image;

	public static void main(String[] args) throws IOException
	{

		NHLAPI.DEBUG = true;

		image = ImageIO.read(new File("examples/res/rink.png"));

		SerchCriteria criteria = new SerchCriteria();
		criteria.setStartDate("2017-10-02");
		criteria.setEndDate("2018-02-11");

		System.out.println("Collecting game data...");
		List<GameData> games = GameManager.getGames(criteria);
		System.out.println("Parsing game data...");

		for(GameData game : games)
		{
			for(PlayData play : game.plays)
			{
				if(play.event.equalsIgnoreCase("shot"))
				{
					int x = play.xCoord;
					int y = play.yCoord;
					if(x < 0)
					{
						x = Math.abs(x);
						y *= -1;
					}
					String key = x + "," + y;
					int count = 1;
					if(locations.containsKey(key))
						count = locations.get(key) + 1;

					if(count > max)
						max = count;

					locations.put(key, count);
				}
			}
		}

		JFrame f = new JFrame("Shot chart");
		f.setLayout(null);
		f.setSize(800, 800);
		JPanel panel = new ShotLocationExample();
		panel.setSize(800, 800);
		panel.setBounds(0, 0, 800, 800);
		panel.setVisible(true);
		panel.setBackground(Color.GREEN);
		f.add(panel);
		f.setVisible(true);
	}

	// X: -99-99
	// Y: -41-42
	public void paintComponent(Graphics g)
	{
		// super.paintComponents(g);
		// TODO: Needs some tweaking of positions
		g.drawImage(image, 0, 0, null);
		for(String pos : locations.keySet())
		{
			int x = Integer.parseInt(pos.substring(0, pos.indexOf(",")));
			int y = Integer.parseInt(pos.substring(pos.indexOf(",") + 1));
			int alpha = (int) (((double) locations.get(pos) / max) * 255);
			g.setColor(new Color(alpha, 255 - alpha, 0, 100));
			g.fillRect((x * 8) + 350, (y * 8), 8, 8);
		}
	}
}
