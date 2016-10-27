import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Scanner

    //jsonファイル名
    String blockName="";
    
    //入力
    def br = new BufferedReader(new InputStreamReader(System.in))
    try {
        println "作成する名称を入力"   
        blockName = br.readLine()
    }catch (IOException e) {
        println "error:"+e.message
        return
    }
    //テクスチャファイル名
    String texName=blockName;
    //このファイルが有るパス
    String home=System.getProperty("user.dir") + "/"

    //blockstates
    def stateList = []
    stateList.add("common.json")
    stateList.add("common_double_slab.json")
    stateList.add("common_slab.json")
    stateList.add("common_stairs.json");
    Pattern p = Pattern.compile("common");
    for(String str in stateList){
        List<String> list=readTextFile(home+"tmp/blockstates/"+str);
        List<String> outList=new ArrayList<>();
        for(String s in list){
            Matcher m = p.matcher(s);
            outList.add(m.replaceFirst(blockName));
        }
        Matcher m = p.matcher(str);
        new File(home+"blockstates/").mkdirs();
        writeTextFile(home+"blockstates/"+m.replaceFirst(blockName), outList);
    }
    
    //model:block
    def blockList = []
    blockList.add("common.json")
    blockList.add("common_slab.json")
    blockList.add("common_upper_slab.json")
    blockList.add("common_stairs.json");
    blockList.add("common_inner_stairs.json");
    blockList.add("common_outer_stairs.json");
    
    for(String str in blockList){
        List<String> list=readTextFile(home+"tmp/models/block/"+str);
        List<String> outList=new ArrayList<>();
        for(String s in list){
            Matcher m = p.matcher(s);
            outList.add(m.replaceFirst(blockName));
        }
        Matcher m = p.matcher(str);
        new File(home+"models/block/").mkdirs();
        writeTextFile(home+"models/block/"+m.replaceFirst(blockName), outList);
    }
    
    //model:item
    def itemList = []
    itemList.add("common.json")
    itemList.add("common_double_slab.json")
    itemList.add("common_slab.json")
    itemList.add("common_stairs.json");
    
    for(String str in itemList){
        List<String> list=readTextFile(home+"tmp/models/item/"+str);
        List<String> outList=new ArrayList<>();
        for(String s in list){
            Matcher m = p.matcher(s);
            outList.add(m.replaceFirst(blockName));
        }
        Matcher m = p.matcher(str);
        new File(home+"models/item/").mkdirs();
        writeTextFile(home+"models/item/"+m.replaceFirst(blockName), outList);
    }
         
    public List readTextFile(String fileName){
        List<String> textList = new ArrayList<>();
        File file = new File(fileName);
        BufferedReader br = Files.newBufferedReader(file.toPath(), Charset.forName("MS932"));
            for (;;) {
                String text = br.readLine(); // 改行コードは含まれない
                if (text == null) {
                    break;
                }
                textList.add(text);
            }
        return textList;
    }

    public static void writeTextFile(String fileName, List<String> textList) {
        File file = new File(fileName);

        file.createNewFile()

        BufferedWriter bw = Files.newBufferedWriter(file.toPath(), Charset.forName("MS932"), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            for (String text : textList) {
                bw.write(text);
                bw.newLine(); // 改行
            }
            bw.close()
    }