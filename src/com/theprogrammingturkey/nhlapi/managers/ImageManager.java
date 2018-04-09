package com.theprogrammingturkey.nhlapi.managers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.TranscodingHints;
import org.apache.batik.transcoder.image.ImageTranscoder;
import org.apache.batik.util.SVGConstants;
import org.apache.commons.io.FileUtils;

public class ImageManager
{
	//@formatter:off
	//TODO: Find better California Golden Seals logo (CSE)
	private static String[] idToString = { "NJD", "NYI", "NYR", "PHI", "PIT", "BOS", "BUF", 
			"MTL", "OTT", "TOR", "ATL_19992000-20102011", "CAR", "FLA", "TBL", "WSH", "CHI",
			"DET", "NSH", "STL", "CGY", "COL", "EDM", "VAN", "ANA", "DAL", "LAK", 
			"PHX_20032004-20132014", "SJS", "CBJ", "MIN", "MNS_19851986-19901991", 
			"QUE_19791980-19941995", "WIN_19901991-19951996", "HFD_19791980-19911992", 
			"CLR_19761977-19811982", "SEN_19171918-19331934", "HAM_19201921", "PIR_19291930", 
			"QUA_19301931", "DCG_19261927-19291930", "MWN_19171918", "QBD_19191920", 
			"MMR_19241925", "NYA_19251926-19291930", "SLE_19341935", "OAK_19671968-19691970",
			"AFM_19721973-19791980", "KCS_19741975-19751976", "CLE_19761977-19771978", 
			"DFL_19301931-19311932", "BRK_19411942", "WPG", "ARI", "VGK", "" ,
			"OAK_19671968-19691970", "TAN_19171918-19181919", "TSP_19191920-19211922",
			"", /*CAN*/"", /*CZE*/"", /*FIN*/"", /*GER*/"", /*RUS*/"", /*SVK*/"", /*SWE*/"",
			/*USA*/"", "", "", "", "", "", "", "", "", "", "", "", "", /*Helsinki*/"",
			/*Stockholm*/"", "", "", "", "", /*Atlantic*/"", /*Metropolitan*/"", /*Central*/"", 
			/*Pacific*/"", /*Team Alfredsson*/"", /*Team Chara*/"", /*Team Foligno*/"", 
			/*Team Toews*/"", /*Team Staal*/"", /*Team Lidstrom*/"", /*All-Stars East*/"", 
			/*All-Stars West*/"", "", /*Young Stars East*/"", /*Young Stars West*/""};
	private static Map<Integer, BufferedImage> imageCache = new HashMap<>();
	//@formatter:on

	public static BufferedImage getLogoForTeam(int teamID, int width, int height, boolean dark)
	{
		if(imageCache.containsKey(teamID))
			return imageCache.get(teamID);

		try
		{
			URL url = new URL("http://cdn.nhle.com/logos/nhl/svg/" + idToString[teamID - 1] + "_" + (dark ? "dark" : "light") + ".svg");

			final BufferedImage[] imagePointer = new BufferedImage[1];

			// Rendering hints can't be set programatically, so
			// we override defaults with a temporary stylesheet.
			// These defaults emphasize quality and precision, and
			// are more similar to the defaults of other SVG viewers.
			// SVG documents can still override these defaults.
			String css = "svg {" + "shape-rendering: geometricPrecision;" + "text-rendering:  geometricPrecision;" + "color-rendering: optimizeQuality;" + "image-rendering: optimizeQuality;}";
			File cssFile = File.createTempFile("batik-default-override-", ".css");
			FileUtils.writeStringToFile(cssFile, css);

			TranscodingHints transcoderHints = new TranscodingHints();
			transcoderHints.put(ImageTranscoder.KEY_XML_PARSER_VALIDATING, Boolean.FALSE);
			transcoderHints.put(ImageTranscoder.KEY_DOM_IMPLEMENTATION, SVGDOMImplementation.getDOMImplementation());
			transcoderHints.put(ImageTranscoder.KEY_DOCUMENT_ELEMENT_NAMESPACE_URI, SVGConstants.SVG_NAMESPACE_URI);
			transcoderHints.put(ImageTranscoder.KEY_DOCUMENT_ELEMENT, "svg");
			transcoderHints.put(ImageTranscoder.KEY_USER_STYLESHEET_URI, cssFile.toURI().toString());
			transcoderHints.put(ImageTranscoder.KEY_WIDTH, Float.valueOf(width));
			transcoderHints.put(ImageTranscoder.KEY_HEIGHT, Float.valueOf(height));

			try
			{

				TranscoderInput input = new TranscoderInput(url.openStream());

				ImageTranscoder t = new ImageTranscoder()
				{

					@Override
					public BufferedImage createImage(int w, int h)
					{
						return new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
					}

					@Override
					public void writeImage(BufferedImage image, TranscoderOutput out) throws TranscoderException
					{
						imagePointer[0] = image;
					}
				};
				t.setTranscodingHints(transcoderHints);
				t.transcode(input, null);
			} catch(TranscoderException ex)
			{
				ex.printStackTrace();
				throw new IOException("Couldn't convert image from :" + url.toString());
			} finally
			{
				cssFile.delete();
			}

			imageCache.put(teamID, imagePointer[0]);
			return imagePointer[0];
		} catch(IOException e)
		{
			e.printStackTrace();
			return new BufferedImage(0, 0, BufferedImage.TYPE_CUSTOM);
		}
	}
}
