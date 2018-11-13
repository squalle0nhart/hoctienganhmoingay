package com.squalle0nhart.hoctienganh.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.squalle0nhart.hoctienganh.R;
import com.squalle0nhart.hoctienganh.model.PharseInfo;
import com.squalle0nhart.hoctienganh.ui.adapter.ConversationListAdapter;
import com.squalle0nhart.hoctienganh.ui.view.EqualSpaceItemDecoration;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by squalleonhart on 3/19/2017.
 */

public class ConversationListActivity extends Activity {
    Context mContext;
    RecyclerView mRecycleView;
    ConversationListAdapter mConversationListAdapter;
    ArrayList<PharseInfo> mListPharse;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grammar);
        mContext = this;
        initView();
    }

    private void initView() {
        mRecycleView = (RecyclerView) findViewById(R.id.rv_grammar);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        mRecycleView.setLayoutManager(layoutManager);
        mRecycleView.setHasFixedSize(true);
        mRecycleView.addItemDecoration(new EqualSpaceItemDecoration(10));
        mListPharse = loadConversationListFromXML();
        mConversationListAdapter = new ConversationListAdapter(mContext, mListPharse);
        mRecycleView.setAdapter(mConversationListAdapter);
    }

    /**
     * Lấy danh sách hội thoại file XML
     */
    private ArrayList<PharseInfo> loadConversationListFromXML() {
        ArrayList<PharseInfo> listPharse = new ArrayList<>();
        InputStream is = getResources().openRawResource(R.raw.lesson_titles);
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(is);

            Element root = document.getDocumentElement();
            NodeList list = root.getChildNodes();
            for (int i = 0; i < list.getLength(); i++) {
                Node note = list.item(i);
                if (note instanceof Element) {
                    Element element = (Element) note;
                    String enText = element.getAttribute("en_title");
                    String viText = element.getAttribute("local_title");
                    listPharse.add(new PharseInfo(enText, viText, 0));
                }
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listPharse;
    }
}
