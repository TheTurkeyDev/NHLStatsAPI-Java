package com.theprogrammingturkey.nhlapi;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.theprogrammingturkey.nhlapi.criteria.SerchCriteria;
import com.theprogrammingturkey.nhlapi.data.GameData;
import com.theprogrammingturkey.nhlapi.managers.GameManager;
import com.theprogrammingturkey.nhlapi.managers.ImageManager;

public class ShotStatsExample
{
	/**
	 * @formatter:off
	 * Keys:
	 * 	Total shots 0-20		TS0020
	 * 	Total shots 20-25		TS2025
	 * 	Total shots 25-30		TS2530
	 * 	Total shots 30-35		TS3035
	 * 	Total shots 35-40		TS3540
	 * 	Total shots 40+			TS40PL
	 * 	Shot Diff 20-			SDN20PL
	 * 	Shot Diff (-20)-(-15)	SDN2015
	 * 	Shot Diff (-15)-(-10)	SDN1510
	 * 	Shot Diff (-10)-(-5)	SDN1005
	 * 	Shot Diff (-5)-(-1)		SDN0501
	 *  Shot Diff 0-0			SDS0000
	 * 	Shot Diff 3-5			SDP0105
	 * 	Shot Diff 5-10			SDP0510
	 * 	Shot Diff 10-15			SDP1015
	 * 	Shot Diff 15-20			SDP1520
	 * 	Shot Diff 20+			SDP20PL
	 * @formatter:on
	 */
	private static Map<String, List<TeamInfo>> info = new HashMap<String, List<TeamInfo>>();

	public static void main(String[] args) throws IOException
	{

		NHLAPI.DEBUG = true;

		info.put("TS0020", new ArrayList<TeamInfo>());
		info.put("TS2025", new ArrayList<TeamInfo>());
		info.put("TS2530", new ArrayList<TeamInfo>());
		info.put("TS3035", new ArrayList<TeamInfo>());
		info.put("TS3540", new ArrayList<TeamInfo>());
		info.put("TS40PL", new ArrayList<TeamInfo>());
		info.put("SDN20PL", new ArrayList<TeamInfo>());
		info.put("SDN2015", new ArrayList<TeamInfo>());
		info.put("SDN1510", new ArrayList<TeamInfo>());
		info.put("SDN1005", new ArrayList<TeamInfo>());
		info.put("SDN0501", new ArrayList<TeamInfo>());
		info.put("SDP20PL", new ArrayList<TeamInfo>());
		info.put("SDP2015", new ArrayList<TeamInfo>());
		info.put("SDP1510", new ArrayList<TeamInfo>());
		info.put("SDP1005", new ArrayList<TeamInfo>());
		info.put("SDP0501", new ArrayList<TeamInfo>());
		info.put("SDS0000", new ArrayList<TeamInfo>());

		SerchCriteria criteria = new SerchCriteria();
		criteria.setStartDate("2017-10-02");
		criteria.setEndDate("2018-04-09");

		System.out.println("Collecting game data...");
		List<GameData> games = GameManager.getGames(criteria);
		System.out.println("Parsing game data...");

		for(GameData game : games)
		{
			int homeTeamID = game.homeTeam.id;
			int homeShots = game.homeBoxScore.shots;
			int awayTeamID = game.awayTeam.id;
			int awayShots = game.awayBoxScore.shots;
			boolean homeWon = game.homeTeamWon();
			
			if(homeTeamID > 55 || awayTeamID > 55)
				continue;

			TeamInfo home;
			if(homeShots <= 20)
				home = getTeamInfoFromID("TS0020", homeTeamID);
			else if(homeShots <= 25)
				home = getTeamInfoFromID("TS2025", homeTeamID);
			else if(homeShots <= 30)
				home = getTeamInfoFromID("TS2530", homeTeamID);
			else if(homeShots <= 35)
				home = getTeamInfoFromID("TS3035", homeTeamID);
			else if(homeShots <= 40)
				home = getTeamInfoFromID("TS3540", homeTeamID);
			else
				home = getTeamInfoFromID("TS40PL", homeTeamID);

			if(homeWon)
				home.incWins();
			else
				home.incLosses();

			TeamInfo away;
			if(awayShots <= 20)
				away = getTeamInfoFromID("TS0020", awayTeamID);
			else if(awayShots <= 25)
				away = getTeamInfoFromID("TS2025", awayTeamID);
			else if(awayShots <= 30)
				away = getTeamInfoFromID("TS2530", awayTeamID);
			else if(awayShots <= 35)
				away = getTeamInfoFromID("TS3035", awayTeamID);
			else if(awayShots <= 40)
				away = getTeamInfoFromID("TS3540", awayTeamID);
			else
				away = getTeamInfoFromID("TS40PL", awayTeamID);

			if(homeWon)
				away.incLosses();
			else
				away.incWins();

			int diff = homeShots - awayShots;
			if(diff >= 20)
			{
				home = getTeamInfoFromID("SDP20PL", homeTeamID);
				away = getTeamInfoFromID("SDN20PL", awayTeamID);
			}
			else if(diff >= 15)
			{
				home = getTeamInfoFromID("SDP2015", homeTeamID);
				away = getTeamInfoFromID("SDN2015", awayTeamID);
			}
			else if(diff >= 10)
			{
				home = getTeamInfoFromID("SDP1510", homeTeamID);
				away = getTeamInfoFromID("SDN1510", awayTeamID);
			}
			else if(diff >= 5)
			{
				home = getTeamInfoFromID("SDP1005", homeTeamID);
				away = getTeamInfoFromID("SDN1005", awayTeamID);
			}
			else if(diff >= 1)
			{
				home = getTeamInfoFromID("SDP0501", homeTeamID);
				away = getTeamInfoFromID("SDN0501", awayTeamID);
			}
			else if(diff == 0)
			{
				home = getTeamInfoFromID("SDS0000", homeTeamID);
				away = getTeamInfoFromID("SDS0000", awayTeamID);
			}
			else if(diff >= -5)
			{
				home = getTeamInfoFromID("SDN0501", homeTeamID);
				away = getTeamInfoFromID("SDP0501", awayTeamID);
			}
			else if(diff >= -10)
			{
				home = getTeamInfoFromID("SDN1005", homeTeamID);
				away = getTeamInfoFromID("SDP1005", awayTeamID);
			}
			else if(diff >= -15)
			{
				home = getTeamInfoFromID("SDN1510", homeTeamID);
				away = getTeamInfoFromID("SDP1510", awayTeamID);
			}
			else if(diff >= -20)
			{
				home = getTeamInfoFromID("SDN2015", homeTeamID);
				away = getTeamInfoFromID("SDP2015", awayTeamID);
			}
			else
			{
				home = getTeamInfoFromID("SDN20PL", homeTeamID);
				away = getTeamInfoFromID("SDP20PL", awayTeamID);
			}

			if(homeWon)
			{
				home.incWins();
				away.incLosses();
			}
			else
			{
				away.incWins();
				home.incLosses();
			}
		}

		newFrame("TS0020");
		newFrame("TS2025");
		newFrame("TS2530");
		newFrame("TS3035");
		newFrame("TS3540");
		newFrame("TS40PL");
		newFrame("SDN20PL");
		newFrame("SDN2015");
		newFrame("SDN1510");
		newFrame("SDN1005");
		newFrame("SDN0501");
		newFrame("SDP20PL");
		newFrame("SDP2015");
		newFrame("SDP1510");
		newFrame("SDP1005");
		newFrame("SDP0501");
		newFrame("SDS0000");
	}

	public static void newFrame(String type)
	{
		JFrame f = new JFrame(type);
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		f.setLayout(null);
		f.setSize(800, 800);
		JPanel panel = new shotTotalJpanel(type);
		panel.setSize(800, 800);
		panel.setBounds(0, 0, 800, 800);
		panel.setVisible(true);
		f.add(panel);
		f.setVisible(true);
	}

	public static TeamInfo getTeamInfoFromID(String type, int id)
	{
		TeamInfo infoReturn = null;
		for(TeamInfo info : info.get(type))
			if(info.getId() == id)
				infoReturn = info;
		if(infoReturn == null)
		{
			infoReturn = new TeamInfo(id, 0, 0);
			info.get(type).add(infoReturn);
		}
		return infoReturn;
	}

	static class shotTotalJpanel extends JPanel
	{
		private static final long serialVersionUID = 1L;

		private BufferedImage paintImage = new BufferedImage(800, 2000, BufferedImage.TYPE_INT_ARGB);

		private String type;
		private boolean saved = false;

		public shotTotalJpanel(String type)
		{
			this.type = type;
		}

		@Override
		protected void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			updatePaint();
			g.drawImage(paintImage, 0, 0, null);
			if(!saved)
			{
				save();
				saved = true;
			}
		}

		// draw painting
		public void updatePaint()
		{
			Graphics g = paintImage.createGraphics();
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, paintImage.getWidth(), paintImage.getHeight());
			g.setColor(Color.WHITE);

			int x = 10;
			int y = 10;
			List<TeamInfo> copy = new ArrayList<TeamInfo>();
			copy.addAll(info.get(type));
			while(copy.size() > 0)
			{
				TeamInfo ti = getMostGP(copy);
				g.drawImage(ImageManager.getLogoForTeam(ti.getId(), 60, 60, true), x, y, null);
				g.drawString("Record: " + ti.getWins() + "-" + ti.getLoses(), x + 60, y + 35);
				int xoff = x + 150;
				g.setColor(Color.GREEN);
				g.fillRect(xoff, y + 15, 10 * ti.getWins(), 30);
				xoff += 10 * ti.getWins();
				g.setColor(Color.RED);
				g.fillRect(xoff, y + 15, 10 * ti.getLoses(), 30);
				g.setColor(Color.WHITE);
				y += 60;
			}

			g.dispose();
			// repaint panel with new modified paint
			repaint();
		}

		public void save()
		{
			try
			{
				ImageIO.write(paintImage, "PNG", new File(type + ".png"));
			} catch(Exception e)
			{
				e.printStackTrace();
			}
		}

		public TeamInfo getMostGP(List<TeamInfo> teams)
		{
			int mostGP = teams.get(0).getGames();
			int index = 0;

			for(int i = 1; i < teams.size(); i++)
			{
				if(mostGP < teams.get(i).getGames())
				{
					mostGP = teams.get(i).getGames();
					index = i;
				}
			}

			return teams.remove(index);
		}
	}

	static class TeamInfo
	{
		private int id;
		private int wins;
		private int losses;

		public TeamInfo(int id, int wins, int losses)
		{
			this.id = id;
			this.wins = wins;
			this.losses = losses;
		}

		public int getId()
		{
			return id;
		}

		public int getWins()
		{
			return wins;
		}

		public int getLoses()
		{
			return losses;
		}

		public int getGames()
		{
			return wins + losses;
		}

		public void incWins()
		{
			wins++;
		}

		public void incLosses()
		{
			losses++;
		}
	}
}
