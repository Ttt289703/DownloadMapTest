package com.example.nestedscrollviewtest;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.example.nestedscrollviewtest.data.Region;
import com.example.nestedscrollviewtest.data.RegionData;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

public class XMLRegionsParser {

    private static final String TAG_REGION = "region";

    private static final String ATTRIBUTE_TYPE = "type";
    private static final String ATTRIBUTE_NAME = "name";
    private static final String ATTRIBUTE_MAP = "map";

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

                        if(parser.getName().equals(TAG_REGION)){

                            int currentDepth = parser.getDepth();
                            regions = parseRegionsInDepth(parser, currentDepth);


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
                    case XmlPullParser.END_TAG:
                        Log.d(TAG, "END_TAG: имя тега = " + parser.getName());
                        break;
                    // содержимое тега
                    case XmlPullParser.TEXT:
                        Log.d(TAG, "текст = " + parser.getText());
                        break;

                    default:
                        break;
                }
                parser.next();
            }
        } catch (Throwable t) {

            t.printStackTrace();
        }

        Log.e("Kek", String.valueOf(regions));
        return regions;
    }

/*    private static ArrayList<Region> parseRegionsInDepth(XmlPullParser parser, int parsingDepth) throws XmlPullParserException, IOException {
        ArrayList<Region> result = new ArrayList<>();

        while(parser.getEventType() != XmlPullParser.END_DOCUMENT || parser.getDepth() >= parsingDepth){
            if(parser.getEventType() == XmlPullParser.START_TAG){
                Region region = parseArguments(parser);
                parser.next();
                int currentDepth = parser.getDepth();

                if(currentDepth > parsingDepth){
                    region.childRegion =  parseRegionsInDepth(parser, currentDepth);
                }
                result.add(region);
            }else{
                parser.next();
            }
        }
        return result;
    }*/

    private static ArrayList<Region> parseRegionsInDepth(XmlPullParser parser, int parsingDepth) throws XmlPullParserException, IOException {
        ArrayList<Region> result = new ArrayList<>();
        Region lastRegion = null;
        int currentDepth = parsingDepth;

        while(parser.getEventType() != XmlPullParser.END_DOCUMENT && parser.getDepth() >= parsingDepth){
            currentDepth = parser.getDepth();

            switch(parser.getEventType()){
                case(XmlPullParser.START_TAG):
                    if(currentDepth > parsingDepth && lastRegion != null){
                        lastRegion.childRegion = parseRegionsInDepth(parser, currentDepth);
                        if (parser.getEventType() == XmlPullParser.END_TAG)
                            parser.next();
                        if (parser.getDepth() < parsingDepth){
                            RegionData.sortRegion(result);
                            return result;
                        }
                    }

                    Region region = parseArguments(parser);
                    lastRegion = region;
                    result.add(region);
                    break;

                case(XmlPullParser.END_DOCUMENT):
                    //RegionData.sortRegion(result);
                    return result;
            }

            parser.next();
        }


        RegionData.sortRegion(result);
        return result;
    }

    private static Region parseArguments(XmlPullParser parser){
        Region region = new Region();

        for(int index = 0; index < parser.getAttributeCount(); index++){
            String attrname = parser.getAttributeName(index);
            String attrvalue = parser.getAttributeValue(index);

            switch (attrname){
                case(ATTRIBUTE_TYPE):
                    if(attrvalue.equals(VALUE_TYPE_CONTINENT))
                        region.type = Region.Type.continent;
                    break;
                case(ATTRIBUTE_NAME):
                    region.name = attrvalue;
                    break;
                case(ATTRIBUTE_MAP):
                    if(attrvalue.equals(VALUE_MAP_YES))
                        region.hasMap = true;
            }
        }
        return region;
    }
}
