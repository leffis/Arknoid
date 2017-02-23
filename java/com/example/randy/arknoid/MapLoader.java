package com.example.randy.arknoid;

import android.Manifest;
import android.content.Context;
import android.os.Environment;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

/* Modified
    * Java
        * Model.java
        * MapLoader.java
        * ItemType.java
        * OnePlayer_game_mode.java
    * AndroidManifest.xml
    * Res
        * raw
 */

/* Txt File Protocol (Format)
	* Specifications
		* Each map is record half of the map that the user actually see
		* The half maps is recorded as the bot side half
		* The protocol below is write as CFL where
			* each terminator is (type,value) and
			* each non terminator is (id)
			* (N) is a special terminator which means "New Line"
		* Edges are added when loading functionally (they will not be inside the saved text file)
		* The "Boolean" type in protocol is actually saved as an Integer (1 or 0)
		* Might need to assign specific values for enum in order to maintain portability
	* Protocol for map files [CFL]
		* (Items)	-> 	(Integer, item type) (Double, orixr) (Double, oriyr) (Boolean, default activate) (N) (Items)
		* (Items) 	-> 	(NULL, null)
	* Protocol for map names file [CFL]
	    * (Maps)    ->  (String, map name) (N) (Maps)
	    * (Maps)    ->  (NULL, null)
 */

public class MapLoader {
    private final Boolean sos = true;

    private Model model;

    private Integer testresid = R.raw.hellowworld;
    private static Integer allmapresid = R.raw.allmap;

    private Map<String,Integer> maps = new HashMap<>();

    private void loadMaps() {
        InputStream inputStream;
        InputStreamReader inputreader;
        BufferedReader readable;
        String linebuf;

        inputStream = model.getMainContext().getResources().openRawResource(allmapresid);
        inputreader = new InputStreamReader(inputStream);
        readable = new BufferedReader(inputreader);

        try {

            while ((linebuf = readable.readLine()) != null) {
                if (sos) Log.d("MapLoader", "loadMaps(): line = " + linebuf);
                maps.put(linebuf,string2resid(linebuf,"raw"));
            }

            readable.close();

        } catch (Exception e) {

            Log.d("MapLoader", "loadTest(): ERROR! Exception Caught! e = " + e.getMessage());

        }
    }

    public static List<String> getMaps(AppCompatActivity mainActivity, Context context) {
        InputStream inputStream;
        InputStreamReader inputreader;
        BufferedReader readable;
        String linebuf;
        List<String> retval;

        retval = new ArrayList<>();

        inputStream = context.getResources().openRawResource(allmapresid);
        inputreader = new InputStreamReader(inputStream);
        readable = new BufferedReader(inputreader);

        try {

            while ((linebuf = readable.readLine()) != null) retval.add(linebuf);

            readable.close();

        } catch (Exception e) {

            Log.d("MapLoader", "getMaps(): ERROR! Exception Caught! e = " + e.getMessage());

        }

        return retval;
    }

    public MapLoader(Model _model) {
        model = _model;

        loadMaps();
    }

    public void loadMap(String mapname, Integer option) { // option = (1,bot) or (2,top) or (3,both)
        int mapresid,i,j,pos,itemtype,activate;
        InputStream inputStream;
        InputStreamReader inputreader;
        BufferedReader readable;
        String linebuf;
        Double orixr,oriyr,y,h;
        Item item;
        Pair<Double,Double> ori;

        if (sos) Log.d("MapLoader","loadMap()::start");

        if (!maps.containsKey(mapname)) {
            Log.d("MapLoader","loadMap(): ERROR! invalid mapname");
            return;
        }

        mapresid = maps.get(mapname);

        inputStream = model.getMainContext().getResources().openRawResource(mapresid);
        inputreader = new InputStreamReader(inputStream);
        readable = new BufferedReader(inputreader);

        try {

            while ((linebuf = readable.readLine()) != null) {

                if (sos) Log.d("MapLoader", "loadMap(): line = " + linebuf);

                pos = 1;
                i = 0;
                itemtype = -1;      // avoid warning
                orixr = (double)-1; // avoid warning
                oriyr = (double)-1; // avoid warning
                activate = -1;      // avoid warning

                for (j = 0; j < linebuf.length(); j++) {

                    if (linebuf.charAt(j) != ' ' && j+1 != linebuf.length()) continue;

                    if (j+1 == linebuf.length()) j++;

                    switch (pos) {
                        case 1:     itemtype = Integer.parseInt(linebuf.substring(i,j));    break;
                        case 2:     orixr = Double.parseDouble(linebuf.substring(i,j));     break;
                        case 3:     oriyr = Double.parseDouble(linebuf.substring(i,j));     break;
                        case 4:     activate = Integer.parseInt(linebuf.substring(i,j));    break;
                        default:    Log.d("MapLoader","loadMap(): ERROR! BAD_POS!");        break;
                    }

                    pos++;
                    i = j+1;
                }

                if (sos) Log.d("MapLoader","loadMap(): (type,orixr,oriyr,activate)="+"("+itemtype+","+orixr+","+oriyr+","+activate+")");

                if (option == 1 || option == 3) { // bot side

                    item = model.itemFactory.createItem(ItemType.int2itemtype(itemtype), orixr, oriyr);
                    item.activated = activate == 1 ? true : false;

                    model.addItem(item);

                }

                if (option == 2 || option == 3) { // top side

                    item = model.itemFactory.createItem(ItemType.int2itemtype(itemtype), orixr, oriyr);
                    item.activated = activate == 1 ? true : false;

                    ori = item.getOri();
                    y = ori.second;
                    h = item.getWH().second;
                    y = y + h/2;
                    y += 2*(0.5-y) - h/2;
                    ori.second = y;

                    item.setOri(ori);
                    item.updatePoints();
                    if (item.isDynamic) ((DynamicItem)item).changeSpeed(new Pair<>((double)0,(double)-1));

                    model.addItem(item);
                }
            }

            readable.close();

        } catch (Exception e) {

            Log.d("MapLoader", "loadMap(): ERROR! Exception Caught! e = " + e.getMessage());

        }
    }

    public void loadTest() {
        InputStream inputStream;
        InputStreamReader inputreader;
        BufferedReader readable;
        String linebuf;

        inputStream = model.getMainContext().getResources().openRawResource(testresid);
        inputreader = new InputStreamReader(inputStream);
        readable = new BufferedReader(inputreader);

        try {
            while ((linebuf = readable.readLine()) != null) {
                Log.d("MapLoader", "loadTest(): line = " + linebuf);
            }
            readable.close();
        } catch (Exception e) {
            Log.d("MapLoader", "loadTest(): ERROR! Exception Caught! e = " + e.getMessage());
        }

        Log.d("MapLoader", "loadTest()::done");

        // System.exit(1);
    }

    public Integer string2resid(String resName, String resFolder) {
        String packagename;

        try {
            packagename = model.getMainActivity().getPackageName();
            return model.getMainActivity().getResources().getIdentifier(resName,resFolder,packagename);
        } catch (Exception e) {
            Log.d("MapLoader","string2resid(): ERROR! Exception Caught! e = " + e.getMessage());
            return null;
        }
    }
}
