package com.example.downloadmapprototype.model.parser;

import android.content.Context;

import com.example.downloadmapprototype.R;
import com.example.downloadmapprototype.model.data.Region;
import com.example.downloadmapprototype.model.data.RegionService;

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

    public static ArrayList<Region> parseRegions(Context context) {
        ArrayList<Region> regions = new ArrayList<>();

        try {
            XmlPullParser parser = context.getResources().getXml(R.xml.regions);
            while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                switch (parser.getEventType()) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals(TAG_REGION)) {
                            int currentDepth = parser.getDepth();
                            regions = parseRegionsInDepth(parser, currentDepth, null);
                        }
                        break;
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
                    return result;
            }
            parser.next();
        }
        RegionService.sortRegion(result);
        return result;
    }

    private static Region parseArguments(XmlPullParser parser) {
        Region region = new Region();
        region.type = Region.Type.other;
        region.state = Region.State.unDownloaded;
        region.hasMap = true;
        region.displayName = "";
        for (int index = 0; index < parser.getAttributeCount(); index++) {
            String attrname = parser.getAttributeName(index);
            String attrvalue = parser.getAttributeValue(index);
            switch (attrname) {
                case ATTRIBUTE_TYPE:
                    if (attrvalue.equals(VALUE_TYPE_CONTINENT)) {
                        region.type = Region.Type.continent;
                    } else if (attrvalue.equals(VALUE_TYPE_HILLSHADE)) {
                        region.type = Region.Type.hillshade;
                    } else if (attrvalue.equals(VALUE_TYPE_MAP)) {
                        region.type = Region.Type.map;
                    } else if (attrvalue.equals(VALUE_TYPE_SRTM)) {
                        region.type = Region.Type.srtm;
                    }
                    break;
                case ATTRIBUTE_NAME:
                    region.setName(attrvalue);
                    break;
                case ATTRIBUTE_MAP:
                    if (attrvalue.equals(VALUE_MAP_NO)) {
                        region.hasMap = false;
                    } else if (attrvalue.equals(VALUE_MAP_YES)) {
                        region.hasMap = true;
                    }
                    break;
                case ATTRIBUTE_TRANSLATE:
                    region.displayName = attrvalue;
                    break;
            }
        }
        region.displayName = parseDisplayName(region);
        return region;
    }

    private static String parseDisplayName(Region region) {
        String displayName = region.displayName;
        if (displayName.isEmpty() || region.type == Region.Type.continent) {
            displayName = capitalize(region.getName());
        } else {
            if (displayName.contains(";")) {
                int endIndex = displayName.indexOf(";");
                displayName = displayName.substring(0, endIndex);
            }
            if (displayName.contains("name:en=")) {
                displayName = displayName.replace("name:en=", "");
            }
            if (displayName.contains("name:")) {
                displayName = region.getName();
            }

            displayName = capitalize(displayName);
        }
        return displayName;
    }

    private static String capitalize(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }
}
