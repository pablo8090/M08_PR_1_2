package com.example.m08_pr_1_2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.util.Xml;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class Ranking extends AppCompatActivity {

    private ArrayList<Result> results = new ArrayList<Result>();
    private File rankingDir;
    private File rankingFile;
    private String content;
    private int nR, pR;

    private String[] intentMessageSplit;

    private void prepareStorage() throws IOException {
        // dir
        rankingDir = new File(getApplicationContext().getFilesDir(),"ranking");
        if (!rankingDir.exists())
            rankingDir.mkdir();

        // file
        rankingFile = new File(rankingDir, "ranking.xml");
       // rankingFile.delete();
        if (!rankingFile.exists())
        {
            rankingFile.createNewFile();
            FileWriter fw = new FileWriter(rankingFile);
            fw.write("<ranking>\n</ranking>");
            fw.close();
        }


    }

    private void readXML() throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document rankingDoc = db.parse(rankingFile);

        NodeList nlResults = rankingDoc.getElementsByTagName("result");
        for (int i = 0; i < nlResults.getLength(); i++)
        {
            nR++;
            Node result = nlResults.item(i);
            NodeList resultProperties = result.getChildNodes();
            String nick = "aaa";
            int tries = 0, time = 0;
            for (int j = 0; j < resultProperties.getLength(); j++)
            {
                pR++;
                Node resultProperty = resultProperties.item(i);
                sw: switch (resultProperty.getNodeName())
                {
                    case "nick":
                        nick = resultProperty.getTextContent();
                        break sw;
                    case "tries":
                        tries = Integer.parseInt(resultProperty.getTextContent());
                        break sw;
                    case "time":
                        time = Integer.parseInt(resultProperty.getTextContent());
                        break sw;
                }

            }

            results.add(new Result(nick,tries,time));
        }

    }

    private void saveResult(Result r) throws IOException {

        content = "";
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                byte[] encoded = Files.readAllBytes(Paths.get(rankingFile.getAbsolutePath()));
                content = new String(encoded, StandardCharsets.US_ASCII);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        StringBuilder sb = new StringBuilder();
        sb.append(content);
        sb.delete(sb.indexOf("</ranking>"), sb.length());
        sb.append("<result>\n");
        sb.append("<nick>" + r.getNick() + "</nick>\n");
        sb.append("<tries>" + r.getTries() + "</tries>\n");
        sb.append("<time>" + r.getTime() + "</time>\n");
        sb.append("</result>\n");
        sb.append("</ranking>");
        content = sb.toString();

        FileWriter fw = new FileWriter(rankingFile);
        fw.write(content);
        fw.close();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        // Setting dir, file,
        try {
            prepareStorage();
        } catch (IOException e) {
            e.printStackTrace();
        }



        if (results.isEmpty())
        {
            try {
                readXML();
            } catch (ParserConfigurationException | IOException | SAXException e) {
                e.printStackTrace();
            }
        }

        // Getting intent extra message info and adding a new Result
        Intent intent = getIntent();
        String intentMessage = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        String[] intentMessageSplit = intentMessage.split(",");
        Result res = new Result(intentMessageSplit[0], Integer.parseInt(intentMessageSplit[1]), Integer.parseInt(intentMessageSplit[2]));
        results.add(res);
        try {
            saveResult(res);
        } catch (IOException e) {
            e.printStackTrace();
        }


        // Creating TableLayout and the row layout parameters
        TableLayout tbl = findViewById(R.id.tlRanking);

        TableRow.LayoutParams textViewParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT,1.0f);
        TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);

        // Setting Header Row
        String headers[] = new String[3];
        headers[0] = "USUARI";
        headers[1] = "INTENTS";
        headers[2] = "TEMPS (s)";
        tbl.addView(generateRow(headers, 25, textViewParams, tableRowParams));

        // Setting all results rows
        Collections.sort(results, new ResultComparator());
        for (Result r : results)
        {
            String[] items = r.getData();
            tbl.addView(generateRow(items, 20,textViewParams, tableRowParams));
        }


        // test

        final TextView tv = findViewById(R.id.textView);
        tv.setText(content);
        // Setting back button

        final Button bt = findViewById(R.id.btBack);
        String s = "";
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                byte[] encoded = Files.readAllBytes(Paths.get(rankingFile.getAbsolutePath()));
                s = new String(encoded, StandardCharsets.US_ASCII);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        bt.setText(nR + "," + pR);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMain();
            }
        });
    }
    public void backToMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private TextView generateTV (String text, int color, int dpTextSize, TableRow.LayoutParams textViewParams)
    {
        TextView tv = new TextView (this);
        tv.setText(text);
        tv.setTextColor(color);
        tv.setLayoutParams(textViewParams);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,dpTextSize);
        return tv;
    }

    private TableRow generateRow (String[] items, int dpTextSize, TableRow.LayoutParams textViewParams, TableRow.LayoutParams tableRowParams) {

        TableRow tr = new TableRow(this);
        tr.setLayoutParams(tableRowParams);
        //tr.setGravity(Gravity.CENTER);

        for (int i = 0; i < items.length; i++)
        {
            TextView tv = generateTV(items[i], Color.BLACK, dpTextSize, textViewParams);
            tr.addView(tv);
        }
        return tr;
    }



    private void closeStorage() throws IOException {
        rankingFile = null;
        rankingDir = null;
    }
}