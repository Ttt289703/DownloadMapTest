package com.example.downloadmaptest.model.parser;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.example.downloadmaptest.R;
import com.example.downloadmaptest.model.data.Region;
import com.example.downloadmaptest.screens.regions.RegionService;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

public class XMLRegionsParser {

    private static final String TAG_REGION = "region";

    private static final String ATTRIBUTE_TYPE = "type";
    private static final String ATTRIBUTE_NAME = "name";
    private static final String ATTRIBUTE_MAP = "map";
    private static final String ATTRIBUTE_TRANSLATE = "translate";

    private static final String VALUE_TYPE_CONTINENT = "continent";
    private static final String VALUE_TYPE_MAP = "map";
    private static final String VALUE_TYPE_HILLSHADE = "hillshade";
    private static final String VALUE_TYPE_SRTM = "srtm";

    private static final String VALUE_MAP_YES = "yes";
    private static final String VALUE_MAP_NO = "no";


    XMLRegionsParser() {

    }

    public static ArrayList<Region> parseRegions(Context context) {
        ArrayList<Region> regions = new ArrayList<>();

        try {
            XmlPullParser parser = context.getResources().getXml(R.xml.regions);

            while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                final String TAG = "ЛогКот";
                String tmp = "";

                switch (parser.getEventType()) {
                    case XmlPullParser.START_DOCUMENT:
                        Log.d(TAG, "Начало документа");
                        break;
                    // начало тэга

                    case XmlPullParser.START_TAG:

                        if (parser.getName().equals(TAG_REGION)) {

                            int currentDepth = parser.getDepth();
                            regions = parseRegionsInDepth(parser, currentDepth, null);


                        }


                        Log.d(TAG,
                                "START_TAG: имя тега = " + parser.getName()
                                        + ", уровень = " + parser.getDepth()
                                        + ", число атрибутов = "
                                        + parser.getAttributeCount());
                        tmp = "";
                        for (int i = 0; i < parser.getAttributeCount(); i++) {
                            tmp = tmp + parser.getAttributeName(i) + " = "
                                    + parser.getAttributeValue(i) + ", ";
                        }
                        if (!TextUtils.isEmpty(tmp))
                            Log.d(TAG, "Атрибуты: " + tmp);
                        break;
                    // конец тега

                    default:
                        break;
                }
                parser.next();
            }
        } catch (Throwable t) {

            t.printStackTrace();
        }

        return regions;
    }


    private static ArrayList<Region> parseRegionsInDepth(XmlPullParser parser, int parsingDepth, Region parentRegion) throws XmlPullParserException, IOException {
        ArrayList<Region> result = new ArrayList<>();
        Region lastRegion = null;
        int currentDepth = parsingDepth;

        while (parser.getEventType() != XmlPullParser.END_DOCUMENT && parser.getDepth() >= parsingDepth) {
            currentDepth = parser.getDepth();

            switch (parser.getEventType()) {
                case (XmlPullParser.START_TAG):
                    if (currentDepth > parsingDepth && lastRegion != null) {
                        lastRegion.childRegions = parseRegionsInDepth(parser, currentDepth, lastRegion);
                        if (parser.getEventType() == XmlPullParser.END_TAG)
                            parser.next();
                        if (parser.getDepth() < parsingDepth) {
                            RegionService.sortRegion(result);
                            return result;
                        }
                    }

                    Region region = parseArguments(parser);
                    lastRegion = region;
                    region.parent = parentRegion;
                    result.add(region);
                    break;

                case (XmlPullParser.END_DOCUMENT):
                    //RegionData.sortRegion(result);
                    return result;
            }

            parser.next();
        }


        RegionService.sortRegion(result);
        return result;
    }

    private static Region parseArguments(XmlPullParser parser) {
        Region region = new Region();
        String translateValue = "";
        Region.Type type = Region.Type.other;

        for (int index = 0; index < parser.getAttributeCount(); index++) {
            String attrname = parser.getAttributeName(index);
            String attrvalue = parser.getAttributeValue(index);

            switch (attrname) {
                case (ATTRIBUTE_TYPE):
                    if (attrvalue.equals(VALUE_TYPE_CONTINENT)) {
                        type = Region.Type.continent;
                    } else if (attrvalue.equals(VALUE_TYPE_HILLSHADE)) {
                        type = Region.Type.hillshade;
                    } else if (attrvalue.equals(VALUE_TYPE_MAP)) {
                        type = Region.Type.map;
                        region.hasMap = true;
                    } else if (attrvalue.equals(VALUE_TYPE_SRTM)) {
                        type = Region.Type.srtm;
                    }
                    break;
                case (ATTRIBUTE_NAME):
                    region.name = attrvalue;
                    break;
                case (ATTRIBUTE_MAP):
                    if (attrvalue.equals(VALUE_MAP_YES)){
                        region.hasMap = true;
                    }else if(attrvalue.equals(VALUE_MAP_YES)){
                        region.hasMap = false;
                    }
                    break;
                case (ATTRIBUTE_TRANSLATE):
                    translateValue = attrvalue;
                    break;
            }
        }

        region.type = type;
        region.displayName = parseTranslateAttr(region, translateValue);

        //region.displayName = region.name;

        return region;
    }

    private static String parseTranslateAttr(Region region, String value) {
        String displayName = "";

        if (value.isEmpty()) {
            displayName = capitalize(region.name);
        } else {
            if (value.contains(";")) {
                int endIndex = value.indexOf(";");
                displayName = value.substring(0, endIndex);

            }

            displayName = displayName.replace("name:en=", "");
/*            Log.e("kekekkl",displayName +"  --  " + displayName.lastIndexOf(displayName));
            int nameIndex = displayName.indexOf("name");
            int lastNameIndex = nameIndex;
            char symbol;
            if(nameIndex != -1){
                for (symbol = displayName.charAt(lastNameIndex); symbol < '='; lastNameIndex++){

                }
                String toBeReplaced = displayName.substring(nameIndex, lastNameIndex);
                Log.e("asdasdasd",displayName +"  --  " + toBeReplaced);

                displayName = displayName.replace(toBeReplaced, "");
            }*/

            if (displayName.isEmpty()) {
                displayName = capitalize(region.name);
            } else {
                displayName = capitalize(displayName);
            }
        }
        return displayName;
    }

    private static String capitalize(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }
}
