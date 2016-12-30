package ruby.bamboo.core;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import ruby.bamboo.api.Constants;

public class UpdateCheck implements Runnable {

    private boolean notifyUpdate;
    private String newVersionNum;
    private String newVersionMessage;

    public UpdateCheck() {
        notifyUpdate = false;
        newVersionMessage = "";
    }

    @SubscribeEvent
    public void serverStarted(PlayerTickEvent event) {
        if (notifyUpdate && newVersionNum != null) {
            if (event.side.isClient()) {
                if (event.player.worldObj.isRemote) {
                    event.player.addChatMessage(new TextComponentString("BambooMod update!:" + newVersionNum + (!newVersionMessage.equals("") ? ":" + newVersionMessage : "")));
                    MinecraftForge.EVENT_BUS.unregister(this);
                }
            }
        }
    }

    @Override
    public void run() {
        try {
            InputStreamReader in = new InputStreamReader(new URL("https://dl.dropboxusercontent.com/u/37248864/update.json").openStream());
            JsonObject jsono = (JsonObject) new JsonParser().parse(in).getAsJsonObject().get(Constants.MC_VER);
            if (jsono != null) {
                newVersionNum = jsono.get("version").getAsString();
                boolean isNewVer = false;
                List<Integer> nowVer = Arrays.stream(Constants.BAMBOO_VER.split("\\.")).map(str -> toInt(str)).collect(Collectors.toList());
                List<Integer> newVer = Arrays.stream(newVersionNum.split("\\.")).map(str -> toInt(str)).collect(Collectors.toList());
                //等しい長さのバージョン情報チェック
                for (int i = 0; i < newVer.size() && i < nowVer.size(); i++) {
                    if (newVer.get(i) > nowVer.get(i)) {
                        isNewVer = true;
                        break;
                    }
                }

                //new 0.1.1 : now 0.1のようなパターン
                if (!isNewVer) {
                    if (newVer.size() > nowVer.size()) {
                        int lastElmNum = nowVer.size() - 1;
                        if (newVer.get(lastElmNum) >= nowVer.get(lastElmNum)) {
                            isNewVer = true;
                        }
                    }
                }

                if (isNewVer) {
                    notifyUpdate = true;
                    newVersionMessage = jsono.get("mes").getAsString();
                    MinecraftForge.EVENT_BUS.register(this);
                }
            }
            in.close();
        } catch (Exception e) {}
    }

    private int toInt(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
