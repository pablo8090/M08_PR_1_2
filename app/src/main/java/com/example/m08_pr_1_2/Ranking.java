package com.example.m08_pr_1_2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.TypedValue;
import android.util.Xml;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlSerializer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


public class Ranking extends AppCompatActivity {

    // Constants
    public static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final String photoExt = ".png";


    // Attributes
    private ArrayList<Result> results = new ArrayList<Result>();
    private File rankingDir;
    private File rankingFile;
    private File rankingPhotoDir;
    private Bitmap rankingBitmap;

    private boolean pauseThread;
    Button bt;


    private String[] intentMessageSplit;




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);





        // Setting dir and file
        try {
            prepareStorage();
        } catch (IOException e) {
            e.printStackTrace();
        }


        // Load the saved results in the XML file to the arraylist of results
        try {
            readXML();
            int a =0;
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }


        // Getting intent extra message info and adding a new Result.
        Intent intent = getIntent();
        rankingBitmap = (Bitmap) intent.getParcelableExtra(MainActivity.EXTRA_BITMAP);
        String intentMessage = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        String[] intentMessageSplit = intentMessage.split(",");
        Result res = new Result(intentMessageSplit[0], Integer.parseInt(intentMessageSplit[1]), Integer.parseInt(intentMessageSplit[2]), rankingBitmap);
        results.add(res);
        sortResults();


        // Save the new result to the xml file
        try {
            saveResult(res);
        } catch (IOException e) {
            e.printStackTrace();
        }




        RecyclerView rv = findViewById(R.id.rv);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        rv.setLayoutManager(lm);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, results);
        rv.setAdapter(adapter);

        DividerItemDecoration divider = new DividerItemDecoration(rv.getContext(), lm.getOrientation());

        rv.addItemDecoration(divider);


        // Setting back button
        bt = findViewById(R.id.btBack);
        bt.setText("Volver a jugar");
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

    private void sortResults(){
        Collections.sort(results, new ResultComparator());
    }
    private TextView generateTV (String text, int color, int dpTextSize, TableRow.LayoutParams textViewParams)
    {
        // Generates a textview within a text, color, size and layout params
        TextView tv = new TextView (this);
        tv.setText(text);
        tv.setTextColor(color);
        tv.setLayoutParams(textViewParams);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,dpTextSize);
        return tv;
    }

    private TableRow generateRow (String[] items, int dpTextSize, TableRow.LayoutParams textViewParams, TableRow.LayoutParams tableRowParams) {

        // Generates a row for given strings item (Each item will be a textview)
        TableRow tr = new TableRow(this);
        tr.setLayoutParams(tableRowParams);

        for (int i = 0; i < items.length; i++)
        {
            TextView tv = generateTV(items[i], Color.BLACK, dpTextSize, textViewParams);
            tr.addView(tv);
        }
        return tr;
    }

    private void prepareStorage() throws IOException {
        // ranking root dir
        rankingDir = new File(getApplicationContext().getFilesDir(),"ranking");
        if (!rankingDir.exists())
            rankingDir.mkdir();

        // photo subdir
        rankingPhotoDir = new File(rankingDir, "photos");
        if (!rankingPhotoDir.exists())
            rankingPhotoDir.mkdir();

        // file
        rankingFile = new File(rankingDir, "ranking.xml");
        //xrankingFile.delete();
        // If ranking.xml doesnt exists, create it and add root tag to it.
        if (!rankingFile.exists())
        {
            rankingFile.createNewFile();
            FileWriter fw = new FileWriter(rankingFile);
            fw.write("<ranking>\n</ranking>");
            fw.close();
        }
    }

    private void readXML() throws ParserConfigurationException, IOException, SAXException {
        // Building document with DOM parser
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document rankingDoc = db.parse(rankingFile);

        // Gets all result nodes and iterate them
        NodeList nlResults = rankingDoc.getElementsByTagName("result");
        for (int i = 0; i < nlResults.getLength(); i++)
        {
            Node result = nlResults.item(i); // get result node
            NodeList resultProperties = result.getChildNodes(); // get child nodes for the result node
            String bitmapPath = "", nick = "";
            int tries = 0, time = 0;
            // Iterate the result child nodes and setting the properties variables with the data
            for (int j = 0; j < resultProperties.getLength(); j++)
            {
                Node resultProperty = resultProperties.item(j);
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

            Bitmap photo = getBitmap(nick + photoExt, rankingPhotoDir);

            // Create and add a new result
            results.add(new Result(nick,tries,time,photo));
        }

    }
    private Bitmap getBitmap(String filename, File dirPath) throws FileNotFoundException {
        File bFile = new File(dirPath.getAbsolutePath(), filename);
        Bitmap b = BitmapFactory.decodeStream(new FileInputStream(bFile));
        return b;
    }
    private void saveResult(Result r) throws IOException {
        // This function saves a result into the XML file


        // Gets all file content to string
        String content = getFileContent(rankingFile.getAbsolutePath());

        // Create a string builder and append contents to it
        StringBuilder sb = new StringBuilder();
        sb.append(content);
        // deleting the last line, which containts closing root tag (</ranking>)
        sb.delete(sb.indexOf("</ranking>"), sb.length());
        // Appending a new result node
        sb.append("\t<result>\n");
        sb.append("\t\t<nick>" + r.getNick() + "</nick>\n");
        sb.append("\t\t<tries>" + r.getTries() + "</tries>\n");
        sb.append("\t\t<time>" + r.getTime() + "</time>\n");
        sb.append("\t</result>\n");
        // append the closing root tag
        sb.append("</ranking>");
        content = sb.toString();

        // Overwrite the file with the content
        FileWriter fw = new FileWriter(rankingFile);
        fw.write(content);
        fw.close();

        File photo = new File(rankingPhotoDir, r.getNick() + photoExt);
        FileOutputStream fos = new FileOutputStream(photo);
        rankingBitmap.compress(Bitmap.CompressFormat.PNG, 100,fos);
        fos.close();
    }

    private String getFileContent(String path) throws IOException {
        // Gets file content
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(path));
        String line = null;
        while ((line = br.readLine()) != null)
        {
            sb.append(line);
        }
        return sb.toString();
    }



}