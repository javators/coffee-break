package it.edu.liceosilvestri.map2.data;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.webkit.URLUtil;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class XMLValidator {

    public static String validateXML(Context ctx, String path, String name) {
        try {
            if (isValidXML(ctx, path, name))
                return "";
            else
                return "XML not valid";
        }
        catch (Exception e) {
            return e.getMessage();
        }
    }

    private static boolean isValidXML(Context ctx, String path, String name) throws Exception {
        AssetManager assets = ctx.getAssets();

        try {
            String files[] = assets.list(path);
            boolean found = false;

            for (String f : files) {
                if (f.equals(name + ".xml"))
                    found = true;
            }

            if (!found)
                throw new Exception("File non trovato.");

            InputStream is = assets.open(path + "/" + name + ".xml");

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document document = builder.parse(is);
            Element root = document.getDocumentElement();

            String errors = "";

            switch (root.getTagName()) {
                case "poi":
                    if (!root.getAttribute("id").equals(name))
                        errors += "Poi ID does not match file name.\n";

                    errors += isValidTag(root, "name");
                    errors += isValidTag(root, "name_long");
                    errors += isValidTag(root, "description");
                    errors += isValidCoord(root, "coord");
                    errors += isValidTag(root, "address");
                    errors += isValidTag(root, "suitable_for");

                    NodeList pathNodes = root.getElementsByTagName("path");
                    int notValidPaths = 0;

                    if (pathNodes.getLength() == 0)
                        errors += "Poi Paths are not present.\n";
                    else {
                        for (int i = 0; i < pathNodes.getLength(); i++) {
                            Node node = pathNodes.item(i);
                            if (node.getNodeType() == Node.ELEMENT_NODE) {
                                String pathid = ((Element) node).getAttribute("id");
                                if (!isValidPath(assets, pathid))
                                    notValidPaths++;
                            }
                            else
                                notValidPaths++;
                        }
                    }

                    if (notValidPaths == 1)
                        errors += "A Poi Path is not valid.\n";
                    else if (notValidPaths > 1)
                        errors += (notValidPaths + " Poi Paths are not valid.\n");

                    String category = "null";
                    try {
                        category = ((Element) root.getElementsByTagName("category").item(0)).getAttribute("id");

                        if (!isValidCategory(ctx, category))
                            errors += "Poi Category is not valid.\n";
                        else {
                            Element extraRoot = (Element) root.getElementsByTagName("extra").item(0);

                            switch (category) {
                                case "monument":
                                    errors += isValidTag(extraRoot,  "description_long");
                                    errors += isValidTag(extraRoot,  "opening_times");
                                    errors += isValidUrl(extraRoot,  "website");
                                    errors += isValidImgs(extraRoot, "image", assets);

                                    break;
                                case "bar":
                                case "restaurant":
                                case "entertainment":
                                    errors += isValidTag(extraRoot, "description_long");
                                    errors += isValidTag(extraRoot, "price_range");
                                    errors += isValidTag(extraRoot, "opening_times");
                                    errors += isValidUrl(extraRoot, "website");
                                    errors += isValidTag(extraRoot, "telephone_number");

                                    break;
                                case "hotel":
                                    errors += isValidTag(extraRoot, "description_long");
                                    errors += isValidTag(extraRoot, "price_range");
                                    errors += isValidUrl(extraRoot, "website");
                                    errors += isValidTag(extraRoot, "telephone_number");
                                    errors += isValidTag(extraRoot, "rating");

                                    break;
                                case "museum":
                                    errors += isValidTag(extraRoot, "description_long");
                                    errors += isValidTag(extraRoot, "opening_times");
                                    errors += isValidUrl(extraRoot, "website");
                                    errors += isValidTag(extraRoot, "telephone_number");

                                    break;
                                case "square":
                                    errors += isValidTag(extraRoot, "description_long");

                                    break;
                                case "station":
                                    errors += isValidTag(extraRoot, "description_long");
                                    errors += isValidUrl(extraRoot, "website");

                                    break;
                                case "park":
                                    errors += isValidTag(extraRoot, "description_long");
                                    errors += isValidTag(extraRoot, "opening_times");
                                    errors += isValidImgs(extraRoot, "image", assets);

                                    break;
                                case "parking":
                                    errors += isValidTag(extraRoot, "description_long");
                                    errors += isValidTag(extraRoot, "opening_times");

                                    break;
                                default:
                                    throw new Exception("Poi Category is not valid.");
                            }
                        }
                    }
                    catch (NullPointerException e) {
                        if (category.equals("null"))
                            errors += "Poi Category is not present.\n";
                        else
                            errors += "Poi Extra is not present.\n";
                    }

                    break;
                case "path":
                    if (!root.getAttribute("id").equals(name))
                        errors += "Path ID does not match file name.\n";

                    errors += isValidTag(root, "name");
                    errors += isValidTag(root, "description");
                    errors += isValidTag(root, "description_long");
                    errors += isValidTag(root, "duration");
                    errors += isValidTag(root, "length");
                    errors += isValidTag(root, "suitable_for");
                    errors += isValidColor(root, "color");

                    NodeList nList = root.getElementsByTagName("point");
                    int notValidPts = 0;

                    if (nList.getLength() == 0)
                        errors += "Path Points are not present.\n";
                    else {
                        for (int i = 0; i < nList.getLength(); i++) {
                            Node node = nList.item(i);
                            if (node.getNodeType() == Node.ELEMENT_NODE) {
                                String coords = ((Element) node).getAttribute("coord");
                                if (!isValidCoord(coords))
                                    notValidPts++;
                            }
                            else
                                notValidPts++;
                        }
                    }

                    if (notValidPts == 1)
                        errors += "A Path Point is not valid.\n";
                    else if (notValidPts > 1)
                        errors += (notValidPts + " Path Points are not valid.\n");


                    nList = root.getElementsByTagName("poi");
                    int notValidPois = 0;

                    if (nList.getLength() == 0)
                        errors += "Path Pois are not present.\n";
                    else {
                        for (int i = 0; i < nList.getLength(); i++) {
                            Node node = nList.item(i);
                            if (node.getNodeType() == Node.ELEMENT_NODE) {
                                String poiid = ((Element) node).getAttribute("id");
                                if (!isValidPoi(assets, poiid))
                                    notValidPois++;
                            }
                            else
                                notValidPois++;
                        }
                    }

                    if (notValidPois == 1)
                        errors += "A Path Poi is not valid.\n";
                    else if (notValidPois > 1)
                        errors += (notValidPois + " Path Pois are not valid.\n");

                    break;
                default:
                    throw new Exception("Root tag is not valid.");
            }

            if (errors.equals(""))
                return true;
            else
                throw new Exception(errors);

        } catch (IOException e) {
            throw new Exception("An IOException occurred.");
        } catch (ParserConfigurationException e) {
            throw new Exception("XML non corretto nella sintassi.");
        } catch (SAXException e) {
            throw new Exception("XML non corretto nella sintassi.");
        }
    }

    private static boolean isValidCategory(Context ctx, String category) {
        Categories categories = Categories.get(ctx);

        for (Category cat : categories) {
            if (cat.getId().equals(category))
                return true;
        }
        return false;
    }

    private static String isValidColor(Element root, String tag) {
        String rootName = capitalize(root.getTagName());
        try {
            String color = root.getElementsByTagName(tag).item(0).getTextContent();
            Color.parseColor(color);
        }
        catch (NullPointerException e) {
            return rootName + " " + capitalize(tag) + "is not present.\n";
        }
        catch (IllegalArgumentException e) {
            return rootName + " " + capitalize(tag) + "is not valid.\n";
        }
        return "";
    }

    private static String isValidCoord(Element root, String tag) {
        String rootname = capitalize(root.getTagName());
        try {
            if (!isValidCoord(root.getElementsByTagName(tag).item(0).getTextContent()))
                return rootname + " Coordinates are not valid.\n";
        }
        catch (NullPointerException e) {
            return rootname + " Coordinates are not present.\n";
        }
        return "";
    }

    private static boolean isValidCoord(String coord) {
        if (!coord.contains(", "))
            return false;

        String[] coords = coord.split(", ");

        if (coords.length != 2)
            return false;

        String lat = coords[0];
        String lng = coords[1];
        double n1, n2;

        try {
            n1 = Double.parseDouble(lat);
            n2 = Double.parseDouble(lng);
        } catch (NumberFormatException e) {
            return false;
        }

        if (n1 < -90 || n1 > 90)
            return false;

        if (n2 < -180 || n2 > 180)
            return false;

        return true;
    }

    private static boolean isValidImage(AssetManager assetManager, String imgname) {
        try {
            String[] imgs = assetManager.list("img");

            if (imgname.contains("img/"))
                imgname = imgname.substring("img/".length());

            for (String img : imgs) {
                if (img.equals(imgname))
                    return true;
            }
            return false;
        }
        catch (IOException e) {
            return false;
        }
    }

    private static String isValidImgs(Element root, String tag, AssetManager assets) {
        String rootName = capitalize(root.getTagName());
        String res = "";
        NodeList nList = root.getElementsByTagName(tag);
        int notValidImgs = 0;

        if (nList.getLength() == 0)
            res += rootName + " " + capitalize(tag) + " are not present.\n";
        else {
            for (int i = 0; i < nList.getLength(); i++) {
                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    String imgname = node.getTextContent();
                    if (!isValidImage(assets, imgname))
                        notValidImgs++;
                }
                else
                    notValidImgs++;
            }
        }

        if (notValidImgs == 1)
            res += "A " + rootName + " " + capitalize(tag) + " is not valid.\n";
        else if (notValidImgs > 1)
            res += (notValidImgs + " " + rootName + " " + capitalize(tag) + "s are not valid.\n");

        return res;
    }

    private static boolean isValidPath(AssetManager assets, String pathid) {
        try {
            String[] files = assets.list("data/paths");
            String filename = pathid + ".xml";

            for (String s : files) {
                if (s.equals(filename))
                    return true;
            }
            return false;
        }
        catch (IOException e) {
            return false;
        }
    }

    private static boolean isValidPoi(AssetManager assets, String poiid) {
        try {
            String[] files = assets.list("data/pois");
            String filename = poiid + ".xml";

            for (String s : files) {
                if (s.equals(filename))
                    return true;
            }
            return false;
        }
        catch (IOException e) {
            return false;
        }
    }

    private static String isValidTag(Element root, String tag) {
        String rootName = capitalize(root.getTagName());
        try {
            if (root.getElementsByTagName(tag).item(0).getTextContent().equals(""))
                return rootName + " " + capitalize(tag) + " is empty.\n";
        }
        catch (NullPointerException e) {
            return rootName + " " + capitalize(tag) + " is not present.\n";
        }

        return "";
    }

    private static String isValidUrl(Element root, String tag) {
        String rootName = capitalize(root.getTagName());
        try {
            if (!URLUtil.isValidUrl(root.getElementsByTagName(tag).item(0).getTextContent()))
                return rootName + " " + capitalize(tag) + " is not valid.\n";
            else
                return "";
        }
        catch (Exception e) {
            return rootName + " " + capitalize(tag) + " is not valid.\n";
        }
    }

    private static String capitalize(String in) {
        if (in != null)
            return in.substring(0,1).toUpperCase() + in.substring(1);
        else
            return "";
    }
}
