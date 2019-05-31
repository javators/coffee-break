package it.edu.liceosilvestri.map2.data;

import android.content.Context;
import android.content.res.AssetManager;
import android.support.annotation.NonNull;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class Categories implements Iterable<Category> {

    private static Categories __categories;
    private Category mCategoryArray[];


    private Categories(Context ctx) {}

    private void load(Context ctx) {

        AssetManager am = ctx.getAssets();
        try {
            InputStream is = am.open("data/categories.xml");

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            /*
                <categories>
                    <category id="monument">
                        <name>Monumenti</name>
                        <name_singular>Monumento</name_singular>
                        <icon></icon>
                        <managedBy>it.edu.liceosilvestri.map2.data.PoiMonument</managedBy>
                    </category>
                    <category id="bar">
                        <name>Bar/Locali</name>
                        <name_singular>Bar/Locale</name_singular>
                        <icon></icon>
                        <managedBy>it.edu.liceosilvestri.map2.data.PoiBar</managedBy>
                    </category>

                    <!--Categoria di appartenenza del POI, es: monument, bar, restaurant, hotel, museum, square, station, park, parking-->

                </categories>
             */
            Document document = builder.parse(is);
            Element root = document.getDocumentElement();


            NodeList nList = document.getElementsByTagName("category");
            int k;

            if (nList.getLength() > 0) {

                mCategoryArray = new Category[nList.getLength()];
                k = 0;

                for (int i = 0; i < nList.getLength(); i++) {
                    Node node = nList.item(i);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {

                        Element eElement = (Element) node;
                        String id = eElement.getAttribute("id");
                        String name = eElement.getAttribute("name");
                        String nameSingular = eElement.getAttribute("name_singular");
                        String icon = eElement.getAttribute("icon");
                        String managedBy = eElement.getAttribute("managedBy");



                        Category cat = new Category(ctx, id, name, nameSingular, icon, managedBy);

                        mCategoryArray[k++] = cat;
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

    }

    public static Categories get(Context ctx) {
        if (__categories == null) {
            __categories = new Categories(ctx);
            __categories.load(ctx);
        }

        return __categories;
    }

    public Category getCategoryAt(int index) {
        if (mCategoryArray != null && (index >= 0 && index < mCategoryArray.length-1))
            return mCategoryArray[index];
        else
            return null;
    }

    public int getLength(){
        return mCategoryArray==null ? 0 : mCategoryArray.length;
    }


    @NonNull
    @Override
    public Iterator<Category> iterator() {
        return new Categories.CategoryIterator();
    }


    public class CategoryIterator implements Iterator<Category> {

        private int mIndex=-1;

        @Override
        public boolean hasNext() {
            if (mCategoryArray!=null && mIndex < mCategoryArray.length-1)
                return true;
            else
                return false;
        }

        @Override
        public Category next() {
            if (mCategoryArray!=null &&mIndex < mCategoryArray.length-1)
                return mCategoryArray[++mIndex];
            else
                return null;
        }
    }
}