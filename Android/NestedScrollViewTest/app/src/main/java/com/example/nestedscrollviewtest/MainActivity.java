package com.example.nestedscrollviewtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.nestedscrollviewtest.data.Region;
import com.example.nestedscrollviewtest.data.RegionDataHolder;
//import com.example.nestedscrollviewtest.databinding.ActivityMainBinding;

import org.xmlpull.v1.XmlPullParser;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RegionDataHolder regionDataHolder;

    //private ActivityMainBinding binding;

    private RecyclerView regionsView;
    private RegionsAdapter regionsAdapter;

    //public String region[] = {"Germany", "Ukraine", "Poland", "Spain", "Belarus", "France", "Finland", "asd", "asd1", "asd2", "asd3", "asd4", "asd5"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //binding = ActivityMainBinding.inflate(getLayoutInflater());
        //setContentView(this.binding.getRoot());

        setContentView(R.layout.activity_main);

        regionDataHolder = new RegionDataHolder();

        regionsView = findViewById(R.id.recyclerViewRegions);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        regionsView.setLayoutManager(layoutManager);


        regionsAdapter = new RegionsAdapter(regionDataHolder.regions.size(), regionDataHolder, new RegionActionListener() {
            @Override
            public void onRegionDetails(Region region, int index) {
                Toast.makeText(MainActivity.this, "Region: " + region.name, Toast.LENGTH_SHORT).show();
                //regionsAdapter.notifyDataSetChanged();
                regionsAdapter.notifyItemChanged(index);
            }

            @Override
            public void onRegionDownload(Region region) {
                Toast.makeText(MainActivity.this, "Region: " + region.name, Toast.LENGTH_SHORT).show();

            }
        });
        regionsView.setAdapter(regionsAdapter);
        Log.e("kek", String.valueOf(regionDataHolder.regions.size()));

        ArrayList<Region> regions = XMLRegionsParser.parseRegions(this);

/*        try {
            XmlPullParser parser = getResources().getXml(R.xml.regions);

            while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                final String TAG = "ЛогКот";
                String tmp = "";

                switch (parser.getEventType()) {
                    case XmlPullParser.START_DOCUMENT:
                        Log.d(TAG, "Начало документа");
                        break;
                    // начало тэга
                    case XmlPullParser.START_TAG:
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
            Toast.makeText(this,
                    "Ошибка при загрузке XML-документа: " + t.toString(),
                    Toast.LENGTH_LONG).show();
        }*/


        //FreeSpaceView spaceView = new FreeSpaceView(this);

    }
}