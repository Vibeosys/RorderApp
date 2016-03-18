package com.vibeosys.rorderapp.data;

import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akshay on 18-03-2016.
 */
public class ConfigSettingsDbDTO extends BaseDTO {

    private String configKey;
    private String configValue;

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    public static List<ConfigSettingsDbDTO> deserializeBill(List<String> serializedStringList) {
        Gson gson = new Gson();
        ArrayList<ConfigSettingsDbDTO> objectList = new ArrayList<>();
        try {

            for (String serializedString : serializedStringList) {
                ConfigSettingsDbDTO deserializeObject = gson.fromJson(serializedString, ConfigSettingsDbDTO.class);
                objectList.add(deserializeObject);
            }
        } catch (Exception e) {
            Log.d("TAG", "##" + e.toString());
        }

        return objectList;
    }
}
